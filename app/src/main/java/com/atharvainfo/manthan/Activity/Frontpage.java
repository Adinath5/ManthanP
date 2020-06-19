package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.manthan.Adapter.ViewPagerAdapter;
import com.atharvainfo.manthan.Class.DatabaseHelper;
import com.atharvainfo.manthan.Class.GooglePlayStoreAppVersionNameLoader;
import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Class.WSCallerVersionListener;
import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.Model.UserData;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.Tools;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.NameValuePair;


public class Frontpage extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, WSCallerVersionListener {

    List<String> images=new ArrayList<>();

    private ActionBar actionBar;
    private Toolbar toolbar;
    String student_id,first_name,last_name,gender,department,student_class,email,role,avator,dept_name,class_name, user_name;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    TextView txt_username,txt_class_dept;
    ImageView Header_img;

    SliderLayout sliderLayout ;
    HashMap<String, String> HashMapForLocalRes ;

    LinearLayout lyt_examination,lyt_notice,lyt_assessment,lyt_subjects,lyt_download,lyt_alerts;
    Button btn_demo_enquiry;

    private static ProgressDialog mProgressDialog;
    private final int jsoncode = 1;
    private ViewPager viewPager;

    SQLiteDatabase mdatabase;
    private DatabaseHelper helper;
    String currentVersion;
    boolean isForceUpdate = true;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);

        deleteCache(Frontpage.this);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI  || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE ) {
                // connected to wif
                try {
                    currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

            }
        } else {

            Intent i = new Intent(Frontpage.this, NoItemInternetImage.class);
            startActivity(i);
        }

        helper = new DatabaseHelper(this);

        initToolbar();

        viewPager = (ViewPager) findViewById(R.id.slider);
        //sliderLayout = (SliderLayout) findViewById(R.id.slider);

        AddImageUrlFormLocalRes();


        initNavigationMenu();

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        // toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Manthan Publication");
        Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
        Tools.setSystemBarLight(this);
    }

    private void initNavigationMenu() {

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        View v = nav_view.getHeaderView(0);
        Header_img = (ImageView) v.findViewById(R.id.image);
        txt_username = (TextView) v.findViewById(R.id.username);
        txt_class_dept = (TextView) v.findViewById(R.id.class_dept);
        //txt_username.setText(first_name+" "+last_name);

        //txt_class_dept.setText(student_id+"  "+class_name +"("+dept_name+")");
        String img_path = MyConfig.Parent_Url+"assets/uploads/avatar/"+avator;
        InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.

       /* try {
            in = (InputStream) new URL(img_path).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
        Header_img.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
        try {
            if(in != null)
                in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("profile", String.valueOf(img_path));
*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                actionBar.setTitle(item.getTitle());
                drawer.closeDrawers();

                String titile = String.valueOf(item.getTitle());

                if(titile.equalsIgnoreCase("Home")){

                    Intent i = new Intent(Frontpage.this, Frontpage.class);
                    startActivity(i);

                }else if(titile.equalsIgnoreCase("About")){

                    Intent i = new Intent(Frontpage.this, EditProfile.class);
                    startActivity(i);

                }
                else if(titile.equalsIgnoreCase("Rate This App")){

                    //  Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Uri uri = Uri.parse("market://details?id=com.atharvainfo.manthan");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.atharvainfo.manthan")));//Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));

                    }
                }
                else if(titile.equalsIgnoreCase("Refer Friend")){

                    String shareBody = "Use this Mobile App for free Online tests & Series Tests,Free Downloads, Exam Details & Alerts.Click On:" + " \n " + getString( R.string.url_app_google_play )+"& referrer= "+ student_id;
                    Intent sharingIntent = new Intent( android.content.Intent.ACTION_SEND );
                    sharingIntent.setType( "text/plain" );
                    sharingIntent.putExtra( android.content.Intent.EXTRA_TEXT, shareBody );
                    sharingIntent.putExtra( Intent.EXTRA_SUBJECT, getString( R.string.app_name ) );
                    startActivity( Intent.createChooser( sharingIntent, getResources().getString( R.string.app_name ) ) );
                }
                else if(titile.equalsIgnoreCase("Help")){

                    Intent i = new Intent(Frontpage.this, Help.class);
                    startActivity(i);
                }
                else if (titile.equalsIgnoreCase("Login")){
                    Intent login = new Intent(Frontpage.this, Login.class);
                    startActivity(login);
                }
                else if(titile.equalsIgnoreCase("Logout")){

                    sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
                    editor = sharedPreferences.edit();
                    editor.remove( "user_name" );
                    editor.remove( "password" );
                    editor.commit();
                    Intent i = new Intent(Frontpage.this, Login.class);
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        Exit();
    }

    public void Exit() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.backbutton))
                .setPositiveButton(getString(R.string.yes_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

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

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void AddImageUrlFormLocalRes(){

        showSimpleProgressDialog(this, "Getting Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_GET_DISOCUNT_BANER);
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
                    onSliderTaskCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();


    }

    public void onSliderTaskCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());
        HashMapForLocalRes = new HashMap<String, String>();
        int imageid =0;
        switch (serviceCode) {
            case jsoncode:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("Result").equals("success")) {
                        removeSimpleProgressDialog();

                        JSONArray json = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject obj = json.getJSONObject(i);
                            //HashMapForLocalRes.put(obj.getString("img_id"), Config.APP_IMAGES_URL + obj.getString("img_path"));
                            images.add(obj.getString("img_path"));
                            imageid ++;
                        }


                        ViewPagerAdapter adapter = new ViewPagerAdapter(images,Frontpage.this);
                        viewPager.setAdapter(adapter);

                        final int finalImageid = imageid;
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                viewPager.post(new Runnable(){

                                    @Override
                                    public void run() {
                                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)% finalImageid);
                                    }
                                });
                            }
                        };
                        timer = new Timer();
                        timer.schedule(timerTask, 3000, 3000);

                    } else {
                        removeSimpleProgressDialog();
                        Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.my_dialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.buttonOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Use this Mobile App for free Online tests & Series Tests,Free Downloads, Exam Details & Alerts.Click On:" + " \n " + getString( R.string.url_app_google_play )+"& referrer= "+ student_id;
                Intent sharingIntent = new Intent( android.content.Intent.ACTION_SEND );
                sharingIntent.setType( "text/plain" );
                sharingIntent.putExtra( android.content.Intent.EXTRA_TEXT, shareBody );
                sharingIntent.putExtra( Intent.EXTRA_SUBJECT, getString( R.string.app_name ) );
                startActivity( Intent.createChooser( sharingIntent, getResources().getString( R.string.app_name ) ) );
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.buttonSkip)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

    private void fetchJSON(){

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        user_name= sharedPreferences.getString("user_name",null);

        Log.i("UserName", user_name);

        showSimpleProgressDialog(this, "Getting User Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("username", user_name);


                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_PROFILE_DATA);
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
                    UserData userData = new UserData(obj.getString("userphone"),
                            obj.getString("useremail"),
                            obj.getString("userpass"),
                            obj.getString("firstname"),
                            obj.getString("lastname"),
                            obj.getString("gender"),
                            obj.getString("classname"),
                            obj.getString("schoolname"),
                            obj.getString("village"),
                            obj.getString("studentid"),
                            obj.getString("student_image"));

                    student_id = obj.getString("studentid");
                    first_name= obj.getString("firstname");
                    last_name = obj.getString("lastname");
                    gender = obj.getString("gender");
                    student_class = obj.getString("classname");
                    email = obj.getString("useremail");
                    class_name = obj.getString("schoolname");

                    txt_username.setText(first_name + " "+ last_name);
                    txt_class_dept.setText(student_class+ " "+ class_name);
                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("idtag", student_id);
                    editor.putString("firstname", first_name);
                    editor.putString("lastname", last_name);
                    editor.putString("gender", gender);
                    editor.putString("studentclass", student_class);
                    editor.putString("emailtag", email);
                    editor.putString("studentschool", class_name);
                    editor.commit();

                    Log.e("fetch data", student_id + "=" + first_name + "=" + student_class);

                    if (userData.getStudent_image() != null && !userData.getStudent_image().isEmpty() && !userData.getStudent_image().equals("null")){

                        byte[] decodedString = Base64.decode(userData.getStudent_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //Bitmap bm = StringToBitMap(imgBitMap);

                        //image = BitmapFactory.decodeStream(studentimage);
                        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), imgBitMap);
                        mDrawable.setCircular(true);
                        Header_img.setImageDrawable(mDrawable);

                        //imageView1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                    }


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


    public void showUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Frontpage.this);

        alertDialogBuilder.setTitle(Frontpage.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(Frontpage.this.getString(R.string.youAreNotUpdatedMessage));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Frontpage.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    finish();
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }
}
