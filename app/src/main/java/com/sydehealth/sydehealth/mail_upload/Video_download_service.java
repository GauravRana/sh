package com.sydehealth.sydehealth.mail_upload;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.volley.UserAPI;
import com.sydehealth.sydehealth.adapters.Actors;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_LOW;

public class Video_download_service extends Service {

    SaveData saveData;
    UsefullData objUsefullData;
    ArrayList<Actors> device_list = new ArrayList<Actors>();
    ArrayList<String> server_list = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }else {
            startForeground(1, new Notification());
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Context context = getApplicationContext();
        objUsefullData = new UsefullData(context);
        saveData = new SaveData(context);
        if(objUsefullData.isNetworkConnected()){
            get_all_video();
        }
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Context context = getApplicationContext();
//        objUsefullData = new UsefullData(context);
//        saveData = new SaveData(context);
//        get_all_video();
//        return START_STICKY;
//    }


    private void video_downloader(String url, String fileName) {

        // delete directory if present.....
        File dir = new File(Definitions.VIDEO_FOLDER);
//        if (dir.isDirectory())
//        {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dir, children[i]).delete();
//            }
//        }


        if (!dir.exists())
        {
            dir.mkdirs();
        }

        // if file already exists.....
        File myFile = new File(dir + "/"+ fileName+".mp4");
        if(!myFile.exists()){
            int download_id = PRDownloader.download(url, Definitions.VIDEO_FOLDER, fileName+".mp4")
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {

                        }
                    }).start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {


                        }

                        @Override
                        public void onError(Error error) {
                            Log.d("onError", "OnError");
                        }
                    });

            // to check the status of download...
            Status status = PRDownloader.getStatus(download_id);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void get_all_video() {

        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));


        UserAPI.get_JsonObjResp(Definitions.ALL_VIDEO_DOWNLOAD, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

    }

    private void set_up_values(JSONObject response) {

        server_list.clear();
        device_list.clear();

        String media_url = "";
        String name = "";
        int id = 0;

        ArrayList<VideoData> listVideos = new ArrayList<>();

        try {
            if (!response.isNull("condition_videos")) {
                JSONArray conditions = response.getJSONArray("condition_videos");

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject in = conditions.getJSONObject(i);

                    name = in.optString("name");
                    media_url = in.optString("media_url");
                    id= in.optInt("id");
                    VideoData data=new VideoData(media_url, name, id);
                    listVideos.add(data);
                    server_list.add(name + "_" + id+".mp4");
                }
            }

            if (!response.isNull("treatment_videos")) {
                JSONArray conditions = response.getJSONArray("treatment_videos");

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject in = conditions.getJSONObject(i);
                    name = in.optString("name");
                    media_url = in.optString("media_url");
                    id= in.optInt("id");
                    VideoData data=new VideoData(media_url, name, id);
                    listVideos.add(data);
                    server_list.add(name + "_" + id+".mp4");
                }
            }

            if (!response.isNull("screensaver_videos")) {
                JSONArray conditions = response.getJSONArray("screensaver_videos");

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject in = conditions.getJSONObject(i);
                    name = in.optString("name");
                    media_url = in.optString("media_url");
                    id= in.optInt("id");
                    VideoData data=new VideoData(media_url, name, id);
                    listVideos.add(data);
                    server_list.add(name + "_" + id+".mp4");
                }
            }

            if (!response.isNull("sub_treatment_videos")) {
                JSONArray conditions = response.getJSONArray("sub_treatment_videos");

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject in = conditions.getJSONObject(i);
                    name = in.optString("name");
                    media_url = in.optString("media_url");
                    id= in.optInt("id");
                    VideoData data=new VideoData(media_url, name, id);
                    listVideos.add(data);
                    server_list.add(name + "_" + id+".mp4");
                }
            }

            Iterator itr= listVideos.iterator();

            //traverse elements of ArrayList object
            while(itr.hasNext()){
                VideoData videoData=(VideoData)itr.next();
                video_downloader(videoData.media_url, videoData.name + "_" + videoData.id);
            }


            try{
                device_list=objUsefullData.get_video_name();
            }catch (Exception e){
                e.printStackTrace();
            }



            for (int i=0;i<server_list.size();i++){

                if(objUsefullData.indexExists(server_list,i)) {
                    if(objUsefullData.indexExists(device_list,i)) {
                        if (!server_list.contains(device_list.get(i).getusername())) {
                            File file = new File(device_list.get(i).getdata());
                            objUsefullData.deleteFile(file);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class VideoData{
        String media_url = "";
        String name = "";
        int id = 0;
        VideoData(String media_url, String name, int id){
            this.media_url = media_url;
            this.name = name;
            this.id = id;
        }
    }

    // when app is killed service does not stop.....
//    @Override
//    public void onTaskRemoved(Intent rootIntent){
//        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
//        restartServiceIntent.setPackage(getPackageName());
//        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmService.set(
//                AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + 1000,
//                restartServicePendingIntent);
//
//        super.onTaskRemoved(rootIntent);
//    }



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
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        this.startForeground(2, notification);
    }
}

