import java.io.FileNotFoundException;
import java.util.Arrays;

public class Simulator{
    League myLeague;
    private final int NUMBER_OF_SIMS = 100000;

    public Simulator(){
        String leagueURI = "nfl.l.6250";
        myLeague = new League(leagueURI);
        }
/*
    public void simulate(){
        for (int i=0; i<NUMBER_OF_SIMS; i++){
            simulateAllGames();
            addPlayoffAppearances();
            reset();
            }
        Arrays.sort(myLeague.getTeams());
        for (int i=myLeague.getTeams.length-1; i>=0; i--){//really best way?
            System.out.println(myLeague.getTeams()[i].getName() + ": "
                + myLeague.getTeams()[i].getPlayoffAppearances()/100000.0);
        }
    }
*/
/*
    private void simulateAllGames(){
        for (Matchup m: myLeague.getSchedule()){
            m.simulate();
        }
    }
*/
}
