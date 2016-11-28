package Graph;


import List.List;

import java.util.ArrayList;

/**
 * Created by nachomora on 11/27/16.
 */
public class Device {
    public String MACAddress;
    public ArrayList<String> pairedDevices;
    public String nombre;
    public boolean isValid;
    public boolean isBot;


    public Device(String address){
        this.MACAddress = address;
        this.pairedDevices = new ArrayList<String>();
        this.isValid = false;
        this.isBot = false;

    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void addPairedDevice(String mac){
        this.pairedDevices.add(mac);
    }

    public void validate(){
        this.isValid = true;
    }
}
