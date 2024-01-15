package com.example.mycontact_management_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import androidx.activity.result.contract.ActivityResultContract;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    Button updateButton;


    ActivityResultLauncher<Intent> callSecondActivity;

    private List<String> namesList = new ArrayList<>();
    private List<String> emailsList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView contactListView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListView = findViewById(R.id.contactListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namesList);
        contactListView.setAdapter(adapter);


        // Load existing contacts from the database
        DBAdapter myDB = new DBAdapter(getApplicationContext());
        myDB.open();
        Cursor cursor = myDB.getAllContacts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.KEY_EMAIL));


                namesList.add(name);
                emailsList.add(email);

            } while (cursor.moveToNext());


            adapter.notifyDataSetChanged();
        }
        myDB.close();

        updateButton = findViewById(R.id.addContactButton);

        callSecondActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                            String name = data.getStringExtra("name");
                            String email = data.getStringExtra("email");


                            // Add the name to the list
                            namesList.add(name);
                            emailsList.add(email);
                            adapter.notifyDataSetChanged();

                            updateButton.setText("Add more contacts");

//                            / Add the new contact to the database
                            DBAdapter myDB = new DBAdapter(getApplicationContext());
                            myDB.open();
                            myDB.insertContact(name, email);
                            myDB.close();


                        }
                    }
                }

        );

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedContactName = namesList.get(position);
                String selectedContactEmail = emailsList.get(position);

                try {
                    Log.i("CLICK", "onItemClick: gpt");

                    Intent thirdActivityIntent = new Intent(MainActivity.this, ContactActivity.class);

                    // Pass the selected contact's name to the 3rd activity
                    thirdActivityIntent.putExtra("name", selectedContactName);
                    thirdActivityIntent.putExtra("email", selectedContactEmail);

                    // Start the 3rd activity
                    startActivity(thirdActivityIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handling the exception or log it out...
                }
            }
        });

    }

    public void onClick(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        callSecondActivity.launch(intent);
    }

}








