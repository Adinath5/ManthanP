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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atharvainfo.manthan.Class.BackgroundWorker;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Class.RegisterUser;
import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.Tools;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.atharvainfo.manthan.utils.HttpRequest;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import cz.msebera.android.httpclient.util.TextUtils;

public class Register extends AppCompatActivity {

    private View parent_view;
    TextInputEditText edit_username,edit_password, edit_emailid;
    Button btn_register;
    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    String student_id,first_name,last_name,gender,department,student_class,email,role,avator,dept_name,class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        parent_view = findViewById(android.R.id.content);

        parent_view = findViewById(android.R.id.content);
        edit_username = (TextInputEditText) findViewById(R.id.username);
        edit_password = (TextInputEditText) findViewById(R.id.password);
        edit_emailid = (TextInputEditText) findViewById(R.id.emailid);

        btn_register = (Button) findViewById(R.id.btn_register);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {
                // connected to wifi


                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        checkRegistration(v);
                    }
                });
            }
        } else {
            Intent i = new Intent(Register.this, NoItemInternetImage.class);
            startActivity(i);
        }


    }


    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length()!=10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }


    public void checkRegistration(View arg0) {
        final String username = edit_username.getText().toString();
        final String pass = edit_password.getText().toString();
        final String email = edit_emailid.getText().toString();

        if (!isValidPassword(pass)) {
            //Set error message for password field
            edit_password.setError("Enter valid password");
        }

        if(!isValidEmail(email)){
            edit_emailid.setError("Enter Valid Email ID");
        }

        if(!isValidPhoneNumber(username)){
            edit_username.setError("Enter Valid Mobile No.");
        }

        String type = "Register";
        // if(isValidEmail(username) && isValidPassword(pass))
        if(isValidPassword(pass) && isValidEmail(email) && isValidPhoneNumber(username))
        {
            //RegisterUser registerUser = new RegisterUser(this);
            //registerUser.execute(type, username, pass, email);
            fetchJSON();
        }

    }



    private void fetchJSON(){

        final String username = edit_username.getText().toString();
        final String pass = edit_password.getText().toString();
        final String email = edit_emailid.getText().toString();

        Log.i("UserName", username);
        Log.i("Pasdword", pass);
        showSimpleProgressDialog(this, "Registring...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("username", username);
                map.put("password", pass);
                map.put("emailid", email);

                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_REGISTER);
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
                    if (jsonObject.optString("Result").equals("success")) {
                        removeSimpleProgressDialog();
                        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("user_name", edit_username.getText().toString());
                        editor.putString("password", edit_password.getText().toString());
                        editor.putString("emailid", edit_emailid.getText().toString());
                        editor.commit();
                        Log.e("Register",edit_username.getText().toString() +"/////"+edit_password.getText().toString() + "/////"+edit_emailid.getText().toString());
                        Intent i = new Intent( Register.this, MainActivity.class );
                        startActivity( i );

                    } else if (jsonObject.optString("Result").equals("User Already Exists")){
                        removeSimpleProgressDialog();
                        Intent i = new Intent( Register.this, Login.class );
                        startActivity( i );

                    } else {
                        removeSimpleProgressDialog();
                        Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if (isSuccess(response)) {

                    Log.d("ResponseList", response);
//                    pref.setUserName(username.toString());




                }else {



                }*/
        }
    }

    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Result").equals("success")) {
                removeSimpleProgressDialog();
                return true;
            } else if (jsonObject.optString("Result").equals("User Already Exists")){
                removeSimpleProgressDialog();
                Intent i = new Intent( Register.this, Login.class );
                startActivity( i );
                return false;
            } else {
                removeSimpleProgressDialog();
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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

    private void GetUserdata() { String data;

        try {
            sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
            sharedPreferences.edit();
            String user_name= sharedPreferences.getString("user_name",null);
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://192.168.1.35/photo_editor/select.php");
            HttpPost httppost = new HttpPost( MyConfig.URL_PROFILE_DATA);
            nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);
            Log.e("Check Data Main", data);
            try
            {
                JSONArray json = new JSONArray(data);
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    student_id = obj.getString("studentid");
                    first_name= obj.getString("firstname");
                    last_name = obj.getString("lastname");
                    gender = obj.getString("gender");
                    student_class = obj.getString("classname");
                    email = obj.getString("useremail");
                    class_name = obj.getString("schoolname");


                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("idtag", student_id);
                    editor.putString("firstname", first_name);
                    editor.putString("lastname", last_name);
                    editor.putString("gender", gender);
                    editor.putString("studentclass", student_class);
                    editor.putString("emailtag", email);
                    editor.putString("studentschool", class_name);
                    editor.commit();

                    Log.e("fetch data",student_id+"="+first_name+"="+student_class);
                }

            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
