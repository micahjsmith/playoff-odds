import javax.xml.parsers.*;
import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class League{
    String baseURI;
    String leagueURI;
    String testURI;
    String resourceID;

    public League(String leagueURI){
        this.leagueURI = leagueURI;
        testURI = "http://fantasysports.yahooapis.com/fantasy/v2/team/"+
            "nfl.l.6250.t.1/";
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(testURI);
        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (SAXException e){
            e.printStackTrace();
        }

        System.out.println("success");

    }
}
