package src;

public class FFProjectionsDriver {
	public static void main(String[] args){
		YFFParser parser = new YFFParser();
		parser.parseMatchup(parser.downloadQuery());
		
	}
}
