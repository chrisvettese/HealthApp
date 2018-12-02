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
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkDoctor {
    private static Socket socket;
    private static ServerSocket serverSocket;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    private static boolean open;

    protected static void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("doctor", "Server socket");
                    serverSocket = new ServerSocket(2472);
                    Log.d("doctor", "waiting to accept");
                    socket = serverSocket.accept();
                    //serverSocket.close();
                    //serverSocket = null;
                    Log.d("doctor", "output stream");
                    out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    Log.d("doctor", "input stream");
                    in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    socket.setTcpNoDelay(true);
                    //Send patient the doctor's name
                    out.writeUTF(Doctor.getName());
                    out.flush();

                    while (open) {
                        byte dataIn = in.readByte();
                        if (dataIn == NetworkClient.CHECK_ID) {
                            receivePatientID(in.readInt(), in.readUTF());
                        }
                        else if (dataIn == NetworkClient.BOOK) {
                            Doctor.requestAppointment(in.readUTF(), in.readUTF());
                        }
                    }
                } catch (IOException e) {
                    Log.d("doctor", e.getMessage());//TODO
                    //patients will die
                }
            }
        }).start();
    }

    /*@Override
    public void onDestroy() {
        try {
            if (serverSocket != null) serverSocket.close();
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            //shrug
        }
    }*/
    protected static void receivePatientID(int ID, String patientName) {
        try {
            if (Doctor.getID() == 0) out.writeBoolean(false);
            else {
                if (ID == Doctor.getID()) {
                    out.writeBoolean(true);
                    Doctor.setPatientName(patientName);
                }
            }
        } catch (IOException e) {

        }
    }
    protected static void confirmAppointment(final String date, final String time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeUTF(date);
                    out.writeUTF(time);
                } catch (IOException e) {
                    //couldn't confirm appointment
                }
            }
        });
    }
}