package com.sydehealth.sydehealth.utility;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sydehealth.sydehealth.database.DatabaseHelper;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.main.Login_activity;
import com.sydehealth.sydehealth.main.Tab_activity;
import com.sydehealth.sydehealth.main.User_account;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by POPLIFY on 4/23/2017.
 */

public class App_startup extends Activity
{
    SaveData save_data;
//    public static final int OVERLAY_PERMISSION_REQ_CODE = 7979;
    UsefullData usefullData;
    static DatabaseHelper db;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public boolean isAppStartUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        isAppStartUp = true;
        db = new DatabaseHelper(getApplicationContext());
        save_data = new SaveData(App_startup.this);
        usefullData=new UsefullData(App_startup.this);
//        check_launcher();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch_activity();
            }
        }, 500);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Definitions.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Definitions.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Definitions.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                }
            }
        };


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        check_launcher();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!Settings.canDrawOverlays(this)) {
//                    Toast.makeText(this, "Lock impossible because not permission!", Toast.LENGTH_SHORT).show();
//                }else
//                {
////                    usefullData.disableStatusBar();
//                    switch_activity();
//                }
//            }
//        }else{
////            usefullData.disableStatusBar();
//            switch_activity();
//        }
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }
//    @Override
//    public void onBackPressed() {
//    }

    public void switch_activity(){

        if (save_data.isExist(Definitions.USER_TOKEN)) {
            if(!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save_data.getInt(Definitions.ID))))  {

                InitializeActivity.mMixpanel.identify(String.valueOf(save_data.getInt(Definitions.ID)));
                InitializeActivity.mMixpanel.getPeople().identify(String.valueOf(save_data.getInt(Definitions.ID)));
                InitializeActivity.mMixpanel.getPeople().set(Definitions.MIXPANEL_NAME, save_data.getString(Definitions.USER_NAME));
                InitializeActivity.mMixpanel.getPeople().set(Definitions.MIXPANEL_ID, save_data.getInt(Definitions.ID));
                InitializeActivity.mMixpanel.getPeople().set(Definitions.MIXPANEL_EMAIL, save_data.getString(Definitions.USER_EMAIL));
                usefullData.schedule_time_events();

            }
            startActivity(new Intent(App_startup.this, Tab_activity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }else{
            db.openDB();
            if(!db.Execute_recent_user().isEmpty()) {
                startActivity(new Intent(App_startup.this, User_account.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                save_data.remove(Definitions.MAIL_IN_DRAFT);
                finish();
            }else {
//                startActivity(new Intent(App_startup.this, Login_activity.class));
                Intent forgot=new Intent(App_startup.this,Login_activity.class);
                forgot.putExtra("request","hide_back");
                startActivity(forgot);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                save_data.remove(Definitions.MAIL_IN_DRAFT);
                finish();
            }
            db.closeDB();

        }
    }

//    public void check_launcher(){
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.canDrawOverlays(this)) {
//                Toast.makeText(this, "Lock impossible because not permission!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
//            } else {
////                usefullData.disableStatusBar();
//                switch_activity();
//            }
//        }
//        else {
////            usefullData.disableStatusBar();
//            switch_activity();
//        }
//    }

    @Override
    protected void onResume () {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Definitions.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Definitions.PUSH_NOTIFICATION));

        // clear the notification area when the app is openedFirebaseMessaging
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    protected void onDestroy () {
        isAppStartUp = false;
        if(!save_data.getString(Definitions.FIREBASE_TOKEN).isEmpty()) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        }
        super.onDestroy();
    }
}
