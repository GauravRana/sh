package com.sydehealth.sydehealth.mail_upload;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.main.EmailActivity;
import com.sydehealth.sydehealth.main.Tab_activity;
import com.sydehealth.sydehealth.model.TreatmentImages;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.EmailToast;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.utility.SaveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class Mail_service extends Service {
    SaveData save_data;
    UsefullData objUsefullData;
    //ArrayList<Actors> screenshot_list;
    ArrayList<TreatmentImages> screenshot_list;
    int mail_sender_id;

    String condition_name ;
    String sub_heading_name ;
    String treatment_option_name ;
    private String treatment_name,sub_treatment_name;

    StringBuilder sb = new StringBuilder();
    StringBuilder bs = new StringBuilder();
    public void upload( String condition_id,  String treatment_option_ids,  String email,  String sub_heading_id  ){



        List<Part> files = new ArrayList<>();
        for (int i = 0; i < screenshot_list.size(); i++) {

            files.add(new FilePart("media[screenshot_" + i + "]", new File(screenshot_list.get(i).getPath())));

        }

        Ion.with(InitializeActivity.context)
                .load("POST", Definitions.APIdomain+ Definitions.SEND_EMAIL)
                .setTimeout(60 * 60 * 60 * 1000)
                .uploadProgress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {

                        int percent = (int) (downloaded * 100 / total);

                    }
                })

                .addHeader("Accept", Definitions.VERSION)
                .addHeader("X-User-Email", save_data.get(Definitions.USER_EMAIL) )
                .addHeader("X-User-Token", save_data.get(Definitions.USER_TOKEN) )
                .addMultipartParts(files)
                .setMultipartParameter("condition_id", condition_id)
//                .setMultipartParameter("condition_video_id", condition_video_id)
//                .setMultipartParameter("condition_image_id", condition_image_id)
                .setMultipartParameter("treatment_option_ids", treatment_option_ids)
//                .setMultipartParameter("advert_id", advert_id)
                .setMultipartParameter("email", email)
//                .setMultipartParameter("comment", comment)
//                .setMultipartParameter("prevention_advice", prevention_advice)
                .setMultipartParameter("condition_presentation_id", sub_heading_id )
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            // error: log the message here
//                            save_data.save(Definitions.POST_COUNT,0);

                            return;
                        }
                        if (result != null) {

                            for(int i=0;i<screenshot_list.size();i++){
                                File file = new File(screenshot_list.get(i).getPath());
                                objUsefullData.deleteFile(file);
                            }


                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                   // Toast.makeText(Mail_service.this, "", Toast.LENGTH_SHORT).show();
                                    /*if(save.getBoolean(Definitions.SHOW_FIREBASE_TOP)){
                                        Firebase_toast.show(activity, title,desc, true, false);
                                    }else {
                                        Firebase_toast.show(activity, title,desc, true, true);
                                    }*/

                                    if(save_data.isExist(Definitions.ID)) {
                                        if (mail_sender_id == save_data.getInt(Definitions.ID)) {

                                            objUsefullData.Mixpanel_events_email("Email Sent","Condition Name", condition_name
                                            ,"Sub Condition Name", sub_heading_name
                                            ,"Treatment Name", bs.toString()
                                            ,"Sub - Treatment Name", sb.toString());
//                                            objUsefullData.Mixpanel_events("Email Sent", "Condition Name", condition_name);
//                                            objUsefullData.Mixpanel_events("Email Sent", "Sub Condition Name", sub_heading_name);
//                                            objUsefullData.Mixpanel_events("Email Sent", "Treatment Name", treatment_name);
//                                            objUsefullData.Mixpanel_events("Email Sent", "Sub - Treatment Name", sb.toString());
                                            EmailToast.show(getApplicationContext(), "Email Sent", true);
                                        }
                                    }
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

                            // result is the response of your server


//                            JSONObject jsonObj = null;
//                            try{
//                                jsonObj = new JSONObject(result);
//
//                                objUsefullData.make_toast(jsonObj.getString("result"));
//
//                            }
//
//                            catch(JSONException e1){
//                                e1.printStackTrace();
//                            }

                        }
                    }
                });
    }


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
        save_data = new SaveData(context);

        mail_sender_id=save_data.getInt(Definitions.ID);
        screenshot_list = new ArrayList<TreatmentImages>();
        screenshot_list.clear();

        if(intent!=null) {
            String condition_id = intent.getStringExtra("condition_id");
//            String condition_video_id = intent.getStringExtra("condition_video_id");
//            String condition_image_id = intent.getStringExtra("condition_image_id");
            String treatment_option_ids = intent.getStringExtra("treatment_option_ids");
//            String advert_id = intent.getStringExtra("advert_id");
//            String comment = intent.getStringExtra("comment");
            String email = intent.getStringExtra("email");
//            String prevention_advice = intent.getStringExtra("prevention_advice");
            String sub_heading_id  = intent.getStringExtra("sub_heading_id");

            condition_name = intent.getStringExtra("condition_name");
            sub_heading_name = intent.getStringExtra("sub_heading_name");
            treatment_option_name = intent.getStringExtra("treatment_option_name");

            try {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(treatment_option_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    treatment_name = jsonArray.getJSONObject(0).getString("treatment_Name");

//                    JSONArray treatment_name_list = jsonArray.getJSONObject(1).getJSONArray("treatment_Name");
//                    List<String> treatmentNamelist = new ArrayList<String>();
//                    for(int i = 0; i < treatment_name_list.length(); i++){
//                        treatmentNamelist.add(treatment_name_list.getString(i));
//                    }

                    List<String> treatmentNamelist = new ArrayList<String>();
                    List<String> list = new ArrayList<String>();
                    for (int i = 0 ;i<jsonArray.length();i++){
//                         ksa = jsonArray.getJSONObject(i).getJSONArray("treatment_Name");
                        treatmentNamelist.add(jsonArray.getJSONObject(i).getString("treatment_Name"));
                    }


//                    for(int f = 0; f < ksa.length(); f++){
//                        treatmentNamelist.add(ksa.getString(f));
//                    }

                    for (String s : treatmentNamelist)
                    {
                        bs.append(s);
                        if(!s.equals(treatmentNamelist.get(treatmentNamelist.size()-1))){
                            bs.append(",");
                        }
                    }

                    for(int i = 0; i < treatmentNamelist.size(); i++){
                        JSONArray hu = jsonArray.getJSONObject(i).getJSONArray("sub_treatment_id");
                        for(int k =0 ;k<hu.length();k++){

                            list.add(hu.getString(k));

                        }
//                        JSONArray hu1 = hu.getJSONObject(i).getJSONArray("");
                    }

//                    JSONArray hu = jsonArray.getJSONObject(0).getJSONArray("sub_treatment_id");




//                    for(int i = 0; i < hu.length(); i++){
//                        list.add(hu.getString(i));
//                    }

                    for (String s1 : list)
                    {
                        sb.append(s1);
                        if(!s1.equals(treatmentNamelist.get(treatmentNamelist.size()-1))){
                            sb.append(",");
                        }



                    }


//                    for(int i =0 ; i < hu.length(); i++){
//                        sub_treatment_name = ""+hu.get(i).toString();
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            // screenshot_list.addAll(Mail_popup.screenshot_list);
            screenshot_list.addAll(EmailActivity.alSelectedImages);
            upload(condition_id, treatment_option_ids,    email,  sub_heading_id );
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