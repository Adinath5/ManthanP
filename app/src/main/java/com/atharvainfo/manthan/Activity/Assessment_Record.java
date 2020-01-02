package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.NameValuePair;


public class Assessment_Record extends AppCompatActivity {

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView txt_exam_name,txt_student_name,txt_right_que,txt_wrong_que,txt_score,txt_result;
    String student_id,exam_id,exam_title,student_class,first_name,last_name, exid;

    AppCompatButton btn_review,btn_dashboard;

    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment__record);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_assessment__record);
        initToolbar();
        txt_exam_name= (TextView) findViewById(R.id.txt_exam_name);
        txt_student_name= (TextView) findViewById(R.id.txt_student_name);
        txt_right_que= (TextView) findViewById(R.id.txt_right_que);
        txt_wrong_que= (TextView) findViewById(R.id.txt_wrong_que);
        txt_score= (TextView) findViewById(R.id.txt_score);
        txt_result= (TextView) findViewById(R.id.txt_result);
        btn_review= (AppCompatButton) findViewById(R.id.btn_review);
        btn_dashboard= (AppCompatButton) findViewById(R.id.btn_dashboard);


        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_class= sharedPreferences.getString("student_class",null);
        student_id= sharedPreferences.getString("idtag",null);
        first_name= sharedPreferences.getString("first_name",null);
        last_name= sharedPreferences.getString("last_name",null);
        exam_id= sharedPreferences.getString("exam_id",null);
        exam_title= sharedPreferences.getString("exam_title",null);
        exid = sharedPreferences.getString("exid",null);
      //  GetAssessmentRecord();
        txt_exam_name.setText(exam_id+" : "+exam_title);
        txt_student_name.setText(first_name+" "+ last_name);

        fetchJSON();

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i = new Intent(Assessment_Record.this, KeyReview.class);
                //startActivity(i);
            }
        });
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Assessment_Record.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Assessment_Record.this, KeyReview.class);
                startActivity(i);

            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(exam_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(Assessment_Record.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Assessment_Record.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void fetchJSON(){


        showSimpleProgressDialog(this, "Getting User Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("studentid", student_id);
                map.put("examid", exam_id);
                map.put("exid", exid);


                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_GET_ASSESSMENT_RESULT);
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

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Result").equals("success")) {
                removeSimpleProgressDialog();

                JSONArray json = jsonObject.getJSONArray("assesrecord");
                for (int i = 0; i < json.length(); i++)
                {

                    JSONObject obj = json.getJSONObject(i);
                    int scr = Integer.valueOf(obj.getString("rightans"));
                    double scor = (scr)*2;
                    txt_right_que.setText(obj.getString("rightans"));
                    txt_wrong_que.setText(obj.getString("wrongans"));
                    txt_score.setText(String.valueOf(scor));

                    Log.e("fetch data", student_id + "=" + first_name + "=" + student_class);

                }
            } else {
                removeSimpleProgressDialog();
                Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
