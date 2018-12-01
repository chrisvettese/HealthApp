package io.github.chrisvettese.healthapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hide text input
        findViewById(R.id.input).setVisibility(View.GONE);
        final MainActivity activity = this;

        final Button connectDoctor = findViewById(R.id.connectDoctor);
        final Button connectClient = findViewById(R.id.connectClient);

        connectDoctor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectDoctor.setVisibility(View.GONE);
                connectClient.setVisibility(View.GONE);
                Doctor.loadName(activity);
            }
        });
        connectClient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectDoctor.setVisibility(View.GONE);
                connectClient.setVisibility(View.GONE);
                setContentView(R.layout.layout_client);
            }
        });
    }
}
