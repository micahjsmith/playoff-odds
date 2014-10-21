package src;

/**
 * Models a Fantasy Football matchup for the purpose of simulating a season. A
 * Matchup consists of two Teams, their two Rosters, and a recorded or simulated
 * score.
 * 
 * @author micahsmith
 * 
 */
public class Matchup {
	private Team homeTeam;
	private Roster homeRoster;
	private double homeScore;
	private Team awayTeam;
	private Roster awayRoster;
	private double awayScore;

	/**
	 * Create a new Matchup instance.
	 * 
	 * @param homeTeam
	 *            the home Team
	 * @param homeRoster
	 *            the home Roster
	 * @param awayTeam
	 *            the away Team
	 * @param awayRoster
	 *            the away Roster
	 */
	public Matchup(Team homeTeam, Roster homeRoster, Team awayTeam,
			Roster awayRoster) {
		this.homeTeam = homeTeam;
		this.homeRoster = homeRoster;
		homeScore = homeRoster.getRecordedScore();
		this.awayTeam = awayTeam;
		this.awayRoster = awayRoster;
		awayScore = awayRoster.getRecordedScore();
	}

	/**
	 * Reset the home and away scores to 0 in order to run another simulation
	 */
	public void reset() {
		homeScore = 0;
		awayScore = 0;
	}

	/**
	 * Simulate the result of this Matchup. Draw random performances from each
	 * Player on each Roster. Add wins, ties, and losses to each Team's record
	 * as necessary by the outcome of the simulation.
	 */
	public void simulate() {
		//simulate home team's players
		for (Player p : homeRoster) {
			homeScore += p.nextPerformance();
		}
		//simulate away team's players
		for (Player q : awayRoster) {
			awayScore += q.nextPerformance();
		}
		if (homeScore > awayScore) { //home team wins 
			homeTeam.addWin();
			awayTeam.addLoss();
		} else if (homeScore < awayScore) { //away team wins
			homeTeam.addLoss();
			awayTeam.addWin();
		} else { // homeScore == awayScore
			homeTeam.addTie();
			awayTeam.addTie();
		}

		homeTeam.addPointsScored(homeScore);
		awayTeam.addPointsScored(awayScore);
	}
}
