package com.gmail.francoluigi95.taskAndroid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;
import com.gmail.francoluigi95.taskAndroid.commons.Task;
import com.google.gson.Gson;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.Set;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;


public class AssignActivity extends AppCompatActivity {
    private AutoCompleteTextView autoComplete;
    private SharedPreferences preferences;
    private Set<String> titles;
    String[] tasks;
    private final String TAG = "LUIGI_DICTIONARY";
    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";
    private TextView textOUT;
    private Button buttonComplete;
    private String title;
    private Task t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        preferences = getSharedPreferences(prefName, MODE_PRIVATE);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_title);
        textOUT = (TextView) findViewById(R.id.noteoutput);

        if (preferences.getStringSet("titles", titles).size()!=0) {


            titles = preferences.getStringSet("titles", titles);

            tasks = titles.toArray(new String[titles.size()]);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, tasks);

            autoComplete.setAdapter(adapter);


            buttonComplete = (Button) findViewById(R.id.buttonComplete);


            autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {


                    title = arg0.getAdapter().getItem(arg2).toString();
                    new AssignActivity.GetRestTask().execute(title);
                }
            });

            buttonComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AssignActivity.AssignRestTask1().execute(title);
                }
            });

        } else
            textOUT.setText("No Tasks");
    }

    public class GetRestTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String title = params[0];


            ClientResource cr;
            Gson gson = new Gson();

            String URI = baseURI + "tasks/" + title;
            String jsonResponse = null;
            cr = new ClientResource(URI);

            try {
                jsonResponse = cr.get().getText();
                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                    throw gson.fromJson(jsonResponse, InvalidKeyException.class);
                t = gson.fromJson(jsonResponse, Task.class);
                jsonResponse = t.toString();

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

    public class AssignRestTask1 extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String title = params[0];


            t.setState("InProgress");

            ClientResource cr;
            Gson gson = new Gson();

            String URI = baseURI + "tasks";
            String jsonResponse = null;
            cr = new ClientResource(URI);

            try {
                jsonResponse = cr.put(gson.toJson(t, Task.class)).getText();
                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                    throw gson.fromJson(jsonResponse, InvalidKeyException.class);

                jsonResponse = t.toString();

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

                Toast.makeText(getApplicationContext(), "Il task è stato assegnato", Toast.LENGTH_SHORT).show();
                textOUT.setText(res);
            }
        }
    }


}


