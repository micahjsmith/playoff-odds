package src;
import java.util.ArrayList;
import java.util.Iterator;

public class Schedule implements Iterable<Matchup> {
	private ArrayList<Matchup> matchups;
	private int week;

	public Schedule(int week, int numberOfTeams) {
		matchups=new ArrayList<Matchup>(numberOfTeams/2);
		this.week = week;
	}

	public void resetMatchups(){
		for (Matchup m : matchups){
			m.reset();
		}
	}
	
	public int getWeek(){
		return week;
	}
	
	public void setMatchup(Matchup myMatchup, int index){
		matchups.add(index, myMatchup);
	}

	@Override
	public Iterator<Matchup> iterator() {
		return matchups.iterator();
	}

}
