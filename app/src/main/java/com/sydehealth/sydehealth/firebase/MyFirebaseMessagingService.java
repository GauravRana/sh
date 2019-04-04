package com.sydehealth.sydehealth.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.drawingview.MainActivity;
import com.sydehealth.sydehealth.main.Tab_activity;
import com.sydehealth.sydehealth.screen_share.Screen_share_activity;
import com.sydehealth.sydehealth.service.MyJobService;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.Firebase_toast;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.volley.InitializeActivity;

import org.json.JSONObject;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    Context context;


//    int id,playpen_id,user_noti_id;
//    String site_url;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        storeTokenAvailablityInPref(false);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            Log.e(TAG, "Notification Title: " + remoteMessage.getNotification().getTitle());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleNotification(""+remoteMessage.getData().get("title"),""+remoteMessage.getData().get("body"));
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String title, String desc) {
        SaveData save=new SaveData(this);
            Context activity=this;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    InitializeActivity.mMixpanel.track("Screenshare Button Clicked");
                    Firebase_toast.show(activity, title,desc, true, false);
                    Intent in= new Intent(context,Screen_share_activity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                    sendNotification();


                }
            });
    }

    private void handleDataMessage(JSONObject json, String title, String body) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }




    private void sendNotification() {
        scheduleJob();
        Intent intent = new Intent(this, Screen_share_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//      Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Screen_share_activity.class);
//      Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Screenshare is ready")
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.FLAG_GROUP_SUMMARY | Notification.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = context.getString(R.string.default_notification_channel_id);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId(channelId);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        assert notificationManager != null;
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        Intent bag = new Intent("Msg");
        LocalBroadcastManager.getInstance(this).sendBroadcast(bag);
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SaveData saveData = new SaveData(getApplicationContext());
        if(!saveData.getBoolean(Definitions.IS_TOKEN_AVAILABLE)) {
            Log.d(TAG, "Refreshed token: " + s);
            String refreshedToken = s;
            // Saving reg id to shared preferences
            storeRegIdInPref(refreshedToken);
            // sending reg id to your server
            sendRegistrationToServer(refreshedToken);
            // Notify UI that registration has completed, so the progress indicator can be hidden.
            Intent registrationComplete = new Intent(Definitions.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            storeTokenAvailablityInPref(true);
        }
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SaveData saveData = new SaveData(getApplicationContext());
        saveData.save(Definitions.FIREBASE_TOKEN,token);
    }

    private void storeTokenAvailablityInPref(boolean isAvailable) {
        SaveData saveData = new SaveData(getApplicationContext());
        saveData.save(Definitions.IS_TOKEN_AVAILABLE,isAvailable);
    }


    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }
}
