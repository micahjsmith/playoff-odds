package src;

public class League {
	//information from ../league_key/teams
	private Team[] teams;
	
	// information from ../league_key/metadata
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
	
	// information from ../league_key/settings
	private String scoring_type;
	private String uses_playoff;
	private String has_playoff_consolation_games;
	private String playoff_start_week;
	private String uses_playoff_reseeding;
	private String num_playoff_teams;
	private String num_playoff_consolation_teams;
	
	//information from ../league_key/players;status=T
	private Player[] players;
	
	public League(String leagueKey){
		this.league_key = leagueKey;
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

	public String getScoring_type() {
		return scoring_type;
	}

	public void setScoring_type(String scoring_type) {
		this.scoring_type = scoring_type;
	}

	public String getUses_playoff() {
		return uses_playoff;
	}

	public void setUses_playoff(String uses_playoff) {
		this.uses_playoff = uses_playoff;
	}

	public String getHas_playoff_consolation_games() {
		return has_playoff_consolation_games;
	}

	public void setHas_playoff_consolation_games(
			String has_playoff_consolation_games) {
		this.has_playoff_consolation_games = has_playoff_consolation_games;
	}

	public String getPlayoff_start_week() {
		return playoff_start_week;
	}

	public void setPlayoff_start_week(String playoff_start_week) {
		this.playoff_start_week = playoff_start_week;
	}

	public String getUses_playoff_reseeding() {
		return uses_playoff_reseeding;
	}

	public void setUses_playoff_reseeding(String uses_playoff_reseeding) {
		this.uses_playoff_reseeding = uses_playoff_reseeding;
	}

	public String getNum_playoff_teams() {
		return num_playoff_teams;
	}

	public void setNum_playoff_teams(String num_playoff_teams) {
		this.num_playoff_teams = num_playoff_teams;
	}

	public String getNum_playoff_consolation_teams() {
		return num_playoff_consolation_teams;
	}

	public void setNum_playoff_consolation_teams(
			String num_playoff_consolation_teams) {
		this.num_playoff_consolation_teams = num_playoff_consolation_teams;
	}

}