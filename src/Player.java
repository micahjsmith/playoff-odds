package src;

import java.util.Random;

/**
 * Model a Fantasy Football player for the purpose of simulation. A Player
 * consists of a unique identifier, descriptive fields (i.e. name), and
 * projected performance for a certain week.
 * 
 * @author micahsmith
 * 
 */
public class Player {
	// identifying information of the player
	private String firstName;
	private String lastName;
	private String playerID; // Player id as accoding to Yahoo Fantasy Sports
								// API, can also be team ID (for defenses)
	private String rosterPosition;
	private String[] eligiblePositions;
	private String team;

	// scoring information of the player
	private double projectedPoints;
	private double recordedPoints;

	// other information for simulation purposes
	private double stdev;
	private Random r;

	// housekeeping
	private final static int N_FIELDS = 8;

	/**
	 * Create a new Player instance
	 * 
	 * @param firstName
	 *            the player's first name
	 * @param lastName
	 *            the player's last name
	 * @param rosterPosition
	 *            the player's rosterPosition
	 * @param team
	 *            the player's (real life) team
	 * @param projectedPoints
	 *            projected points for a single week
	 * @param hasPlayed
	 */
	public Player(String firstName, String lastName, String playerID,
			String team, String rosterPosition, String[] eligiblePositions,
			double recordedPoints, double projectedPoints) {
		this.firstName = firstName;
		this.lastName = lastName;
		lastName = lastName.trim();
		this.playerID = playerID;
		this.rosterPosition = rosterPosition;
		
		//set up eligible positions.
		this.eligiblePositions = eligiblePositions;
		cleanEligiblePositions();
		
		this.team = team;
		this.recordedPoints = recordedPoints;
		this.projectedPoints = projectedPoints;

		// This is a crude way for now to estimate standard deviation. Future
		// versions of the program will improve this.
		// TODO improve stdev calc.
		stdev = 3.0;
		r = new Random();
	}

	private void cleanEligiblePositions() {
		for (int i= 0; i<eligiblePositions.length; i++){
			if (Roster.isDefensivePosition(eligiblePositions[i]))
				eligiblePositions[i] = "D";
		}
	}

	/**
	 * Construct a player from a single string with all necessary information.
	 * Any string output by the toString method is guaranteed to result in a
	 * successful input to this constructor.
	 * 
	 * @param fullySpecifiedPlayerInfo
	 *            a fully specified player info string, according to the
	 *            specifications of toString()
	 */
	public Player(String fullySpecifiedPlayerInfo) {
		String[] info = fullySpecifiedPlayerInfo.split(",");
		if (info.length != N_FIELDS)
			System.err.println("Input string incorrectly specified ("
					+ info.length + "fields only)");
		else {
			this.firstName = info[0];
			this.lastName = info[1];
			lastName = lastName.trim();
			this.playerID = info[2];
			this.rosterPosition = info[3];
			this.eligiblePositions = info[4].split(";");
			cleanEligiblePositions();
			this.team = info[5];
			this.recordedPoints = Double.parseDouble(info[6]);
			this.projectedPoints = Double.parseDouble(info[7]);
		}

		stdev = 3.0;
		r = new Random();
	}

	/**
	 * Test if a valid Yahoo-specified player ID has been assigned to this
	 * Player object.
	 * 
	 * @return whether a valid player ID has been assigned to this Player object
	 */
	public boolean hasPlayerID() {
		return playerID != "";
	}

	/**
	 * Return the Yahoo-specified player ID. Returns -1 if no player ID has been
	 * assigned to this Player object.
	 * 
	 * @return player ID, or -1 of no player ID has been assigned to this Player
	 *         object.
	 */
	public String getPlayerID() {
		return playerID;
	}

	/**
	 * Return the roster position of this Player
	 * 
	 * @return the roster position of this Player
	 */
	public String getPosition() {
		return rosterPosition;
	}

	/**
	 * Return the eligible positions of this Player
	 * @return the eligible positions of this Player.
	 */
	public String[] getEligiblePositions(){
		return eligiblePositions;
	}
	
	/**
	 * Return the (real life) team of this Player
	 * 
	 * @return return the team of this Player
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Return the first name of this Player
	 * 
	 * @return the name of this Player
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Return the last name of this Player
	 * 
	 * @return the name of this Player
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Return the Yahoo-supplied projected points of the Player
	 * 
	 * @return the projected points
	 */
	public double getProjectedPoints() {
		return projectedPoints;
	}

	/**
	 * Return the Yahoo-supplied recorded points of the Player
	 * 
	 * @return the recorded points
	 */
	public double getRecordedPoints() {
		return recordedPoints;
	}

	/**
	 * Set projected points to a given value
	 * 
	 * @param projectedPoints
	 *            value to set this Player's projected points
	 */
	public void setProjectedPoints(double projectedPoints) {
		this.projectedPoints = projectedPoints;
	}

	/**
	 * Return the standard deviation
	 * 
	 * @return the standard deviation
	 */
	public double getStdev() {
		return stdev;
	}

	/**
	 * Set the standard deviation
	 * 
	 * @param stdev
	 *            the standard deviation
	 */
	public void setStdev(double stdev) {
		this.stdev = stdev;
	}

	/**
	 * If the player has a recorded performance in the given situation, return
	 * the recorded perforamnce. Otherwise, return a randomly drawn fantasy
	 * performance. The weekly performance of each player is modeled as a normal
	 * random variable with mean <projectedPoints> as supplied by Yahoo and
	 * standard error <stdev> calculated as a specified fraction of
	 * projectedPoints. This derivation is suspect, to say the least, and will
	 * be improved in future versions.
	 * 
	 * @return the recorded performance or a randomly drawn fantasy performance
	 */
	public double nextPerformance() {
		if (recordedPoints > 0)
			return recordedPoints;
		else
			return r.nextGaussian() * stdev + projectedPoints;
	}

	/**
	 * Test if this Player instance refers to the same unique player as another
	 * Player instance.
	 * 
	 * @param other
	 *            the other Player
	 * @return whether this Player instance refers to the same unique player as
	 *         another Player instance
	 */
	public boolean equals(Player other) {
		// If both objects have player IDs assigned, this is an unique
		// identifier.
		if (other.hasPlayerID() && hasPlayerID())
			return other.getPlayerID() == this.playerID;

		// Otherwise, we must make other checks.
		boolean areEqual = true;
		areEqual = areEqual & other.getFirstName().equals(firstName);
		areEqual = areEqual & other.getLastName().equals(lastName);
		areEqual = areEqual & other.getTeam().equals(team);
		areEqual = areEqual & other.getPosition().equals(rosterPosition);

		return areEqual;
	}

	/**
	 * Overload the default toString() method.
	 */
	public String toString() {
		String eligPosString = eligiblePositions[0];
		for (int i = 1; i < eligiblePositions.length; i++) {
			eligPosString += ";" + eligiblePositions[i];
		}

		return String.format("%s,%s,%s,%s,%s,%s,%2.2f,%2.2f", firstName,
				lastName, playerID, rosterPosition, eligPosString, team,
				recordedPoints, projectedPoints);
	}

	/**
	 * Set the roster position
	 * @param rosterPosition
	 */
	public void setPosition(String rosterPosition) {
		this.rosterPosition = rosterPosition;
	}
}
