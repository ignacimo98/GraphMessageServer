package ConnectionLogic;

import BTree.BTree;
import Graph.*;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Created by nachomora on 11/24/16.
 */
public class Server extends Thread {
    public static Graph connections = new Graph();
    public static BTree<String, String> serverTree = new BTree<String,String>();
    private int serverPORT = 2407;
    private ServerSocket serverSocket;
    public static int maxClients = 50;
    private static SubServer[] clientConnections = new SubServer[maxClients];

    public Server(){
        try {
            serverSocket = new ServerSocket(serverPORT);
            start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getServerPORT(){
        return serverPORT;
    }

    public String getIp(){
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (enumNetworkInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

                while (enumInetAddress.hasMoreElements()){
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server corriendo en: " + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e){
            e.printStackTrace();
            ip += "Something Wrong!" + e.toString() + "\n";
        }
        return ip;
    }

    @Override
    public void run() {
        while (!this.interrupted()){
            try {
            Socket connection = this.serverSocket.accept();
            assignConnectionToSubServer(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void assignConnectionToSubServer(Socket connection) {
        for (int i = 0; i < maxClients; i++){
            if ( this.clientConnections[i] == null ) {
                this.clientConnections[i] = new SubServer(connection, i);
                break;
            }
        }
    }




    public static void main(String[] args) {
        Server server = new Server();
        System.out.println(server.getIp()+':'+server.getServerPORT());
        Device d1 = new Device("12:43:12:53:14");
        d1.addPairedDevice("43:12:43:12:56");
        d1.addPairedDevice("76:23:53:12:42");
        Server.connections.addDevice(d1);
        Device d2 = new Device("43:12:43:12:56");
        d2.addPairedDevice("12:43:12:53:14");
        d2.addPairedDevice("76:23:53:12:42");
        Server.connections.addDevice(d2);
        Device d3 = new Device("76:23:53:12:42");
        d3.addPairedDevice("12:43:12:53:14");
        d3.addPairedDevice("43:12:43:12:56");
        Server.connections.addDevice(d3);
        Server.lookForConnections();



    }

    public static void lookForConnections() {
        for (Device device : Server.connections.deviceList){
            if (device.isValid){
                for (String pairedMac : device.pairedDevices){
                    for (Device dispositivo : Server.connections.deviceList){
                        if (pairedMac.equals(dispositivo.MACAddress)) {
                            for(Edge edge : Server.connections.edgeList) {
                                if (!edge.compareTo(new Edge(device, dispositivo))) {
                                    Server.connections.addEdge(device, dispositivo);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
