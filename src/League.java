package src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class League {
	private Team[] teams;
	public static final int N_TEAMS = 10;
	public static final int N_WEEKS = 16;
	public static final int N_REG_SEASON_WEEKS = 14;
	public static final int N_PLAYOFF_WEEKS = 2;
	private int currentWeek = 9;
	private Schedule[] schedules;

	public League(Team[] teams, Schedule[] schedules) {
		this.teams = teams;
		this.schedules = schedules;
	}

	public Team[] getTeams() {
		return teams;
	}

	public int getCurrentWeek() {
		return currentWeek;
	}

	public Schedule getSchedule(int week) {
		return schedules[week - 1];
	}

	public Team chooseTeam(int teamID) {
		for (int i = 0; i < teams.length; i++) {
			if (teamID == teams[i].getTeamID())
				return teams[i];
		}
		return null;
	}

	public void resetTeams() {
		for (int i = 0; i < teams.length; i++) {
			teams[i].reset();
		}

	}

	public void resetMatchups() {
		for (int i = 0; i < schedules.length; i++) {
			schedules[i].resetMatchups();
		}

	}

	/*
	 * Let y^hat_{i,t} ~ N(mu_{i,t}, sigma_i) We want to estimate sigma_i so we
	 * can draw y^hat_{i,t}.
	 */
	public ArrayList<PlayerPerformance> calculatePlayerVariance() {
		// Add all players to a set.
		Set<PlayerPerformance> allPlayers = new HashSet<PlayerPerformance>();
		for (int i = 0; i < currentWeek; i++) {
			for (Matchup m : schedules[0]) {
				Roster r = m.getAwayRoster();
				for (PlayerPerformance p : r) {
					allPlayers.add(p);
				}
			}
		}

		// For each player, calculate the variance of their recorded points, and
		// save to player.
		for (PlayerPerformance p : allPlayers) {
			System.out.println("hello");
		}

		//Print results to file.
		return null;
	}
}
