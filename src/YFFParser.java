package src;

import java.io.IOException;
import java.util.ArrayList;

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

		// parse offense
		Element offenseTable = rosterDoc.getElementById("statTable0-wrap");
		Elements offenseTableRows = offenseTable.getElementsByTag("tr");
		// remove the first two elements, which are table headers.
		offenseTableRows.remove(0);
		offenseTableRows.remove(0);
		ArrayList<PlayerPerformance> rosterList = new ArrayList<PlayerPerformance>();
		for (Element el : offenseTableRows) {
			rosterList.add(parsePlayerFromTableRow(el));
		} // End of loop through all Elements

		// Parse kicker
		Element kickerTable = offenseTable.nextElementSibling();
		Elements kickerTableRows = kickerTable.getElementsByTag("tr");
		kickerTableRows.remove(0);
		kickerTableRows.remove(0);
		for (Element el : kickerTableRows) {
			rosterList.add(parsePlayerFromTableRow(el));
		}

		// Parse defense
		Element defenseTable = kickerTable.nextElementSibling();
		Elements defenseTableRows = defenseTable.getElementsByTag("tr");
		defenseTableRows.remove(0);
		defenseTableRows.remove(0);
		for (Element el : defenseTableRows) {
			rosterList.add(parsePlayerFromTableRow(el));
		}

		// Parse IDP
		Element defensivePlayerTable = defenseTable.nextElementSibling();
		Elements defensivePlayerTableRows = defensivePlayerTable
				.getElementsByTag("tr");
		defensivePlayerTableRows.remove(0);
		defensivePlayerTableRows.remove(0);
		for (Element el : defensivePlayerTableRows) {
			rosterList.add(parsePlayerFromTableRow(el));
		}

		return new Roster(rosterList, teamID, week);
	}

	private PlayerPerformance parsePlayerFromTableRow(Element el) {
		// In rows.get(idx).getElementsByTag("td") we are returned the following
		// text: 0:
		// QB 1: PlayerPerformance Note Drew Brees NO - QB Sun 10:00 am vs TB ...
		// 4: recorded points, or "-"; this field can also be "Bye Week" (if one
		// of the fields 2-4 is not populated?)
		// 5: projected points
		// 6: percent
		// started 7 thru 19: recorded stats, by category.
		Elements playerColumns = el.getElementsByTag("td");
		String rosterPos = playerColumns.get(0).text();

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
		if (!Roster.isValidPosition(rosterPos)) {
			System.err.println("Parsed roster position not valid: " + rosterPos);
			rosterPos = "";
		}

		// parse the player ID out of the end of the player card link, and
		// convert to int.
		String playerID = href.substring(href.lastIndexOf('/') + 1,
				href.length());

		// parse the first and last name out of the player full name.
		String[] playerNameSplit = playerName.split(" ");
		int playerNamePieces = playerNameSplit.length;
		String firstName, lastName;
		if (playerNamePieces > 1) {
			// normal player
			firstName = playerNameSplit[0];
			lastName = "";
			for (int i = 1; i < playerNamePieces; i++)
				lastName = lastName + " " + playerNameSplit[i];
		} else if (playerNameSplit.length == 1) {
			// defense
			firstName = playerName;
			lastName = "";
		} else {
			// something else is happening.
			System.err.println("Error: PlayerPerformance name is empty.");
			firstName = "";
			lastName = "";
		}

		// parse team identifier and eligible roster positions
		// format: TEAM - POS,POS, ...
		String eligiblePosRegex = "[a-zA-Z]* - [a-zA-Z]*,*[a-zA-Z]*,*[a-zA-Z]*";
		String[] eligiblePositions;
		if (team.matches(eligiblePosRegex)){
			String[] eligiblePosSplit = team.split(" - ");
			team = eligiblePosSplit[0];
			eligiblePositions = eligiblePosSplit[1].split(",");	
		} else {
			System.err.println("Team/pos descriptor did not match regex.");
			team = "";
			eligiblePositions = new String[1];
			eligiblePositions[0] = "";
		}

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

		return new PlayerPerformance(firstName, lastName, playerID, team, rosterPos,
				eligiblePositions, recordedPoints0, projectedPoints0);
	}
}
