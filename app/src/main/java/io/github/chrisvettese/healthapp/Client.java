package io.github.chrisvettese.healthapp;

import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

public class Client {
    private static String name;
    private static int doctorID;

    public static void setup(final MainActivity activity) {
        activity.findViewById(R.id.viewAppointmentsClient).setVisibility(View.GONE);
        try {
            FileInputStream nameIn = activity.openFileInput("id.txt");
            StringBuilder builder = new StringBuilder();
            int character;
            while ((character = nameIn.read()) != -1) {
                builder.append((char) character);
            }
            nameIn.close();
            doctorID = Integer.parseInt(nameIn.toString());
        } catch (IOException e) {
            //Client hasn't entered their doctor's id yet
            Client.setupIDInput(activity);
        }
        activity.findViewById(R.id.backButtonClient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setContentView(R.layout.activity_main);
                MainActivity.initMainActivity(activity);
            }
        });
    }

    public static void setupIDInput(MainActivity activity) {
        final TextInputLayout idInput = activity.findViewById(R.id.idInput);
        idInput.getEditText().setSingleLine();
        idInput.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionID, KeyEvent event) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    doctorID = Integer.parseInt(idInput.getEditText().getText().toString());
                    //Check if doctorID is valid
                    return true;
                }
                return false;
            }
        });
    }
    public static void setName(String name) {
        Client.name = name;
    }
}