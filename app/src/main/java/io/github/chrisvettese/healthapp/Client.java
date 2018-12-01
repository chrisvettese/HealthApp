package io.github.chrisvettese.healthapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static String name;
    private static String doctorName;
    private static int doctorID;

    private static boolean bookingMode = false;
    private static boolean viewingMode = false;

    private static List<String[]> dates;

    public static void setup(final MainActivity activity) {
        dates = new ArrayList<>();
        activity.findViewById(R.id.viewAppointmentsClient).setVisibility(View.GONE);
        activity.findViewById(R.id.appointmentList).setVisibility(View.GONE);

        activity.findViewById(R.id.dateInput).setVisibility(View.GONE);
        activity.findViewById(R.id.timeInput).setVisibility(View.GONE);
        activity.findViewById(R.id.submitClient).setVisibility(View.GONE);
        try {
            FileInputStream nameIn = activity.openFileInput("id.txt");
            StringBuilder builder = new StringBuilder();
            int character;
            while ((character = nameIn.read()) != -1) {
                builder.append((char) character);
            }
            nameIn.close();
            doctorID = Integer.parseInt(nameIn.toString());
            Intent intent = new Intent(activity, NetworkClient.class);
            new NetworkClient().startService(intent);
            NetworkClient.validateID(doctorID);
        } catch (IOException e) {
            //Client hasn't entered their doctor's id yet
            Client.setupIDInput(activity);
        }
        //Back button
        activity.findViewById(R.id.backButtonClient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bookingMode && !viewingMode) {
                    activity.setContentView(R.layout.activity_main);
                    MainActivity.initMainActivity(activity);
                }
                else if (bookingMode) {
                    bookingMode = false;
                    activity.findViewById(R.id.dateInput).setVisibility(View.GONE);
                    activity.findViewById(R.id.timeInput).setVisibility(View.GONE);
                    activity.findViewById(R.id.submitClient).setVisibility(View.GONE);

                    activity.findViewById(R.id.viewAppointmentsClient).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.requestClient).setVisibility(View.VISIBLE);
                }
                else if (viewingMode) {
                    viewingMode = false;
                    activity.findViewById(R.id.viewAppointmentsClient).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.requestClient).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.appointmentList).setVisibility(View.GONE);
                }
            }
        });
    }

    public static void setupIDInput(final MainActivity activity) {
        new NetworkClient().startService(null);

        final TextInputLayout idInput = activity.findViewById(R.id.idInput);
        idInput.getEditText().setSingleLine();
        idInput.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionID, KeyEvent event) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    doctorID = Integer.parseInt(idInput.getEditText().getText().toString());
                    //Check if doctorID is valid
                    boolean validID = NetworkClient.validateID(doctorID);
                    //Correct ID entered: hide this input and show all buttons
                    if (validID) {
                        idInput.setVisibility(View.GONE);
                        Button viewButton = activity.findViewById(R.id.viewAppointmentsClient);
                        viewButton.setVisibility(View.VISIBLE);
                        Button requestClient = activity.findViewById(R.id.requestClient);
                        requestClient.setVisibility(View.VISIBLE);

                        //Add listener for requesting appointments
                        requestClient.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Client.setRequestView(activity);
                            }
                        });
                        //Add listener for viewing appointments
                        viewButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Client.setAppointmentView(activity);
                            }
                        });
                        return true;
                    }
                }
                return false;
            }
        });
    }
    protected static void setName(String name) {
        Client.name = name;
    }
    protected static String getName() {
        return name;
    }
    protected static void setDoctorName(String name) {
        Client.doctorName = name;
    }
    protected static void addAppointment(String date, String time) {
        dates.add(new String[] {date, time});
    }
    /**When the "Book Appointment" button is pressed*/
    protected static void setRequestView(final MainActivity a) {
        bookingMode = true;

        final TextInputLayout dateInput = a.findViewById(R.id.dateInput);
        final TextInputLayout timeInput = a.findViewById(R.id.timeInput);
        final Button submitClient = a.findViewById(R.id.submitClient);

        dateInput.setVisibility(View.VISIBLE);
        timeInput.setVisibility(View.VISIBLE);
        submitClient.setVisibility(View.VISIBLE);

        a.findViewById(R.id.viewAppointmentsClient).setVisibility(View.GONE);
        a.findViewById(R.id.requestClient).setVisibility(View.GONE);
        //When patient clicks submit to book appointment, verify they entered valid input and send to doctor
        submitClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateInput.getEditText().getText().toString();
                String time = timeInput.getEditText().getText().toString();
                //If input for date and time
                if (date.length() == 8 && time.length() == 4) {
                    //Send appointment request to doctor
                    NetworkClient.sendAppointmentRequest(date, time);
                    //Disable booking mode
                    bookingMode = false;
                    dateInput.setVisibility(View.GONE);
                    timeInput.setVisibility(View.GONE);
                    submitClient.setVisibility(View.GONE);

                    a.findViewById(R.id.viewAppointmentsClient).setVisibility(View.VISIBLE);
                    a.findViewById(R.id.requestClient).setVisibility(View.VISIBLE);
                } else {
                    dateInput.getEditText().setText("");
                    timeInput.getEditText().setText("");
                }
            }
        });
    }
    /**When patient wants to view booked appointments*/
    protected static void setAppointmentView(final MainActivity a) {
        viewingMode = true;
        a.findViewById(R.id.viewAppointmentsClient).setVisibility(View.GONE);
        a.findViewById(R.id.requestClient).setVisibility(View.GONE);
        ScrollView list = a.findViewById(R.id.appointmentList);
        list.removeAllViews();
        for (String[] date : dates) {
            TextView textView = new TextView(a);
            textView.setText(date[0] + ", " + date[1]);
            list.addView(textView);
        }

        list.setVisibility(View.VISIBLE);
    }
}