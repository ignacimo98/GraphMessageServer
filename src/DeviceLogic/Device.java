package DeviceLogic;


import List.List;

/**
 * Created by nachomora on 11/27/16.
 */
public class Device {
    public String MACAddress;
    public List pairedDevices;
    public String location;


    public Device(String address){
        this.MACAddress = address;
        this.pairedDevices = new List();
    }

    public void setPairedDevices(){

    }
}
