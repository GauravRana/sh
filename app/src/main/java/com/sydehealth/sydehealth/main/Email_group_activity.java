package com.sydehealth.sydehealth.main;

import android.content.Intent;
import android.os.Bundle;

import com.sydehealth.sydehealth.utility.TabGroupActivity;


/**
 * Created by POPLIFY on 12/22/2016.
 */

public class Email_group_activity extends TabGroupActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent edit = new Intent(Email_group_activity.this, EmailActivity.class);
        edit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(edit);


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
