package src;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Simulator {
	League myLeague;
	private final int NUMBER_OF_SIMULATIONS = 100000;

	public Simulator() {
		try {
			myLeague = new League("League.txt", "Schedule.txt");
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void simulate(){
		for (int i=0; i<NUMBER_OF_SIMULATIONS; i++){
			simulateAllGames();
			addPlayoffAppearances();
			reset();
		}
		
		Arrays.sort(myLeague.getTeams());
		for (int i=myLeague.getTeams().length-1; i>=0; i--){
			System.out.println(myLeague.getTeams()[i].getName() + ":  "+myLeague.getTeams()[i].getPlayoffAppearances()/100000.0);
		}
	}
	
	private void simulateAllGames() {
		for (Matchup m : myLeague.getSchedule()){
			m.simulate();
		}
	}
	
	private void addPlayoffAppearances() {
		Arrays.sort(myLeague.getTeams());
		//for (int i=0; i<10; i++)
		//System.out.println(myLeague.getTeams()[i]);
		for (int i=6; i<10; i++){
			myLeague.getTeams()[i].addPlayoffAppearance();
		}
	}
	
	private void reset(){
		for (int i=0; i<10; i++){
			myLeague.getTeams()[i].reset();
		}
		for (int i=0; i<15; i++){
			myLeague.getSchedule().resetMatchups();
		}
	}
}
