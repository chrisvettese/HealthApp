package io.github.chrisvettese.healthapp;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Doctor {
    private static String name;
    private static int id;

    public static void setup(final MainActivity activity) {
        //Back button
        activity.findViewById(R.id.backButtonDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setContentView(R.layout.activity_main);
                MainActivity.initMainActivity(activity);
            }
        });
        //Add Patient button
        activity.findViewById(R.id.addPatientDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = new Random().nextInt(900000) + 100000;
                //Open server for client to connect TODO
            }
        });
    }

    public static void setName(String name) {
        Doctor.name = name;
    }
}