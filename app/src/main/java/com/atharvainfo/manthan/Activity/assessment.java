package com.atharvainfo.manthan.Activity;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atharvainfo.manthan.Class.DatabaseHelper;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.Model.Quetion_Model_List;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

public class assessment extends AppCompatActivity {


    List<Quetion_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "result";

    String dateformat="yyyy-MM-dd";
    SimpleDateFormat dateform=new SimpleDateFormat(dateformat, Locale.US);

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText edit_fill_answer;
    public TextView txt_quetion, txt_quetion1,txt_quetion2, txt_quetion3,txt_quetion4;
    RadioGroup radiogrp;
    RadioButton radio1,radio2,radio3,radio4;

    TextView txt_timer;
    LinearLayout empty_view;
    String student_id,exam_id,exam_title,exam_duration, noofqt;
    String exid;

    Button btn_Previous,btn_Next,btn_submit;
    int i=0;
    String realaAns,student_retake;

    ProgressDialog progressDialog;
    private boolean isCanceled = false;

    AppCompatCheckBox check_invalid;
    int count;

    SQLiteDatabase mdatabase;
    DatabaseHelper helper;

    ImageView image_quetion,image_op1,image_op2,image_op3,image_op4, image_quetion1, image_quetion2, image_quetion3,image_quetion4;

    String selected_answer;
    int radioid;
    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;
    String rans="";
    String wans="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_assessment);

        helper = new DatabaseHelper(this);
        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String student_class= sharedPreferences.getString("student_class",null);
        student_id= sharedPreferences.getString("idtag",null);
        exam_id= sharedPreferences.getString("exam_id",null);
        exam_title= sharedPreferences.getString("exam_title",null);
        String exam_type= sharedPreferences.getString("exam_type",null);
        String exam_fee= sharedPreferences.getString("exam_fee",null);
        String exam_deadline= sharedPreferences.getString("exam_deadline",null);
        String exam_department= sharedPreferences.getString("exam_department",null);
        final String exam_class= sharedPreferences.getString("exam_class",null);
        exam_duration= sharedPreferences.getString("exam_duration",null);
        String exam_subject= sharedPreferences.getString("exam_subject",null);
        String exam_passmark= sharedPreferences.getString("exam_passmark",null);
        String exam_retake= sharedPreferences.getString("exam_retake",null);
        String exam_status= sharedPreferences.getString("exam_status",null);
        String next_retake= sharedPreferences.getString("next_retake",null);
        String quetions= sharedPreferences.getString("quetions",null);
        String exam_attended= sharedPreferences.getString("exam_attended",null);
        student_retake= sharedPreferences.getString("student_retake",null);
        String exam_allowed= sharedPreferences.getString("exam_allowed",null);
        String next_retake_b= sharedPreferences.getString("next_retake_b",null);

        noofqt = quetions;
        initToolbar();
        deleteCache(assessment.this);
        showDialog();

        txt_quetion = (TextView) findViewById(R.id.txt_quetion);
        txt_quetion1 = (TextView) findViewById(R.id.txt_quetion1);
        txt_quetion2 = (TextView) findViewById(R.id.txt_quetion2);
        txt_quetion3 = (TextView) findViewById(R.id.txt_quetion3);
        txt_quetion4 = (TextView) findViewById(R.id.txt_quetion4);
        //txt_quetion1 = (TextView) findViewById(R.id.txt_quetion4);
        radiogrp = (RadioGroup) findViewById(R.id.radiogrp);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        edit_fill_answer= (EditText) findViewById(R.id.edit_fill_answer);

        image_quetion = (ImageView) findViewById(R.id.image_quetion);
        image_op1 = (ImageView) findViewById(R.id.image_op1);
        image_op2 = (ImageView) findViewById(R.id.image_op2);
        image_op3 = (ImageView) findViewById(R.id.image_op3);
        image_op4 = (ImageView) findViewById(R.id.image_op4);
        image_quetion1 = (ImageView) findViewById(R.id.image_quetion1);
        image_quetion2 = (ImageView) findViewById(R.id.image_quetion2);
        image_quetion3 = (ImageView) findViewById(R.id.image_quetion3);

