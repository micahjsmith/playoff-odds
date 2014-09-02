package src;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YFFParser {
	private String baseURI;
	private String leagueID;
	private String resourceID;
	private String query;
	private String testURI;

	/**
	 * Create a new parser object with default paths to YFF resources.
	 */
	public YFFParser() {
		baseURI = "http://football.fantasysports.yahoo.com/f1/";
		leagueID = "27235/";
		resourceID = "matchup?";
		query = "week=1&mid1=1&mid2=10";
		testURI = baseURI + leagueID + resourceID + query;
	}

	/**
	 * Create a new parser object with specified paths to YFF resources.
	 * 
	 * @param baseURI
	 *            path to YFF application
	 * @param leagueID
	 *            league ID
	 * @param resourceID
	 *            resource ID (such as "matchup", "team", "player")
	 * @param query
	 *            query provided to resource (such as "week=1&mid1=2&mid2=6")
	 */
	public YFFParser(String baseURI, String leagueID, String resourceID,
			String query) {
		this.baseURI = baseURI;
		this.leagueID = leagueID;
		this.resourceID = resourceID;
		this.query = query;
	}

	/**
	 * Create a connection to a default query and download the result.
	 */
	public Document downloadQuery() {
		return downloadQuery(testURI);
	}

	/**
	 * Create a connection a specified query and download the result. Can return
	 * a null Document if there is an error downloading content or connecting to
	 * the requested URI.
	 * 
	 * @param myQuery
	 * @return a Jsoup-parsed Document with the contents of the requested URI
	 */
	public Document downloadQuery(String myQuery) {
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
	 * Given a document that represents a parsed matchup page on Yahoo, parse
	 * full rosters, players, and teams and load into a Matchup object.
	 * 
	 * @param doc a Document that represents a parsed matchup page
	 * @return a complete Matchup
	 */
	public Matchup parseMatchup(Document doc) {
		Element startersDiv0 = doc.getElementById("matchupcontent1");
		Elements startersDiv1 = startersDiv0
				.getElementsMatchingOwnText("(^[A-Z]\\. [A-Z][a-z]*$)|(^\\(Empty\\)$)");
		System.out.println(startersDiv1.text());
		// for (Element e : startersDiv1){
		// //do something here.
		// }
		// Element bench = doc.getElementById("matchupcontent2");

		return null;
	}
}
