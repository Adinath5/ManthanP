package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atharvainfo.manthan.Adapter.Review_Adapter;
import com.atharvainfo.manthan.Class.DatabaseHelper;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Model.Review_Model;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KeyReview extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Review_Adapter mAdapter;
    List<Review_Model> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SQLiteDatabase mdatabase;
    DatabaseHelper helper;

    String student_id,exam_id,exam_title,student_class,first_name,last_name, exid, SelectedId, SelectedAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_review);

        initToolbar();

        helper = new DatabaseHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        rowListItem=new ArrayList<Review_Model>();

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_class= sharedPreferences.getString("student_class",null);
        student_id= sharedPreferences.getString("idtag",null);
        first_name= sharedPreferences.getString("first_name",null);
        last_name= sharedPreferences.getString("last_name",null);
        exam_id= sharedPreferences.getString("exam_id",null);
        exam_title= sharedPreferences.getString("exam_title",null);
        exid = sharedPreferences.getString("exid",null);

        get_REVIEW();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review");
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


    public void get_REVIEW(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_QUETIONS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                       /* try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getCategory(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("Result").equals("Success")) {
                                JSONArray json = jsonObject.getJSONArray("qlist");

                                //j = new JSONObject(response);
                                //result = j.getJSONArray(JSON_ARRAY);
                                result = jsonObject.getJSONArray("qlist");
                                getCategory(result);
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

                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String student_id= sharedPreferences.getString("idtag",null);
                String exam_id= sharedPreferences.getString("exam_id",null);

                Log.e("exam_id",exam_id);

                params.put("student_id",student_id);
                params.put("exam_id",exam_id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(KeyReview.this);
        queue.add(stringRequest);
    }
    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){

            try {

            JSONObject json = j.getJSONObject(i);
            String qid = json.getString("quetion_id");
            helper.openDatabase();
            helper.close();
            mdatabase = helper.getWritableDatabase();
            String sql ="Select studentid,examid,exid,selected_and,checkedid from assesmenttbl where exid ='"+  exid +"' and studentid='"+ student_id +"' and examid='"+ exam_id +"' and questionid= '"+ qid +"'";
            Cursor cursor = mdatabase.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SelectedAns = cursor.getString(3);
                    SelectedId = cursor.getString(4);
                }
            }
            cursor.close();
            mdatabase.close();

            Log.i("Selected ID", SelectedId.toString());
            Log.i("Selected Ans",SelectedAns.toString());

                rowListItem.add(new Review_Model(json.getString("quetion_id"),
                        json.getString("question"),
                        json.getString("qtpart1"),
                        json.getString("qtpart2"),
                        json.getString("qtpart3"),
                        json.getString("qtpart4"),
                        json.getString("qtpart5"),
                        json.getString("op1"),
                        json.getString("op2"),
                        json.getString("op3"),
                        json.getString("op4"),
                        json.getString("answer"),
                        json.getString("question_image"),
                        json.getString("question_image1"),
                        json.getString("question_image2"),
                        json.getString("question_image3"),
                        json.getString("question_image4"),
                        json.getString("op1_image"),
                        json.getString("op2_image"),
                        json.getString("op3_image"),
                        json.getString("op4_image"),
                        json.getString("questionno"),SelectedId, SelectedAns

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            mAdapter =new Review_Adapter(KeyReview.this,rowListItem);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }



    }
}
