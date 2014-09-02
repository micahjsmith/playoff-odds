package src;
import java.util.ArrayList;
import java.util.Iterator;

public class Schedule implements Iterable<Matchup> {
	// Match-ups in week 1 have index 0 to (1*(NUMBER_OF_TEAMS/2) - 1), match
	// ups in week 2 have index (1*(NUMBER_OF_TEAMS/2) to
	// (2*(NUMBER_OF_TEAMS/2)-1), etc.
	private ArrayList<Matchup> matchups;
	public final int NUMBER_OF_TEAMS;

	public Schedule(int regularSeasonWeeks, int numberOfTeams) {
		NUMBER_OF_TEAMS = numberOfTeams;
		matchups=new ArrayList<Matchup>(regularSeasonWeeks*(NUMBER_OF_TEAMS/2));
	}

	public void resetMatchups(){
		for (Matchup m : matchups){
			m.reset();
		}
	}
	
	public void setMatchup(Matchup myMatchup, int index){
		matchups.add(index, myMatchup);
	}

	@Override
	public Iterator<Matchup> iterator() {
		return matchups.iterator();
	}

}
