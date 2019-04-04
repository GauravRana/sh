package com.sydehealth.sydehealth.volley;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.sydehealth.sydehealth.main.Tab_activity;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;

import javax.annotation.Nullable;

import io.fabric.sdk.android.Fabric;


public class InitializeActivity extends Application implements Application.ActivityLifecycleCallbacks {


    Activity current_activity;
    public static Context context;
    public static MixpanelAPI mMixpanel;
    public SaveData saveData;
    public static int isRadioPlay = -1;
    public static int isRadioPause = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        //JobManager.create(this).addJobCreator(new DemoJobCreator());
        Definitions.initializeApplication(getApplicationContext());
        registerActivityLifecycleCallbacks(this);
        Fresco.initialize(this);
        Fabric.with(this, new Crashlytics());
        context = InitializeActivity.this;
        mMixpanel = MixpanelAPI.getInstance(this, Definitions.MIXPANEL_TOKEN);
        saveData = new SaveData(InitializeActivity.this);
        saveData.remove(Definitions.MAIL_IN_DRAFT);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }

    public static int getIsRadioPlay() {
        return isRadioPlay;
    }

    public static void setIsRadioPlay(int isRadioPlay) {
        InitializeActivity.isRadioPlay = isRadioPlay;
    }

    public static int getIsRadioPause() {
        return isRadioPause;
    }

    public static void setIsRadioPause(int isRadioPause) {
        InitializeActivity.isRadioPause = isRadioPause;
    }

    @Override
    public void onActivityCreated(Activity arg0, Bundle arg1) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mMixpanel.flush();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

        current_activity = activity;

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public static Context getInstance() {
        return context;
    }


}
