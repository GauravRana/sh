package com.sydehealth.sydehealth.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;

import com.sydehealth.sydehealth.utility.UserAPI;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.you_tube.YouTubeFailureRecoveryActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CustomPlayerControlActivity extends YouTubeFailureRecoveryActivity {

    private boolean isMute = false;
    private boolean isFirst = false;
    private CounterClass timer;  //Inner User-defined Class
    long remainMilli = 0;
    boolean isRunning = false;
    boolean campaigns_play = false;
    int current_video_id = 0, current_advert_id = 0, current_campaigns_index = 0, current_add_index = 0;
    ImageView addons_layer;
    ArrayList<String> campaigns_list = new ArrayList<String>();
    ArrayList<String> temp_addons_list = new ArrayList<String>();
    UsefullData usefullData;
    SaveData saveData;
    Intent i;
    String request;
    String advert_url;
    boolean first_time = true;
    ProgressDialog loader;
    private YouTubePlayer youTubePlayer;
    private MyPlayerStateChangeListener myPlayerStateChangeListener;
    private MyPlaybackEventListener myPlaybackEventListener;
    Button youtube_back, youtube_vol_high_button, youtube_vol_low_button, youtube_mute_button;
    AudioManager audioManager;
    int mMaxVolume;
    int current_volume;
    boolean sound = false, is_screen_visible = true;
    SettingsContentObserver mSettingsContentObserver;
    private ArrayList<String> playlistUrls = new ArrayList();
    private Context mContext;
    private static final String TAG = "CustomPlayerControlActi";


    private int video_count = 0;
    private CountDownTimer cTimer;

    public static boolean isAlreadyMediaPlay = false;


    int increaseinSound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_player);

        mContext = this;
        i = getIntent();
        isFirst = true;


        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(Definitions.GOOGLE_API_KEY, this);

        campaigns_list = i.getStringArrayListExtra("campaigns_list");
        request = i.getStringExtra("request_from");
        if (!campaigns_list.isEmpty()) {
            Collections.shuffle(campaigns_list);
        }
        initializeView();

        mSettingsContentObserver = new SettingsContentObserver(new Handler());
        getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                mSettingsContentObserver);

        if (i.getBooleanExtra("sky_news", false)) {
            advert_url = Definitions.NEWS_ADVERT;
            start_campaigns();
            usefullData.Mixpanel_events("Playlist Played", "Playlist Name", i.getStringExtra("track_name"));
            video_count = 1;
        } else if (request.equals("playlist")) {
            current_video_id = i.getIntExtra("current_video_id", 0);
            advert_url = Definitions.PLAYLIST_ADVERT_PART_ONE + current_video_id + Definitions.PLAYLIST_ADVERT_PART_TWO;
            start_campaigns();
            usefullData.Mixpanel_events("Playlist Played", "Playlist Name", i.getStringExtra("track_name"));
           // usefullData.Mixpanel_events("Video Count", "Video Count", i.getStringExtra("track_name"));
        }
        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
        myPlaybackEventListener = new MyPlaybackEventListener();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        youtube_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtube_back.setEnabled(false);
                finish();


            }
        });



        youtube_vol_high_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int increase6dec = 6;
                int increase4dec = 4;
                int increase3dec = 3;
                int increase2dec = 2;

                if (current_volume > 1 && current_volume < 6){
                    if(isMute || isFirst) {
                        if(isFirst){
                           isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume + increase4dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }
                    //handle_volume_btn(current_volume);
                }else if (current_volume >= 6 && current_volume < 10){
                    if(isMute || isFirst) {
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume + increase4dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }

                    //handle_volume_btn(current_volume);
                }else if (current_volume >= 10 && current_volume < 13){
                    if(isMute || isFirst) {
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume + increase3dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }
                }
                else if (current_volume >= 13 && current_volume < 15){
                    if(isMute || isFirst) {
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume + increase2dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }
                    //handle_volume_btn(current_volume);
                }else {
                    if(isMute || isFirst) {
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume + increase2dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(current_volume), Toast.LENGTH_SHORT).show();
                handle_volume_btn(current_volume);

            }
        });
        youtube_vol_low_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int decrease6dec = 6;
                int decrease4dec = 4;
                int decrease3dec = 3;
                int decrease2dec = 2;


                if (current_volume > 13 && current_volume <= 15) {
                    if(isMute || isFirst) {
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume - decrease2dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }
                    //handle_volume_btn(current_volume);
                }else if (current_volume <= 13 && current_volume > 10) {
                    if(isMute || isFirst){
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume - decrease3dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }

                    //handle_volume_btn(current_volume);
                }else if (current_volume <= 10 && current_volume > 6) {

                    if(isMute || isFirst){
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume - decrease4dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else{
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }

                }
                else if (current_volume <= 6 && current_volume > 2){
                    if(isMute || isFirst){
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume - decrease4dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }


                    //handle_volume_btn(current_volume);
                }else{
                    if(isMute || isFirst){
                        if(isFirst){
                            isMute = true;
                        }
                        isFirst = false;
                        increaseinSound = current_volume - decrease2dec;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, increaseinSound, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    }else {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume, 0);
                        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        isMute = true;
                    }
                }

                //Toast.makeText(getApplicationContext(), String.valueOf(current_volume), Toast.LENGTH_SHORT).show();
                handle_volume_btn(current_volume);

            }
        });

        youtube_mute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sound) {
                    sound = false;
                    isMute = false;
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    youtube_mute_button.setText(getResources().getText(R.string.ic_mute));
                } else {
                    sound = true;
                    isMute = true;
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 6, 0);
                    current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    youtube_mute_button.setText(getResources().getText(R.string.ic_unmute));
                }
            }
        });
        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        handle_volume_btn(current_volume);
    }

    public void handle_volume_btn(int current_volume) {


//        switch (current_volume) {
//            case 5:
//                youtube_vol_low_button.setAlpha(.5f);
//                youtube_vol_low_button.setEnabled(false);
//                youtube_vol_high_button.setAlpha(1f);
//                youtube_vol_high_button.setEnabled(true);
//                youtube_mute_button.setText(getResources().getString(R.string.ic_mute));
//                break;
//            case 15:
//                youtube_vol_high_button.setAlpha(.5f);
//                youtube_vol_high_button.setEnabled(false);
//                youtube_vol_low_button.setAlpha(1f);
//                youtube_vol_low_button.setEnabled(true);
//                youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
//                break;
//            default:
//                youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
//                youtube_vol_low_button.setAlpha(1f);
//                youtube_vol_low_button.setEnabled(true);
//                youtube_vol_high_button.setAlpha(1f);
//                youtube_vol_high_button.setEnabled(true);
//                youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
//                break;
//
//        }


        if(current_volume < 2){
                youtube_vol_low_button.setAlpha(.5f);
                youtube_vol_low_button.setEnabled(false);
                youtube_vol_high_button.setAlpha(1f);
                youtube_vol_high_button.setEnabled(true);
                youtube_mute_button.setText(getResources().getString(R.string.ic_mute));
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            youtube_mute_button.setText(getResources().getText(R.string.ic_mute));
            sound = false;

        }else if(current_volume == 15){
            youtube_vol_high_button.setAlpha(.5f);
            youtube_vol_high_button.setEnabled(false);
            youtube_vol_low_button.setAlpha(1f);
            youtube_vol_low_button.setEnabled(true);
            youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
            sound = true;

        }else {
            youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
            youtube_vol_low_button.setAlpha(1f);
            youtube_vol_low_button.setEnabled(true);
            youtube_vol_high_button.setAlpha(1f);
            youtube_vol_high_button.setEnabled(true);
            youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
            sound = true;
        }

    }

    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        if (timer != null) {
            timer.cancel();
        }

        if (youTubePlayer != null) {
            youTubePlayer.release();
        }
        usefullData.Mixpanel_events_VideoCount("Video Count", "Video Count", String.valueOf(video_count/2),"Track Name",i.getStringExtra("track_name"));
        video_count = 0;

        if(InitializeActivity.getIsRadioPause() == 1 && InitializeActivity.getIsRadioPlay() == -1) {
            Tab_activity.jcplayerView.pause();
        }else if(InitializeActivity.getIsRadioPlay() == 0 && InitializeActivity.getIsRadioPause()==-1){
            Tab_activity.jcplayerView.continueAudio();
        }
