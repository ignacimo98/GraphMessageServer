package ConnectionLogic;


import Graph.Device;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * Created by nachomora on 11/24/16.
 */
public class SubServer extends Thread {
    private int id;
    private Socket connection;
    private Device dispositivo;

    //atributos de cada cliente


    //constructor
    public SubServer(Socket connection, int id){
        this.id = id;
        this.connection = connection;
        start();
    }

    public Device getDispositivo() {
        return dispositivo;
    }

    @Override
    public void run(){
        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String firstInput = input.readLine();
            System.out.println(firstInput);
            JSONObject JSONDispositivo = new JSONObject(firstInput);
            this.dispositivo = new Device(JSONDispositivo.getString("MacAddress"));
            JSONArray MacAddressArray = JSONDispositivo.getJSONArray("DispositivosApareados");

            for (int i = 0; i<MacAddressArray.length(); i++){
                this.dispositivo.addPairedDevice(MacAddressArray.getString(i));
            }
            System.out.println(dispositivo.MACAddress);
            System.out.println(dispositivo.pairedDevices.toString());

            PrintStream output = new PrintStream(connection.getOutputStream());
            String msg;
            if (BannedDevices.getInstance().isBanned(this.dispositivo.MACAddress)){
                msg = "Sorry perro, está baneao!\n";
                output.print(msg);
            } else {
                msg = "Bienvenido, es el cliente [" + id + "]\n";
                output.print(msg);
                Server.connections.addDevice(dispositivo);
                this.dispositivo.validate();
                Server.lookForConnections();

                while (true) {
                    String iterStr = input.readLine();
                    if (iterStr != null){
                        JSONObject toDo = new JSONObject(iterStr);
                        if(toDo.getString("accion").equals("buscar")){
                            output.print(this.buscar(toDo.getString("Search")));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String buscar(String search) {
        return Server.serverTree.search(search);
    }


}
