package src;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Simulator{
    private LeagueInfoParser parser;
    private League league;
    private final int NUMBER_OF_SIMS = 100000;

    public Simulator(){
        parser = new LeagueInfoParser();
        //parser.testResource("http://fantasysports.yahooapis.com/fantasy/v2/player/nfl.p.5479/stats;type=week;week=8");
        league = parser.parseLeague();        
        if(true);//breakpoint here
    }

    public void simulate(){/*
        for (int i=0; i<NUMBER_OF_SIMS; i++){
            simulateAllGames();
            addPlayoffAppearances();
            reset();
            }
        Arrays.sort(myLeague.getTeams());
        for (int i=myLeague.getTeams.length-1; i>=0; i--){//really best way?
            System.out.println(myLeague.getTeams()[i].getName() + ": "
                + myLeague.getTeams()[i].getPlayoffAppearances()/100000.0);
        }*/
    }

/*
    private void simulateAllGames(){
        for (Matchup m: myLeague.getSchedule()){
            m.simulate();
        }
    }
*/
}
