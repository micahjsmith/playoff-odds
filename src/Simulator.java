package src;

import java.util.Arrays;

public class Simulator {
	League league;
	private final int N_SIMULATIONS = 100000;
	private final int CURRENT_WEEK = 9;

	public Simulator(League league) {
		this.league = league;
	}

	public void simulate() {
		for (int i = 0; i < N_SIMULATIONS; i++) {
			simulateAllGames();
			addPlayoffAppearances();
			reset();
		}

		Arrays.sort(league.getTeams());
		for (int i = league.getTeams().length - 1; i >= 0; i--) {
			String teamName = league.getTeams()[i].getName();
			double playoffPct = league.getTeams()[i].getPlayoffAppearances() * 1.0 / N_SIMULATIONS;
			System.out.format("%s: %1.3f\n",teamName, playoffPct);
		}
	}

	private void simulateAllGames() {
		for (int week = CURRENT_WEEK; week <= League.N_REG_SEASON_WEEKS; week++) {
			for (Matchup m : league.getSchedule(week)) {
				m.simulate();
			}
		}
	}

	private void addPlayoffAppearances() {
		Arrays.sort(league.getTeams());
		for (int i = 6; i < 10; i++) {
			league.getTeams()[i].addPlayoffAppearance();
		}
	}

	private void reset() {
		league.resetTeams();
		league.resetMatchups();
	}
}