        btn_submit= (Button) findViewById(R.id.btn_submit);
        btn_Previous = (Button) findViewById(R.id.btn_Previous);
        btn_Next = (Button) findViewById(R.id.btn_Next);
        txt_timer=(TextView)findViewById( R.id.txt_timer);
        int minutes = Integer.parseInt(exam_duration);

        //check_invalid = (AppCompatCheckBox) findViewById(R.id.check_invalid);
        exid = UUID.randomUUID().toString();
        Submit_Assessment();


        new CountDownTimer(60*minutes*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(isCanceled)
                {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                }else {
                    long millis = millisUntilFinished;
                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    txt_timer.setText(hms);//set text
                }
            }
            public void onFinish() {
                txt_timer.setText("TIME'S UP!!");
                UpdateAssesment(); //On finish change timer text
            }
        }.start();


        rowListItem=new ArrayList<Quetion_Model_List>();
        get_Questions();

        if(i==0){
            btn_Previous.setVisibility(View.GONE);
        }else{
            btn_Previous.setVisibility(View.VISIBLE);
        }

        if(i==0){
            btn_Previous.setVisibility(View.GONE);
        }else{
            btn_Previous.setVisibility(View.VISIBLE);
        }
/*
        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //textview.setText(s.toString());
                String quetion_id = rowListItem.get(i).getQuetion_id();
                String real_ans = rowListItem.get(i).getAnswer();
                Calendar c = Calendar.getInstance();
                String exDate = dateform.format(c.getTime());

                helper.openDatabase();
                helper.close();
                mdatabase = helper.getWritableDatabase();


                ContentValues contentValues = new ContentValues();
                contentValues.put("studentid", student_id);
                contentValues.put("examid", exam_id);
                contentValues.put("questionid", quetion_id);
                contentValues.put("selected_and", s.toString());
                contentValues.put("realanswer", real_ans);
                contentValues.put("examdate", exDate);
                contentValues.put("classname", exam_class);
                contentValues.put("checkedid", "");
                contentValues.put("exid", exid);

                long id = mdatabase.insert("assesmenttbl", null , contentValues);
                if (id > 0) {

                }
                mdatabase.close();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        edit_fill_answer.addTextChangedListener(inputTextWatcher);
*/

        radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb= (RadioButton) findViewById(checkedId);
                int selectedId = radiogrp.getCheckedRadioButtonId();
                // Log.e("ID", String.valueOf(selectedId));
                if (checkedId == R.id.radio1) {
                    radioid = 1;
                    selected_answer = "A";
                }
                else if (checkedId == R.id.radio2){
                    radioid = 2;
                    selected_answer = "B";
                }
                else if (checkedId == R.id.radio3) {

                    radioid = 3;
                    selected_answer = "C";

                } else if (checkedId == R.id.radio4) {

                    radioid = 4;
                    selected_answer = "D";
                }else{
                    radioid=0;
                    selected_answer="";
                }

            }
        });


        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_Next.setVisibility(View.VISIBLE);
                i--;

                radiogrp.clearCheck();
                Log.e("PreviousQ: Student_id",student_id+"///"+exam_id+"//"+rowListItem.get(i).getQuetion_id()+"//"+exid);

                helper.openDatabase();
                helper.close();
                mdatabase = helper.getWritableDatabase();
                String sql ="Select studentid,examid,questionid,checkedid,selected_and,realanswer from assesmenttbl where exid ='"+ exid +"' and studentid='"+ student_id +"' and examid='"+ exam_id +"' and questionid='"+ rowListItem.get(i).getQuetion_id() +"'";
                Cursor cursor = mdatabase.rawQuery(sql, null);
                if (cursor.getCount() > 0){
                    while (cursor.moveToNext()){

                        int selected_id = cursor.getInt(3);
                        String selected_answer = cursor.getString(4);
                        Log.e("Selected ID :", String.valueOf(selected_id));
                        Log.e("Selected Ans : ", selected_answer);

                        if (selected_answer != null && !selected_answer.isEmpty() && !selected_answer.equals("null")){
                            if(selected_id==1){
                                radio1.setChecked(true);
                            }else if(selected_id==2){
                                radio2.setChecked(true);
                            }else if(selected_id==3){
                                radio3.setChecked(true);
                            }else if(selected_id==4){
                                radio4.setChecked(true);
                            }else{
                                // edit_fill_answer.setVisibility(View.VISIBLE);
                                //  radiogrp.setVisibility(View.GONE);
                                edit_fill_answer.setText(selected_answer);
                            }

                        }else{
                            edit_fill_answer.setText(null);
                            radiogrp.clearCheck();
                        }
                    }
                } else {
                    edit_fill_answer.setText(null);
                    radiogrp.clearCheck();
                }

                cursor.close();
                mdatabase.close();

                int srno = i+1;
                // Log.e("size", String.valueOf(rowListItem.size()) +" i = "+i +"srno:"+srno);

                if(i==0){
                    btn_Previous.setVisibility(View.GONE);
                    btn_Next.setVisibility(View.VISIBLE);
                }

                // txt_quetion.setText(srno+".  "+rowListItem.get(i).getQuestion());
                txt_quetion.setText(HtmlCompat.fromHtml(srno+".  "+rowListItem.get(i).getQuestion(), 0));
                //txt_quetion1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getQpart1(), 0));

                if (rowListItem.get(i).getQpart1() != null && !rowListItem.get(i).getQpart1().isEmpty() && !rowListItem.get(i).getQpart1().equals("null")){
                    txt_quetion1.setVisibility(View.VISIBLE);
                    String htmlText1 = rowListItem.get(i).getQpart1();
                    txt_quetion1.setText(HtmlCompat.fromHtml(htmlText1, 0));

                } else {
                    txt_quetion1.setVisibility(View.GONE);
                }

                if (rowListItem.get(i).getQpart2() != null && !rowListItem.get(i).getQpart2().isEmpty() && !rowListItem.get(i).getQpart2().equals("null")){
                    txt_quetion2.setVisibility(View.VISIBLE);
                    String htmlText2 = rowListItem.get(i).getQpart2();
                    txt_quetion2.setText(HtmlCompat.fromHtml(htmlText2, 0));

                } else {
                    txt_quetion2.setVisibility(View.GONE);
                }
                if (rowListItem.get(i).getQpart3() != null && !rowListItem.get(i).getQpart3().isEmpty() && !rowListItem.get(i).getQpart3().equals("null")){
                    txt_quetion3.setVisibility(View.VISIBLE);
                    String htmlText3 = rowListItem.get(i).getQpart3();
                    txt_quetion3.setText(HtmlCompat.fromHtml(htmlText3, 0));

                } else {
                    txt_quetion3.setVisibility(View.GONE);
                }
                if (rowListItem.get(i).getQpart4() != null && !rowListItem.get(i).getQpart4().isEmpty() && !rowListItem.get(i).getQpart4().equals("null")){
                    txt_quetion4.setVisibility(View.VISIBLE);
                    String htmlText4 = rowListItem.get(i).getQpart4();
                    txt_quetion4.setText(HtmlCompat.fromHtml(htmlText4, 0));

                } else {
                    txt_quetion4.setVisibility(View.GONE);
                }


                if(rowListItem.get(i).getOp1().equals("-")  && rowListItem.get(i).getOp1().equals("-")){
                    edit_fill_answer.setVisibility(View.VISIBLE);
                    radiogrp.setVisibility(View.GONE);
                }else{
                    edit_fill_answer.setVisibility(View.GONE);
                    radiogrp.setVisibility(View.VISIBLE);
                       /* radio1.setText(rowListItem.get(i).getOp1());
                        radio2.setText(rowListItem.get(i).getOp2());
                        radio3.setText(rowListItem.get(i).getOp3());
                        radio4.setText(rowListItem.get(i).getOp4());*/
                    radio1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp1(), 0));
                    radio2.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp2(), 0));
                    radio3.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp3(), 0));
                    radio4.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp4(), 0));


                    if (rowListItem.get(i).getQuestion_image() != null && !rowListItem.get(i).getQuestion_image().isEmpty() && !rowListItem.get(i).getQuestion_image().equals("null")){

                        image_quetion1.setVisibility(View.VISIBLE);
                        byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_quetion1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView


                    }else{
                        image_quetion1.setVisibility(View.GONE);
                    }
                    if (rowListItem.get(i).getQuestion_image1() != null && !rowListItem.get(i).getQuestion_image1().isEmpty() && !rowListItem.get(i).getQuestion_image1().equals("null")){

                        image_quetion2.setVisibility(View.VISIBLE);

                        byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image1(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_quetion2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                        //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                        //Log.e("quetion img", String.valueOf(img_path));

                    }else{
                        image_quetion2.setVisibility(View.GONE);
                    }

                    if (rowListItem.get(i).getQuestion_image2() != null && !rowListItem.get(i).getQuestion_image2().isEmpty() && !rowListItem.get(i).getQuestion_image2().equals("null")){

                        image_quetion3.setVisibility(View.VISIBLE);

                        byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image2(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_quetion3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                        //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                        //Log.e("quetion img", String.valueOf(img_path));

                    }else{
                        image_quetion3.setVisibility(View.GONE);
                    }

                    if (rowListItem.get(i).getOp1_image() != null && !rowListItem.get(i).getOp1_image().isEmpty() && !rowListItem.get(i).getOp1_image().equals("null")){

                        image_op1.setVisibility(View.VISIBLE);
                        byte[] decodedString = Base64.decode(rowListItem.get(i).getOp1_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_op1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                    }else{
                        image_op1.setVisibility(View.GONE);
                    }

                    if (rowListItem.get(i).getOp2_image() != null && !rowListItem.get(i).getOp2_image().isEmpty() && !rowListItem.get(i).getOp2_image().equals("null")){

                        image_op2.setVisibility(View.VISIBLE);
                        byte[] decodedString = Base64.decode(rowListItem.get(i).getOp2_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_op2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                    }else{
                        image_op2.setVisibility(View.GONE);
                    }


                    if (rowListItem.get(i).getOp3_image() != null && !rowListItem.get(i).getOp3_image().isEmpty() && !rowListItem.get(i).getOp3_image().equals("null")){

                        image_op3.setVisibility(View.VISIBLE);
                        byte[] decodedString = Base64.decode(rowListItem.get(i).getOp3_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_op3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                    }else{
                        image_op3.setVisibility(View.GONE);
                    }


                    if (rowListItem.get(i).getOp4_image() != null && !rowListItem.get(i).getOp4_image().isEmpty() && !rowListItem.get(i).getOp4_image().equals("null")){

                        image_op4.setVisibility(View.VISIBLE);
                        byte[] decodedString = Base64.decode(rowListItem.get(i).getOp4_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image_op4.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                    }else{
                        image_op4.setVisibility(View.GONE);
                    }


                }
            }
        });


        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Log.e("student_id data",student_id+"//"+exam_id+"///"+i);



                Calendar c = Calendar.getInstance();
                //selected_answer = rb.getText().toString();
                String quetion_id = rowListItem.get(i).getQuetion_id();
                String real_ans = rowListItem.get(i).getAnswer();
                String exDate = dateform.format(c.getTime());



                helper.openDatabase();
                helper.close();
                mdatabase = helper.getWritableDatabase();


                ContentValues contentValues = new ContentValues();
                contentValues.put("studentid", student_id);
                contentValues.put("examid", exam_id);
                contentValues.put("questionid", quetion_id);
                contentValues.put("selected_and", selected_answer);
                contentValues.put("realanswer", real_ans);
                contentValues.put("examdate", exDate);
                contentValues.put("classname", exam_class);
                contentValues.put("checkedid", String.valueOf(radioid));
                contentValues.put("exid", exid);

                long id = mdatabase.insert("assesmenttbl", null , contentValues);
                if (id > 0) {

                }
                mdatabase.close();

                radiogrp.clearCheck();

                i++;

                Log.e("NextQ Student_id",student_id+"///"+exam_id+"//"+rowListItem.get(i).getQuetion_id()+"//"+exid);
                quetion_id = rowListItem.get(i).getQuetion_id();
                helper.openDatabase();
                helper.close();
                mdatabase = helper.getWritableDatabase();
                String sql ="Select studentid,examid,questionid,checkedid,selected_and,realanswer from assesmenttbl where exid ='"+  exid +"' and studentid='"+ student_id +"' and examid='"+ exam_id +"' and questionid='"+ rowListItem.get(i).getQuetion_id() +"'";
                Cursor cursor = mdatabase.rawQuery(sql, null);
                if (cursor.getCount() > 0){
                    while (cursor.moveToNext()){

                        int selected_id = cursor.getColumnIndex("checkedid");
                        selected_answer = cursor.getColumnName(4);

                        if (selected_answer != null && !selected_answer.isEmpty() && !selected_answer.equals("null")){
                            if(selected_id==1){
                                radio1.setChecked(true);
                            }else if(selected_id==2){
                                radio2.setChecked(true);
                            }else if(selected_id==3){
                                radio3.setChecked(true);
                            }else if(selected_id==4){
                                radio4.setChecked(true);
                            }else{
                                // edit_fill_answer.setVisibility(View.VISIBLE);
                                //  radiogrp.setVisibility(View.GONE);
                                edit_fill_answer.setText(selected_answer);
                            }

                        }else{
                            edit_fill_answer.setText(null);
                            radiogrp.clearCheck();
                        }
                    }
                } else {
                    edit_fill_answer.setText(null);
                    radiogrp.clearCheck();
                }

                cursor.close();
                mdatabase.close();



                btn_Previous.setVisibility(View.VISIBLE);


                if(i<rowListItem.size()){
                    int srno = i+1;
                    //Log.e("size", String.valueOf(rowListItem.size()) +" i = "+i +"srno:"+srno);

                    if(rowListItem.size()==i+1){
                        btn_Next.setVisibility(View.GONE);
                        btn_Previous.setVisibility(View.VISIBLE);
                    }
                    // txt_quetion.setText(srno+".  "+rowListItem.get(i).getQuestion());
                    txt_quetion.setText(HtmlCompat.fromHtml(srno+".  "+rowListItem.get(i).getQuestion(), 0));
                    //txt_quetion1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getQpart1(), 0));

                    if (rowListItem.get(i).getQpart1() != null && !rowListItem.get(i).getQpart1().isEmpty() && !rowListItem.get(i).getQpart1().equals("null")){
                        txt_quetion1.setVisibility(View.VISIBLE);
                        String htmlText1 = rowListItem.get(i).getQpart1();
                        txt_quetion1.setText(HtmlCompat.fromHtml(htmlText1, 0));

                    } else {
                        txt_quetion1.setVisibility(View.GONE);
                    }

                    if (rowListItem.get(i).getQpart2() != null && !rowListItem.get(i).getQpart2().isEmpty() && !rowListItem.get(i).getQpart2().equals("null")){
                        txt_quetion2.setVisibility(View.VISIBLE);
                        String htmlText2 = rowListItem.get(i).getQpart2();
                        txt_quetion2.setText(HtmlCompat.fromHtml(htmlText2, 0));

                    } else {
                        txt_quetion2.setVisibility(View.GONE);
                    }
                    if (rowListItem.get(i).getQpart3() != null && !rowListItem.get(i).getQpart3().isEmpty() && !rowListItem.get(i).getQpart3().equals("null")){
                        txt_quetion3.setVisibility(View.VISIBLE);
                        String htmlText3 = rowListItem.get(i).getQpart3();
                        txt_quetion3.setText(HtmlCompat.fromHtml(htmlText3, 0));

                    } else {
                        txt_quetion3.setVisibility(View.GONE);
                    }
                    if (rowListItem.get(i).getQpart4() != null && !rowListItem.get(i).getQpart4().isEmpty() && !rowListItem.get(i).getQpart4().equals("null")){
                        txt_quetion4.setVisibility(View.VISIBLE);
                        String htmlText4 = rowListItem.get(i).getQpart4();
                        txt_quetion4.setText(HtmlCompat.fromHtml(htmlText4, 0));

                    } else {
                        txt_quetion4.setVisibility(View.GONE);
                    }

                    if(rowListItem.get(i).getOp1().equals("-")  && rowListItem.get(i).getOp1().equals("-")){
                        edit_fill_answer.setVisibility(View.VISIBLE);
                        radiogrp.setVisibility(View.GONE);
                    }else{
                        edit_fill_answer.setVisibility(View.GONE);
                        radiogrp.setVisibility(View.VISIBLE);
                          /*  radio1.setText(rowListItem.get(i).getOp1());
                            radio2.setText(rowListItem.get(i).getOp2());
                            radio3.setText(rowListItem.get(i).getOp3());
                            radio4.setText(rowListItem.get(i).getOp4());*/
                        radio1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp1(), 0));
                        radio2.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp2(), 0));
                        radio3.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp3(), 0));
                        radio4.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp4(), 0));


                        if (rowListItem.get(i).getQuestion_image() != null && !rowListItem.get(i).getQuestion_image().isEmpty() && !rowListItem.get(i).getQuestion_image().equals("null")){

                            image_quetion1.setVisibility(View.VISIBLE);

                            byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_quetion1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView


                        }else{
                            image_quetion1.setVisibility(View.GONE);
                        }
                        if (rowListItem.get(i).getQuestion_image1() != null && !rowListItem.get(i).getQuestion_image1().isEmpty() && !rowListItem.get(i).getQuestion_image1().equals("null")){

                            image_quetion2.setVisibility(View.VISIBLE);

                            byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image1(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_quetion2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                            //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                            //Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_quetion2.setVisibility(View.GONE);
                        }

                        if (rowListItem.get(i).getQuestion_image2() != null && !rowListItem.get(i).getQuestion_image2().isEmpty() && !rowListItem.get(i).getQuestion_image2().equals("null")){

                            image_quetion3.setVisibility(View.VISIBLE);

                            byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image2(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_quetion3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                            //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                            //Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_quetion3.setVisibility(View.GONE);
                        }


                        if (rowListItem.get(i).getOp1_image() != null && !rowListItem.get(i).getOp1_image().isEmpty() && !rowListItem.get(i).getOp1_image().equals("null")){

                            image_op1.setVisibility(View.VISIBLE);

                            byte[] decodedString = Base64.decode(rowListItem.get(i).getOp1_image(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_op1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                            //Log.e("quetion img", String.valueOf(img_path));

                        }else{
                            image_op1.setVisibility(View.GONE);
                        }

                        if (rowListItem.get(i).getOp2_image() != null && !rowListItem.get(i).getOp2_image().isEmpty() && !rowListItem.get(i).getOp2_image().equals("null")){

                            image_op2.setVisibility(View.VISIBLE);

                            byte[] decodedString = Base64.decode(rowListItem.get(i).getOp2_image(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_op2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView



                        }else{
                            image_op2.setVisibility(View.GONE);
                        }


                        if (rowListItem.get(i).getOp3_image() != null && !rowListItem.get(i).getOp3_image().isEmpty() && !rowListItem.get(i).getOp3_image().equals("null")){

                            image_op3.setVisibility(View.VISIBLE);
                            byte[] decodedString = Base64.decode(rowListItem.get(i).getOp3_image(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_op3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                        }else{
                            image_op3.setVisibility(View.GONE);
                        }


                        if (rowListItem.get(i).getOp4_image() != null && !rowListItem.get(i).getOp4_image().isEmpty() && !rowListItem.get(i).getOp4_image().equals("null")){

                            image_op4.setVisibility(View.VISIBLE);
                            byte[] decodedString = Base64.decode(rowListItem.get(i).getOp4_image(), Base64.DEFAULT);
                            Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            image_op4.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                        }else{
                            image_op4.setVisibility(View.GONE);
                        }

                    }


                }


                //i++;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(assessment.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Submit Exam")
                        .setMessage("Do you really want to Submit the Exam ?")
                        .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isCanceled = true;
                                UpdateAssesment();
                            }
                        })
                        .setNegativeButton(getString(R.string.no_dialog), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(exam_title);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(assessment.this);
        progressDialog.setTitle("Assessment");
        progressDialog.setMessage("Please wait while showing Data ...  ");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ProgressBar progressbar=(ProgressBar)progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF7043"), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void get_Questions(){

        StringRequest stringRequest = new StringRequest( Request.Method.POST, MyConfig.URL_GET_QUETIONS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("Result").equals("Success")) {
                            JSONArray json = jsonObject.getJSONArray("qlist");

                            //j = new JSONObject(response);
                            //result = j.getJSONArray(JSON_ARRAY);
                            result = jsonObject.getJSONArray("qlist");
                            getCategory(result);
                            } else {
                                progressDialog.dismiss();
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
                String student_class= sharedPreferences.getString("student_class",null);
                String student_id= sharedPreferences.getString("idtag",null);
                String exam_id= sharedPreferences.getString("exam_id",null);

                // Log.e("exam_id",exam_id);

                params.put("exam_id",exam_id);


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(assessment.this);
        queue.add(stringRequest);

    }


    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Quetion_Model_List(json.getString("quetion_id"),
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
                        json.getString("questionno")

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0) {
            progressDialog.dismiss();
            int srno = i + 1;
            //txt_quetion.setText(srno+".  "+rowListItem.get(i).getQuestion());
            //txt_quetion.setText(Html.fromHtml(rowListItem.get(i).getQuestion(), new URLImageParser(txt_quetion, this), null));
            String htmlText = rowListItem.get(i).getQuestion();
            txt_quetion.setText(HtmlCompat.fromHtml(htmlText, 0));

            if (rowListItem.get(i).getQpart1() != null && !rowListItem.get(i).getQpart1().isEmpty() && !rowListItem.get(i).getQpart1().equals("null")){
                txt_quetion1.setVisibility(View.VISIBLE);
                String htmlText1 = rowListItem.get(i).getQpart1();
                txt_quetion1.setText(HtmlCompat.fromHtml(htmlText1, 0));

            } else {
                txt_quetion1.setVisibility(View.GONE);
            }

            if (rowListItem.get(i).getQpart2() != null && !rowListItem.get(i).getQpart2().isEmpty() && !rowListItem.get(i).getQpart2().equals("null")){
                txt_quetion2.setVisibility(View.VISIBLE);
                String htmlText2 = rowListItem.get(i).getQpart2();
                txt_quetion2.setText(HtmlCompat.fromHtml(htmlText2, 0));

            } else {
                txt_quetion2.setVisibility(View.GONE);
            }
            if (rowListItem.get(i).getQpart3() != null && !rowListItem.get(i).getQpart3().isEmpty() && !rowListItem.get(i).getQpart3().equals("null")){
                txt_quetion3.setVisibility(View.VISIBLE);
                String htmlText3 = rowListItem.get(i).getQpart3();
                txt_quetion3.setText(HtmlCompat.fromHtml(htmlText3, 0));

            } else {
                txt_quetion3.setVisibility(View.GONE);
            }

            if (rowListItem.get(i).getQpart4() != null && !rowListItem.get(i).getQpart4().isEmpty() && !rowListItem.get(i).getQpart4().equals("null")){
                txt_quetion4.setVisibility(View.VISIBLE);
                String htmlText4 = rowListItem.get(i).getQpart4();
                txt_quetion4.setText(HtmlCompat.fromHtml(htmlText4, 0));

            } else {
                txt_quetion4.setVisibility(View.GONE);
            }

            if(rowListItem.get(i).getOp1().equals("-")  && rowListItem.get(i).getOp1().equals("-")){
                edit_fill_answer.setVisibility(View.VISIBLE);
                radiogrp.setVisibility(View.GONE);
            }else{
                edit_fill_answer.setVisibility(View.GONE);
                radiogrp.setVisibility(View.VISIBLE);
                //  radio1.setText(rowListItem.get(i).getOp1());
                //   radio2.setText(rowListItem.get(i).getOp2());
                //  radio3.setText(rowListItem.get(i).getOp3());
                // radio4.setText(rowListItem.get(i).getOp4());

                radio1.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp1(), 0));
                radio2.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp2(), 0));
                radio3.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp3(), 0));
                radio4.setText(HtmlCompat.fromHtml(rowListItem.get(i).getOp4(), 0));

                if (rowListItem.get(i).getQuestion_image() != null && !rowListItem.get(i).getQuestion_image().isEmpty() && !rowListItem.get(i).getQuestion_image().equals("null")){

                    image_quetion1.setVisibility(View.VISIBLE);

                    byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_quetion1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                    //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                    //Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_quetion1.setVisibility(View.GONE);
                }
                if (rowListItem.get(i).getQuestion_image1() != null && !rowListItem.get(i).getQuestion_image1().isEmpty() && !rowListItem.get(i).getQuestion_image1().equals("null")){

                    image_quetion2.setVisibility(View.VISIBLE);

                    byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image1(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_quetion2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                    //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                    //Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_quetion2.setVisibility(View.GONE);
                }
                if (rowListItem.get(i).getQuestion_image2() != null && !rowListItem.get(i).getQuestion_image2().isEmpty() && !rowListItem.get(i).getQuestion_image2().equals("null")){

                    image_quetion3.setVisibility(View.VISIBLE);

                    byte[] decodedString = Base64.decode(rowListItem.get(i).getQuestion_image2(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_quetion3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                    //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                    //Log.e("quetion img", String.valueOf(img_path));

                }else{
                    image_quetion3.setVisibility(View.GONE);
                }

                if (rowListItem.get(i).getOp1_image() != null && !rowListItem.get(i).getOp1_image().isEmpty() && !rowListItem.get(i).getOp1_image().equals("null")){

                    image_op1.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(rowListItem.get(i).getOp1_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_op1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    image_op1.setVisibility(View.GONE);
                }

                if (rowListItem.get(i).getOp2_image() != null && !rowListItem.get(i).getOp2_image().isEmpty() && !rowListItem.get(i).getOp2_image().equals("null")){

                    image_op2.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(rowListItem.get(i).getOp2_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_op2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    image_op2.setVisibility(View.GONE);
                }


                if (rowListItem.get(i).getOp3_image() != null && !rowListItem.get(i).getOp3_image().isEmpty() && !rowListItem.get(i).getOp3_image().equals("null")){

                    image_op3.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(rowListItem.get(i).getOp3_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_op3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    image_op3.setVisibility(View.GONE);
                }


                if (rowListItem.get(i).getOp4_image() != null && !rowListItem.get(i).getOp4_image().isEmpty() && !rowListItem.get(i).getOp4_image().equals("null")){

                    image_op4.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(rowListItem.get(i).getOp4_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    image_op4.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    image_op4.setVisibility(View.GONE);
                }

            }
        }else{
            progressDialog.dismiss();
            empty_view.setVisibility(View.VISIBLE);

        }


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
        Intent i = new Intent(assessment.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    public void Submit_Assessment(){

      AddNewAssesment();
    }


    private void AddNewAssesment(){

        final String studentid = student_id;
        final String examid = exam_id;
        final String exmid = exid;
        Calendar c = Calendar.getInstance();
        //selected_answer = rb.getText().toString();

        final String exDate = dateform.format(c.getTime());
        String noofq = noofqt;


        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("exid", exmid);
        editor.commit();


        showSimpleProgressDialog(this, "Registring...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("studentid", studentid);
                map.put("examid", examid);
                map.put("exid", exmid);
                map.put("exdate", exDate);

                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_ADD_ASSESSMENT);
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

                    } else {
                        removeSimpleProgressDialog();
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


    private void UpdateAssesment(){

        final String studentid = student_id;
        final String examid = exam_id;
        final String exmid = exid;
        Calendar c = Calendar.getInstance();
        //selected_answer = rb.getText().toString();
        final String exDate = dateform.format(c.getTime());
        final String noofq = noofqt;
        int rightanscount =0;
        int wronganscount =0;
        String rlans, sland;


        Boolean res;
        helper.openDatabase();
        helper.close();
        mdatabase = helper.getWritableDatabase();
        String sql ="Select studentid,examid,exid,selected_and,realanswer from assesmenttbl where exid ='"+  exid +"' and studentid='"+ student_id +"' and examid='"+ exam_id +"'";
        Cursor cursor = mdatabase.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                rlans = cursor.getString(4);
                sland = cursor.getString(3);
                Log.i("Real Ans", rlans);
                Log.i("Selected Ans", sland);

                res = rlans.equals(sland);
                Log.i("Result", res.toString());
                if (res == true){
                    rightanscount += 1;
                } else if (res == false) {
                    wronganscount +=1;
                }
            }
            rans = String.valueOf(rightanscount);
            wans = String.valueOf( wronganscount);
        }
        cursor.close();
        mdatabase.close();


        showSimpleProgressDialog(this, "Registring...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("studentid", studentid);
                map.put("examid", examid);
                map.put("exid", exmid);
                map.put("exdate", exDate);
                map.put("noofqt", noofq);
                map.put("rightans", rans);
                map.put("wrongans", wans);

                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_UPDATE_ASSESSMENT);
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
                    onUpdateAssessmentCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void onUpdateAssessmentCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("Result").equals("success")) {
                        removeSimpleProgressDialog();
                        isCanceled = true;
                        Intent i = new Intent(assessment.this, Assessment_Record.class);
                        startActivity(i);
                        finish();
                    } else {
                        removeSimpleProgressDialog();
                        Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }
}
