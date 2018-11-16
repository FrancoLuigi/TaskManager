package com.gmail.francoluigi95.taskAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Map;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private static final int ALERTTAG = 0, PROGRESSTAG = 1;

    private static final String TAG = "AlertDialogActivity";

    private DialogFragment mDialog;

    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferences = getSharedPreferences(prefName, MODE_PRIVATE);


        Button buttonGetFree = (Button) findViewById(R.id.buttonGetFree);
        Button buttonAdd = (Button) findViewById(R.id.buttonAdd);
        Button buttonComplete = (Button) findViewById(R.id.buttonComplete);
        Button buttonGetState = (Button) findViewById(R.id.buttonGetState);
        Button buttonRemove = (Button) findViewById(R.id.buttonRemove);
        Button buttonModify = (Button) findViewById(R.id.buttonModify);
        Button buttonAssign = (Button) findViewById(R.id.buttonAssign);

        Button buttonGet = (Button) findViewById(R.id.buttonGet);


        buttonGetFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, GetFreeActivity.class);
                startActivity(Intent);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, RemoveActivity.class);
                startActivity(Intent);
            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, GetActivity.class);
                startActivity(Intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(Intent);
            }
        });


        buttonGetState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, GetAllActivity.class);
                startActivity(Intent);
            }
        });

        buttonGetState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, GetAllActivity.class);
                startActivity(Intent);
            }
        });
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, CompleteActivity.class);
                startActivity(Intent);
            }
        });

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, ModifyActivity.class);
                startActivity(Intent);
            }
        });
        buttonAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, AssignActivity.class);
                startActivity(Intent);
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                // if (preferences.contains("user")) {
                showDialogFragment(ALERTTAG);

                return true;
            case R.id.infoApp:

                Intent Intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(Intent);
                return true;
            case R.id.help:
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    // Show desired Dialog
    public void showDialogFragment(int dialogID) {

        switch (dialogID) {

            // Show AlertDialog
            case ALERTTAG:

                // Create a new AlertDialogFragment
                mDialog = AlertDialogFragment.newInstance();

                // Show AlertDialogFragment
                mDialog.show(getFragmentManager(), "Alert");

                break;

            // Show ProgressDialog
            case PROGRESSTAG:

                // Create a new ProgressDialogFragment
                mDialog = ProgressDialogFragment.newInstance();

                // Show new ProgressDialogFragment
                mDialog.show(getFragmentManager(), "Shutdown");
                break;
        }
    }

    // Abort or complete ShutDown based on value of shouldContinue
    private void continueShutdown(boolean shouldContinue) {
        if (shouldContinue) {

            // Prevent further interaction with the ShutDown Butotn


            // Show ProgressDialog as shutdown process begins
            showDialogFragment(PROGRESSTAG);

            // Finish the ShutDown process
            finishShutdown();
        } else {

            // Abort ShutDown and dismiss dialog
            mDialog.dismiss();
        }
    }

    private void finishShutdown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Pretend to do something before
                    // shutting down
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    Log.i(TAG, e.toString());
                } finally {


                    preferences.edit().remove("username").apply();
                    preferences.edit().remove("password").apply();
                    preferences.edit().remove("user").apply();


                    Intent Intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(Intent);
                }
            }
        }).start();
    }

    // Class that creates the AlertDialog
    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Do you really want to exit?")

                    // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)


                    // Set up No Button
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    ((MainActivity) getActivity())
                                            .continueShutdown(false);
                                }
                            })

                    // Set up Yes Button
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {

                                    ((MainActivity) getActivity())
                                            .continueShutdown(true);

                                }
                            }).create();
        }
    }

    // Class that creates the ProgressDialog
    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        // Build ProgressDialog
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            //Create new ProgressDialog
            final ProgressDialog dialog = new ProgressDialog(getActivity());

            // Set Dialog message
            dialog.setMessage("Loading");

            // Dialog will be displayed for an unknown amount of time
            dialog.setIndeterminate(true);

            return dialog;
        }
    }


}