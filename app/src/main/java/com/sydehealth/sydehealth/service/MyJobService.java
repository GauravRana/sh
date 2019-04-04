package com.sydehealth.sydehealth.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.sydehealth.sydehealth.screen_share.Screen_share_activity;
import com.sydehealth.sydehealth.utility.Firebase_toast;

public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        //Firebase_toast.show(activity, title, desc, true, false);
        Intent in = new Intent(getBaseContext(), Screen_share_activity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}