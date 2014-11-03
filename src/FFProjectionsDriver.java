package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FFProjectionsDriver {

	public static String baseURI = "http://football.fantasysports.yahoo.com/f1/";
	public static String leagueID = "27235/";

	public static void main(String[] args) {
		// Download all rosters.
		downloadAllRosters();

		// Load rosters into memory
		Roster[][] rosters = new Roster[League.N_WEEKS][League.N_TEAMS];
		for (int i = 1; i <= League.N_TEAMS; i++) {
			for (int j = 1; j <= League.N_WEEKS; j++) {
				String rosterPath = String.format("resources/Roster_T%d_W%d.txt", i, j);
				rosters[j - 1][i - 1] = new Roster(new File(rosterPath), i,j);
			}
		}

		// Load teams ("league") into memory
		Scanner teamsScanner = null;
		Team[] teams = new Team[League.N_TEAMS];
		try {
			teamsScanner = new Scanner(new File("resources/League.txt"));
			for (int i = 0; i < League.N_TEAMS; i++) {
				teams[i] = new Team(teamsScanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			teamsScanner.close();
		}

		// Load schedules into memory
		Schedule[] schedules = new Schedule[League.N_REG_SEASON_WEEKS];
		for (int week = 0; week < League.N_REG_SEASON_WEEKS; week++) {
			Scanner scheduleScanner = null;
			String schedulePath = String.format("resources/Schedule_W%d.txt", week + 1);
			schedules[week] = new Schedule(week + 1, League.N_TEAMS);
			try {
				scheduleScanner = new Scanner(new File(schedulePath));
				for (int k = 0; k < League.N_TEAMS / 2; k++) {
					String myMatchupString = scheduleScanner.nextLine();
					String[] myMatchupStringSplit = myMatchupString.split(",");
					int teamIndex1 = Integer.parseInt(myMatchupStringSplit[0]);
					int teamIndex2 = Integer.parseInt(myMatchupStringSplit[1]);
					Matchup myMatchup = new Matchup(teams[teamIndex1 - 1],
							rosters[week][teamIndex1 - 1],
							teams[teamIndex2 - 1],
							rosters[week][teamIndex2 - 1]);
					schedules[week].setMatchup(myMatchup, k);
					
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				scheduleScanner.close();
			}

		} //End of load schedules
		
		League myLeague = new League(teams,schedules);
		Simulator simulator = new Simulator(myLeague);
		simulator.simulate();
		
		

	} //End of main method

	/*
	 * Download all rosters, parse into Roster format, and write to file.
	 */
	private static void downloadAllRosters() {
		YFFParser parser = new YFFParser(baseURI, leagueID);
		for (int i = 1; i <= League.N_TEAMS; i++) {
			for (int j = 1; j <= League.N_WEEKS; j++) {
				Roster r1 = parser.parseRoster(i, j);
				r1.setOptimalLineup();
				try {
					r1.writeRosterToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}//End of class
