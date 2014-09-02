package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class League {
	private Team[] teams;
	private Schedule schedule;

	public League(String leaguePath, String schedulePath)
			throws FileNotFoundException {
		teams = new Team[10];
		schedule = new Schedule(2, 10);

		// Fill out teams.
		Scanner teamsScanner = new Scanner(new File(leaguePath));
		for (int i = 0; i < 10; i++) {
			String[] result = teamsScanner.nextLine().split(",");
			teams[i] = new Team(result[0], Integer.parseInt(result[1]),
					Integer.parseInt(result[2]), Integer.parseInt(result[3]),
					Double.parseDouble(result[4]));
		}
		teamsScanner.close();

		// Fill out schedule.
		Scanner scheduleScanner = new Scanner(new File(schedulePath));
		for (int i = 0; i < schedule.NUMBER_OF_TEAMS; i++) {
			String[] result = scheduleScanner.nextLine().split(",");
			schedule.setMatchup(new Matchup(chooseTeam(result[0]), new Roster(
					new File(result[1]+".txt")), chooseTeam(result[2]), new Roster(
					new File(result[3]+".txt"))), i);
		}
		scheduleScanner.close();

	}

	public Team[] getTeams() {
		return teams;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	private Team chooseTeam(String teamName) {
		for (int i = 0; i < teams.length; i++) {
			if (teamName.equals(teams[i].getName()))
				return teams[i];
		}
		return null;
	}
}
