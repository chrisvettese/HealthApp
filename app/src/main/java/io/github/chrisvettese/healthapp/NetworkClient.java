package io.github.chrisvettese.healthapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient extends Service {
    //Tell doctor to check ID
    public static final byte CHECK_ID = (byte) -200;
    //Tell doctor about requested appointment time; Tell client about appointment time
    public static final byte BOOK = (byte) -199;

    private Socket clientSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private boolean open = true;

    @Override
    public void onCreate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket("localhost", 2468);
                    in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                    out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

                    Client.setDoctorName(in.readUTF());

                    while (open) {
                        //Receive update from doctor
                        byte dataIn = in.readByte();
                        if (dataIn == BOOK) {
                            confirmAppointmentDate(in.readUTF(), in.readUTF());
                        }
                    }
                } catch (IOException e) {
                    //Either client is disconnected or server isn't open yet
                    //Do nothing for now
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        open = false;
        try {
            if (clientSocket != null) clientSocket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {

        }
    }

    protected static boolean validateID(int ID) {
        try {
            out.writeByte(CHECK_ID);
            out.writeInt(ID);
            out.writeUTF(Client.getName());
            return in.readBoolean();
        } catch (IOException e) {
            //oops
        }
        return false;
    }
    protected static void sendAppointmentRequest(String date, String time) {
        try {
            out.writeByte(BOOK);
            out.writeUTF(date);
            out.writeUTF(time);
        } catch (IOException e) {
            //oh no
        }
    }
    protected static void confirmAppointmentDate(String date, String time) {
        Client.addAppointment(date, time);
    }
}