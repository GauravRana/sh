package com.sydehealth.sydehealth.main;

import android.content.Intent;
import android.os.Bundle;

import com.sydehealth.sydehealth.utility.TabGroupActivity;


/**
 * Created by POPLIFY on 12/22/2016.
 */

public class Settings_group_activity extends TabGroupActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



            startChildActivity("SettingActivity", new Intent(this,Setting_activity.class));

    }

//    @Override
//    public void onBackPressed() {
//
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }

    @Override
    public void onBackPressed() {
        finishFromChild(getParent());
        super.onBackPressed();

    }

}
