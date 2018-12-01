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

public class Doctor {
    private static String name;

    public static  void setName(String name) {
        Doctor.name = name;
    }
}