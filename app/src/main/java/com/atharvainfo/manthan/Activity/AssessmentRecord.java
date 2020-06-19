package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atharvainfo.manthan.Adapter.Assessment_Adapter;
import com.atharvainfo.manthan.Adapter.Exam_Adapter;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Model.Assessment_Model_List;
import com.atharvainfo.manthan.Model.Exam_Model_List;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AssessmentRecord extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Assessment_Adapter mAdapter;
    List<Assessment_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";
    private final int jsoncode = 1;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AutoCompleteTextView edit_search;
    LinearLayout empty_view;

    ProgressDialog progressDialog;
    private static ProgressDialog mProgressDialog;
    String student_id, ClassName, ExamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_record);



        empty_view  = (LinearLayout) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        edit_search=(AutoCompleteTextView)findViewById( R.id.et_search);
        rowListItem=new ArrayList<Assessment_Model_List>();
        //get_MyRecords();

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);

        initToolbar();
        getAssessmentRecord();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Exam Results");
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


    private void getAssessmentRecord(){


        showSimpleProgressDialog(this, "Getting User Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("studentid", student_id);

                // map.put("exid", exid);


                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_GET_ASSESSMENT);
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

                JSONArray j = jsonObject.getJSONArray("Data");
                for (int i = 0; i < j.length(); i++)
                {
                    JSONObject json = j.getJSONObject(i);
                    rowListItem.add(new Assessment_Model_List(json.getString("student_id"),
                            json.getString("exam_id"),
                            json.getString("examdate"),
                            json.getString("exid"),
                            json.getString("noofqt"),
                            json.getString("rightans"),
                            json.getString("wrongans"),
                            json.getString("solveqt"),
                            json.getString("excompleted")

                    ) );

                }

                if (rowListItem.size() > 0){
//                    progressDialog.dismiss();
                    removeSimpleProgressDialog();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
                    mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                    //rowListItem = get_ALL_CATEGORY();
                    mAdapter =new Assessment_Adapter(AssessmentRecord.this,rowListItem);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }else{
                    // progressDialog.dismiss();
                    removeSimpleProgressDialog();
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
