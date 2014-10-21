package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.lang.Iterable;

public class Roster implements Iterable<Player> {
	public static final int ROSTER_STARTERS_SIZE = 10;
	public static final int ROSTER_BENCH_SIZE = 5;
	public static final int ROSTER_SIZE = ROSTER_STARTERS_SIZE
			+ ROSTER_BENCH_SIZE;

	private ArrayList<Player> roster;
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

		this.roster = roster;
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
		roster = new ArrayList<Player>(ROSTER_SIZE);
		Scanner rosterScanner = null;
		try {
			rosterScanner = new Scanner(file);
			while (rosterScanner.hasNextLine()) {
				String player = rosterScanner.nextLine();
				roster.add(new Player(player));
			}
		} catch (FileNotFoundException e) {
			System.err.print("Roster file not found.");
			e.printStackTrace();
		} finally {
			rosterScanner.close();
		}
	}

	public double getRecordedScore() {
		double result = 0;
		for (Player p : roster) {
			if (!p.getPosition().equals("BN")) {
				result += p.getRecordedPoints();
			}
		}
		return result;
	}
	
	public double getProjectedScore(){
		double result = 0;
		for (Player p : roster) {
			if (!p.getPosition().equals("BN")) {
				result += p.getProjectedPoints();
			}
		}
		return result;
	}

	@Override
	public Iterator<Player> iterator() {
		return roster.iterator();
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
				|| pos.equals("D") || pos.equals("BN"))
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
			for (Player p : roster) {
				w.println(p.toString());
			}
		} finally {
			w.close();
		}
	}
}
