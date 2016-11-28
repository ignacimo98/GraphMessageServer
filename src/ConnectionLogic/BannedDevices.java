package ConnectionLogic;

import List.List;

/**
 * Created by nachomora on 11/27/16.
 */
public class BannedDevices {

    private List bannedDevices;
    private static BannedDevices instance;


    private BannedDevices(){
        this.bannedDevices = new List();
        bannedDevices.insertFirst("7C:7D:3D:44:A2:59");

    }

    public static BannedDevices getInstance(){
        if (instance == null){
            instance = new BannedDevices();
        }
        return instance;
    }

    public boolean isBanned(String mac){
        return bannedDevices.isIn(mac);
    }
    public void ban(String mac){
        bannedDevices.insertFirst(mac);
    }


    public void unban(String mac){
        bannedDevices.remove(mac);
    }


}
