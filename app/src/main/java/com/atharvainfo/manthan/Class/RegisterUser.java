package com.atharvainfo.manthan.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.atharvainfo.manthan.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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

import static android.content.Context.MODE_PRIVATE;


public class RegisterUser extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String user_name,password, emailid;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    String student_id,first_name,last_name,gender,department,student_class,email,role,avator,dept_name,class_name;
    public RegisterUser(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        if(type.equals("Register")) {


            try {
                user_name = params[1];
                password = params[2];
                emailid = params[3];

                URL url = new URL( MyConfig.URL_REGISTER);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+ URLEncoder.encode(user_name,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(password,"UTF-8")+"&"
                        + URLEncoder.encode("emailid","UTF-8")+"="+ URLEncoder.encode(emailid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Register Status");
    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage(result);
        //alertDialog.show();
        Log.d("Result", result.toString());
        if(result.equals( "Register Success" )) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("user_name", user_name);
            editor.putString("password", password);
            editor.putString("emailid", emailid);
            editor.commit();
            Log.e("Register",user_name +"/////"+password + "/////"+emailid);
            Intent i = new Intent( context, MainActivity.class );
            context.startActivity( i );
            alertDialog.dismiss();
        } else if (result.equals("User Already Exists")){
           // GetUserdata();
            Intent i = new Intent( context, MainActivity.class );
            context.startActivity( i );
            alertDialog.dismiss();
        }


        /*else{
            if(result.equals( "2" )) {
                sharedPreferences = context.getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("user_name", user_name);
                editor.putString("password", password);
                editor.putString("result", result);
                editor.commit();
                Log.e("login",user_name +"/////"+password +"/////"+result);
                Intent i = new Intent( context, Update_Curr_Location.classes );
                context.startActivity( i );
                alertDialog.dismiss();
            }
        }*/



    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
