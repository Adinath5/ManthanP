package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atharvainfo.manthan.Adapter.Exam_Adapter;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.Model.Exam_Model_List;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Examination extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Exam_Adapter mAdapter;
    List<Exam_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AutoCompleteTextView edit_search;
    LinearLayout empty_view;
    Button testone, testtow, testthree, testfour, testfive,testsix,testseven,testeight,testnine,testten;
    Button testonepaper1, testonepaper2, testtwopaper1, testtwopaper2,testthreepaper1, testthreepaper2, testfourpaper1,testfourpaper2;
    Button testfivepaper1, testfivepaper2, testsixpaper1, testsixpaper2,testsevenpaper1, testsevenpaper2, testeightpaper1,testeightpaper2;
    Button testninepaper1, testninepaper2, testtenpaper1, testtenpaper2;

    CardView cardview1, cardview2, cardView3, cardView4,cardView5,cardView6,cardView7,cardView8,cardView9,cardView10;
    String student_id, ClassName, ExamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);

        initToolbar();
        deleteCache(Examination.this);

        //showDialog();

     //   get_Examinations();

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);
        ClassName= sharedPreferences.getString("classname",null);
        //ExamId = sharedPreferences.getString("exam_id",null);


        System.out.print(ClassName);
        System.out.print(ExamId);
        Log.i("ClassName", ClassName);
        //Log.i("ExamID", ExamId);


        testone = findViewById(R.id.testone);
        testtow = findViewById(R.id.testtwo);
        testthree = findViewById(R.id.testthree);
        testfour = findViewById(R.id.testfour);
        testfive = findViewById(R.id.testfive);
        testsix = findViewById(R.id.testsix);
        testseven = findViewById(R.id.testseven);
        testeight = findViewById(R.id.testeight);
        testnine = findViewById(R.id.testnine);
        testten = findViewById(R.id.testten);


        testonepaper1 = findViewById(R.id.tpaperone);
        testonepaper2 = findViewById(R.id.tpapertwo);
        testtwopaper1 = findViewById(R.id.testtwopaperone);
        testtwopaper2 = findViewById(R.id.testtwopapertwo);
        testthreepaper1 = findViewById(R.id.testthreepaperone);
        testthreepaper2 = findViewById(R.id.testthreepapertwo);
        testfourpaper1 = findViewById(R.id.testfourpaperone);
        testfourpaper2 = findViewById(R.id.testfourpapertwo);
        testfivepaper1 = findViewById(R.id.testfivepaperone);
        testfivepaper2 = findViewById(R.id.testfivepapertwo);
        testsixpaper1 = findViewById(R.id.testsixpaperone);
        testsixpaper2 = findViewById(R.id.testsixpapertwo);
        testsevenpaper1 = findViewById(R.id.testsevenpaperone);
        testsevenpaper2 = findViewById(R.id.testsevenpapertwo);
        testeightpaper1 = findViewById(R.id.testeightpaperone);
        testeightpaper2 = findViewById(R.id.testeightpapertwo);
        testninepaper1 = findViewById(R.id.testninepaperone);
        testninepaper2 = findViewById(R.id.testninepapertwo);
        testtenpaper1 = findViewById(R.id.testtenpaperone);
        testtenpaper2 = findViewById(R.id.testtenpapertwo);



        cardview1 = findViewById(R.id.card_view1);
        cardview2 = findViewById(R.id.card_view2);
        cardView3 = findViewById(R.id.card_view3);
        cardView4 = findViewById(R.id.card_view4);
        cardView5 = findViewById(R.id.card_view5);
        cardView6 = findViewById(R.id.card_view6);
        cardView7 = findViewById(R.id.card_view7);
        cardView8 = findViewById(R.id.card_view8);
        cardView9 = findViewById(R.id.card_view9);
        cardView10 = findViewById(R.id.card_view10);


        testone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview1.setVisibility(View.VISIBLE);
                cardview2.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                cardView4.setVisibility(View.GONE);
                cardView5.setVisibility(View.GONE);
                cardView6.setVisibility(View.GONE);
                cardView7.setVisibility(View.GONE);
                cardView8.setVisibility(View.GONE);
                cardView9.setVisibility(View.GONE);
                cardView10.setVisibility(View.GONE);
            }
        });

        testtow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview1.setVisibility(View.GONE);
                cardview2.setVisibility(View.VISIBLE);
                cardView3.setVisibility(View.GONE);
                cardView4.setVisibility(View.GONE);
                cardView5.setVisibility(View.GONE);
                cardView6.setVisibility(View.GONE);
                cardView7.setVisibility(View.GONE);
                cardView8.setVisibility(View.GONE);
                cardView9.setVisibility(View.GONE);
                cardView10.setVisibility(View.GONE);
            }
        });

        testthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview1.setVisibility(View.GONE);
                cardview2.setVisibility(View.GONE);
                cardView3.setVisibility(View.VISIBLE);
                cardView4.setVisibility(View.GONE);
                cardView5.setVisibility(View.GONE);
                cardView6.setVisibility(View.GONE);
                cardView7.setVisibility(View.GONE);
                cardView8.setVisibility(View.GONE);
                cardView9.setVisibility(View.GONE);
                cardView10.setVisibility(View.GONE);
            }
        });
        testfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview1.setVisibility(View.GONE);
                cardview2.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                cardView4.setVisibility(View.VISIBLE);
                cardView5.setVisibility(View.GONE);
                cardView6.setVisibility(View.GONE);
                cardView7.setVisibility(View.GONE);
                cardView8.setVisibility(View.GONE);
                cardView9.setVisibility(View.GONE);
                cardView10.setVisibility(View.GONE);
            }
        });
        testfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview1.setVisibility(View.GONE);
                cardview2.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                cardView4.setVisibility(View.GONE);
                cardView5.setVisibility(View.VISIBLE);
                cardView6.setVisibility(View.GONE);
                cardView7.setVisibility(View.GONE);
                cardView8.setVisibility(View.GONE);
                cardView9.setVisibility(View.GONE);
                cardView10.setVisibility(View.GONE);
            }
        });
                testsix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardview1.setVisibility(View.GONE);
                        cardview2.setVisibility(View.GONE);
                        cardView3.setVisibility(View.GONE);
                        cardView4.setVisibility(View.GONE);
                        cardView5.setVisibility(View.GONE);
                        cardView6.setVisibility(View.VISIBLE);
                        cardView7.setVisibility(View.GONE);
                        cardView8.setVisibility(View.GONE);
                        cardView9.setVisibility(View.GONE);
                        cardView10.setVisibility(View.GONE);
                    }
                });

                testseven.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardview1.setVisibility(View.GONE);
                        cardview2.setVisibility(View.GONE);
                        cardView3.setVisibility(View.GONE);
                        cardView4.setVisibility(View.GONE);
                        cardView5.setVisibility(View.GONE);
                        cardView6.setVisibility(View.GONE);
                        cardView7.setVisibility(View.VISIBLE);
                        cardView8.setVisibility(View.GONE);
                        cardView9.setVisibility(View.GONE);
                        cardView10.setVisibility(View.GONE);
                    }
                });

                testeight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardview1.setVisibility(View.GONE);
                        cardview2.setVisibility(View.GONE);
                        cardView3.setVisibility(View.GONE);
                        cardView4.setVisibility(View.GONE);
                        cardView5.setVisibility(View.GONE);
                        cardView6.setVisibility(View.GONE);
                        cardView7.setVisibility(View.GONE);
                        cardView8.setVisibility(View.VISIBLE);
                        cardView9.setVisibility(View.GONE);
                        cardView10.setVisibility(View.GONE);
                    }
                });
                testnine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardview1.setVisibility(View.GONE);
                        cardview2.setVisibility(View.GONE);
                        cardView3.setVisibility(View.GONE);
                        cardView4.setVisibility(View.GONE);
                        cardView5.setVisibility(View.GONE);
                        cardView6.setVisibility(View.GONE);
                        cardView7.setVisibility(View.GONE);
                        cardView8.setVisibility(View.GONE);
                        cardView9.setVisibility(View.VISIBLE);
                        cardView10.setVisibility(View.GONE);
                    }
                });

                testten.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardview1.setVisibility(View.GONE);
                        cardview2.setVisibility(View.GONE);
                        cardView3.setVisibility(View.GONE);
                        cardView4.setVisibility(View.GONE);
                        cardView5.setVisibility(View.GONE);
                        cardView6.setVisibility(View.GONE);
                        cardView7.setVisibility(View.GONE);
                        cardView8.setVisibility(View.GONE);
                        cardView9.setVisibility(View.GONE);
                        cardView10.setVisibility(View.VISIBLE);
                    }
                });

        testonepaper1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   // Log.i("ExamID", ExamId);

                //if (ClassName == getString(R.string.firstclass)) {
                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("exam_id", ExamId);
                    editor.putString("exam_title", "PaperOne");
                    editor.putString("exam_type", "Free");
                    editor.putString("exam_fee", "0.00");
                    editor.putString("exam_class", ClassName);
                    editor.putString("exam_duration", "60");
                    editor.putString("exam_status", "");
                    editor.putString("quetions", "75");
                    editor.putString("exam_allowed", "Yes");
                    editor.putString("terms", "We would request you to kindly read the terms of service carefully and consider these as the terms of agreement between you and us. This governs the use and access to the Android Application and the services to you and your associates. ");
                    editor.commit();

                    Intent i = new Intent(Examination.this, view_exam.class);
                    startActivity(i);
                    finish();
               // }

            }
        });

        testonepaper2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // if (ClassName == getString(R.string.firstclass)) {
                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("exam_id", ExamId);
                    editor.putString("exam_title", "PaperTwo");
                    editor.putString("exam_type", "Free");
                    editor.putString("exam_fee", "0.00");
                    editor.putString("exam_class", ClassName);
                    editor.putString("exam_duration", "60");
                    editor.putString("exam_status", "");
                    editor.putString("quetions", "75");
                    editor.putString("exam_allowed", "Yes");
                    editor.putString("terms", "We would request you to kindly read the terms of service carefully and consider these as the terms of agreement between you and us. This governs the use and access to the Android Application and the services to you and your associates. ");
                    editor.commit();

                    Intent i = new Intent(Examination.this, view_exam.class);
                    startActivity(i);
                    finish();
                //}

            }
        });


    }

    private void initToolbar() {

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String classname = sharedPreferences.getString("classname",null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(classname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(Examination.this);
        progressDialog.setTitle("Exams");
        progressDialog.setMessage("Please wait while showing Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }


    private void fetchJSON(){


        showSimpleProgressDialog(this, "Getting User Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("studentid", student_id);
               // map.put("examid", exam_id);
               // map.put("exid", exid);


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
                   // txt_right_que.setText(obj.getString("rightans"));
                   // txt_wrong_que.setText(obj.getString("wrongans"));

                   // Log.e("fetch data", student_id + "=" + first_name + "=" + student_class);

                }
            } else {
                removeSimpleProgressDialog();
                Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void get_Examinations(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_Examinations,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getCategory(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String student_class= sharedPreferences.getString("student_class",null);
                String student_id= sharedPreferences.getString("idtag",null);

                Log.e("student_class",student_class +" student_id:"+student_id);

                params.put("student_class",student_class);
                params.put("student_id",student_id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Examination.this);
        queue.add(stringRequest);
    }
    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Exam_Model_List(json.getString("exam_id"),
                        json.getString("duration"),
                        json.getString("passmark"),
                        json.getString("re_take_after"),
                        json.getString("exam_title"),
                        json.getString("type"),
                        json.getString("exam_fee"),
                        json.getString("dept_name"),
                        json.getString("class_name"),
                        json.getString("subject"),
                        json.getString("deadline"),
                        json.getString("exam_status"),
                        json.getString("next_retake"),
                        json.getString("quetions"),
                        json.getString("exam_attended"),
                        json.getString("student_retake"),
                        json.getString("exam_allowed"),
                        json.getString("next_retake_b"),
                        json.getString("terms")

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0){
            progressDialog.dismiss();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            //rowListItem = get_ALL_CATEGORY();
            mAdapter =new Exam_Adapter(Examination.this,rowListItem);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else{
            progressDialog.dismiss();
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }


        // Capture Text in EditText
        edit_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edit_search.getText().toString().toLowerCase( Locale.getDefault());
                mAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Examination.this, classselect.class);
        startActivity(i);
        finish();
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
