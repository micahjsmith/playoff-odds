package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.lang.Iterable;;


public class Roster implements Iterable<Player> {
	public static final int ROSTER_SIZE=10;
	private ArrayList<Player> roster;
	
	public Roster(File myFile){
		roster=new ArrayList<Player>(ROSTER_SIZE);
		try {
			Scanner rosterScanner = new Scanner(myFile);
			while (rosterScanner.hasNext()){
				String[] result = rosterScanner.nextLine().split(",");
				roster.add(new Player(result[0],Double.parseDouble(result[1]),Integer.parseInt(result[2])));
			}
			rosterScanner.close();
		} catch (FileNotFoundException e) {
			System.err.print("Roster file not found.");
			e.printStackTrace();
		}

	}

	@Override
	public Iterator<Player> iterator() {
		return roster.iterator();
	}


}
