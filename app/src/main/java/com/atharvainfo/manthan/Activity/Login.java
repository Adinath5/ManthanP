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

import org.json.JSONArray;
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
    String usertype;

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


        edit_username = (TextInputEditText) findViewById(R.id.username);

        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);

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

                        if(!edit_username.getText().toString().matches("[0-9]{10}")){
                            Toast.makeText(Login.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                        } else {
                            fetchJSON();
                        }
                    }
                });


            }
        } else {
            Intent i = new Intent(Login.this, NoItemInternetImage.class);
            startActivity(i);
        }



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

        Log.i("UserName", username);
     //   Log.i("Pasdword", pass);
        showSimpleProgressDialog(this, "Signing Up...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("username", username);
                //map.put("password", pass);

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
                        JSONArray json = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject obj = json.getJSONObject(i);
                            sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("user_name", edit_username.getText().toString());
                            editor.putString("user_type", obj.getString("usertype"));
                            editor.commit();
                            usertype = obj.getString("usertype");
                        }

                        Log.e("Login",edit_username.getText().toString());
                        if (usertype.equals("CoOrdinator")) {
                            Intent i = new Intent(Login.this, codashboard.class);
                            startActivity(i);
                        }
                        if (usertype.equals("Teacher")) {
                            Intent i = new Intent(Login.this, codashboard.class);
                            startActivity(i);
                        }
                        if (usertype.equals("Student")) {
                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);
                        }

                    } else if (jsonObject.optString("result").equals("failure")) {
                        removeSimpleProgressDialog();
                        Intent i = new Intent( Login.this, Register.class );
                        startActivity( i );

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
