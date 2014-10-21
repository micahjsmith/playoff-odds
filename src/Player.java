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
	private String position;
	private String team;

	// scoring information of the player
	private double projectedPoints;
	private double recordedPoints;

	// other information for simulation purposes
	private boolean hasPlayed;
	private double stderr;
	private Random r;

	// housekeeping
	private final static int N_FIELDS = 7;

	/**
	 * Create a new Player instance
	 * 
	 * @param firstName
	 *            the player's first name
	 * @param lastName
	 *            the player's last name
	 * @param position
	 *            the player's position
	 * @param team
	 *            the player's (real life) team
	 * @param projectedPoints
	 *            projected points for a single week
	 * @param hasPlayed
	 */
	public Player(String firstName, String lastName, String playerID,
			String team, String position, double recordedPoints,
			double projectedPoints) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.playerID = playerID;
		this.position = position;
		this.team = team;
		this.recordedPoints = recordedPoints;
		this.projectedPoints = projectedPoints;
		// This is a crude way for now to estimate standard error. Future
		// versions of the program will improve this.
		// TODO improve stderr calc.
		stderr = projectedPoints / 3.0;

		r = new Random();
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
			this.playerID = info[2];
			this.position = info[3];
			this.team = info[4];
			this.recordedPoints = Double.parseDouble(info[5]);
			this.projectedPoints = Double.parseDouble(info[6]);
		}
		
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
	 * Return the position of this Player
	 * 
	 * @return the position of this Player
	 */
	public String getPosition() {
		return position;
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
	 * Return the standard error
	 * 
	 * @return the standard error
	 */
	public double getStderr() {
		return stderr;
	}

	/**
	 * Set the standard error
	 * 
	 * @param stderr
	 *            the standard error
	 */
	public void setStderr(double stderr) {
		this.stderr = stderr;
	}

	/**
	 * Return a randomly drawn fantasy performance. The weekly performance of
	 * each player is modeled as a normal random variable with mean
	 * <projectedPoints> as supplied by Yahoo and standard error <stderr>
	 * calculated as a specified fraction of projectedPoints. This derivation is
	 * suspect, to say the least, and will be improved in future versions.
	 * 
	 * @return a randomly drawn fantasy performance
	 */
	public double nextPerformance() {
		if (hasPlayed)
			return projectedPoints;
		else
			return r.nextGaussian() * stderr + projectedPoints;
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
		areEqual = areEqual & other.getPosition().equals(position);

		return areEqual;
	}

	/**
	 * Overload the default toString() method.
	 */
	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%2.2f,%2.2f", firstName, lastName,
				playerID, position, team, recordedPoints, projectedPoints);
	}
}
