package Graph;


import java.util.ArrayList;


/**
 * Created by nachomora on 11/28/16.
 */
public class Graph {
    public ArrayList<Device> deviceList;
    public ArrayList<Edge> edgeList;

    public Graph(){
        this.edgeList = new ArrayList<Edge>();
        this.deviceList = new ArrayList<Device>();
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    public void addDevice(Device device){
        this.deviceList.add(device);
    }

    public void addEdge(Device one, Device two){
        this.edgeList.add(new Edge(one, two));
    }

    public int numDevices(){
        return deviceList.size();
    }
}
