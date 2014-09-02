package src;

/**
 * Models a Fantasy Football team for the purpose of simulation. A team
 * consists of a unique identifier and a number of wins, losses, ties, and total
 * points scored that quanitifies its performance. Each week, a Roster belongs
 * to a specific team and two Rosters compose a single Matchup.
 * 
 * @author micahsmith
 * 
 */
public class Team implements Comparable<Team> {
	private final String name;
	private int wins;
	private final int winsRealized;
	private int losses;
	private final int lossesRealized;
	private int ties;
	private final int tiesRealized;
	private double totalPointsScored;
	private final double totalPointsScoredRealized;
	private double playoffAppearances;

	/**
	 * Create a new Team instance. 
	 * 
	 * @param name
	 *            the name of the team
	 * @param winsRealized
	 *            number of wins recorded at the time the simulation is started
	 * @param lossesRealized
	 *            number of losses recorded at the time the simulation is
	 *            started
	 * @param tiesRealized
	 *            number of ties recorded at the time the simulation is started
	 * @param totalPointsScoredRealized
	 *            total points scored at the time the simulation is started
	 */
	public Team(String name, int winsRealized, int lossesRealized,
			int tiesRealized, double totalPointsScoredRealized) {
		this.name = name;
		this.wins = winsRealized;
		this.winsRealized = winsRealized;
		this.losses = lossesRealized;
		this.lossesRealized = lossesRealized;
		this.ties = tiesRealized;
		this.tiesRealized = tiesRealized;
		this.totalPointsScored = totalPointsScoredRealized;
		this.totalPointsScoredRealized = totalPointsScoredRealized;
		playoffAppearances = 0;
	}

	/**
	 * Reset wins, ties, losses, and total points scored to their realized
	 * values for the purposes of running another simulation. Realized values
	 * refers to the actual values in the league at the time the simulation is
	 * run.
	 */
	public void reset() {
		wins = winsRealized;
		ties = tiesRealized;
		losses = lossesRealized;
		totalPointsScored = totalPointsScoredRealized;
	}

	/**
	 * Return the name of the team
	 * 
	 * @return the name of the team
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the number of playoff appearances the team has recorded over the
	 * course of the simulation
	 * 
	 * @return number of playoff appearances recorded
	 */
	public double getPlayoffAppearances() {
		return playoffAppearances;
	}

	/**
	 * Increment the number of playoff appearances recorded over the course of
	 * the simulation
	 */
	public void addPlayoffAppearance() {
		playoffAppearances++;
	}

	/**
	 * Return the number of wins recorded by the team
	 * 
	 * @return the number of wins recorded by the team
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * Set the number of wins recorded by the team
	 * 
	 * @param wins
	 *            number of wins recorded
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}

	/**
	 * Increment the number of wins recorded by the team
	 */
	public void addWin() {
		wins++;
	}

	/**
	 * Return the number of losses recorded by the team
	 * 
	 * @return the number of losses recorded by the team
	 */
	public int getLosses() {
		return losses;
	}

	/**
	 * Set the number of losses recorded by the team
	 * 
	 * @param tosses
	 *            number of losses recorded
	 */
	public void setLosses(int losses) {
		this.losses = losses;
	}

	/**
	 * Increment the number of losses recorded by the team
	 */
	public void addLoss() {
		losses++;
	}

	/**
	 * Return the number of ties recorded by the team
	 * 
	 * @return the number of ties recorded by the team
	 */
	public int getTies() {
		return ties;
	}

	/**
	 * Set the number of ties recorded by the team
	 * 
	 * @param ties
	 *            number of ties recorded
	 */
	public void setTies(int ties) {
		this.ties = ties;
	}

	/**
	 * Increment the number of ties recorded by the team
	 */
	public void addTie() {
		ties++;
	}

	/**
	 * Return total points scored by team
	 * 
	 * @return total points scored by team
	 */
	public double getTotalPointsScored() {
		return totalPointsScored;
	}

	/**
	 * Set total points scored by team to given value
	 * 
	 * @param totalPointsScored
	 *            given value of total points scored
	 */
	public void setTotalPointsScored(double totalPointsScored) {
		this.totalPointsScored = totalPointsScored;
	}

	/**
	 * Add given value to total points scored by team.
	 * 
	 * @param pointsScored
	 *            given value to add to total points scored
	 */
	public void addPointsScored(double pointsScored) {
		totalPointsScored += pointsScored;
	}

	/**
	 * Return string representation of team.
	 */
	public String toString() {
		return name + ", " + wins + "-" + losses + "-" + ties + ", "
				+ totalPointsScored;
	}

	/**
	 * Provide a custom comparator. Team A > Team B iff Team A leads Team B in
	 * the standings.
	 */
	@Override
	public int compareTo(Team o) {
		if (wins > o.getWins())
			return 1;
		if (wins < o.getWins())
			return -1;
		else {
			if (totalPointsScored > o.getTotalPointsScored())
				return 1;
			if (totalPointsScored < o.getTotalPointsScored())
				return -1;
			return 0;
		}

	}

}
