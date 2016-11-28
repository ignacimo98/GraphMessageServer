package ConnectionLogic;

import DeviceLogic.Device;
import com.oracle.javafx.jmx.json.JSONReader;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by nachomora on 11/24/16.
 */
public class SubServer extends Thread {
    private int id;
    private Socket connection;

    //atributos de cada cliente


    //constructor
    public SubServer(Socket connection, int id){
        this.id = id;
        this.connection = connection;
        start();
    }

    @Override
    public void run(){
        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONObject JSONDispositivo = new JSONObject(input.readLine());
            Device dispositivo = new Device(JSONDispositivo.getString("MacAddress"));
            System.out.println(JSONDispositivo.get("DispositivosApareados"));
            System.out.println(dispositivo.MACAddress);

            PrintStream output = new PrintStream(connection.getOutputStream());
            String msg;
            if (BannedDevices.getInstance().isBanned(dispositivo.MACAddress)){
                msg = "Sorry perro, está baneao!";
            } else {
                msg = "Bienvenido, es el cliente [" + id + "]\n";
            }

            output.print(msg);






        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
