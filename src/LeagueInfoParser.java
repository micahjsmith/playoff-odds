package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.scribe.oauth.*;
import org.scribe.model.*;
import org.scribe.builder.*;
import org.scribe.builder.api.*;

public class LeagueInfoParser {
	private String baseURI = "http://fantasysports.yahooapis.com/fantasy/v2/";
	private OAuthService service;
	private Token accessToken;
	private static final long ONE_HOUR = 3600000;
	private final String apiKey = "dj0yJmk9TUdoV3NDdDhNSGNtJmQ9WVdrOU4wVTRiVmhwTkdjbWNHbzlPRFE1TVRRNU5EWXkmcz1jb25zdW1lcnNlY3JldCZ4PTQ3";
	private final String apiSecret = "041d6cc9ae3961aa987e3769f1809d572ee09a79";

	/**
	 * Open connection to Yahoo! API service to access protected information.
	 */
	public LeagueInfoParser() {
		// The following skeleton is due to Scribe example
		service = new ServiceBuilder()
				.provider(YahooApi.class)
				.apiKey(apiKey)
				.apiSecret(apiSecret).build();

		accessToken = buildTokenFromFile();
	}

	/**
	 * Parse league metadata, settings, active players, teams, and matchups
	 * 
	 * @return
	 */
	public League parseLeague() {
		OAuthRequest request = new OAuthRequest(Verb.GET, baseURI
				+ "league/nfl.l.6250/metadata");
		service.signRequest(accessToken, request);
		Response response = request.send();

		// if code says okay, proceed with analyzing results, otherwise, return
		// error message.
		String body = response.getBody();
		League result = new League(parseXml("league_key", body));

		// relevant league metadata
		result.setCurrent_week(parseXml("current_week", body));
		result.setDraft_status(parseXml("draft_status", body));
		result.setLeague_id(parseXml("league_id", body));
		result.setName(parseXml("name", body));
		result.setUrl(parseXml("url", body));
		result.setEnd_date(parseXml("end_date", body));
		result.setEnd_week(parseXml("end_week", body));
		result.setStart_date(parseXml("start_date", body));
		result.setStart_week(parseXml("start_week", body));
		result.setNum_teams(parseXml("num_teams", body));

		// relevant league settings
		OAuthRequest request2 = new OAuthRequest(Verb.GET, baseURI
				+ "league/nfl.l.6250/settings");
		service.signRequest(accessToken, request2);
		Response response2 = request2.send();
		String body2 = response2.getBody();

		result.setHas_playoff_consolation_games(parseXml(
				"has_playoff_consolation_games", body2));
		result.setNum_playoff_consolation_teams(parseXml(
				"num_playoff_consolation_teams", body2));
		result.setNum_playoff_teams(parseXml("num_playoff_teams", body2));
		result.setPlayoff_start_week(parseXml("playoff_start_week", body2));
		result.setScoring_type(parseXml("scoring_type", body2));
		result.setUses_playoff(parseXml("uses_playoff", body2));
		result.setUses_playoff_reseeding(parseXml("uses_playoff_reseeding",
				body2));

		//team identification info
		result.setTeams(parseTeams());
		return result;
	}

	private Team[] parseTeams() {
		Team[] teams = new Team[10];
		// parse all teams
		OAuthRequest request3 = new OAuthRequest(Verb.GET, baseURI
				+ "league/nfl.l.6250/teams");
		service.signRequest(accessToken, request3);
		Response response3 = request3.send();

		// scan response
		Scanner teamsSc = new Scanner(response3.getBody());
		String startTag = "<team>";
		String endTag = "</team>";

		// next() will return something of the form "myteaminformation</team>
		teamsSc.useDelimiter(startTag);
		teamsSc.next(); // skip scanner past introductory league info

		int count = 0;
		while (teamsSc.hasNext()) {
			String next = teamsSc.next();

			// remove trailing tag + noise
			next = next.substring(0, next.indexOf(endTag));
			
			//populate fields
			Team newTeam = new Team(parseXml("team_key",next));
			newTeam.setTeam_id(parseXml("team_id",next));
			newTeam.setName(parseXml("name",next));
			newTeam.setUrl(parseXml("url",next));
			newTeam.setTeam_logos(parseXml("team_logos",next));
			newTeam.setWaiver_priority(parseXml("waiver_priority",next));
			newTeam.setFaab_balance(parseXml("faab_balance",next));
			newTeam.setNumber_of_moves(parseXml("number_of_moves",next));
			newTeam.setNumber_of_trades(parseXml("number_of_trades",next));
			newTeam.setRoster_adds(parseXml("roster_adds",next));
			newTeam.setManagers(parseXml("managers",next));
			
			teams[count]=newTeam;
			count++;
		}
		teamsSc.close();
		
		return teams;
	}

	/*
	 * returns the string if tag is found TODO: throws error if not found TODO:
	 * is it better to implement this using a scanner?
	 */
	private String parseXml(String tag, String body) {
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		return body.substring(body.indexOf(startTag) + startTag.length(),
				body.indexOf(endTag));
	}

	/*
	 * For testing purposes, to avoid typing in code to browser. Token should be
	 * written in file as: <line 1> <current time in millis> <line 2>
	 * Token[<token> , <secret>]
	 */
	private Token buildTokenFromFile() {
		File archivedToken = new File(
				"/Users/micahsmith/workspace/playoff-odds/resources/archived_token.txt");
		// FOR application testing PURPOSES ONLY
		// TODO write token to file at close of program?

		try {
			Scanner codes = new Scanner(archivedToken);
			String lastUpdated = codes.nextLine();
			String lastRawResponse = codes.nextLine();
			String lastSecret = codes.nextLine();
			String lastToken = codes.nextLine();
			codes.close();

			// token is valid, and reusable, for exactly one hour per Yahoo
			if (System.currentTimeMillis() - Long.parseLong(lastUpdated) < ONE_HOUR) {
				return new Token(lastToken, lastSecret, lastRawResponse);
			}

		} catch (FileNotFoundException e) {
			// do nothing, we must process token ourselves.
		} catch (NoSuchElementException e) {
			// there are no entries in the codes file, we must process
			// ourselves.
		}

		Token result = buildToken();

		// Record token information in file for later recovery.
		try {
			writeTokenToFile(result, archivedToken);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private void writeTokenToFile(Token accessToken, File f) throws IOException {
		PrintWriter w = new PrintWriter(f);
		w.println(System.currentTimeMillis());
		w.println(accessToken.getRawResponse());
		w.println(accessToken.getSecret());
		w.println(accessToken.getToken());
		w.close();
	}

	private Token buildToken() {
		// Obtain the Request Token
		Token requestToken = service.getRequestToken();

		// Obtain authorization
		System.out.println("Authorize here:");
		System.out.println(service.getAuthorizationUrl(requestToken));
		System.out.println("And paste the verifier here");
		System.out.print(">>");

		Scanner in = new Scanner(System.in);
		Verifier verifier = new Verifier(in.nextLine());
		in.close();

		// Trade the Request Token and Verifier for the Access Token
		return service.getAccessToken(requestToken, verifier);
	}

	public void testResource(String resource){
		OAuthRequest request = new OAuthRequest(Verb.GET, resource);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println(response.getBody());
	}
}
