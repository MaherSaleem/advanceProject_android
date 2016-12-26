package com.example.maher.labadcanedproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AfterLoggedIn extends AppCompatActivity {

    TextView weclomeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_logged_in);
        weclomeTextView= (TextView)findViewById(R.id.welcome_text_view);
        weclomeTextView.setText("Welcome " + GlobalClass.studentName);

    }
}
