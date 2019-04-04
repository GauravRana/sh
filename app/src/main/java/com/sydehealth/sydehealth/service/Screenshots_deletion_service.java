package com.sydehealth.sydehealth.service;

import android.util.Log;

import java.io.File;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

public class Screenshots_deletion_service extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        // Start your job in a seperate thread, calling jobFinished(params, needsRescheudle) when you are done.
        // See the javadoc for more detail.


        String key = params.getExtras().getString("file_path");

        File file = new File(key);
        if (file.exists()) {
            boolean deleted = file.delete();
            jobFinished(params, false);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Stop the running job, returing true if it needs to be recheduled.
        // See the javadoc for more detail.
        Log.e("---deletion_service---", "" + params);
        return true;
    }
}