package io.github.chrisvettese.healthapp;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static boolean isDoctor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity activity = this;

        final Button connectDoctor = findViewById(R.id.connectDoctor);
        final Button connectClient = findViewById(R.id.connectClient);
        final TextInputLayout nameInput = findViewById(R.id.nameInput);

        //Hide text input
        findViewById(R.id.nameInput).setVisibility(View.GONE);

        connectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDoctor.setVisibility(View.GONE);
                connectClient.setVisibility(View.GONE);
                isDoctor = true;
                NameHandler.loadName(activity, isDoctor);
            }
        });
        connectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDoctor.setVisibility(View.GONE);
                connectClient.setVisibility(View.GONE);
                NameHandler.loadName(activity, false);
            }
        });
        nameInput.getEditText().setSingleLine();
        nameInput.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionID, KeyEvent event) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    NameHandler.setName(nameInput.getEditText().getText().toString(), activity, isDoctor);
                    if (isDoctor) {
                        if (isDoctor) {
                            setContentView(R.layout.layout_doctor);
                        } else {
                            setContentView(R.layout.layout_client);
                            Client.setup(activity);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
