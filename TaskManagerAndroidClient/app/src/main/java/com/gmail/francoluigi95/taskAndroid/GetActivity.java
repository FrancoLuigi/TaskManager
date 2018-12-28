package com.gmail.francoluigi95.taskAndroid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.Gson;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.Set;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;
import com.gmail.francoluigi95.taskAndroid.commons.Task;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class GetActivity extends AppCompatActivity {


    private final String TAG = "LUIGI_DICTIONARY";

    private Gson gson;
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";

    private AutoCompleteTextView autoComplete;
    private EditText textTitle;

    private TextView textOUT;

    private Button buttonGet;

    private SharedPreferences preferences;
    private Set<String> titles;
    String[] tasks;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        buttonGet = (Button) findViewById(R.id.buttongetactivity);


        textOUT = (TextView) findViewById(R.id.noteOutput);

        textOUT.setScroller(new Scroller(getApplicationContext()));
        textOUT.setMaxLines(3);
        textOUT.setHorizontalScrollBarEnabled(true);
        textOUT.setMovementMethod(new ScrollingMovementMethod());
        textOUT.setTextColor(Color.BLUE);
        textOUT.setTextSize(3, 10);
        gson = new Gson();


        preferences = getSharedPreferences(prefName, MODE_PRIVATE);

        autoComplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_title);

        if(preferences.contains("titles")) {
            if (preferences.getStringSet("titles", titles).size() != 0) {


                titles = preferences.getStringSet("titles", titles);

                tasks = titles.toArray(new String[titles.size()]);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, tasks);

                autoComplete.setAdapter(adapter);


                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {


                        title = arg0.getAdapter().getItem(arg2).toString();

                    }
                });

                buttonGet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Se non viene inserito il titolo mostra un messaggio
                        if (autoComplete.getText().toString().equalsIgnoreCase("")) {
                            textOUT.setText("Insert Title");
                        }
                        // altrimenti ottiene il task inserito nella textview
                        else{
                            new GetActivity.GetRestTask().execute(title);
                        }
                    }
                });

            }
        }else
            textOUT.setText("No Tasks");
    }

    public class GetRestTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            String title = params[0];
            String jsonResponse = null;
            if (title != null) {

                Task t;

                ClientResource cr;
                Gson gson = new Gson();

                String URI = baseURI + "tasks/" + title;

                cr = new ClientResource(URI);

                try {
                    jsonResponse = cr.get().getText();
                    if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE)
                        throw gson.fromJson(jsonResponse, InvalidKeyException.class);
                    t = gson.fromJson(jsonResponse, Task.class);
                    jsonResponse = t.toString();

                } catch (IOException e1) {


                    jsonResponse = "Error: " + cr.getStatus().getCode() + " - " + cr.getStatus().getDescription() +
                            " - " + cr.getStatus().getReasonPhrase();
                    Log.e(TAG, jsonResponse);


                } catch (InvalidKeyException e2) {
                    String error2 = "Error: " + cr.getStatus().getCode() + " - " + e2.getMessage();
                    Log.e(TAG, error2);
                }

                return jsonResponse;
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
