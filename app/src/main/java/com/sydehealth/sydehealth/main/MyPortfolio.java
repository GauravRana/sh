package com.sydehealth.sydehealth.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;


import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.model.Classified;
import com.sydehealth.sydehealth.model.PowerAIRes;
import com.sydehealth.sydehealth.retrofit.MyApiEndpointInterface;
import com.sydehealth.sydehealth.retrofit.NetworkHandler;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.Normal_toast;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;

public class MyPortfolio extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_portfolio);
        getAllPics();

    }

    public void getAllPics() {
        MyApiEndpointInterface apiService =
                NetworkHandler.getRetrofit(1).create(MyApiEndpointInterface.class);

        // condition 1 is for BASE URL.....
        Call<Void> call = apiService.getPortfolioPics();
        call.enqueue(new Callback<Void>() {

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //save_data.saveProgress("isFailurePowerAPI", "true");
                Normal_toast.show(getApplicationContext(), "Upload Failed TimeOut", false);
                //dismissProgress();
                // Enable button
                //stopSelf();
            }

            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                //dismissProgress();
                // Enable button

            }

        });

    }
}



