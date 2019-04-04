package com.sydehealth.sydehealth.main;

import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.sydehealth.sydehealth.JzvdStdVolumeAfterFullscreen;
import com.sydehealth.sydehealth.adapters.Actors;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.SaveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import cn.jzvd.JzvdStd;
import cn.jzvd.OnComplete;

public class Screen_saver_activity extends AppCompatActivity {

    SaveData save_data;
    UsefullData objUsefullData;
    static boolean isActive = false;
    private String screenSaverFolder = Definitions.VIDEO_FOLDER;
    private ArrayList<String> allVideoList = null;
    private static final String TAG = "Screen_saver_activity";
    //    int stopPosition;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer exo_player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
//    ProgressBar video_loader;
    ArrayList<Actors> actorsList = new ArrayList<Actors>();
    int current_position=0;
    int error_count=0;
    JzvdStdVolumeAfterFullscreen jzvdStd;
    public static boolean isScreenSaverPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_saver_activity);

//        video_loader=(ProgressBar) findViewById(R.id.screensaver_loader);
        save_data = new SaveData(Screen_saver_activity.this);
        objUsefullData = new UsefullData(Screen_saver_activity.this);

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(Screen_saver_activity.this, Util.getUserAgent(Screen_saver_activity.this, "mediaPlayerSample"), (TransferListener) bandwidthMeter);
        window = new Timeline.Window();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        exo_player = ExoPlayerFactory.newSimpleInstance(Screen_saver_activity.this, trackSelector);

        exo_player.setPlayWhenReady(shouldAutoPlay);
        exo_player.setRepeatMode(Player.REPEAT_MODE_ONE);


        String response = getIntent().getStringExtra("response");
        if(!response.isEmpty()) {
            handle_data(response);
        }

    }

    private void handle_data(String data) {
        try {
            actorsList.clear();
            JSONObject jsonObj = new JSONObject(data);
            JSONArray screensaver_videos = jsonObj.getJSONArray("screensaver_videos");
            for (int k = 0; k < screensaver_videos.length(); k++) {

                JSONObject screensaver_videos_in = screensaver_videos.getJSONObject(k);
                Actors actor = new Actors();
                int id = screensaver_videos_in.optInt("id");
                String videoName = screensaver_videos_in.optString("name");
                String media_url = screensaver_videos_in.optString("media_url");

                actor.setcreated_date(videoName);
                actor.setpicture(media_url);
                actor.setItem_id(id);

                actorsList.add(actor);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!actorsList.isEmpty()) {

            Collections.shuffle(actorsList);

            current_position=0;
            play_list();
        }

    }

    private void play_list() {
        File myFile = new File(Definitions.VIDEO_FOLDER + actorsList.get(current_position).getcreated_date() + "_" + actorsList.get(current_position).getItem_id() + ".mp4");
        objUsefullData.Mixpanel_events("Screensaver", "Screensaver Name", actorsList.get(current_position).getcreated_date());
//      submit_screensaver(actorsList.get(current_position).getItem_id());
        if (myFile.exists()) {
            if (objUsefullData.isValidVideo(Definitions.VIDEO_FOLDER + actorsList.get(current_position).getcreated_date() + "_" + actorsList.get(current_position).getItem_id() + ".mp4") ){
                initPlayer(Definitions.VIDEO_FOLDER + actorsList.get(current_position).getcreated_date() + "_" + actorsList.get(current_position).getItem_id() + ".mp4");
            } else {
                initPlayer(actorsList.get(current_position).getpicture());
            }
        } else {
            initPlayer(actorsList.get(current_position).getpicture());
        }
    }

    private void getAllScreenSaverVideos() {

        allVideoList = new ArrayList<String>(); //ArrayList cause you don't know how many files there is
        File folder = new File(screenSaverFolder); //This is just to cast to a File type since you pass it as a String
        File[] filesInFolder = folder.listFiles(); // This returns all the folders and files in your path
        for (File file : filesInFolder) { //For each of the entries do:
            if (!file.isDirectory() && file.getAbsolutePath().endsWith(".mp4")) { //check that it's not a dir
                allVideoList.add(file.getName()); //push the filename as a string
            }
        }
    }

    private String getNextRandomVideo() {
        String path = "";

        if (allVideoList == null)
            return "";

        int min = 0;
        int max = allVideoList.size() - 1;

        do {
            Random r = new Random();
            int randomVideoIndex = r.nextInt(max - min + 1) + min;
            path = screenSaverFolder + allVideoList.get(randomVideoIndex);

        } while (!objUsefullData.isValidVideo(path));

        return path;
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (timer != null) {
//            timer.cancel();
//        }
//    }
//
//    public void start_screensaver() {
//
//        if (timer != null) {
//            timer.cancel();
//        }
//
//        remainMilli = 0;
//        timer = new CounterClass(10000, 1000);
//        timer.start();   //Start the timer
//        isRunning = true;
//    }
//
//    public class CounterClass extends CountDownTimer {
//
//        //All three methods (constructor) need to be overridden to use this class
//
//        //Default Constructor
//        public CounterClass(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//
//        }
//
//        //When timer is ticking, what should happen at that duration; will go in this method
//        @Override
//        public void onTick(long millisUntilFinished) {
//            remainMilli = millisUntilFinished;
//            long time_left = TimeUnit.MILLISECONDS.toSeconds(remainMilli) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainMilli));
////            Log.e("screen image Timer -->",""+time_left);
////            if(time_left==15 ){
////                if(objUsefullData.isNetworkConnected()) {
////                    submit_screensaver(current_screensaver_id);
////                }
////            }
//
//
//        }
//
//        //When time is finished, what should happen: will go in this method
//        @Override
//        public void onFinish() {
//            // reset all variables
//            isRunning = false;
//            remainMilli = 0;
////            if (objUsefullData.isNetworkConnected()) {
////                submit_screensaver(current_screensaver_id);
////            }
//
//        }
//    }

//    private void get_screensaver() {
//
//
//        Map<String, String> headers = new HashMap<>();
//
//        headers.put("Accept", Definitions.VERSION);
//        headers.put("X-User-Email", save_data.get(Definitions.USER_EMAIL));
//        headers.put("X-User-Token", save_data.get(Definitions.USER_TOKEN));
//
//
//        UserAPI.get_JsonObjResp(Definitions.SCREENSAVER_FETCH + current_screensaver_id, headers, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.v("TAG response", response.toString());
//
//                        try {
//                            if (!response.isNull("result")) {
//
//
//                                if (response.getJSONObject("result").has("id")) {
//
//
//                                    objUsefullData.Mixpanel_events("Screensaver", "Advert Name", response.getJSONObject("result").optString("name"));
//
//                                    current_screensaver_id = response.getJSONObject("result").optInt("id");
//
//                                    new DownloadImage().execute(response.getJSONObject("result").optString("thumbnail_url"));
//                                }
//                            }
////                            if(!isRunning){
////                                start_adds_play();
////                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.v("TAG response", error.toString());
////                        if (objUsefullData.isNetworkConnected()) {
////                            get_screensaver();
////                        }
//                    }
//                });
//
//    }
//
//    private void submit_screensaver(final int screensaver_id) {
//
//        //Define Headers
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Accept", Definitions.VERSION);
//        headers.put("X-User-Email", save_data.get(Definitions.USER_EMAIL));
//        headers.put("X-User-Token", save_data.get(Definitions.USER_TOKEN));
//
//
//        JSONObject user = new JSONObject();
//        try {
//            user.put("screensaver_id", screensaver_id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.v("TAG", user.toString());
//
//        UserAPI.post_JsonResp(Definitions.SCREENSAVER_SUBMIT, user, headers, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Log.e("TAG response", response.toString());
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("TAG response", error.toString());
//
//                    }
//                });
//
//    }


//    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
//
//        @Override
//        protected Drawable doInBackground(String... arg0) {
//            // This is done in a background thread
//            return downloadImage(arg0[0]);
//        }
//
//        /**
//         * Called after the image has been downloaded
//         * -> this calls a function on the main thread again
//         */
//        protected void onPostExecute(Drawable image) {
//            setImage(image);
//        }
//
//
//        /**
//         * Actually download the Image from the _url
//         *
//         * @param _url
//         * @return
//         */
//        private Drawable downloadImage(String _url) {
//            //Prepare to download image
//            URL url;
//            BufferedOutputStream out;
//            InputStream in;
//            BufferedInputStream buf;
//
//            //BufferedInputStream buf;
//            try {
//                url = new URL(_url);
//                in = url.openStream();
//
//            /*
//             * THIS IS NOT NEEDED
//             *
//             * YOU TRY TO CREATE AN ACTUAL IMAGE HERE, BY WRITING
//             * TO A NEW FILE
//             * YOU ONLY NEED TO READ THE INPUTSTREAM
//             * AND CONVERT THAT TO A BITMAP
//            out = new BufferedOutputStream(new FileOutputStream("testImage.jpg"));
//            int i;
//
//             while ((i = in.read()) != -1) {
//                 out.write(i);
//             }
//             out.close();
//             in.close();
//             */
//
//                // Read the inputstream
//                buf = new BufferedInputStream(in);
//
//                // Convert the BufferedInputStream to a Bitmap
//                Bitmap bMap = BitmapFactory.decodeStream(buf);
//                if (in != null) {
//                    in.close();
//                }
//                if (buf != null) {
//                    buf.close();
//                }
//
//                return new BitmapDrawable(bMap);
//
//            } catch (Exception e) {
//                Log.e("Error reading file", e.toString());
//            }
//
//            return null;
//        }
//
//    }

//    private void setImage(Drawable drawable) {
//        saver.setBackgroundDrawable(drawable);
//        if (isActive) {
//            start_screensaver();
//        }
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

//        if (timer != null) {
//            timer.cancel();
//        }
//        releasePlayer();

        onBackPressed();

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
//        videoView.pause();
        isActive = false;
//        if (timer != null) {
//            timer.cancel();
//        }
//        if (exo_player != null) {
//            pausePlayer();
//        }
        if (jzvdStd!=null) {
            try {

            JzvdStd.goOnPlayOnPause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        videoView.seekTo(stopPosition);
//        videoView.start();


        isActive = true;
        if (jzvdStd!=null) {
            try{
            JzvdStd.goOnPlayOnResume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        start_screensaver();
//        if (exo_player != null ) {
//
//            startPlayer();
//
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    private void releasePlayer() {
        if (exo_player != null) {
            shouldAutoPlay = exo_player.getPlayWhenReady();
            exo_player.release();
            exo_player = null;
            trackSelector = null;


        }
    }

    public void initPlayer(String url){
        isScreenSaverPlaying = true;
        jzvdStd = (JzvdStdVolumeAfterFullscreen) findViewById(R.id.player_view);
        jzvdStd.setUp(url,
                "",
                0);
        jzvdStd.startVideo_with_scaling();

        jzvdStd.setAllControlsVisiblity(8,8,8,0,8,8,8);

        jzvdStd.setListener(new OnComplete() {
            @Override
            public void OnComplete() {
                jzvdStd.release();
                current_position++;
                if (objUsefullData.indexExists(actorsList, current_position)) {
                    play_list();
                }else {
                    current_position=0;
                    play_list();
                }
            }
        });






    }

//    private void initPlayer( final String url) {
//
//        try {
////            if (objUsefullData.isNetworkConnected()) {
////                player.play(url);
//
//
//            releasePlayer();
//
//            simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.video_view);
//            simpleExoPlayerView.requestFocus();
////                simpleExoPlayerView.showController();
//            simpleExoPlayerView.setUseController(false);
//
//
//
//
//
//
//            TrackSelection.Factory videoTrackSelectionFactory =
//                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
//
//            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
////            exo_player = ExoPlayerFactory.newSimpleInstance(Screen_saver_activity.this, trackSelector);
////            exo_player=ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(Screen_saver_activity.this),
////                    trackSelector, new DefaultLoadControl());
//
//            exo_player =  ExoPlayerFactory.newSimpleInstance(Screen_saver_activity.this,
//            new DefaultRenderersFactory(this),
//                    trackSelector, new DefaultLoadControl());
//
//            simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
//            exo_player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
//            exo_player.setVolume(0);
//
//            simpleExoPlayerView.setPlayer(exo_player);
//
//
//            simpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
//                @Override
//                public void onVisibilityChange(int i) {
//                    simpleExoPlayerView.hideController();
//                }
//            });
//
//            exo_player.addListener(new ExoPlayer.EventListener() {
////                @Override
////                public void onTimelineChanged(Timeline timeline, Object manifest) {
////
////                }
//
//
//
//                @Override
//                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//                }
//
//                @Override
//                public void onLoadingChanged(boolean isLoading) {
//
//                    if (isLoading){
//                        video_loader.setVisibility(View.VISIBLE);
//
//                    } else {
//                        video_loader.setVisibility(View.GONE);
//                    }
//
//                }
//
//                @Override
//                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
////                    if (playbackState == ExoPlayer.STATE_ENDED) {
////                        simpleExoPlayerView.hideController();
////                        simpleExoPlayerView.setUseController(false);
////                        video_loader.setVisibility(View.GONE);
////                        current_position++;
////                        if (objUsefullData.indexExists(actorsList, current_position)) {
////                            play_list();
////                        }else {
////                            current_position=0;
////                            play_list();
////                        }
////
////
////                    } else if (playbackState == ExoPlayer.STATE_READY) {
////                        simpleExoPlayerView.showController();
////                        simpleExoPlayerView.setUseController(true);
////                        video_loader.setVisibility(View.GONE);
////                    }
////
////                    if (playbackState == ExoPlayer.STATE_BUFFERING){
////                        video_loader.setVisibility(View.VISIBLE);
////                    } else {
////                        video_loader.setVisibility(View.GONE);
////                    }
//
//                    switch (playbackState) {
//                        case Player.STATE_ENDED:
//                            Log.i("EventListenerState", "Playback ended!");
//                            simpleExoPlayerView.hideController();
//                            simpleExoPlayerView.setUseController(false);
//                            video_loader.setVisibility(View.GONE);
//                            current_position++;
//                            if (objUsefullData.indexExists(actorsList, current_position)) {
//                                play_list();
//                            }else {
//                                current_position=0;
//                                play_list();
//                            }
//
//
//                            break;
//                        case Player.STATE_READY:
//                            Log.i("EventListenerState", "Playback State Ready!");
//
//                            video_loader.setVisibility(View.GONE);
//                            break;
//                        case Player.STATE_BUFFERING:
//                            Log.i("EventListenerState", "Playback buffering");
//                            video_loader.setVisibility(View.VISIBLE);
//
//
//                            break;
//                        case Player.STATE_IDLE:
//
//                            break;
//
//                    }
//
//                }
//
//                @Override
//                public void onRepeatModeChanged(int repeatMode) {
//
//                }
//
//                @Override
//                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//                }
//
//                @Override
//                public void onPlayerError(ExoPlaybackException error) {
//
////                    if(exo_player!=null){
////                        exo_player.retry();
////                    }
//
//                    if(error_count>3) {
//                        if (actorsList.size() > 1) {
//                            error_count++;
//                            current_position++;
//                            if (objUsefullData.indexExists(actorsList, current_position)) {
//                                play_list();
//                            } else {
//                                current_position = 0;
//                                play_list();
//                            }
//                        } else {
//                            releasePlayer();
//                            onBackPressed();
//                        }
//                    }else {
//                        releasePlayer();
//                        onBackPressed();
//                    }
//
//
//
//                }
//
////                @Override
////                public void onPositionDiscontinuity(int a) {
////
////                }
//
//                @Override
//                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//                }
//
//                @Override
//                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//                }
//                @Override
//                public void onPositionDiscontinuity(int reason) {
//
//                }
//
//                @Override
//                public void onSeekProcessed() {
//
//                }
//            });
//            shouldAutoPlay=true;
//            exo_player.setPlayWhenReady(shouldAutoPlay);
//
//            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
////            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
////            extractorsFactory.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES);
//
//
//            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
//                    mediaDataSourceFactory, extractorsFactory, null, null);
//
//            exo_player.prepare(mediaSource);
//            simpleExoPlayerView.hideController();
//
////            }
//
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//
//
//    }


    private void pausePlayer(){
        exo_player.setPlayWhenReady(false);
        exo_player.getPlaybackState();
    }
    private void startPlayer() {
        exo_player.setPlayWhenReady(true);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        if (Util.SDK_INT > 23) {
//            releasePlayer();
//        }
//
//
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isScreenSaverPlaying = false;
    }


    // jzMediaInterface.setVolume(0, 0);
}
