# ContactSaver
This is a Contact Management App involving a Database to store and retrieve contact details. 
Architecture Involved: MVVM

Here, I have 4 activities involved. Those are.
•	MainActivity.java
•	SecondActivity.java
•	ContactActivity.java
•	DBAdapter.java

In MainActivity.java, under the onCreate()

I coded this part of a block of code for loading the database whenever the app is opened.

```

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

```
While registering the secondActivity result, this part of the code adds the information to the Database.
```

//                             Add the new contact to the database
                            DBAdapter myDB = new DBAdapter(getApplicationContext());
                            myDB.open();
                            myDB.insertContact(name, email);
                            myDB.close();

```

In DBAdapter.java,

There are different methods which help to store, update, delete and add contact details.

```


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_EMAIL = "email";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "name text not null, email text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public long insertContact(String name, String email)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_EMAIL, email);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    public boolean deleteContact(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_EMAIL}, null, null, null, null, null);
    }
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_NAME, KEY_EMAIL}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) { mCursor.moveToFirst(); }
        return mCursor;
    }
    public boolean updateContact(long rowId, String name, String email)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);

        args.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
```
In SecondActivity.java, get the details and pass them back to the MainActivity.java

```


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

```



Similarly, the ContactActivity.java class is required to know more about a person.


```
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
```

In activity_main.xml,  the list view and button are defined.

```
    <ListView
        android:id="@+id/contactListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight = "1"
        />



    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center">

        <Button
            android:id="@+id/addContactButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="onClick"
            android:text="Add Contact" />
    </LinearLayout>
```


Results: GIF provided for the detailed running of the APP.

![ContactV3](https://github.com/Abbas-Hussain-Muzammil/ContactSaver/assets/87604394/a8b42fda-8a11-4aa6-a442-9142dd48cbb6)


