package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.lang.Iterable;

/**
 * Be careful, as iterable only over starters.
 * 
 * @author micahsmith
 * 
 */
public class Roster implements Iterable<Player> {
	public static final int ROSTER_STARTERS_SIZE = 10;
	public static final int ROSTER_BENCH_SIZE = 5;
	public static final int ROSTER_SIZE = ROSTER_STARTERS_SIZE
			+ ROSTER_BENCH_SIZE;

	private ArrayList<Player> starters;
	private ArrayList<Player> bench;
	private int teamID;
	private int week;

	/**
	 * Construct a Roster object from a given array list of Players
	 * 
	 * @param roster
	 *            a Roster from the given Players.
	 */
	public Roster(ArrayList<Player> roster, int teamID, int week) {
		if (roster.size() != ROSTER_SIZE)
			System.err.println("Roster not of correct size (" + roster.size()
					+ " instead of " + ROSTER_SIZE + ")");

		starters = new ArrayList<Player>(ROSTER_STARTERS_SIZE);
		bench = new ArrayList<Player>(ROSTER_BENCH_SIZE);
		for (Player p : roster) {
			if (!p.getPosition().equals("BN") || !p.getPosition().equals("BN")) {
				starters.add(p);
			} else
				bench.add(p);
		}

		this.teamID = teamID;
		this.week = week;
	}

	/**
	 * Load a roster into memory from a file.
	 * 
	 * @param file
	 *            file with Roster
	 */
	public Roster(File file) {
		starters = new ArrayList<Player>(ROSTER_STARTERS_SIZE);
		bench = new ArrayList<Player>(ROSTER_BENCH_SIZE);
		Scanner rosterScanner = null;
		try {
			rosterScanner = new Scanner(file);
			while (rosterScanner.hasNextLine()) {
				String player = rosterScanner.nextLine();
				Player p = new Player(player);
				if (!p.getPosition().equals("BN")) {
					starters.add(p);
				} else
					bench.add(p);
			}
		} catch (FileNotFoundException e) {
			System.err.print("Roster file not found.");
			e.printStackTrace();
		} finally {
			rosterScanner.close();
		}

	}

	public Roster(File file, int teamID, int week) {
		this(file);
		this.teamID = teamID;
		this.week = week;
	}

	public double getRecordedScore() {
		double result = 0;
		for (Player p : starters) {
			if (!p.getPosition().equals("BN")) {
				result += p.getRecordedPoints();
			}
		}
		return result;
	}

	public double getProjectedScore() {
		double result = 0;
		for (Player p : starters) {
			if (!p.getPosition().equals("BN")) {
				result += p.getProjectedPoints();
			}
		}
		return result;
	}

	public int getTeamID() {
		return teamID;
	}

	public int getWeek() {
		return week;
	}

	@Override
	public Iterator<Player> iterator() {
		return starters.iterator();
	}

	/**
	 * Determine if the given position is valid within this league.
	 * 
	 * @param pos
	 * @return
	 */
	public static boolean isValidPosition(String pos) {
		// TODO update this so that the position names are an enum, possibly
		// TODO update this so that the position names are populated
		// automatically by a query to league settings through API
		if (pos.equals("QB") || pos.equals("RB") || pos.equals("WR")
				|| pos.equals("TE") || pos.equals("K") || pos.equals("DEF")
				|| pos.equals("D") || pos.equals("BN") || pos.equals("IR"))
			return true;
		else
			return false;
	}

	/**
	 * Determine if the eligible position of this player is for an IDP spot.
	 * 
	 * @param pos
	 *            the eligible position
	 * @return whether the position is for an IDP spot
	 */
	public static boolean isDefensivePosition(String pos) {
		if (pos.equals("S") || pos.equals("DT") || pos.equals("LB")
				|| pos.equals("CB") || pos.equals("DE"))
			return true;
		else
			return false;
	}

	/**
	 * 
	 */
	public void writeRosterToFile() throws IOException {
		String fileName = "resources/Roster_T" + teamID + "_W" + week + ".txt";
		PrintWriter w = new PrintWriter(fileName);
		try {
			for (Player p : starters) {
				w.println(p.toString());
			}
			for (Player p : bench) {
				w.println(p.toString());
			}
		} finally {
			w.close();
		}
	}

	/**
	 * Swap starters and bench to set the optimal lineup for that week.
	 */
	public void setOptimalLineup() {
		// loop through starters
		for (int i = 0; i < starters.size(); i++) {
			// if they have already played, we cannot swap them out. continue to
			// next player.
			if (starters.get(i).getRecordedPoints() > 0)
				continue;

			double points = starters.get(i).getProjectedPoints();
			String pos = starters.get(i).getPosition();

			// loop through bench
			for (int j = 0; j < bench.size(); j++) {
				// if they have already played, we cannot swap them in.
				if (bench.get(j).getRecordedPoints() > 0)
					continue;

				// loop through eligible positions
				String[] eligPos = bench.get(j).getEligiblePositions();
				double pointsBench = bench.get(j).getProjectedPoints();
				boolean swapped = false;
				for (int k = 0; k < eligPos.length; k++) {
					String posBench = eligPos[k];

					// if positions match and have higher projection
					if (pos.equals(posBench) && pointsBench > points) {

						// swap them.
						Player temp = starters.get(i);
						temp.setPosition("BN");
						bench.get(j).setPosition(eligPos[k]);
						starters.set(i, bench.get(j));
						bench.set(j, temp);

						// repeat analysis of this starter to see if better
						// decision possible.
						swapped = true;
						break;
					}
				}
				if (swapped) {
					i--;
					break;
				}

			}
		}
	}
}