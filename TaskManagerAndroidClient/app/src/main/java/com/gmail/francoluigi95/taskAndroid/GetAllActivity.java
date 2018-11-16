package com.gmail.francoluigi95.taskAndroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;
import com.gmail.francoluigi95.taskAndroid.commons.Task;
import com.gmail.francoluigi95.taskAndroid.commons.User;
import com.google.gson.Gson;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class GetAllActivity extends AppCompatActivity {

    private final String TAG = "LUIGI_DICTIONARY";

    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";

    private SharedPreferences preferences;


    private ArrayList<String> tasks;


    private ListView list;
    private ArrayAdapter<String> adapter;
    private String username;
    private TextView noTask;
    private Set<String> titles;
    private String[] tasks1;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all);


        preferences = getSharedPreferences(prefName, MODE_PRIVATE);
        list = (ListView) findViewById(R.id.listView);


        noTask = (TextView) findViewById(R.id.textView);

        if (preferences.contains("titles")) {


            titles = preferences.getStringSet("titles", titles);

            tasks1 = titles.toArray(new String[titles.size()]);


            gson = new Gson();


            new GetAllActivity.GetAllRestTask().execute();


        } else
            noTask.setText("No Tasks");
    }


    public class GetAllRestTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            ClientResource cr;
            Gson gson = new Gson();

            username = preferences.getString("username", null);

            if (username == null) {

                username = preferences.getString("user", null);

            }

            String URI = baseURI + "allTasks/" + username;
            String jsonResponse = null;
            cr = new ClientResource(URI);


            try {

                jsonResponse = cr.get().getText();

                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                    throw gson.fromJson(jsonResponse, InvalidKeyException.class);
                //tasks = gson.fromJson(jsonResponse, String.class);
                tasks = gson.fromJson(jsonResponse, ArrayList.class);
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


            if (res != null) {


                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, tasks);


                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);
            }
        }

    }


}
