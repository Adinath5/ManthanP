package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.Tools;

import org.json.JSONArray;


public class classselect extends AppCompatActivity {

    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AutoCompleteTextView edit_search;
    LinearLayout empty_view;

    LinearLayout lyt_first,lyt_second,lyt_third,lyt_fourth,lyt_five,lyt_six, lyt_seven, lyt_eight,lyt_nine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classselect);

        initToolbar();

        lyt_first = (LinearLayout) findViewById(R.id.lyt_first);
        lyt_second = (LinearLayout) findViewById(R.id.lyt_second);
        lyt_third = (LinearLayout) findViewById(R.id.lyt_third);
        lyt_fourth = (LinearLayout) findViewById(R.id.lyt_fourth);
        lyt_five = (LinearLayout) findViewById(R.id.lyt_fifth);
        lyt_six= (LinearLayout) findViewById(R.id.lyt_sixth);
        lyt_seven= (LinearLayout) findViewById(R.id.lyt_seventh);
        lyt_eight= (LinearLayout) findViewById(R.id.lyt_eight);
        lyt_nine= (LinearLayout) findViewById(R.id.lyt_ninth);

        lyt_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("classname", getString(R.string.firstclass));
                //editor.putString("exam_id", getString(R.string.firstexid));
                editor.commit();
                System.out.print(R.string.Secondclass);
                Intent i = new Intent(classselect.this, Examination.class);
                startActivity(i);

            }
        });

        lyt_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("classname", getString(R.string.Secondclass));
                //editor.putString("exam_id", getString(R.string.twoexid));
                editor.commit();
                System.out.print(R.string.Secondclass);

                Intent i = new Intent(classselect.this, Examination.class);
                startActivity(i);
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Class");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(classselect.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
