package src;
public class Team {
	private String team_key;
	private String team_id;
	private String name;
	private String url;
	private String team_logos; //field with more subresources
	private String waiver_priority;
	private String faab_balance;
	private String number_of_moves;
	private String number_of_trades;
	private String roster_adds; //field with more subresources
	private String managers; //field with more subresources
	
	
	
	public Team(String team_key){
		this.team_key = team_key;
	}

	public String getTeam_key() {
		return team_key;
	}

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
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



	public String getTeam_logos() {
		return team_logos;
	}



	public void setTeam_logos(String team_logos) {
		this.team_logos = team_logos;
	}



	public String getWaiver_priority() {
		return waiver_priority;
	}



	public void setWaiver_priority(String waiver_priority) {
		this.waiver_priority = waiver_priority;
	}



	public String getFaab_balance() {
		return faab_balance;
	}



	public void setFaab_balance(String faab_balance) {
		this.faab_balance = faab_balance;
	}



	public String getNumber_of_moves() {
		return number_of_moves;
	}



	public void setNumber_of_moves(String number_of_moves) {
		this.number_of_moves = number_of_moves;
	}



	public String getNumber_of_trades() {
		return number_of_trades;
	}



	public void setNumber_of_trades(String number_of_trades) {
		this.number_of_trades = number_of_trades;
	}



	public String getRoster_adds() {
		return roster_adds;
	}



	public void setRoster_adds(String roster_adds) {
		this.roster_adds = roster_adds;
	}



	public String getManagers() {
		return managers;
	}



	public void setManagers(String managers) {
		this.managers = managers;
	}
}
