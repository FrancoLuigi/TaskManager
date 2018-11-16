package com.gmail.francoluigi95.taskAndroid;

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
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.Set;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class RemoveActivity extends AppCompatActivity {


    private final String TAG = "LUIGI_DICTIONARY";

    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";
    private EditText textIN;
    private EditText password;
    private EditText username;
    public TextView textOUT;
    private AutoCompleteTextView autoComplete;
    private SharedPreferences preferences;
    private Set<String> titles;
    String[] tasks;
    private Button buttonRemove;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);


        textOUT = (TextView) findViewById(R.id.noteOutput);
        textOUT.setScroller(new Scroller(getApplicationContext()));
        textOUT.setMaxLines(2);

        textOUT.setHorizontalScrollBarEnabled(true);
        textOUT.setMovementMethod(new ScrollingMovementMethod());


        textOUT.setTextColor(Color.BLUE);
        textOUT.setTextSize(3, 10);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_title);
        gson = new Gson();

        preferences = getSharedPreferences(prefName, MODE_PRIVATE);


        if (preferences.contains("titles")) {


            titles = preferences.getStringSet("titles", titles);

            tasks = titles.toArray(new String[titles.size()]);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, tasks);

            // Set the adapter for the AutoCompleteTextView
            autoComplete.setAdapter(adapter);


            buttonRemove = (Button) findViewById(R.id.buttonRemove);


            // Create an ArrayAdapter containing country names


            autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {


                    title = arg0.getAdapter().getItem(arg2).toString();

                }
            });

            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new RemoveActivity.RemoveRestTask().execute(title);

                }
            });


        } else
            textOUT.setText("No Tasks");
    }


 /*   public void remove(String s){
        new RemoveRestTask().execute(s);
    }
*/

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


            textOUT.setTextColor(Color.BLUE);
            textOUT.setTextSize(3, 10);
            if (res != null) {
                textOUT.setText(res);

                titles.remove(title);
                preferences.edit().putStringSet("titles", titles).apply();
            }
        }

    }


}
