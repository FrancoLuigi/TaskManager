package com.gmail.francoluigi95.taskAndroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.gmail.francoluigi95.taskAndroid.commons.Task;
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

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class ModifyActivity extends AppCompatActivity {

    private final String TAG = "LUIGI_DICTIONARY";

    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";

    private EditText text;

    private TextView textOUT;
    private Button button;
    private String username;
    private SharedPreferences preferences;
    private Set<String> titles;
    private EditText data;

    protected int mYear;
    protected int mMonth;
    protected int mDay;
    private AutoCompleteTextView autoComplete;
    private String title;

    String[] tasks;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);


        data = (EditText) findViewById(R.id.data);

        text = (EditText) findViewById(R.id.text);
        button = (Button) findViewById(R.id.buttonModify);
        textOUT = (TextView) findViewById(R.id.task);
        gson = new Gson();
        textOUT.setTextColor(Color.BLUE);
        textOUT.setTextSize(3, 10);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        text.setSingleLine();
        textOUT.setScroller(new Scroller(getApplicationContext()));
        textOUT.setMaxLines(2);

        textOUT.setHorizontalScrollBarEnabled(true);
        textOUT.setMovementMethod(new ScrollingMovementMethod());
        data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(0);

            }
        });


        updateDisplay();

        preferences = getSharedPreferences(prefName, MODE_PRIVATE);

        autoComplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_title);


        if(preferences.contains("titles")) {
            if (preferences.getStringSet("titles", titles).size() != 0) {


                titles = preferences.getStringSet("titles", titles);

                tasks = titles.toArray(new String[titles.size()]);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, tasks);

                // Set the adapter for the AutoCompleteTextView
                autoComplete.setAdapter(adapter);


                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {


                        title = arg0.getAdapter().getItem(arg2).toString();

                    }
                });
            }
        }else
            textOUT.setText("No Tasks");


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
    //textOUT.setSingleLine();


    public void goput(View v) {

        if (text.getText().toString().equalsIgnoreCase("")) {
            textOUT.setText("insert text");
        } else if (!preferences.contains("titles")) {
            textOUT.setText("No Tasks");
        } else {
            new ModifyActivity.PutRestTask().execute(title, text.getText().toString(), data.getText().toString());
        }
    }

    ;


    public class PutRestTask extends AsyncTask<String, Void, String> {

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
                jsonResponse = cr.put(gson.toJson(t, Task.class)).getText();
                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                    throw gson.fromJson(jsonResponse, InvalidKeyException.class);
                System.out.println("Response :" + gson.fromJson(jsonResponse, String.class));

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

            if (res != null) {
                textOUT.setText(res);
            }
        }
    }

}
