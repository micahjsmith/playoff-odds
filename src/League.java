package src;

public class League {
	private Team[] teams;
	public static final int N_TEAMS = 10;
	public static final int N_WEEKS = 16;
	public static final int N_REG_SEASON_WEEKS = 14;
	public static final int N_PLAYOFF_WEEKS = 2;
	private Schedule[] schedules;

	public League(Team[] teams, Schedule[] schedules){
		this.teams = teams;
		this.schedules = schedules;
	}

	public Team[] getTeams() {
		return teams;
	}

	public Schedule getSchedule(int week) {
		return schedules[week-1];
	}

	public Team chooseTeam(int teamID) {
		for (int i = 0; i < teams.length; i++) {
			if (teamID==teams[i].getTeamID())
				return teams[i];
		}
		return null;
	}

	public void resetTeams() {
		for (int i = 0; i < teams.length; i++){
			teams[i].reset();
		}
		
	}

	public void resetMatchups() {
		for (int i = 0; i < schedules.length; i++){
			schedules[i].resetMatchups();
		}
		
	}
}
