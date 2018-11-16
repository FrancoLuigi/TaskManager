package com.gmail.francoluigi95.taskAndroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.restlet.resource.ClientResource;
import org.restlet.security.User;

import java.io.IOException;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;

public class RegisterActivity extends AppCompatActivity {

    private String baseURI = "http://10.0.2.2:8182/TaskRegApplication/";
    private EditText username;
    private EditText password;
    private Button registrationButton;
    private Gson gson;
    private final String TAG = "LUIGI";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        registrationButton = (Button) findViewById(R.id.registerUser);


        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().length() == 0 || password.getText().toString().length() == 0) /* Verifico se username e password inseriti nei campi siano sufficientemente lunghi*/ {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Username e/o password non riempiti correttamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    new RegisterUserTask().execute("registration", username.getText().toString(), password.getText().toString());
            }
        });

    }


    /*
        Task per le operazioni del client
    */

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
