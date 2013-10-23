import java.util.Scanner;

import org.scribe.oauth.*;
import org.scribe.model.*;
import org.scribe.builder.*;
import org.scribe.builder.api.*;


public class LeagueInfoParser{
	private String baseURI = "http://fantasysports.yahooapis.com/fantasy/v2/";
	private OAuthService service;
	private Token accessToken;
	
	/**
	 * Open connection to Yahoo! API service to access protected information.
	 */
	public LeagueInfoParser(){
		//The following skeleton is due to Scribe example
		service = new ServiceBuilder()
		.provider(YahooApi.class)
		.apiKey("dj0yJmk9TUdoV3NDdDhNSGNtJmQ9WVdrOU4wVTRiVmhwTkdjbWNHbzlPRFE1TVRRNU5EWXkmcz1jb25zdW1lcnNlY3JldCZ4PTQ3")
		.apiSecret("041d6cc9ae3961aa987e3769f1809d572ee09a79")
		.build();
		Scanner in = new Scanner(System.in);

		// 	Obtain the Request Token
		Token requestToken = service.getRequestToken();

		// Obtain authorization
		System.out.println("Authorize here:");
		System.out.println(service.getAuthorizationUrl(requestToken));
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		in.close();
		
		// 	Trade the Request Token and Verifier for the Access Token
		accessToken = service.getAccessToken(requestToken, verifier);
	}
	
	public League parseLeague(){
		OAuthRequest request = new OAuthRequest(Verb.GET, baseURI + "league/nfl.l.6250/teams");
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    
	    //if code says okay, proceed with analyzing results, otherwise, return error message.
	    String body = response.getBody();
	    League result = new League(parseXml("league_key",body));
	    result.setCurrent_week(parseXml("current_week",body));
	    result.setDraft_status(parseXml("draft_status",body));
	    result.setLeague_id(parseXml("league_id",body));
	    result.setName(parseXml("name",body));
	    result.setUrl(parseXml("url",body));
	    result.setEnd_date(parseXml("end_date",body));
	    result.setEnd_week(parseXml("end_week",body));
	    result.setStart_date(parseXml("start_date",body));
	    result.setStart_week(parseXml("start_week",body));
	    result.setNum_teams(parseXml("num_teams",body));
	    return result;
	}
	
	/*
	 * returns the string if tag is found
	 * TODO: throws error if not found
	 */
	private String parseXml(String tag, String body){
		String result = body.substring(body.indexOf("<"+tag+">")+tag.length(), body.indexOf("</"+tag+">"));
		return result;
	}
	public Schedule parseSchedule(){
		return null;
	}
	
}
