package com.sydehealth.sydehealth.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sydehealth.sydehealth.utility.UsefullData;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Calendar c = Calendar.getInstance();
//        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
//        Toast.makeText(context, "Alarm BroadcastReceiver--"+timeOfDay, Toast.LENGTH_SHORT).show();
        UsefullData usefullData=new UsefullData(context);
        usefullData.time_check();

        usefullData.run_video_downloader_service();

    }
}
