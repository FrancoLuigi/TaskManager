package com.gmail.francoluigi95.taskAndroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.francoluigi95.taskAndroid.commons.Task;
import com.google.gson.Gson;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;
import static com.gmail.francoluigi95.taskAndroid.RemoveActivity.*;

public class GetFreeActivity extends AppCompatActivity {

    private final String TAG = "LUIGI_DICTIONARY";

    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";


    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> tasks;

    private TextView noTask;
    public TextView textOUT;
    private Set<String> titles;
    private String[] tasks1;
    private SharedPreferences preferences;
    private String title;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_free);
        textOUT = (TextView) findViewById(R.id.taskOutput);


        textOUT.setScroller(new Scroller(getApplicationContext()));
        textOUT.setMaxLines(2);

        textOUT.setHorizontalScrollBarEnabled(true);
        textOUT.setMovementMethod(new ScrollingMovementMethod());


        textOUT.setTextColor(Color.BLUE);
        textOUT.setTextSize(3, 10);


        list = (ListView) findViewById(R.id.listView);
        preferences = getSharedPreferences(prefName, MODE_PRIVATE);

        if (preferences.contains("titles")) {


            titles = preferences.getStringSet("titles", titles);

            tasks1 = titles.toArray(new String[titles.size()]);


            gson = new Gson();


            new GetFreeActivity.GetFreeRestTask().execute();
        } else
            textOUT.setText("No Tasks");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                new GetFreeActivity.GetRestTask().execute((String) adattatore.getItemAtPosition(pos));


            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GetFreeActivity.this);
                builder1.setMessage("Vuoi eliminare questo task?");
                builder1.setCancelable(true);


                AlertDialog.Builder builder = builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                new GetFreeActivity.RemoveRestTask().execute((String) arg0.getItemAtPosition(pos));

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();


                return true;
            }
        });


    }

    public class GetFreeRestTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            ClientResource cr;
            Gson gson = new Gson();

            String URI = baseURI + "freeTasks";
            String jsonResponse = null;
            cr = new ClientResource(URI);


            try {

                jsonResponse = cr.get().getText();
                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                    throw gson.fromJson(jsonResponse, InvalidKeyException.class);

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


    public class GetRestTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String title = params[0];

            Task t;

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

    public class RemoveRestTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            ClientResource cr;
            Gson gson = new Gson();
            String title = params[0];
            String URI = baseURI + "tasks/" + title;
            String jsonResponse = null;
            cr = new ClientResource(URI);


            try {
                jsonResponse = cr.delete().getText();
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
                jsonResponse = "Error: " + cr.getStatus().getCode() + " - " + e2.getMessage();
                Log.e(TAG, jsonResponse);
            }

            return jsonResponse;
        }


        @Override
        protected void onPostExecute(String res) {


            if (res != null) {


                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                titles.remove(title);
                preferences.edit().putStringSet("titles", titles).apply();

                Intent Intent = new Intent(GetFreeActivity.this, MainActivity.class);
                startActivity(Intent);
            }
        }

    }


}





