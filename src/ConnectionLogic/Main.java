package ConnectionLogic;

import java.util.Date;

/**
 * Created by nachomora on 11/24/16.
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        System.out.println(server.getIp()+':'+server.getServerPORT());


    }
}
