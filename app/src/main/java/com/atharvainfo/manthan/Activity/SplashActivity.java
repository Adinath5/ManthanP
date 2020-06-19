package com.atharvainfo.manthan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.manthan.MainActivity;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.ConnectivityReceiver;


public class SplashActivity extends AppCompatActivity {

    private Animation animation;
    private ImageView logo;
    private TextView appTitle;
    private TextView appSlogan;
    SharedPreferences sp;
    public static final String PREFS="PREFS";
    private static final int REQUEST= 112;
    String loginid = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        appTitle = findViewById(R.id.grocery);
        appSlogan = findViewById(R.id.slogan);


        String[] PERMISSIONS = {android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.NFC,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            Log.d("TAG","@@@ IN IF hasPermissions");
            ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
        } else {
            Log.d("TAG","@@@ IN ELSE hasPermissions");
            //callNextActivity();
        }


        if (ConnectivityReceiver.isConnected()) {
            if (savedInstanceState == null) {
                flyIn();
            }

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    endSplash();
                }
            }, 4000);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection ");
            builder.setMessage("Do you want to go to settings");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent j = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(j);
                    System.exit(0);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    System.exit(0);
                }
            });
            builder.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    /// callNextActivity();
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ⓘ Exit ! " + getString(R.string.app_name));
        alertDialogBuilder
                .setMessage(Html.fromHtml("<p style='text-align:center;'>Please Fill the required details</p><h3 style='text-align:center;'>Click Yes to Exit !</h4>"))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);

                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 5);
    }


    private void flyIn() {
        //animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        //logo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.app_name_animation);
        appTitle.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation);
        appSlogan.startAnimation(animation);
    }

    private void endSplash() {
        //animation = AnimationUtils.loadAnimation(this,
        //        R.anim.logo_animation_back);
        //logo.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.app_name_animation_back);
        appTitle.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.pro_animation_back);
        appSlogan.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {

                sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
                sharedPreferences.edit();
                String usernamelog= sharedPreferences.getString("user_name",null);
                String usertype= sharedPreferences.getString("user_type",null);

                loginid=sharedPreferences.getString("user_name",null);
                if(loginid!=null) {
                    if (usertype.equalsIgnoreCase("Student")){
                        Intent intent = new Intent(getApplicationContext(),
                                MainActivity.class);
                    startActivity(intent);
                    finish();
                 }
                } else {

                    Intent intent = new Intent(getApplicationContext(),
                            Frontpage.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

    }
}
