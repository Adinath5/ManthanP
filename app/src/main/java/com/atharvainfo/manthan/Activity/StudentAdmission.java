package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.manthan.Class.DatabaseHelper;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Model.UserData;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import cz.msebera.android.httpclient.NameValuePair;

public class StudentAdmission extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    String districtlist,schoolnamelst,last_name,gender,department,student_class,email,role,avator,dept_name,class_name, user_name,admissionfee;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    TextView txt_username,txt_class_dept,txtadfee,txtdateofb;
    ImageView Header_img;

    SliderLayout sliderLayout ;
    HashMap<String, String> HashMapForLocalRes ;

    LinearLayout lyt_examination,lyt_notice,lyt_assessment,lyt_subjects,lyt_download,lyt_alerts;
    Button btn_demo_enquiry,btncanclepur,btnsavepurchase;

    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;
    private ViewPager viewPager;

    SQLiteDatabase mdatabase;
    private DatabaseHelper helper;
    String currentVersion;
    boolean isForceUpdate = true;
    Timer timer;
    TextInputEditText txtfirstname,txtmiddlename,txtlastname,txtmobileno,txtemailid,txtcity,txttaluka,txtpincode;
    Spinner spstandard,spmedium,spdistrict,spschoolname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_admission);

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        String usernamelog= sharedPreferences.getString("user_name",null);
        String usertype= sharedPreferences.getString("user_type",null);

        txtfirstname = findViewById(R.id.txtfirstname);
        txtmiddlename = findViewById(R.id.txtmiddlename);
        txtlastname = findViewById(R.id.txtlastname);
        txtmobileno = findViewById(R.id.txtmobileno);
        txtemailid = findViewById(R.id.txtemailid);
        txtcity = findViewById(R.id.txtcity);
        txttaluka = findViewById(R.id.txttaluka);
        txtadfee = findViewById(R.id.txtadfee);
        txtdateofb = findViewById(R.id.txtdateofb);
        txtpincode = findViewById(R.id.txtpincode);

        spstandard = findViewById(R.id.spstandard);
        spmedium = findViewById(R.id.spmedium);
        spdistrict = findViewById(R.id.spdistrict);
        spschoolname = findViewById(R.id.spschoolname);
        btncanclepur = findViewById(R.id.btncanclepur);
        btnsavepurchase = findViewById(R.id.btnsavepurchase);


        initToolbar();

        spmedium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!spstandard.getSelectedItem().equals("") && !spmedium.getSelectedItem().equals("")){
                    getAdmissionFee();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getDistrictList();

        spdistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spdistrict.getSelectedItem().equals("")){
                    getSchoolData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Admission Form");
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        Tools.setSystemBarLight(this);
    }

    private void getAdmissionFee(){

        final String stdstandard = spstandard.getSelectedItem().toString();
        final String stdmedium = spmedium.getSelectedItem().toString();

        showSimpleProgressDialog(this, "Manthan Publication...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("standard", stdstandard.toString());
                map.put("medium",stdmedium.toString());

                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_GET_ADMISSION_FEE);
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

                JSONArray json = jsonObject.getJSONArray("Data");
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    admissionfee = obj.getString("m_t_price");
                    txtadfee.setText(admissionfee);

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

    private void getDistrictList(){
        final String stdstandard = spstandard.getSelectedItem().toString();

        showSimpleProgressDialog(this, "Manthan Publication...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("standard", stdstandard.toString());
                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_GET_DISTLIST);
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
                    onTaskDistCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void onTaskDistCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());
        List<String> dlist = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Result").equals("success")) {
                removeSimpleProgressDialog();

                JSONArray json = jsonObject.getJSONArray("Data");
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    districtlist = obj.getString("districtname");
                    dlist.add(districtlist);

                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dlist);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spdistrict.setAdapter(dataAdapter);

            } else {
                removeSimpleProgressDialog();
                Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getSchoolData(){
        final String stddistrict = spdistrict.getSelectedItem().toString();

        showSimpleProgressDialog(this, "Manthan Publication...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("districtname", stddistrict.toString());
                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_GET_DISTSCHOOLLIST);
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
                    onDistTaskDistCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void onDistTaskDistCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());
        List<String> sclist = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Result").equals("success")) {
                removeSimpleProgressDialog();

                JSONArray json = jsonObject.getJSONArray("Data");
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    schoolnamelst = obj.getString("schoolname");
                    sclist.add(schoolnamelst);

                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sclist);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spschoolname.setAdapter(dataAdapter);

            } else {
                removeSimpleProgressDialog();
                Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
