package com.gmail.francoluigi95.taskAndroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.francoluigi95.taskAndroid.commons.ErrorCodes;
import com.gmail.francoluigi95.taskAndroid.commons.InvalidKeyException;
import com.gmail.francoluigi95.taskAndroid.commons.Task;
import com.google.gson.Gson;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static com.gmail.francoluigi95.taskAndroid.LogInActivity.prefName;

public class InfoActivity extends AppCompatActivity {

    private TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


    }
}

