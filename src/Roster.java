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
		for (Player p : roster){
			if (!p.getPosition().equals("BN")){
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
				if (!p.getPosition().equals("BN")){
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

	public Roster(File file, int teamID, int week){
		this(file);
		this.teamID=teamID;
		this.week=week;
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
	
	public double getProjectedScore(){
		double result = 0;
		for (Player p : starters) {
			if (!p.getPosition().equals("BN")) {
				result += p.getProjectedPoints();
			}
		}
		return result;
	}

	public int getTeamID(){
		return teamID;
	}
	
	public int getWeek(){
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
			for (Player p : starters) {
				w.println(p.toString());
			}
		} finally {
			w.close();
		}
	}
}
