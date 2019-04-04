package com.sydehealth.sydehealth.main;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.SaveData;

import androidx.appcompat.app.AppCompatActivity;
import cn.jzvd.JzvdStd;


public class Radio_player extends AppCompatActivity {


    Button youtube_back, youtube_vol_high_button, youtube_vol_low_button, youtube_mute_button;
    AudioManager audioManager;
    int mMaxVolume;
    int current_volume;
    boolean sound = true;
    SettingsContentObserver mSettingsContentObserver;
    UsefullData usefullData;
    SaveData saveData;
    //    private ProgressBar progress;
    String url = "http://media-ice.musicradio.com/HeartLondonMP3";
    boolean click_enable = false;
    JzvdStd jzvdStd;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer exo_player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private BandwidthMeter bandwidthMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_player);

        init();
        initPlayer(url);
        listener();
    }

    private void listener() {

        youtube_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        youtube_vol_high_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume + 5, 0);
                current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                handle_volume_btn(current_volume);
            }
        });
        youtube_vol_low_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume - 5, 0);
                current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                handle_volume_btn(current_volume);

            }
        });
        youtube_mute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sound) {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    youtube_mute_button.setText(getResources().getText(R.string.ic_mute));

                    sound = false;
                } else {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    youtube_mute_button.setText(getResources().getText(R.string.ic_unmute));

                    sound = true;
                }

            }
        });
    }

    private void init() {

        usefullData = new UsefullData(this);
        saveData = new SaveData(this);

        mSettingsContentObserver = new SettingsContentObserver(new Handler());
        getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                mSettingsContentObserver);

        youtube_back = (Button) findViewById(R.id.youtube_back);
        youtube_vol_high_button = (Button) findViewById(R.id.youtube_vol_high_button);
        youtube_vol_low_button = (Button) findViewById(R.id.youtube_vol_low_button);
        youtube_mute_button = (Button) findViewById(R.id.youtube_mute_button);
        youtube_back.setTypeface(usefullData.get_awosome_font());
        youtube_vol_high_button.setTypeface(usefullData.get_awosome_font());
        youtube_vol_low_button.setTypeface(usefullData.get_awosome_font());
        youtube_mute_button.setTypeface(usefullData.get_awosome_font());

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        handle_volume_btn(current_volume);
    }

    public void handle_volume_btn(int current_volume) {


        switch (current_volume) {
            case 0:
                youtube_vol_low_button.setAlpha(.5f);
                youtube_vol_low_button.setEnabled(false);
                youtube_vol_high_button.setAlpha(1f);
                youtube_vol_high_button.setEnabled(true);
                youtube_mute_button.setText(getResources().getString(R.string.ic_mute));
                break;

            case 15:
                youtube_vol_high_button.setAlpha(.5f);
                youtube_vol_high_button.setEnabled(false);
                youtube_vol_low_button.setAlpha(1f);
                youtube_vol_low_button.setEnabled(true);
                youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
                break;

            default:
                youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
                youtube_vol_low_button.setAlpha(1f);
                youtube_vol_low_button.setEnabled(true);
                youtube_vol_high_button.setAlpha(1f);
                youtube_vol_high_button.setEnabled(true);
                youtube_mute_button.setText(getResources().getString(R.string.ic_unmute));
                break;

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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exo_player != null ) {
            pausePlayer();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (exo_player != null) {

             startPlayer();

        }

    }


//    public void initPlayer(String url){
//
//
//
//
////        jzvdStd = (JzvdStd) findViewById(R.id.audio_view);
////
////        jzvdStd.setUp(url,
////                "",
////                0);
////        jzvdStd.startVideo();
////
////
////
////        jzvdStd.setStart(new OnStart() {
////            @Override
////            public void OnStart() {
////                jzvdStd.onClickUiToggle();
////                jzvdStd.startDismissControlViewTimer();
////            }
////        });
////
////        jzvdStd.setListener(new OnComplete() {
////            @Override
////            public void OnComplete() {
////                jzvdStd.setVisibility(View.GONE);
////
////            }
////        });
//
//
//
//
//
//
//    }

    private void initPlayer(final String url) {

        try {
            if (usefullData.isNetworkConnected()) {

                releasePlayer();

                simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.audio_view);
                simpleExoPlayerView.requestFocus();
                simpleExoPlayerView.showController();
                simpleExoPlayerView.setUseController(true);


                bandwidthMeter = new DefaultBandwidthMeter();
                window = new Timeline.Window();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                exo_player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

                simpleExoPlayerView.setPlayer(exo_player);

                exo_player.setPlayWhenReady(true);

                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                        Util.getUserAgent(this, "hi"), new DefaultBandwidthMeter());

                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                exo_player.prepare(mediaSource);

            } else {
                usefullData.make_alert(getResources().getString(R.string.no_internet), false, Radio_player.this);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private void releasePlayer() {
        if (exo_player != null) {
            exo_player.release();
            exo_player = null;
            trackSelector = null;
        }
    }

    private void pausePlayer() {
        exo_player.setPlayWhenReady(false);
        exo_player.getPlaybackState();
    }

    private void startPlayer() {
        exo_player.setPlayWhenReady(true);
        exo_player.getPlaybackState();
    }


}
