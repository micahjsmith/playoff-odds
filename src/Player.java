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
	// Player id as according to Yahoo Fantasy Sports API
	private int playerID;

	// Name should be formatted as <FirstInitial>. <LastName>
	private String name;

	private boolean hasPlayed;
	private double projectedPoints;
	private double stderr;
	private Random r;

	/**
	 * Create a "default" player from a given name, with projected points a
	 * standard normal.
	 * 
	 * @param name
	 *            player's name
	 */
	public Player(String name) {
		playerID = 0;
		this.name = name;
		hasPlayed=false;
		projectedPoints = 0;
		stderr = 1;
		r = new Random();
	}

	/**
	 * Create a new Player instance
	 * 
	 * @param name
	 *            the player's name
	 * @param projectedPoints
	 *            projected points for a single week
	 * @param hasPlayed
	 */
	public Player(String name, double projectedPoints, int hasPlayed) {
		this.name = name;
		this.projectedPoints = projectedPoints;

		// This is a crude way for now to estimate standard error. Future
		// versions of the program will improve this.
		stderr = projectedPoints / 3.0;

		r = new Random();

		if (hasPlayed == 1)
			this.hasPlayed = true;
		else
			this.hasPlayed = false;
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
		if (other.getName().equals(name))
			return true;
		return false;
	}

	/**
	 * Return the name of this Player, formatted as <FirstInitial>. <LastName>
	 * 
	 * @return the name of this Player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the player ID
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
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
	 * Return the Yahoo-supplied projected points of the Player
	 * @return
	 */
	public double getProjectedPoints() {
		return projectedPoints;
	}

	/**
	 * Set projected points to a given value
	 * @param projectedPoints value to set this Player's projected points
	 */
	public void setProjectedPoints(double projectedPoints) {
		this.projectedPoints = projectedPoints;
	}

	/**
	 * Return the standard error
	 * @return the standard error
	 */
	public double getStderr() {
		return stderr;
	}

	/**
	 * Set the standard error
	 * @param stderr the standard error
	 */
	public void setStderr(double stderr) {
		this.stderr = stderr;
	}

}
