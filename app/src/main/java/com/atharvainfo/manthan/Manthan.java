package com.atharvainfo.manthan;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.atharvainfo.manthan.utils.ConnectivityReceiver;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Manthan extends Application {
    public static Manthan mInstance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static synchronized Manthan getInstance(){
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private boolean backgroundRunning = false;
    private FirebaseAnalytics firebaseAnalytics;

    synchronized public boolean isBackgroundRunning() {
        return backgroundRunning;
    }

    synchronized public void setBackgroundRunning(boolean backgroundRunning) {
        this.backgroundRunning = backgroundRunning;
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }

    public void setFirebaseAnalytics(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    public Manthan(){
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();

        //Fabric.with(this, new Crashlytics());

        //Firebase.setAndroidContext(this);
        // enable disk persistence
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
        SystemClock.sleep(500);
        mInstance = this;

        //pre-load fonts for rtEditor
        //FontManager.preLoadFonts(this);

    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    synchronized public FirebaseAnalytics getTracker() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        return firebaseAnalytics;
    }


    public void sendEvent(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    public void setUserProperty(String id, String name) {
        firebaseAnalytics.setUserProperty(id, name);

    }

}
