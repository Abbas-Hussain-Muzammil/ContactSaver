package com.example.mycontact_management_app;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class SecondActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
    public void handleSubmit(View view){


        EditText nameEditText = findViewById(R.id.editTextName);
        EditText emailEditText = findViewById(R.id.editTextEmail);

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        // Checking if the details are filled in properly
        if (name.isEmpty() || email.isEmpty()) {
            // Toast message indicating the form is incomplete
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        } else {

            // Creating an Intent to send the data back to MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("email", email);

            // Set the result code to indicate success
            setResult(MainActivity.RESULT_OK, resultIntent);

            // returning from SecondActivity back to MainActivity
            finish();


        }



    }


}