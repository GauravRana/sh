package com.sydehealth.sydehealth.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.main.Tab_activity;
import com.sydehealth.sydehealth.screen_share.BasicCustomVideoRenderer;
import com.sydehealth.sydehealth.screen_share.OpenTokConfig;
import com.sydehealth.sydehealth.screen_share.Screen_share_activity;
import com.sydehealth.sydehealth.screen_share.WebServiceCoordinator;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_LOW;


/**
 * gaurav
 * The service automates the screenshare process .
 */

public class ScreenShareService extends JobService implements  WebServiceCoordinator.Listener,
        Session.SessionListener,
        SubscriberKit.SubscriberListener,
        View.OnClickListener {

    private Subscriber mSubscriber;
    private Session mSession;
    private SaveData saveData;


    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")
    private WebServiceCoordinator mWebServiceCoordinator;


    @Override
    public void onCreate() {
        super.onCreate();
        saveData = new SaveData(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();

        }else{
            startForeground(1,new Notification());
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //get_details();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        get_details();
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public ScreenShareService() {
        super();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnected(Session session) {
        Log.d("TAG", "onConnected: Connected to session: " + session.getSessionId());
//        Intent edit = new Intent(this, Screen_share_activity.class);
//        startActivity(edit);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.d("TAG", "onDisconnected: Disconnected from session: " + session.getSessionId());
        //usefullData.make_alert("Something went wrong", true, Screen_share_activity.this);
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
       // saveData.saveScreenShareActive("isActive", true);
        Log.d("TAG", "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        Intent edit = new Intent(this, Screen_share_activity.class);
        startActivity(edit);
//        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        //saveData.saveScreenShareActive("isActive", true);
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

    }

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

    }

    private void initializeSession(String apiKey, String sessionId, String token) {

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }


    private void set_up_values(JSONObject response) {
        try {
            OpenTokConfig.TOKEN = response.optString("token");
            OpenTokConfig.SESSION_ID = response.optString("session_id");
            requestPermissions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void get_details() {
            alert(getResources().getString(R.string.share_start));

        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));


        UserAPI.get_JsonObjResp(Definitions.SCREEN_SHARE_DETAILS, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        set_up_values(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        usefullData.make_toast("Something went wrong");
                    }
                });
    }


    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e("TAG", "Error " + alertTitle + ": " + errorMessage);

        alert("Something went wrong");
    }

    public void alert(final String msg) {
//        loader = MyCustomProgressDialog.makeProgress(Screen_share_activity.this, msg, "Please wait...");

//        show_dialog(msg,"Please wait...");
//        if (!((Activity) this).isFinishing()) {
//            dialog.show();
//        }

    }

    private void requestPermissions() {
        // if there is no server URL set
        if (OpenTokConfig.CHAT_SERVER_URL == null) {
            // use hard coded session values
            if (OpenTokConfig.areHardCodedConfigsValid()) {
                initializeSession(OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID, OpenTokConfig.TOKEN);
            } else {
                showConfigError("Configuration Error", OpenTokConfig.hardCodedConfigErrorMessage);
            }
        } else {
            // otherwise initialize WebServiceCoordinator and kick off request for session data
            // session initialization occurs once data is returned, in onSessionConnectionDataReady
            if (OpenTokConfig.isWebServerConfigUrlValid()) {
                mWebServiceCoordinator = new WebServiceCoordinator(this, this);
                mWebServiceCoordinator.fetchSessionConnectionData(OpenTokConfig.SESSION_INFO_ENDPOINT);
            } else {
                showConfigError("Configuration Error", OpenTokConfig.webServerConfigErrorMessage);
            }
        }

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

        private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.sydehealth.sydehealth";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID );
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Screenshare is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        this.startForeground(2, notification);
        }


    public Notification getNotification() {
        String NOTIFICATION_CHANNEL_ID = "com.sydehealth.sydehealth";
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setSmallIcon(android.R.drawable.ic_menu_mylocation).setContentTitle("snap map fake location");
        Notification notification = mBuilder
                .setPriority(PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        return notification;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }
}
