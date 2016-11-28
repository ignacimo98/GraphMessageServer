package Resources;

import Resources.List.List;

/**
 * Created by andre on 27/11/2016.
 * Project: TEC
 * Using IntelliJ
 */
public class BadWordsList {
	
	private static List badWordsList;
	
	public static List getBadWordsList() {
		return badWordsList;
	}
	
	public static void setBadWordsList() {
		badWordsList = new List();
		badWordsList.insertFirst("picha");
		badWordsList.insertFirst("mierda");
		badWordsList.insertFirst("puta");
		badWordsList.insertFirst("carepicha");
		badWordsList.insertFirst("malparido");
		badWordsList.insertFirst("se van a quedar en datos");
		badWordsList.insertFirst("verga");
		badWordsList.insertFirst("culero");
		badWordsList.insertFirst("playo");
		badWordsList.insertFirst("marica");
	}
}
