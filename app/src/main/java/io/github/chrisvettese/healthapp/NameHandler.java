package io.github.chrisvettese.healthapp;

import android.content.Context;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NameHandler {
    public static void loadName(MainActivity activity, boolean isDoctor) {
        try {
            FileInputStream nameIn = activity.openFileInput("name.txt");
            StringBuilder builder = new StringBuilder();
            int character;
            while ((character = nameIn.read()) != -1) {
                builder.append((char) character);
            }
            nameIn.close();
            setName(builder.toString(), activity, isDoctor);
        } catch (IOException e) {
            //User is opening app for first time, ask to enter name
            activity.findViewById(R.id.nameInput).setVisibility(View.VISIBLE);
        }
    }
    public static void setName(String name, MainActivity activity, boolean isDoctor) {
        if (isDoctor) Doctor.setName(name);
        else Client.setName(name);

        try {
            FileOutputStream nameOut = activity.openFileOutput("name.txt", Context.MODE_PRIVATE);
            nameOut.write(name.getBytes());
            nameOut.close();
        } catch (IOException e) {
            //give up
        }
    }
}
