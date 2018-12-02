package io.github.chrisvettese.healthapp;

import android.content.Intent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Doctor {
    private static String name;
    private static String patientName;
    private static int id;

    private static List<String[]> requests;
    private static List<String[]> dates;

    private static boolean viewingRequests = false, viewingAppointments = false, bookingAppointments = false;

    public static void setup(final MainActivity activity) {
        requests = new ArrayList<>();
        dates = new ArrayList<>();

        //Back button
        activity.findViewById(R.id.backButtonDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewingRequests || viewingAppointments || bookingAppointments) {
                    viewingRequests = false;
                    viewingAppointments = false;
                    bookingAppointments = false;

                    activity.findViewById(R.id.addPatientDoctor).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.datesDoctor).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.appointmentDoctor).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.requestDoctor).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.idLabelDoctor).setVisibility(View.VISIBLE);

                    activity.findViewById(R.id.listDoctor).setVisibility(View.GONE);
                } else {
                    activity.setContentView(R.layout.activity_main);
                    MainActivity.initMainActivity(activity);
                }
            }
        });
        //Add Patient button
        activity.findViewById(R.id.addPatientDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = new Random().nextInt(900000) + 100000;
                TextView text = activity.findViewById(R.id.idLabelDoctor);
                text.setText("Tell the patient this ID: " + id);

                //Intent intent = new Intent(activity.getBaseContext(), NetworkDoctor.class);
                //activity.startService(intent);
                NetworkDoctor.start();
            }
        });
        //Doctor wants to view appointment dates
        activity.findViewById(R.id.datesDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewingAppointments = true;
                hideButtons(activity);
                ScrollView list = activity.findViewById(R.id.listDoctor);
                list.removeAllViews();
                for (String[] date : dates) {
                    TextView textView = new TextView(activity);
                    textView.setText(date[0] + ", " + date[1]);
                    list.addView(textView);
                }

                list.setVisibility(View.VISIBLE);
            }
        });
        //Doctor wants to view requests for appointments
        activity.findViewById(R.id.requestDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewingRequests = true;
                hideButtons(activity);
                ScrollView list = activity.findViewById(R.id.listDoctor);
                list.removeAllViews();
                for (String[] request : requests) {
                    TextView textView = new TextView(activity);
                    textView.setText(request[0] + ", " + request[1]);
                    list.addView(textView);
                }

                list.setVisibility(View.VISIBLE);
            }
        });
        //Doctor wants to confirm an appointment with patient
        activity.findViewById(R.id.appointmentDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingAppointments = true;
                hideButtons(activity);
            }
        });
    }

    public static void setName(String name) {
        Doctor.name = name;
    }
    public static String getName() {
        return name;
    }
    public static int getID() {
        return id;
    }
    public static void setPatientName(String name) {
        Doctor.patientName = name;
    }
    public static void requestAppointment(String date, String time) {
        requests.add(new String[] {date, time});
    }
    private static void hideButtons(MainActivity activity) {
        activity.findViewById(R.id.addPatientDoctor).setVisibility(View.GONE);
        activity.findViewById(R.id.datesDoctor).setVisibility(View.GONE);
        activity.findViewById(R.id.appointmentDoctor).setVisibility(View.GONE);
        activity.findViewById(R.id.requestDoctor).setVisibility(View.GONE);
        activity.findViewById(R.id.idLabelDoctor).setVisibility(View.GONE);
    }
}