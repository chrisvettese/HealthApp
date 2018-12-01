package io.github.chrisvettese.healthapp;

import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Doctor {
    public static String loadName(MainActivity activity) {
        try {
            FileReader nameIn = new FileReader(activity.getFilesDir() + "name.txt");
        } catch (FileNotFoundException e) {
            //Ask doctor to enter name
            activity.findViewById(R.id.input).setVisibility(View.VISIBLE);
        }
        //activity.setContentView(R.layout.layout_doctor);
        return "";
    }
}