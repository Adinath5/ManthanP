package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.Tools;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;


public class view_exam extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    EditText edit_enroll_num;

    TextView edit_status,txt_exam_name,txt_subject,txt_deadline,txt_duration,txt_next_re_take,txt_passmark,txt_quetions,
            txt_exam_type,txt_exam_fee,edit_attende_status;
    Button btn_begin_assessment,btn_check_number;
    LinearLayout lyt_error,lyt_error_attend,lyt_enroll_num;
    String student_id,exam_id;
    String terms ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        initToolbar();

        btn_check_number= (Button) findViewById(R.id.btn_check_number);
        btn_begin_assessment = (Button) findViewById(R.id.btn_begin_assessment);
        lyt_error = (LinearLayout) findViewById(R.id.lyt_error);
        lyt_error_attend = (LinearLayout) findViewById(R.id.lyt_error_attend);
        lyt_enroll_num = (LinearLayout) findViewById(R.id.lyt_enroll_num);

        edit_status = (TextView) findViewById(R.id.edit_status);
        txt_exam_name = (TextView) findViewById(R.id.txt_exam_name);
        txt_subject = (TextView) findViewById(R.id.txt_subject);
        txt_duration = (TextView) findViewById(R.id.txt_duration);
        txt_quetions = (TextView) findViewById(R.id.txt_quetions);
        txt_exam_type = (TextView) findViewById(R.id.txt_exam_type);
        txt_exam_fee = (TextView) findViewById(R.id.txt_exam_fee);
        edit_attende_status = (TextView) findViewById(R.id.edit_attende_status);
        edit_enroll_num= (EditText) findViewById(R.id.edit_enroll_num);


        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);
        exam_id= sharedPreferences.getString("exam_id",null);

        String exam_title= sharedPreferences.getString("exam_title",null);
        String exam_type= sharedPreferences.getString("exam_type",null);
        String exam_fee= sharedPreferences.getString("exam_fee",null);
        String exam_class= sharedPreferences.getString("exam_class",null);
        String exam_duration= sharedPreferences.getString("exam_duration",null);
        String exam_status= sharedPreferences.getString("exam_status",null);
        String next_retake= sharedPreferences.getString("next_retake",null);
        String quetions= sharedPreferences.getString("quetions",null);
        String exam_allowed= sharedPreferences.getString("exam_allowed",null);
        terms= sharedPreferences.getString("terms",null);

        Log.e("data : " ,exam_allowed +"///"+student_id+"//"+exam_id);


        if (exam_status != null && !exam_status.isEmpty() && !exam_status.equals("null")){

            btn_begin_assessment.setVisibility(View.GONE);
            lyt_error.setVisibility(View.VISIBLE);

        }else {
            if(exam_type.equals("Paid")){
                if(exam_allowed.equals("NO")){
                    lyt_error.setVisibility(View.GONE);
                    lyt_enroll_num.setVisibility(View.VISIBLE);
                    btn_begin_assessment.setVisibility(View.GONE);
                }
            }else{
                btn_begin_assessment.setVisibility(View.VISIBLE);
                lyt_error.setVisibility(View.GONE);
            }

        }
        edit_status.setText(exam_status);
        txt_exam_name.setText(exam_title);
        txt_duration.setText(exam_duration+" Minutes");
        txt_quetions.setText(quetions);
        txt_exam_type.setText(exam_type);
        txt_exam_fee.setText(exam_fee);

        btn_begin_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTermServicesDialog();
            }
        });

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Assessment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Examination.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTermServicesDialog() {
        final Dialog dialog = new Dialog(view_exam.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_term_of_services);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ((TextView) dialog.findViewById(R.id.txt_terms)).setText(terms);
        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(view_exam.this)
                        .setIcon(R.drawable.logo)
                        .setTitle("Begin Assessment")
                        .setMessage("Are you sure you want to begin the assessment ?")
                        .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {

                                // Intent i = new Intent(View_Exam.this, StartExam.class);
                                Intent i = new Intent(view_exam.this, assessment.class);
                                startActivity(i);
                                finish();
                                dialog1.dismiss();


                            }
                        })
                        .setNegativeButton(getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                            }
                        })
                        .show();
                dialog.dismiss();

            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Button Decline Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Examination.class);
        startActivity(i);
        finish();
    }
}
