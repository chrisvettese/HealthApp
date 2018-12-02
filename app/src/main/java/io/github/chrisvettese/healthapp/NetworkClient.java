package io.github.chrisvettese.healthapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient {
    //Tell doctor to check ID
    public static final byte CHECK_ID = (byte) -200;
    //Tell doctor about requested appointment time; Tell client about appointment time
    public static final byte BOOK = (byte) -199;

    private static Socket clientSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static boolean open = true;


    protected static void start() {
        Log.d("test", "Starting network client");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("test", "Client socket");
                    clientSocket = new Socket("localhost", 2472);
                    Log.d("test", "out");
                    out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                    Log.d("test", "in");
                    in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                    Log.d("test", "done");
                    Client.setDoctorName(in.readUTF());

                    while (open) {
                        //Receive update from doctor
                        byte dataIn = in.readByte();
                        if (dataIn == BOOK) {
                            confirmAppointmentDate(in.readUTF(), in.readUTF());
                        }
                    }
                } catch (IOException e) {
                    Log.d("test", e.getMessage());
                    //Either client is disconnected or server isn't open yet
                    //Do nothing for now
                }
            }
        }).start();
        Log.d("test", "Started thread");//TODO
    }
    /*@Override
    public void onDestroy() {
        open = false;
        try {
            if (clientSocket != null) clientSocket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {

        }
    }*/

    protected static boolean validateID(final int ID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeByte(CHECK_ID);
                    out.writeInt(ID);
                    out.writeUTF(Client.getName());
                    out.flush();
                    //in.readBoolean(); TODO
                } catch (IOException e) {
                    //oops
                }
            }
        }).start();
        //Should return boolean from server
        return true;
    }
    protected static void sendAppointmentRequest(final String date, final String time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeByte(BOOK);
                    out.writeUTF(date);
                    out.writeUTF(time);
                    out.flush();
                } catch (IOException e) {
                    //oh no
                }
            }
        }).start();
    }
    protected static void confirmAppointmentDate(String date, String time) {
        Client.addAppointment(date, time);
    }
}