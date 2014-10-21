package src;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YFFParser {
	private String baseURI;
	private String leagueID;

	/**
	 * Create a new parser object with specified paths to YFF resources.
	 * 
	 * @param baseURI
	 *            path to YFF application
	 * @param leagueID
	 *            league ID
	 */
	public YFFParser(String baseURI, String leagueID) {
		this.baseURI = baseURI;
		this.leagueID = leagueID;
	}

	/**
	 * Create a connection a specified query and download the result. Can return
	 * a null Document if there is an error downloading content or connecting to
	 * the requested URI.
	 * 
	 * @param myQuery
	 * @return a Jsoup-parsed Document with the contents of the requested URI
	 */
	public Document download(String myQuery) {
		Document doc = null;
		try {
			doc = Jsoup.connect(myQuery).get();
		} catch (IOException e) {
			System.err.println("There was an error downloading content from "
					+ myQuery);
			e.printStackTrace();
		}

		return doc;
	}

	/**
	 * 
	 * @param teamID
	 * @param week
	 * @return a Roster object
	 */
	public Roster parseRoster(int teamID, int week) {
		String rosterQuery = "" + teamID + "/";
		if (week != 0) {
			rosterQuery += "team?&week=" + week + "&stat1=S&stat2=W";
		}

		String myQuery = constructQuery(rosterQuery);
		Document rosterDoc = download(myQuery);

		//parse offense
		Element offenseTable = rosterDoc.getElementById("statTable0-wrap");
		Elements offenseTableRows = offenseTable.getElementsByTag("tr");
		// remove the first two elements, which are table headers.
		offenseTableRows.remove(0);
		offenseTableRows.remove(0);
		ArrayList<Player> rosterList = new ArrayList<Player>();
		for (Element el : offenseTableRows) {
			rosterList.add(parsePlayerFromTableRow(el));
		} // End of loop through all Elements
		
		
		//Parse kicker
		Element kickerTable = offenseTable.nextElementSibling();
		Elements kickerTableRows = kickerTable.getElementsByTag("tr");
		kickerTableRows.remove(0);
		kickerTableRows.remove(0);
		for (Element el : kickerTableRows){
			rosterList.add(parsePlayerFromTableRow(el));
		}
		
		//Parse defense
		Element defenseTable = kickerTable.nextElementSibling();
		Elements defenseTableRows = defenseTable.getElementsByTag("tr");
		defenseTableRows.remove(0);
		defenseTableRows.remove(0);
		for (Element el : defenseTableRows){
			rosterList.add(parsePlayerFromTableRow(el));
		}
		
		//Parse IDP
		Element defensivePlayerTable = defenseTable.nextElementSibling();
		Elements defensivePlayerTableRows = defensivePlayerTable.getElementsByTag("tr");
		defensivePlayerTableRows.remove(0);
		defensivePlayerTableRows.remove(0);
		for (Element el : defensivePlayerTableRows){
			rosterList.add(parsePlayerFromTableRow(el));
		}
		
		return new Roster(rosterList, teamID, week);
	}

	private Player parsePlayerFromTableRow(Element el) {
		// In rows.get(idx).getElementsByTag("td") we are returned the following
		// text: 0:
		// QB 1: Player Note Drew Brees NO - QB Sun 10:00 am vs TB ...
		// 4: recorded points, or "-"; this field can also be "Bye Week" (if one
		// of the fields 2-4 is not populated?)
		// 5: projected points
		// 6: percent
		// started 7 thru 19: recorded stats, by category.
		Elements playerColumns = el.getElementsByTag("td");
		String pos = playerColumns.get(0).text();

		// Parse the more detailed information from second element.
		Element details = playerColumns.get(1);
		Elements details1 = details.getElementsByClass("name");
		String href, playerName, team;
		if (details1.size() == 1) {
			Element details2 = details1.get(0);
			href = details2.attr("href");
			playerName = details2.text();
			Element details3 = details2.nextElementSibling();
			team = details3.text();
		} else {
			System.err
					.println(details1.size()
							+ " elements were returned in the player details Elements.");
			href = "";
			playerName = "";
			team = "";
		}

		// Get recorded/projected points from column 4/5
		String recordedPoints = playerColumns.get(4).text();
		String projectedPoints = playerColumns.get(5).text();

		// check pos is valid position.
		if (!Roster.isValidPosition(pos)) {
			System.err.println("Parsed roster position not valid: " + pos);
			pos = "";
		}

		// parse the player ID out of the end of the player card link, and
		// convert to int.
		String playerID = href.substring(href.lastIndexOf('/') + 1, href.length());

		// parse the first and last name out of the player full name.
		String[] playerNameSplit = playerName.split(" ");
		int playerNamePieces = playerNameSplit.length;
		String firstName, lastName;
		if (playerNamePieces>1){
			//normal player
			firstName = playerNameSplit[0];
			lastName = "";
			for (int i = 1; i < playerNamePieces; i++)
				lastName = lastName +" "+playerNameSplit[i];
		} else if (playerNameSplit.length==1){
			//defense
			firstName = playerName;
			lastName = "";
		} else {
			//something else is happening.
			System.err.println("Error: Player name is empty.");
			firstName = "";
			lastName = "";
		}

		// parse team identifier. note that this drops the RL position TODO
		// parse RL position
		team = team.substring(0, team.indexOf(' '));

		// parse points. Note that if player is on bye, these fields will
		// not populate. Note that if a player has not played that week,
		// the recordedPoints field will appear as "-". However,
		// unfortunately, this is actually the unicode 8211 ("\u2013") EN
		// DASH.

		// check stat1=S, stat2=W if week>=current week.
		double recordedPoints0 = 0, projectedPoints0 = 0;
		if (recordedPoints.equals("Bye Week")) {
			// If a player is on bye, these fields will not populate.
			recordedPoints0 = 0.0;
			projectedPoints0 = 0.0;
		} else if (!recordedPoints.matches("[0-9]*.[0-9]+")) {
			// If the player has not player yet, recordedPoints will be "-"
			recordedPoints0 = 0.0;
			projectedPoints0 = Double.parseDouble(projectedPoints);
		} else {
			// Should work as expected. If there are problems, check the
			// exact query in browser.
			recordedPoints0 = Double.parseDouble(recordedPoints);
			projectedPoints0 = Double.parseDouble(projectedPoints);
		}

		 return new Player(firstName, lastName, playerID, team, pos,
				recordedPoints0, projectedPoints0);
	}

	/**
	 * Download and parse team roster page on Yahoo for a given team. The
	 * current week is used by default. Parse into Players and return a Roster
	 * object
	 * 
	 * @param teamID
	 *            the ID of the team on Yahoo
	 * @return a path to a file readable as a Roster
	 */
	public Roster parseRoster(int teamID) {
		return parseRoster(teamID, 0);
	}

	/**
	 * This method does not work fully and is likely to become deprecated. Given
	 * a document that represents a parsed matchup page on Yahoo, parse full
	 * rosters, players, and teams and write into a file readable as Roster.
	 * 
	 * @param doc
	 *            a Document that represents a parsed matchup page
	 * @return an array of Strings with the path to written Rosters
	 * @deprecated
	 */
	public String[] parseMatchup(Document doc) {
		Element startersDiv = doc.getElementById("matchupcontent1");

		/*
		 * All possible text fields representing a player will fit one of the
		 * following patterns (regexes). This is of course a naive way to select
		 * the players. Much better would be to use the div/class selector
		 * syntax that is provided by Jsoup. However, the intention here is to
		 * first get a working downloader. TODO Select correct elements without
		 * using regex.
		 */
		String startersNamesRegex = "(^[A-Z]\\. [A-Z][a-z]*$)"; // match player
																// name
																// <FirstInitial>.
																// <Last>
		startersNamesRegex += "|(^\\(Empty\\)$)"; // match empty slot
		startersNamesRegex += "|(^Arizona$)"; // match defenses
		startersNamesRegex += "|(^Atlanta$)";
		startersNamesRegex += "|(^Baltimore$)";
		startersNamesRegex += "|(^Buffalo$)";
		startersNamesRegex += "|(^Carolina$)";
		startersNamesRegex += "|(^Chicago$)";
		startersNamesRegex += "|(^Cincinnati$)";
		startersNamesRegex += "|(^Cleveland$)";
		startersNamesRegex += "|(^Dallas$)";
		startersNamesRegex += "|(^Denver$)";
		startersNamesRegex += "|(^Detroit$)";
		startersNamesRegex += "|(^Green Bay$)";
		startersNamesRegex += "|(^Houston$)";
		startersNamesRegex += "|(^Indianapolis$)";
		startersNamesRegex += "|(^Jacksonville$)";
		startersNamesRegex += "|(^Kansas City$)";
		startersNamesRegex += "|(^Miami$)";
		startersNamesRegex += "|(^Minnesota$)";
		startersNamesRegex += "|(^New England$)";
		startersNamesRegex += "|(^New Orleans$)";
		startersNamesRegex += "|(^New York$)";
		startersNamesRegex += "|(^New York$)";
		startersNamesRegex += "|(^Oakland$)";
		startersNamesRegex += "|(^Philadelphia$)";
		startersNamesRegex += "|(^Pittsburgh$)";
		startersNamesRegex += "|(^San Diego$)";
		startersNamesRegex += "|(^San Francisco$)";
		startersNamesRegex += "|(^Seattle$)";
		startersNamesRegex += "|(^St\\. Louis$)";
		startersNamesRegex += "|(^Tampa Bay$)";
		startersNamesRegex += "|(^Tennessee$)";
		startersNamesRegex += "|(^Washington$)";

		Elements starters = startersDiv
				.getElementsMatchingOwnText(startersNamesRegex);

		/*
		 * All possible text fields representing projected points will fit the
		 * following regex.
		 */
		String startersPointsRegex = "[0-9]+\\.[0-9][0-9]";
		Elements startersDiv1Points = startersDiv
				.getElementsMatchingOwnText(startersPointsRegex);

		Element bench = doc.getElementById("matchupcontent2");

		List<String> home = new ArrayList<String>();
		List<Double> homePoints = new ArrayList<Double>();
		List<String> away = new ArrayList<String>();
		List<Double> awayPoints = new ArrayList<Double>();

		// load player names into array
		boolean loadToHome = true;
		for (Element el : starters) {
			if (loadToHome)
				home.add(el.text());
			else
				away.add(el.text());
			loadToHome = !loadToHome;
		}

		// load points into array
		loadToHome = true;
		for (Element el : startersDiv1Points) {
			if (loadToHome)
				homePoints.add(Double.parseDouble(el.text()));
			else
				awayPoints.add(Double.parseDouble(el.text()));
			loadToHome = !loadToHome;
		}

		// write home team to file
		String homeTeamFileName = "resources/HomeTeam.txt";
		File homeTeamFile = new File(homeTeamFileName);
		try {
			PrintWriter w = new PrintWriter(homeTeamFile);
			for (int i = 0; i < home.size(); i++) {
				w.println(home.get(i) + "," + homePoints.get(i) + ",0");
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write away team to file
		String awayTeamFileName = "resources/AwayTeam.txt";
		File awayTeamFile = new File(awayTeamFileName);
		try {
			PrintWriter w = new PrintWriter(awayTeamFile);
			for (int i = 0; i < away.size(); i++) {
				w.println(away.get(i) + "," + awayPoints.get(i) + ",0");
			}
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] fileNames = { homeTeamFileName, awayTeamFileName };
		return fileNames;
	}

	/**
	 * Construct a complete query by tacking on a given string to the league's
	 * base URL
	 * 
	 * @param myQuery
	 *            query to tack on to league's base URL
	 * @return complete query
	 */
	public String constructQuery(String myQuery) {
		return baseURI + leagueID + myQuery;
	}
}
