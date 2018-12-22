package com.gmail.francoluigi95.taskAndroid;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;
import com.google.gson.Gson;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.security.User;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;

import java.io.IOException;


public class LogInActivity extends AppCompatActivity {

    private EditText mUsernameView;
    private EditText mPasswordView;
    public static final String prefName = "Task";
    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";
    private Button mLoginInButton;
    private Button mSignInButton;
    private TextView mResultOp;
    private CheckBox checkBox;
    private static final String TAG = "AlertDialogActivity";
    private DialogFragment mDialog;
    private SharedPreferences preferences;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);

        preferences = getSharedPreferences(prefName, MODE_PRIVATE);


        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mLoginInButton = (Button) findViewById(R.id.login_button);
        mResultOp = (TextView) findViewById(R.id.textView);


        if (preferences.contains("username")) {


            continueShutdown();

        }


        mLoginInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUsernameView.getText().toString().equals("") || mPasswordView.getText().toString().equals("")) {
                    // View parent = (View) findViewById(R.id.activity_login_page);
                    mResultOp.setText("Insert Data");
                    //     Toast.makeText(getApplicationContext(), "Insert Data", Toast.LENGTH_SHORT).show();
                    //                           sn.make(parent, "Insert Data", Snackbar.LENGTH_SHORT).show();
                } else
                    // new LoginRestTask().execute(mUsernameView.getText().toString(), mPasswordView.getText().toString());
                    continueShutdown();
            }
        });


        mSignInButton = (Button) findViewById(R.id.register_bottom);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mUsernameView.getText().toString().length() == 0 || mPasswordView.getText().toString().length() == 0) /* Verifico se username e password inseriti nei campi siano sufficientemente lunghi*/ {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // aggiunto per il testing
                            mResultOp.setVisibility(View.INVISIBLE);    // rendo la textView invisible per far apparire soltanto il toast
                            mResultOp.setText("Username e/o password non riempiti correttamente");
                            Toast.makeText(getApplicationContext(), "Username e/o password non riempiti correttamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    new LogInActivity.RegisterUserTask().execute("registration", mUsernameView.getText().toString(),  mPasswordView.getText().toString());
            }
        });


    }

    private void continueShutdown() {

        // Show ProgressDialog as shutdown process begins
        mDialog = MainActivity.ProgressDialogFragment.newInstance();

        // Show new ProgressDialogFragment
        mDialog.show(getFragmentManager(), "Shutdown");

        // Finish the ShutDown process
        finishShutdown();


    }

    private void finishShutdown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Pretend to do something before
                    // shutting down
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Log.i(TAG, e.toString());
                } finally {

                    if (preferences.contains("username")) {


                        new LoginRestTask().execute(preferences.getString("username", null), preferences.getString("password", null));
                    } else
                        new LoginRestTask().execute(mUsernameView.getText().toString(), mPasswordView.getText().toString());


                }
            }
        }).start();


    }


    public class LoginRestTask extends AsyncTask<String, Void, Integer> {




        protected Integer doInBackground(String... params) {

            String URI = "login";
            gson = new Gson();
            ClientResource cr = new ClientResource(baseURI + URI);
            String gsonResponse = null;
            Boolean response;
            //      Log.i("Connection", "Connection establishing");

            try {
                gsonResponse = cr.post(gson.toJson(params[0] + ";" + params[1], String.class)).getText();
                if (cr.getStatus().getCode() == 200) {
                    response = gson.fromJson(gsonResponse, Boolean.class);
                    if (response)
                        return 0;
                    return 2;

                } else if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE) {
                    return 1;


                } else {
                    return 2;

                }


            } catch (ResourceException | IOException e1) {
                String text = "Error: " + cr.getStatus().getCode() + " - " + cr.getStatus().getDescription() + " - " + cr.getStatus().getReasonPhrase();
                Log.e(TAG, text);
                return 3;
            }
        }


        protected void onPostExecute(Integer c) {

            if (c == 0) {
                Intent myIntent = new Intent(LogInActivity.this, MainActivity.class);

                SharedPreferences.Editor edit = preferences.edit();

                checkBox = (CheckBox) findViewById(R.id.checkBox);
                if (checkBox.isChecked()) {

                    edit.putString("username", String.valueOf(mUsernameView.getText())).apply();
                    edit.putString("password", String.valueOf(mPasswordView.getText())).apply();

                } else if (!checkBox.isChecked()) {

                    edit.putString("user", String.valueOf(mUsernameView.getText())).apply();

                }


                startActivity(myIntent);




            } else if (c == 1) {
                mResultOp.setText("Unregistered User");
                Toast.makeText(getApplicationContext(), "Unregistered User", Toast.LENGTH_SHORT).show();


            } else if (c == 2) {
                mResultOp.setText("Wrong credentials");
                Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_SHORT).show();

            }

            mDialog.dismiss();

        }




    }

    public class RegisterUserTask extends AsyncTask<String, Void, String> {

        /* AsyncTask per l'esecuzione in background delle operazioni asincrone dedicate alla registrazione di un nuovo utente */

        private String response;

        @Override
        protected String doInBackground(String... params) {
            ClientResource cr;
            cr = new ClientResource(baseURI + params[0]); // Creo la risorsa client basandomi su una URI costituita da baseURI + parametro passato dal metodo di gestione di un componente grafico (bottone) */
            gson = new Gson();

            String response = null;
            User user = new User(params[1], params[2]); // Creo l'oggetto utente con i parametri ottenuti dal metodo per l'interazione con il componente grafico (e forniti dagli EditText)

            try {
                response = cr.post(gson.toJson(user, User.class)).getText(); // Effettuo la Request HTTP con metodo "POST" e inserisco in response la Response HTTP.
                if (cr.getStatus().getCode() == ErrorCodes.INVALID_KEY_CODE) // Se mi viene restituito il codice di errore per una chiave invalida, lancio l'eccezione (ad esempio, Username duplicato)
                    throw gson.fromJson(response, InvalidKeyException.class);
            } catch (IOException e) {
                String text = "Error: " + cr.getStatus().getCode() + " - " + cr.getStatus().getDescription() + " - " + cr.getStatus().getReasonPhrase();
                Log.e(TAG, text);
            } catch (InvalidKeyException e) {
                response = null;

                runOnUiThread(new Runnable() { // Poiché queste operazioni si stanno effettuando in un metodo dell'AsyncTask, per la visualizzazione di Toast è necessario eseguire un "Thread UI".
                    public void run() {
                        // aggiunto per il testing
                        mResultOp.setVisibility(View.INVISIBLE);
                        mResultOp.setText("Username già utilizzato.");
                        Toast.makeText(getApplicationContext(), "Username già utilizzato.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return response;
        }

        @Override
        protected void onPostExecute(final String res) {
            if (res != null)
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() { // Poiché queste operazioni si stanno effettuando in un metodo dell'AsyncTask, per la visualizzazione di Toast è necessario eseguire un "Thread UI".
                public void run() {

                }
            });


        }
    }

}
