package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.Tools;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;

public class Help extends AppCompatActivity {

    EditText edit_fullname,edit_contact,edit_email,edit_message;
    Button bt_submit;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();


        edit_fullname = (EditText) findViewById(R.id.edit_fullname);
        edit_contact = (EditText) findViewById(R.id.edit_contact);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_message = (EditText) findViewById(R.id.edit_message);
        bt_submit = (Button) findViewById(R.id.bt_submit);


    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validate() {
        boolean valid = true;

        String fullname = edit_fullname.getText().toString();
        String contact = edit_contact.getText().toString();
        String email = edit_email.getText().toString();
        String message = edit_message.getText().toString();

        if (message.isEmpty()) {
            edit_message.setError("Enter Message");
            valid = false;
        } else {
            edit_message.setError(null);
        }
        if (fullname.isEmpty()) {
            edit_fullname.setError("Enter your name");
            valid = false;
        } else {
            edit_fullname.setError(null);
        }
        if (contact.isEmpty()) {
            edit_contact.setError("Enter Contact Number");
            valid = false;
        } else {
            edit_contact.setError(null);
        }
        if (TextUtils.isEmpty(email)) {
            edit_email.setError("Field required");
            valid = false;
        } else if (!isEmailValid(email)) {
            edit_email.setError("Invalid email");
            valid = false;
        }else {
            edit_email.setError(null);
        }

        return valid;
    }
    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void emptyInputEditText(){
        edit_fullname.setText(null);
        edit_contact.setText(null);
        edit_email.setText(null);
        edit_message.setText(null);
    }


}
