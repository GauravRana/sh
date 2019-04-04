package com.sydehealth.sydehealth.mail_upload;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;

import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.drawingview.MainActivity;
import com.sydehealth.sydehealth.main.Tab_activity;
import com.sydehealth.sydehealth.service.Screenshots_deletion_service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.os.PersistableBundle;

import static androidx.core.app.NotificationCompat.PRIORITY_LOW;

public class Screenshots_service extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();

        }else{
            startForeground(1,new Notification());
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {

        new final_save_image().execute("0");

        }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class final_save_image extends AsyncTask<String, Void, Boolean> {


        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                if(urls[0].equals("0")) {
                    MainActivity.mDrawingView.saveImage(MainActivity.sdcardPath, "Drawing_temp", Bitmap.CompressFormat.PNG, 70);
                    MainActivity.finalBitmap = BitmapFactory.decodeFile(MainActivity.sdcardPath + "/" + "Drawing_temp" + ".png");
                }

                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Jukepad");
                myDir.mkdirs();
                File file = new File (myDir, "/" + now + ".png");
                if (file.exists ())
                    file.delete ();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    MainActivity.finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                    out.flush();
                    out.close();

                    File[] fileList = myDir.listFiles();

                    JobScheduler jobScheduler = JobScheduler.getInstance(getApplicationContext());
                    PersistableBundle extras = new PersistableBundle();
                    extras.putString("file_path", file.getAbsolutePath());
                    JobInfo job = new JobInfo.Builder(fileList.length+1, new ComponentName(getApplicationContext(), Screenshots_deletion_service.class))
                            .setMinimumLatency(86400000)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                            .setRequiresCharging(false)
                            .setExtras(extras)
                            .build();
                    jobScheduler.schedule(job);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(Mail_service.this, "", Toast.LENGTH_SHORT).show();
                                    /*if(save.getBoolean(Definitions.SHOW_FIREBASE_TOP)){
                                        Firebase_toast.show(activity, title,desc, true, false);
                                    }else {
                                        Firebase_toast.show(activity, title,desc, true, true);
                                    }*/


                    int noOfScreensShots=0;
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/Jukepad");
                    if (Tab_activity.tv_count != null && Tab_activity.ll_email != null) {
                        if (myDir.exists()) {
                            File[] fileList = myDir.listFiles();
                            if(fileList!=null) {
                                noOfScreensShots = fileList.length;
                            }
                            if (noOfScreensShots > 0) {
                                Tab_activity.tv_count.setVisibility(View.VISIBLE);
                                Tab_activity.tv_count.setText(noOfScreensShots + "");
                            } else {
                                Tab_activity.tv_count.setText("0");
                                Tab_activity.tv_count.setVisibility(View.GONE);
                                //Tab_activity.ll_email.setBackground(null);
                            }
                        } else {
                            Tab_activity.tv_count.setText("0");
                            Tab_activity.tv_count.setVisibility(View.GONE);
                            //Tab_activity.ll_email.setBackground(null);
                        }
                    }
                }
            });

        }
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
                .setContentTitle("Screenshot service")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        this.startForeground(2, notification);
    }


}
