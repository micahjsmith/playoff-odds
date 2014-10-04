package src;

import java.io.IOException;

public class FFProjectionsDriver {

	public static void main(String[] args) {
		YFFParser parser = new YFFParser();
		for (int i = 1; i <= 10; i++) {
			Roster r1 = parser.parseRoster(i);
			try {
				r1.writeRosterToFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
