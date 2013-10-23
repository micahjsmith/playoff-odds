
public class League {
	private Team[] teams;
	private String league_key;
	private String league_id;
	private String name;
	private String url;
	private String draft_status;
	private String current_week;
	private String num_teams;
	private String start_week;
	private String start_date;
	private String end_week;
	private String end_date;
	
	public League(String leagueKey){
		this.league_key = leagueKey;
		System.out.println(leagueKey);
	}
	
	public String getLeague_key() {
		return league_key;
	}

	public void setLeague_key(String league_key) {
		this.league_key = league_key;
	}

	public String getLeague_id() {
		return league_id;
	}

	public void setLeague_id(String league_id) {
		this.league_id = league_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDraft_status() {
		return draft_status;
	}

	public void setDraft_status(String draft_status) {
		this.draft_status = draft_status;
	}

	public String getCurrent_week() {
		return current_week;
	}

	public void setCurrent_week(String current_week) {
		this.current_week = current_week;
	}

	public String getNum_teams() {
		return num_teams;
	}

	public void setNum_teams(String num_teams) {
		this.num_teams = num_teams;
	}

	public String getStart_week() {
		return start_week;
	}

	public void setStart_week(String start_week) {
		this.start_week = start_week;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_week() {
		return end_week;
	}

	public void setEnd_week(String end_week) {
		this.end_week = end_week;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	public Team[] getTeams() {
		return teams;
	}

	public void setTeams(Team[] teams) {
		this.teams = teams;
	}

}