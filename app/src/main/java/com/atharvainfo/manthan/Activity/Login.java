package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.atharvainfo.manthan.Class.BackgroundWorker;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.NameValuePair;


public class Login extends AppCompatActivity {

    private View parent_view;
    TextInputEditText edit_username,edit_password;
    Button btn_sign_in, btn_register;

    AppCompatCheckBox check_keepsigned;
    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        parent_view = findViewById(android.R.id.content);

        parent_view = findViewById(android.R.id.content);
        edit_username = (TextInputEditText) findViewById(R.id.username);
        edit_password = (TextInputEditText) findViewById(R.id.password);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_register = (Button) findViewById(R.id.btn_register);
        check_keepsigned = (AppCompatCheckBox) findViewById(R.id.check_keepsigned);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {
                // connected to wifi

                btn_sign_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
                        checkLogin(view);
                    }
                });

                ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Intent i = new Intent(Login.this, ForgotPWD.class);
                      //  startActivity(i);
                        // Snackbar.make(parent_view, "Sign up for an account", Snackbar.LENGTH_SHORT).show();
                    }
                });

                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent register = new Intent(Login.this, Register.class);
                        startActivity(register);
                        finish();
                    }
                });
            }
        } else {
            Intent i = new Intent(Login.this, NoItemInternetImage.class);
            startActivity(i);
        }

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String usernamelog= sharedPreferences.getString("user_name",null);
        String passwordlog= sharedPreferences.getString("password",null);
        // String resultlog="2";
        Log.e("login",usernamelog +"/////"+passwordlog);
        if((!(usernamelog == null) )){
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // perform logic
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String username1= sharedPreferences.getString("username1",null);
        String password1= sharedPreferences.getString("password1",null);
        if((!(username1 == null) )){
            check_keepsigned.setChecked(true);
            edit_username.setText(username1);
            edit_password.setText(password1);
        }


        check_keepsigned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("username1", edit_username.getText().toString());
                    editor.putString("password1",  edit_password.getText().toString());
                    editor.commit();
                    Log.e("checked", edit_username.getText().toString() +"/////"+ edit_password.getText().toString());


                }

            }
        });

    }

    public void checkLogin(View arg0) {
        final String username = edit_username.getText().toString();

        final String pass = edit_password.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            edit_password.setError("Enter valid password");
        }
        String type = "login";
        // if(isValidEmail(username) && isValidPassword(pass))
        if(isValidPassword(pass))
        {
            //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            //backgroundWorker.execute(type, username, pass);
            fetchJSON();
        }
    }
    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }


    private void fetchJSON(){

        final String username = edit_username.getText().toString();
        final String pass = edit_password.getText().toString();

        Log.i("UserName", username);
        Log.i("Pasdword", pass);
        showSimpleProgressDialog(this, "Signing Up...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("username", username);
                map.put("password", pass);

                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_LOGIN);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss",result);
                try {
                    onTaskCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void onTaskCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("result").equals("success")) {
                        removeSimpleProgressDialog();
                        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("user_name", edit_username.getText().toString());
                        editor.putString("password", edit_password.getText().toString());
                        editor.commit();
                        Log.e("Login",edit_username.getText().toString() +"/////"+edit_password.getText().toString());
                        Intent i = new Intent( Login.this, MainActivity.class );
                        startActivity( i );

                    } else {
                        removeSimpleProgressDialog();
                        Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
    }
    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            removeSimpleProgressDialog();
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
