package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atharvainfo.manthan.Adapter.Notice_Adapter;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Model.Notice_Model_List;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Notice extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Notice_Adapter mAdapter;
    List<Notice_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    LinearLayout empty_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        initToolbar();
        showDialog();

        empty_view  = (LinearLayout) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        rowListItem=new ArrayList<Notice_Model_List>();
        get_Notice();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(Notice.this);
        progressDialog.setTitle("Notice");
        progressDialog.setMessage("Please wait while showing Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
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


    public void get_Notice(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_NOTICE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                           // j = new JSONObject(response);
                           // result = j.getJSONArray(JSON_ARRAY);
                           // getRecord(result);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("Result").equals("Notice Not Found")) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "No Notice Found", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray json = jsonObject.getJSONArray("notlist");

                                //j = new JSONObject(response);
                                //result = j.getJSONArray(JSON_ARRAY);
                                result = jsonObject.getJSONArray("notlist");
                                getRecord(result);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });
      /*  {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String student_id= sharedPreferences.getString("idtag",null);

                Log.e("student_id",student_id);

                params.put("student_id",student_id);

                return params;
            }
        };*/
        RequestQueue queue = Volley.newRequestQueue(Notice.this);
        queue.add(stringRequest);
    }
    private void getRecord(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Notice_Model_List(json.getString("id"),
                        json.getString("title"),
                        json.getString("notice"),
                        json.getString("posted_date"),
                        json.getString("admin_email"),
                        json.getString("admin_avator")

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
            mAdapter =new Notice_Adapter(Notice.this,rowListItem);
            mRecyclerView.setAdapter(mAdapter);

            // on item list clicked
            mAdapter.setOnItemClickListener(new Notice_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Notice_Model_List obj, int position) {
                    // Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
                }
            });


        }else{
            progressDialog.dismiss();
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }



    }
}
