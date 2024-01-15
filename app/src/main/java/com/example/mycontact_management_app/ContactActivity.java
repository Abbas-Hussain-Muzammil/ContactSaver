package com.example.mycontact_management_app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {


    TextView nameTextView;
    TextView emailTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);



         nameTextView = (TextView)findViewById(R.id.tv1);
         emailTextView = (TextView)findViewById(R.id.tv2);



        // Using the below code to transfer data from initial activity to last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("name");
            String email = extras.getString("email");
            nameTextView.setText("Name: " + name );
            emailTextView.setText("Email: "+ email);
        }

    }


}
