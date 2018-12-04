package com.gmail.francoluigi95.taskAndroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.francoluigi95.taskAndroid.commons.Task;
import com.gmail.francoluigi95.taskAndroid.commons.User;
import com.google.gson.Gson;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class AddActivity extends AppCompatActivity {

    private final String TAG = "LUIGI_DICTIONARY";

    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";

    private EditText text;
    private EditText data;
    private EditText textTitle;
    private TextView textOUT;
    private SharedPreferences preferences;
    private Set<String> titles;

    private String username;
    protected int mYear;
    protected int mMonth;
    protected int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        textTitle = (EditText) findViewById(R.id.title);
        text = (EditText) findViewById(R.id.text);
        data = (EditText) findViewById(R.id.data);
        textOUT = (TextView) findViewById(R.id.task);
        gson = new Gson();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        textTitle.setSingleLine();
        text.setSingleLine();
        textOUT.setTextColor(Color.BLUE);
        textOUT.setTextSize(3, 10);


        data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(0);

            }
        });


        updateDisplay();

    }

    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this,
                mDateSetListener,
                mYear, mMonth, mDay);
    }

    protected void updateDisplay() {
        data.setText(
                new StringBuilder()
                        .append(mDay).append("/")
                        .append(mMonth + 1).append("/")
                        .append(mYear).append(" "));
    }

    protected DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };


    public class LoginRestTask extends AsyncTask<String, Void, String> {

        private Task t;


        protected String doInBackground(String... params) {
            String title = params[0];
            String text = params[1];
            String dateS = params[2];


            preferences = getSharedPreferences(prefName, MODE_PRIVATE);


            username = preferences.getString("username", null);

            if (username == null) {

                username = preferences.getString("user", null);

            }

            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyy");
            Date date;
            try {
                date = sd.parse(dateS);
            } catch (ParseException e) {
                System.err.println("Catturata un'eccezione " + e.getMessage() + " nella lettura della data");
                System.err.println("Alla data verra assegnato il valore della data odierna;");
                date = new Date();
            }


            t = new Task(title, text, date);
            t.setUser(username);


            ClientResource cr;
            Gson gson = new Gson();

            String URI = baseURI + "tasks";
            String jsonResponse = null;
            cr = new ClientResource(URI);


            try {

                jsonResponse = cr.post(gson.toJson(t, Task.class)).getText();
                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                    throw gson.fromJson(jsonResponse, InvalidKeyException.class);

            } catch (ResourceException | IOException e1) {
                if (org.restlet.data.Status.CLIENT_ERROR_UNAUTHORIZED.equals(cr.getStatus())) {
                    // Unauthorized access
                    jsonResponse = "Access unauthorized by the server, check your credentials";
                    Log.e(TAG, jsonResponse);
                } else {

                    jsonResponse = "Error: " + cr.getStatus().getCode() + " - " + cr.getStatus().getDescription() +
                            " - " + cr.getStatus().getReasonPhrase();
                    Log.e(TAG, jsonResponse);
                }

            } catch (InvalidKeyException e2) {
                String error2 = "Error: " + cr.getStatus().getCode() + " - " + e2.getMessage();
                Log.e(TAG, error2);
            }

            return jsonResponse;

        }

        @Override
        protected void onPostExecute(String res) {
            textOUT.setTextColor(Color.BLUE);
            textOUT.setTextSize(3, 10);

            preferences.getStringSet("titles", titles);
            if (res != null) {
                textOUT.setText(res);
            }

            titles = preferences.getStringSet("titles", titles);

            if (titles == null) {
                titles = new TreeSet<>();
            }

            titles.add(String.valueOf(textTitle.getText()));
            SharedPreferences.Editor edit = preferences.edit();

            edit.putStringSet("titles", titles).apply();


        }
    }


    public void goPost(View v) {

        if (textTitle.getText().toString().equalsIgnoreCase("") || text.getText().toString().equalsIgnoreCase("")) { // Nel campo input deve essere inserita la key
            textOUT.setText("Insert Title, Text and Date");
        }
        else if(textTitle.getText().length()<3){

            textOUT.setText("Minimum length of the task's title is 3 characters");
        }
        else {

            new AddActivity.LoginRestTask().execute(textTitle.getText().toString(), text.getText().toString(), data.getText().toString());
        }

    }
}
