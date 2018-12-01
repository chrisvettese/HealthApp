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

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static boolean isDoctor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainActivity(this);
    }
    public static void initMainActivity(final MainActivity a) {
        a.setContentView(R.layout.activity_main);

        final Button connectDoctor = a.findViewById(R.id.connectDoctor);
        final Button connectClient = a.findViewById(R.id.connectClient);
        final TextInputLayout nameInput = a.findViewById(R.id.nameInput);

        //Hide text input
        a.findViewById(R.id.nameInput).setVisibility(View.GONE);

        connectDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDoctor.setVisibility(View.GONE);
                connectClient.setVisibility(View.GONE);
                isDoctor = true;
                NameHandler.loadName(a, isDoctor);
            }
        });
        connectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectDoctor.setVisibility(View.GONE);
                connectClient.setVisibility(View.GONE);
                NameHandler.loadName(a, false);
            }
        });
        nameInput.getEditText().setSingleLine();
        nameInput.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionID, KeyEvent event) {
                if (actionID == EditorInfo.IME_ACTION_DONE) {
                    NameHandler.setName(nameInput.getEditText().getText().toString(), a, isDoctor);
                    return true;
                }
                return false;
            }
        });
        //Reset Button - Tries to delete saved files
        a.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(a.getFilesDir(), "name.txt").delete();
                new File(a.getFilesDir(), "id.txt").delete();
            }
        });
    }
}