//        usefullData.cancel_alert();
        finish();
    }



    private void start_campaigns() {
        if (!campaigns_list.isEmpty()) {
            if (current_campaigns_index < campaigns_list.size()) {
                campaigns_play = true;
                start_campaigns_play(current_campaigns_index);
                current_campaigns_index++;
            } else if (current_campaigns_index == campaigns_list.size()) {
                current_campaigns_index = 0;
                campaigns_play = true;
                start_campaigns_play(current_campaigns_index);
                current_campaigns_index++;

            }
        } else {
            get_addons();
        }


    }

    private void set_addons(final String url) {

//        if (first_time) {
//            first_time = false;
        if (is_screen_visible) {
            Glide.with(this)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            // Do something with bitmap here.
//                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            addons_layer.setImageBitmap(bitmap);
                            Log.e("GalleryAdapter", "Glide working... ");
                        }
                    });
        }

//        } else {
//            new DownloadImage().execute(url);
//        }

    }

    public void start_campaigns_play(int count) {

        remainMilli = 0;
        manage_timer();
        timer = new CounterClass(21000, 1000);
        timer.start();   //Start the timer
        isRunning = true;
        set_addons(campaigns_list.get(count));

    }

    public void start_add_play(int count) {

        remainMilli = 0;
        manage_timer();
        //21000
        timer = new CounterClass(21000, 1000);
        timer.start();   //Start the timer
        isRunning = true;
        set_addons(temp_addons_list.get(count));

    }

    private void initializeView() {
        usefullData = new UsefullData(CustomPlayerControlActivity.this);
        saveData = new SaveData(this);
        addons_layer = (ImageView) findViewById(R.id.addons_layer);

        youtube_back = (Button) findViewById(R.id.youtube_back);
        youtube_vol_high_button = (Button) findViewById(R.id.youtube_vol_high_button);
        youtube_vol_low_button = (Button) findViewById(R.id.youtube_vol_low_button);
        youtube_mute_button = (Button) findViewById(R.id.youtube_mute_button);
        youtube_back.setTypeface(usefullData.get_awosome_font());
        youtube_vol_high_button.setTypeface(usefullData.get_awosome_font());
        youtube_vol_low_button.setTypeface(usefullData.get_awosome_font());
        youtube_mute_button.setTypeface(usefullData.get_awosome_font());

        usefullData.showProgress2();
    }


    public class CounterClass extends CountDownTimer {

        //All three methods (constructor) need to be overridden to use this class

        //Default Constructor
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        //When timer is ticking, what should happen at that duration; will go in this method
        @Override
        public void onTick(long millisUntilFinished) {
            remainMilli = millisUntilFinished;
            long time_left = TimeUnit.MILLISECONDS.toSeconds(remainMilli) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainMilli));
            Log.e("Timer -->", "" + time_left);
            if (time_left == 8 && campaigns_play) {
                if (is_screen_visible) {
                    if (usefullData.isNetworkConnected()) {
                        get_addons();
                    }


                }
            }

            if (time_left == 15 && !temp_addons_list.isEmpty() && !campaigns_play) {
                if (is_screen_visible) {
                    if (usefullData.isNetworkConnected()) {
                        submit_route(current_advert_id);
                    }


                }
            }
        }

        //When time is finished, what should happen: will go in this method
        @Override
        public void onFinish() {
            // reset all variables
            isRunning = false;
            remainMilli = 0;
            if (campaigns_play) {
                campaigns_play = false;
                start_adds_play();

            } else {
                campaigns_play = true;
                start_campaigns();
            }
        }
    }

    public void start_adds_play() {
        if (!temp_addons_list.isEmpty()) {
            if (current_add_index < temp_addons_list.size()) {
                campaigns_play = false;
                start_add_play(current_add_index);
                current_add_index++;
            } else {
                start_campaigns();
            }
        } else {
            start_campaigns();
        }
    }


    private void get_addons() {
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

        UserAPI.get_JsonObjResp(advert_url + current_advert_id, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        try {
                            if (!response.isNull("result")) {

                                if (response.getJSONObject("result").has("id")) {

                                    usefullData.Mixpanel_events("Advert Viewed", "Advert Name", response.getJSONObject("result").optString("name"));
                                    current_advert_id = response.getJSONObject("result").optInt("id");
                                    temp_addons_list.add(response.getJSONObject("result").optString("thumbnail_url"));
                                }
                            }
                            if (!isRunning) {
                                start_adds_play();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.v("TAG response", error.toString());
//                        get_addons();

                    }
                });

    }

    private void submit_route(final int advert_id) {

        //Define Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));


        JSONObject user = new JSONObject();
        try {
            user.put("advert_id", advert_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TAG", user.toString());

        UserAPI.post_JsonResp(Definitions.SUBMIT_ROUTE, user, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("TAG response", error.toString());
                    }
                });

    }


    private void getYoutubeVideos(final String playListId, final String nextPage) {

        //Define Headers
        Map<String, String> headers = new HashMap<>();
//        headers.put("Accept", Definitions.VERSION);
//        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
//        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

        String url = Definitions.YOUTUBE_DOMAIN + playListId + "&key=" +
                Definitions.GOOGLE_API_KEY + "&maxResults=50" + "&pageToken=" + nextPage;

        UserAPI.get_JsonObjRespCustom(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "onResponse: " + response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_arr_item = jsonArray.getJSONObject(i);
                                JSONObject json_snippet = json_arr_item.getJSONObject("snippet");
                                JSONObject json_resource = json_snippet.getJSONObject("resourceId");
                                playlistUrls.add(json_resource.getString("videoId"));
                            }

                            if (response.has("nextPageToken")) {
                                getYoutubeVideos(playListId, response.getString("nextPageToken"));
                            } else {
                                saveData.storePlayList(playListId, playlistUrls);
                                if(youTubePlayer!=null) {
                                    playYoutubeFromList(playlistUrls);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            Log.v("TAG response", error.toString());
                            usefullData.dismissProgress2();
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if(response.statusCode == 500){
                                    alert(getResources().getString(R.string.no_api_hit));
                                }
                            }else{
                                alert(getResources().getString(R.string.no_internet_speed));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }




                    }
                });

    }

    private void playYoutubeFromList(ArrayList<String> playlistUrls) {
        if (youTubePlayer !=null) {
            Collections.shuffle(playlistUrls);
            youTubePlayer.cueVideos(playlistUrls);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youTubePlayer.setFullscreen(false);
            youTubePlayer.setShowFullscreenButton(false);
            youTubePlayer.play();
            Tab_activity.jcplayerView.pause();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    usefullData.dismissProgress2();
                }
            });
            onPause();
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        is_screen_visible = true;
//        usefullData = new UsefullData(CustomPlayerControlActivity.this);
//        if (i.getBooleanExtra("sky_news", false)) {
//            play_sky_news();
//        } else {
////            if (player != null) {
////                player.onResume();
////            }
//        }


        if (youTubePlayer != null && !youTubePlayer.isPlaying()) {
            youTubePlayer.play();
            Tab_activity.jcplayerView.pause();
        }


    }


    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

        @Override
        protected Drawable doInBackground(String... arg0) {
            // This is done in a background thread
            return downloadImage(arg0[0]);
        }

        /**
         * Called after the image has been downloaded
         * -> this calls a function on the main thread again
         */
        protected void onPostExecute(Drawable image) {
            setImage(image);
        }


        /**
         * Actually download the Image from the _url
         *
         * @param _url
         * @return
         */
        private Drawable downloadImage(String _url) {
            //Prepare to download image
            URL url;
            BufferedOutputStream out;
            InputStream in;
            BufferedInputStream buf;

            //BufferedInputStream buf;
            try {
                url = new URL(_url);
                in = url.openStream();

            /*
             * THIS IS NOT NEEDED
             *
             * YOU TRY TO CREATE AN ACTUAL IMAGE HERE, BY WRITING
             * TO A NEW FILE
             * YOU ONLY NEED TO READ THE INPUTSTREAM
             * AND CONVERT THAT TO A BITMAP
            out = new BufferedOutputStream(new FileOutputStream("testImage.jpg"));
            int i;

             while ((i = in.read()) != -1) {
                 out.write(i);
             }
             out.close();
             in.close();
             */

                // Read the inputstream
                buf = new BufferedInputStream(in);

                // Convert the BufferedInputStream to a Bitmap
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }

                return new BitmapDrawable(bMap);

            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }

            return null;
        }

    }

    private void setImage(Drawable drawable) {
        addons_layer.setBackgroundDrawable(drawable);
    }

    @Override
    public void onPause() {

        super.onPause();
        is_screen_visible = false;
//        if (player != null) {
//            player.onPause();
//        }
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);

    }

    @Override
    protected void onStop() {
        //youTubePlayer=null;
        super.onStop();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try{
            View view = getCurrentFocus();
            if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
                int scrcoords[] = new int[2];
                view.getLocationOnScreen(scrcoords);
                float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                float y = ev.getRawY() + view.getTop() - scrcoords[1];
                if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                    ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
            }
            usefullData.schedule_time_events();
            return super.dispatchTouchEvent(ev);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public class MyCustomProgressDialog extends ProgressDialog {
        String set_msg;

        public ProgressDialog makeProgress(Context context, String msg) {
            this.setIndeterminate(true);
            this.setCancelable(false);
            set_msg = msg;
            return this;
        }

        public MyCustomProgressDialog(Context context) {
            super(context);
        }

        public MyCustomProgressDialog(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_progress_alert);

            TextView pop_title = (TextView) findViewById(R.id.pop_title_alert);
            TextView pop_msg = (TextView) findViewById(R.id.pop_msg_alert);
            Button back = (Button) findViewById(R.id.back_alert);

            pop_msg.setText(set_msg);

            pop_title.setTypeface(usefullData.get_semibold());
            pop_msg.setTypeface(usefullData.get_semibold());
            back.setTypeface(usefullData.get_google_bold());

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            this.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finish();
                    }

                    return true;
                }
            });
        }

        @Override
        public void show() {
            super.show();
        }

        @Override
        public void dismiss() {
            super.dismiss();
        }
    }

    public void alert(final String msg) {
        MyCustomProgressDialog myCustomProgressDialog =
                new MyCustomProgressDialog(CustomPlayerControlActivity.this, R.style.CustomDialog_new);
        loader = myCustomProgressDialog.makeProgress(CustomPlayerControlActivity.this, msg);

        if (!((Activity) this).isFinishing()) {
            loader.show();
        }

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer = player;
            youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
            youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);

            try {
                if (i.getBooleanExtra("sky_news", false)) {
                    player.cueVideo(i.getStringExtra("video_url").trim());
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    player.setFullscreen(false);
                    player.play();
                    usefullData.dismissProgress2();
                } else {
                    String playListId = i.getStringExtra("video_url");

                    if (saveData.getPlayList(playListId).isEmpty()) {
                        getYoutubeVideos(playListId, "");
                    } else {
                        playYoutubeFromList(saveData.getPlayList(playListId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_player_view);
    }


    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        private void updateLog(String prompt) {
//            Toast.makeText(CustomPlayerControlActivity.this,prompt,Toast.LENGTH_LONG).show();

        }

        ;

        @Override
        public void onAdStarted() {
            updateLog("onAdStarted()");
        }

        @Override
        public void onError(
                YouTubePlayer.ErrorReason arg0) {
//            updateLog("onError(): " + arg0.toString());
//            usefullData.make_alert("Invalid Playlist",true,CustomPlayerControlActivity.this);
        }

        @Override
        public void onLoaded(String arg0) {
            updateLog("onLoaded(): " + arg0);
            if(youTubePlayer!=null) {
                youTubePlayer.play();
//                Tab_activity.jcplayerView.pause();
            }
        }

        @Override
        public void onLoading() {
            updateLog("onLoading()");
        }

        @Override
        public void onVideoEnded() {
            updateLog("onVideoEnded()");
        }

        @Override
        public void onVideoStarted() {
            updateLog("onVideoStarted()");
//            Tab_activity.jcplayerView.pause();
        }

    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        private void updateLog(String prompt) {

//            Toast.makeText(CustomPlayerControlActivity.this,prompt,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBuffering(boolean arg0) {
            updateLog("onBuffering(): " + String.valueOf(arg0));
            if(Tab_activity.jcplayerView.isPlaying()) {
                Tab_activity.jcplayerView.pause();
            }
        }

        @Override
        public void onPaused() {
            updateLog("onPaused()");
        }

        @Override
        public void onPlaying() {
            updateLog("onPlaying()");
            cTimer = new CountDownTimer(150000, 100) {
                public void onTick(long millisUntilFinished) {
                    if(Tab_activity.jcplayerView.isPlaying()){
                        Tab_activity.jcplayerView.pause();
                    }
                    updateLog("seconds remaining: " + millisUntilFinished / 1000);
                }
                public void onFinish() {

                }

            };

            cTimer.start();
            if(Tab_activity.jcplayerView.isPlaying()) {
                Tab_activity.jcplayerView.pause();
            }
        }

        @Override
        public void onSeekTo(int arg0) {
            updateLog("onSeekTo(): " + String.valueOf(arg0));
        }

        @Override
        public void onStopped() {
            updateLog("onStopped()");
            video_count++;
        }
    }


    public class SettingsContentObserver extends ContentObserver {

        public SettingsContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            handle_volume_btn(current_volume);
        }
    }

    public void manage_timer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}