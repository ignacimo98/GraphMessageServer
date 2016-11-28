package Graph;


import java.util.ArrayList;
import java.util.List;


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

    public List<Device> getVertexes() {
        return deviceList;
    }

    public List<Edge> getEdges() {
        return edgeList;
    }

    public int numDevices(){
        return deviceList.size();
    }


}
