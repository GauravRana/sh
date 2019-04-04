package com.sydehealth.sydehealth.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
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
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.RetainingDataSourceSupplier;
import com.sydehealth.sydehealth.adapters.Actors;
import com.sydehealth.sydehealth.adapters.ConditionPopupAdapter;
import com.sydehealth.sydehealth.adapters.TreatmentExpandableAdapter;
import com.sydehealth.sydehealth.adapters.TreatmentExpandableChildListListener;
import com.sydehealth.sydehealth.adapters.TreatmentExpandableListListener;
import com.sydehealth.sydehealth.drawingview.MainActivity;
import com.sydehealth.sydehealth.model.SubHeadings;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.OnSwipeTouchListener;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.OnComplete;
import cn.jzvd.OnStart;

import static com.sydehealth.sydehealth.main.Tab_activity.setAutoOrientationEnabled;


public class TestActivity extends Fragment implements ExoPlayer.EventListener, TreatmentExpandableListListener, TreatmentExpandableChildListListener {

    TextView tv_replay, tv_condition_video, tv_treatment_option;
    LinearLayout condition_video_btn, treatment_option_btn;
    TextView condition_video_txt, treatment_option_txt, header_text;
    ListView condition_mRecyclerView;
    ExpandableListView treatment_mRecyclerView;
    UsefullData usefullData;
    SaveData saveData;
    int current_condition;
    ArrayList<Actors> actorsList = new ArrayList<Actors>();
    ArrayList<Actors> presentation_List = new ArrayList<Actors>();
    ArrayList<Actors> treatment_list = new ArrayList<Actors>();
    ArrayList<String> campaigns_list = new ArrayList<String>();
    ArrayList<Actors> treatment = new ArrayList<Actors>();
    ArrayList<Actors> condition_image = new ArrayList<Actors>();
    ArrayList<String> all_video_urls = new ArrayList<String>();
    ArrayList<String> temp_addons_list = new ArrayList<String>();
    RoundedImageView advert;
    //RoundedImageView treatment_image;
    RelativeLayout treatments_layer;
    boolean first_time = true;
    int current_video_id = 0, current_advert_id = 0, current_campaigns_index = 0, current_add_index = 0;
    private CounterClass timer;  //Inner User-defined Class
    long remainMilli = 0;
    boolean isRunning = false;
    boolean campaigns_play = false;
    String advert_url;
    RelativeLayout mk_player_layout;
    String condition_pre_txt, treatment_pre_txt, condition_name;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer exo_player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    condition_adapter c_adapter;
    treatment_adapter t_adapter;
    Button full_screen, high_vol, low_vol, mute;
    AudioManager audioManager;
    int mMaxVolume;
    int current_volume;
    LinearLayout header_view, treatment_view;
    CardView video_view;
    boolean video_full_screen = false;
    Button bottom_scroll_condition, bottom_scroll_treatment, edit_image;
    TextView arrow;
    boolean condition_scroll_up = false, treatment_scroll_up = false;
    public static Bitmap paint;
    Context mContext;
    View view;
    SettingsContentObserver mSettingsContentObserver;
    TextView not_found_data;
    boolean sound=true;
    LinearLayout circles;
    public static boolean isTabOpened=false;
    int current_presentation_position=0;
    boolean is_controller_visible, is_screen_visible=true;
    boolean video_finished=true;
    ProgressBar video_loader;
    boolean is_advert_visible=true;
    ImageView temp_layer;
    JzvdStd jzvdStd;
    Bitmap image;
    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec

    Uri uriHigh;
    Uri uriLow;
    SimpleDraweeView draweeView;
    SimpleExoPlayer player_view_exo;
    File myFile;


    int groupID = 0;
    int childId1 = 0;
    int childId2 = 0;
    int childId3 = 0;
    int childIdOther = 0;

    String childIdName1 = "";
    String childIdName2 = "";
    String childIdName3 = "";
    String childIdOtherName = "";



    /** Android Views **/
    private LinearLayout layoutTop3Bar;
    private TextView barElement1;
    private TextView barElement2;
    private TextView barElement3;
    private TextView barPreview;


    private LinearLayout llBarElement1;
    private LinearLayout llBarElement2;
    private LinearLayout llBarElement3;


    public static boolean isElement1 = false;
    public static boolean isElement2 = false;
    public static boolean isElement3 = false;

    public static boolean isPortfolio = false;
    public static boolean isGroup = false;
    public static boolean isChild = false;

    public  boolean isChildClick = false;


    public static boolean isGroup1Clicked = false;
    public static boolean isGroup2Clicked = false;
    public static boolean isGroup3Clicked = false;

    /** Android Views **/


    private TreatmentExpandableAdapter treatmentExpandableAdapter;
    private ArrayList<String> listDataHeader = new ArrayList<>();
    private ArrayList<String> listDataHeaderImage = new ArrayList<>();
    private ArrayList<Integer> listDataId = new ArrayList<>();

    private ArrayList<String> listSubDataHeader;
    private ArrayList<String> listSubDataHeaderImage = new ArrayList<>();
    private ArrayList<Integer> listSubDataId ;

    private ArrayList<Actors>listDataSubHeadings = new  ArrayList<>();

    private int dataId = 0;

    int totalElements = 0;


    private  HashMap<String, ArrayList<String>> listTreatmentData = new HashMap<String, ArrayList<String>>();
    private  HashMap<String, ArrayList<Integer>> listTreatmentId = new HashMap<String, ArrayList<Integer>>();

    private LinearLayout viewLayout;

    int childId = 0;

    private boolean isConditionShown = false;
    private boolean isTreatmentShown = false;

    public boolean isFirstClick = false;


    public boolean groupExpanded = false;

    public boolean isUpDownClick = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_condition_player, container, false);
            initView();

        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        is_screen_visible=false;
//        if (exo_player != null && !video_finished) {
//            pausePlayer();
//        }

        Pause_timer();

        if (jzvdStd!=null) {
            try {
                JzvdStd.goOnPlayOnPause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Resume();
        is_screen_visible=true;
//        if (exo_player != null ) {
//            if(!video_finished) {
//                startPlayer();
//            }else {
//                exo_player.seekTo(0);
//            }
//        }

        if (jzvdStd!=null) {
            try{
                JzvdStd.goOnPlayOnResume();
                if(!jzvdStd.isCurrentPlay()) {
                    jzvdStd.startVideo();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Method to initialize views
     */
    private void initView() {
        mContext = getActivity();
        current_condition = getArguments().getInt("current_condition", 0);

        usefullData = new UsefullData(mContext);
        saveData = new SaveData(mContext);
        //iniExoPlayer(view);
        temp_layer=(ImageView) view.findViewById(R.id.temp_layer);
        viewLayout = (LinearLayout) view.findViewById(R.id.view);
        video_loader=(ProgressBar) view.findViewById(R.id.video_loader);
        circles = (LinearLayout) view.findViewById(R.id.circles);
        tv_replay = (TextView) view.findViewById(R.id.tv_replay);
        tv_condition_video = (TextView) view.findViewById(R.id.tv_condition_video);
        tv_treatment_option = (TextView) view.findViewById(R.id.tv_treatment_option);
        treatment_view = (LinearLayout) view.findViewById(R.id.treatment_view);
        bottom_scroll_condition = (Button) view.findViewById(R.id.bottom_scroll_condition);
        bottom_scroll_condition.setTypeface(usefullData.get_awosome_font());
        edit_image = (Button) view.findViewById(R.id.edit_image);
        edit_image.setTypeface(usefullData.get_awosome_font());
        bottom_scroll_treatment = (Button) view.findViewById(R.id.bottom_scroll_treatment);
        bottom_scroll_treatment.setTypeface(usefullData.get_awosome_font());
        not_found_data = (TextView) view.findViewById(R.id.not_found_data);
        not_found_data.setTypeface(usefullData.get_montserrat_regular());
        arrow = (TextView) view.findViewById(R.id.arrow);
        arrow.setTypeface(usefullData.get_awosome_font());

        draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "mediaPlayerSample"), (TransferListener) bandwidthMeter);
        window = new Timeline.Window();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

//        DefaultTrackSelector.Parameters params = trackSelector.getParameters();
//        DefaultTrackSelector.Parameters newParams = trackSelector.getParameters().withMaxVideoBitrate(bitrate).withMaxVideoSize(width, height);
//        trackSelector.setParameters(newParams);

//        FixedTrackSelection.Factory fixedTrackSelect;
//        fixedTrackSelect = new FixedTrackSelection.Factory();
//        trackSelector = new DefaultTrackSelector(fixedTrackSelect);
//        trackSelector.setParameters(trackSelector.getParameters()
//                        .withMaxVideoSize(300, 300) //really draw full available layout
//                        .withMaxVideoBitrate(Integer.MAX_VALUE);

        exo_player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        exo_player.setPlayWhenReady(shouldAutoPlay);
        exo_player.setRepeatMode(Player.REPEAT_MODE_ONE);


        current_video_id = getArguments().getInt("condition_id", 0);
        advert_url = Definitions.CONDITIONS_ADVERT_PART_ONE + current_video_id + Definitions.CONDITIONS_ADVERT_PART_TWO;
        mk_player_layout = (RelativeLayout) view.findViewById(R.id.mk_player_layout);
        advert = (RoundedImageView) view.findViewById(R.id.advert);
        //treatment_image = (RoundedImageView) view.findViewById(R.id.treatment_image);
        treatments_layer = (RelativeLayout) view.findViewById(R.id.treatments_layer);
        condition_video_btn = (LinearLayout) view.findViewById(R.id.condition_video_btn);
        treatment_option_btn = (LinearLayout) view.findViewById(R.id.treatment_option_btn);
        condition_video_txt = (TextView) view.findViewById(R.id.condition_video_txt);
        treatment_option_txt = (TextView) view.findViewById(R.id.treatment_option_txt);
        header_text = (TextView) view.findViewById(R.id.header_text);

        condition_video_txt.setTypeface(usefullData.get_montserrat_regular());
        treatment_option_txt.setTypeface(usefullData.get_montserrat_regular());
        header_text.setTypeface(usefullData.get_montserrat_regular());
        tv_condition_video.setTypeface(usefullData.get_awosome_font_400());
        tv_treatment_option.setTypeface(usefullData.get_awosome_font_400());

        treatment_mRecyclerView = (ExpandableListView) view.findViewById(R.id.treatment_mRecyclerView);
        t_adapter = new treatment_adapter(mContext, R.layout.row_one_condition, treatment);
        treatment_mRecyclerView.setDivider(new ColorDrawable(getResources().getColor(R.color.light_grey))); // set color
        treatment_mRecyclerView.setDividerHeight(1);
        listSubDataHeader = new ArrayList<>() ;
        listSubDataId = new ArrayList<>();
        listDataHeaderImage = new ArrayList<>();


        treatmentExpandableAdapter = new TreatmentExpandableAdapter(getActivity(), listDataHeader, listDataId, listDataHeaderImage, listSubDataHeader, listSubDataId, listSubDataHeaderImage, this);
        treatment_mRecyclerView.setAdapter(treatmentExpandableAdapter);

        //treatment_mRecyclerView.setAdapter(t_adapter);
        treatment_mRecyclerView.setSelector(R.color.list_selector);
//        LinearLayoutManager treatment_mLinearLayoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        treatment_mRecyclerView.setLayoutManager(treatment_mLinearLayoutManager);

        condition_mRecyclerView = (ListView) view.findViewById(R.id.condition_mRecyclerView);
        c_adapter = new condition_adapter(mContext, R.layout.row_one_condition, actorsList);
        condition_mRecyclerView.setDivider(new ColorDrawable(getResources().getColor(R.color.light_grey))); // set color
        condition_mRecyclerView.setDividerHeight(1);
        condition_mRecyclerView.setAdapter(c_adapter);
        condition_mRecyclerView.setSelector(R.color.list_selector);
//        LinearLayoutManager condition_mLinearLayoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        condition_mRecyclerView.setLayoutManager(condition_mLinearLayoutManager);

        full_screen = (Button) view.findViewById(R.id.fullscreen_button);
        high_vol = (Button) view.findViewById(R.id.vol_high_button);
        low_vol = (Button) view.findViewById(R.id.vol_low_button);
        mute = (Button) view.findViewById(R.id.mute_button);

        layoutTop3Bar =  view.findViewById(R.id.layoutTop3Bar);
        barElement1 =  view.findViewById(R.id.barElement1);
        barElement2 =  view.findViewById(R.id.barElement2);
        barElement3 =  view.findViewById(R.id.barElement3);
        barPreview =  view.findViewById(R.id.barPreview);

        llBarElement1 = view.findViewById(R.id.ll_barElement1);
        llBarElement2 = view.findViewById(R.id.ll_barElement2);
        llBarElement3 = view.findViewById(R.id.ll_barElement3);

        full_screen.setTypeface(usefullData.get_awosome_font());
        high_vol.setTypeface(usefullData.get_awosome_font());
        low_vol.setTypeface(usefullData.get_awosome_font());
        mute.setTypeface(usefullData.get_awosome_font());
        tv_replay.setTypeface(usefullData.get_awosome_font());
        barElement1.setTypeface(usefullData.get_montserrat_regular());
        barElement2.setTypeface(usefullData.get_montserrat_regular());
        barElement3.setTypeface(usefullData.get_montserrat_regular());
        header_view = (LinearLayout) view.findViewById(R.id.header_view);

        video_view = (CardView) view.findViewById(R.id.video_view);
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //handle_volume_btn(current_volume);
        quickLinkinitializeEverything();
        listeners();

        if (saveData.isExist(Definitions.CONDITIONS_JSON_DATA)) {
            try {

                JSONObject jsonObj = new JSONObject(saveData.getString(Definitions.CONDITIONS_JSON_DATA));
                set_up_values(jsonObj);
                c_adapter.notifyDataSetChanged();
                t_adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        treatment_mRecyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    bottom_scroll_treatment.setText(getResources().getString(R.string.ic_arrow_up));
                    treatment_scroll_up = true;
                } else {
                    bottom_scroll_treatment.setText(getResources().getString(R.string.ic_bottom_arrow));
                    treatment_scroll_up = false;
                }
            }
        });

        condition_mRecyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    bottom_scroll_condition.setText(getResources().getString(R.string.ic_arrow_up));
                    condition_scroll_up = true;

                } else {
                    bottom_scroll_condition.setText(getResources().getString(R.string.ic_bottom_arrow));
                    condition_scroll_up = false;
                }
            }
        });

        mSettingsContentObserver = new SettingsContentObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true,
                mSettingsContentObserver);

    }

    /**
     * Method to initialize listeners
     */
    private void listeners() {
        video_view.setOnTouchListener(new OnSwipeTouchListener(mContext) {

            public void onSwipeRight() {
                if(!video_full_screen && !is_advert_visible) {
                    initPrev();
                }
            }


            public void onSwipeLeft() {
                if(!video_full_screen && !is_advert_visible) {
                    initNext();
                }
            }

        });


        bottom_scroll_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int f_index = condition_mRecyclerView.getFirstVisiblePosition();
                int l_index = condition_mRecyclerView.getLastVisiblePosition();
                if (!condition_scroll_up) {
                    l_index = l_index + 5;
                    if (usefullData.indexExists(actorsList, l_index)) {
                        condition_mRecyclerView.smoothScrollToPosition(l_index);
                        bottom_scroll_condition.setText(getResources().getString(R.string.ic_bottom_arrow));
                        condition_scroll_up = false;
                    } else {
                        condition_mRecyclerView.smoothScrollToPosition(actorsList.size() - 1);
                        bottom_scroll_condition.setText(getResources().getString(R.string.ic_arrow_up));
                        condition_scroll_up = true;

                    }
                } else {
                    condition_mRecyclerView.smoothScrollToPosition(0);
                    bottom_scroll_condition.setText(getResources().getString(R.string.ic_bottom_arrow));
                    condition_scroll_up = false;

                }


            }
        });

        bottom_scroll_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUpDownClick = true;
                int f_index = treatment_mRecyclerView.getFirstVisiblePosition();
                int l_index = treatment_mRecyclerView.getLastVisiblePosition();
                if (!treatment_scroll_up) {
                    l_index = l_index + 5;
                    if (usefullData.indexExists(treatment, l_index)) {
                        treatment_mRecyclerView.smoothScrollToPosition(l_index);
                        bottom_scroll_treatment.setText(getResources().getString(R.string.ic_bottom_arrow));
                        treatment_scroll_up = false;
                    } else {
                        int totalElements = treatment.size();
                        for(int i = 0 ; i < treatment.size() ; i++){
                            if(treatment_mRecyclerView.isGroupExpanded(i)) {
                                totalElements = 0;
                                totalElements = treatmentExpandableAdapter.getChildrenCount(i) + treatment.size();
                            }
                        }

                        treatment_mRecyclerView.smoothScrollToPosition(totalElements);
                        bottom_scroll_treatment.setText(getResources().getString(R.string.ic_arrow_up));
                        treatment_scroll_up = true;

                    }
                } else {
                    treatment_mRecyclerView.smoothScrollToPosition(0);
                    bottom_scroll_treatment.setText(getResources().getString(R.string.ic_bottom_arrow));
                    treatment_scroll_up = false;

                }


            }
        });

        full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (video_full_screen) {
                    player_full_screen(false);
                } else {
                    player_full_screen(true);
                }


            }
        });
        high_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume + 5, 0);
                current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                handle_volume_btn(current_volume);
            }
        });
        low_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current_volume - 5, 0);
                current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                handle_volume_btn(current_volume);

            }
        });
        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//                current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                handle_volume_btn(current_volume);
                if(sound){
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    mute.setText(getResources().getText(R.string.ic_mute));

                    sound=false;
                }else {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    mute.setText(getResources().getText(R.string.ic_unmute));
                    sound=true;
                }



            }
        });


        condition_mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                isFirstClick = true;
                quickLinkinitializeEverything();
                for(int i = 0; i < listDataHeader.size(); i++){
                    treatment_mRecyclerView.collapseGroup(i);
                }
                is_advert_visible=false;
//                treatment_mRecyclerView.setSelector(R.color.white);
                condition_mRecyclerView.setSelector(R.color.list_selector);


                SpannableString spannableString_2 = new SpannableString(actorsList.get(position).getcomnt());
                ClickableSpan clickableSpan_2 = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {

                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(getResources().getColor(R.color.white));
//                        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                        textPaint.setTypeface(usefullData.get_montserrat_semibold());
                        textPaint.setUnderlineText(false);

                    }
                };
                spannableString_2.setSpan(clickableSpan_2, 0,
                        actorsList.get(position).getcomnt().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                header_text.setText(spannableString_2, TextView.BufferType.SPANNABLE);

//                usefullData.Mixpanel_multiple_events("Condition Image Viewed", "Condition", actorsList.get(position).getcomnt(), "Image URL", actorsList.get(position).getpicture());
                saveData.saveConditionID("condition_id", actorsList.get(position).getItem_id());
                saveData.saveConditionName("condition_name", actorsList.get(position).getcomnt());
                get_presentation(Definitions.PRESENTATION,actorsList.get(position).getItem_id());


            }
        });

        tv_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usefullData.indexExists(presentation_List,current_presentation_position)) {
                    player_handler(current_presentation_position);
                }else {
                    usefullData.make_alert(getResources().getString(R.string.wrong), false, mContext);

                }

            }
        });

        treatment_mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

//                condition_mRecyclerView.setSelector(R.color.white);
                is_advert_visible=false;
                treatment_mRecyclerView.setSelector(R.color.list_selector);
//                switch_player_or_treatment(false);
//                treatment_image.setEnabled(false);
//                if (treatment.get(position).getlikes() != null) {
//                    treatment_image.setEnabled(true);
//                }

                // edit_image.setVisibility(View.VISIBLE);

                SpannableString spannableString_2 = new SpannableString(treatment.get(position).getlikes2());
                ClickableSpan clickableSpan_2 = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {

                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(getResources().getColor(R.color.white));
//                        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                        textPaint.setTypeface(usefullData.get_montserrat_semibold());
                        textPaint.setUnderlineText(false);
                    }
                };
                spannableString_2.setSpan(clickableSpan_2, 0,
                        treatment.get(position).getlikes2().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                header_text.setText(spannableString_2, TextView.BufferType.SPANNABLE);
                usefullData.Mixpanel_multiple_events("Treatment Option Image Viewed", "Treatment Option", treatment.get(position).getlikes2(), "Image URL", treatment.get(position).getpicture());
                get_presentation(Definitions.GALLERY, treatment.get(position).getItem_id());



//                Glide.with(mContext)
//                        .load(treatment.get(position).gettitle())
//                        .asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                // Do something with bitmap here.
//                                edit_image.setVisibility(View.GONE);
//                                treatment_image.setImageBitmap(bitmap);
//
//                                Glide.with(mContext)
//                                        .load(treatment.get(position).getpicture())
//                                        .asBitmap()
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .into(new SimpleTarget<Bitmap>() {
//                                            @Override
//                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                                // Do something with bitmap here.
//                                                edit_image.setVisibility(View.VISIBLE);
//                                                treatment_image.setImageBitmap(bitmap);
//
//                                            }
//                                        });
//                            }
//                        });

            }
        });


        condition_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //layoutTop3Bar.setVisibility(View.INVISIBLE);
                //quickLinkinitializeEverything();
//                for(int i = 0; i < listDataHeader.size(); i++){
//                    treatment_mRecyclerView.collapseGroup(i);
//                }

                quickLinkinitializeEverything();
                //((Tab_activity)getActivity()).ll_email.setBackground(null);
                isTabOpened=false;
                is_advert_visible=false;

                if (actorsList.isEmpty()) {
                    not_found_data.setVisibility(View.VISIBLE);
                    not_found_data.setText(getResources().getString(R.string.no_condtion));
                    condition_mRecyclerView.setVisibility(View.GONE);
                } else {
                    not_found_data.setVisibility(View.GONE);
                    condition_mRecyclerView.setVisibility(View.VISIBLE);
                }
                condition_video_btn.setBackground(getResources().getDrawable(R.drawable.condition_white));
                treatment_option_btn.setBackground(getResources().getDrawable(R.drawable.condition_gradient));
                treatment_option_txt.setTextColor(getResources().getColor(R.color.white));
                tv_treatment_option.setTextColor(getResources().getColor(R.color.white));
                condition_video_txt.setTextColor(getResources().getColor(R.color.stats));
                tv_condition_video.setTextColor(getResources().getColor(R.color.stats));
                treatment_mRecyclerView.setVisibility(View.GONE);
                condition_mRecyclerView.setVisibility(View.VISIBLE);

//                send_email_click.setVisibility(View.GONE);
                bottom_scroll_treatment.setVisibility(View.GONE);
                if (actorsList.size() > 7) {
                    bottom_scroll_condition.setVisibility(View.VISIBLE);
                } else {
                    bottom_scroll_condition.setVisibility(View.GONE);
                }

                String temp=condition_name;
                if (temp.length() > 27) {
                    temp = temp.substring(0, 27)+ "...";
                }

                SpannableString spannableString_2 = new SpannableString(condition_pre_txt + temp);
                ClickableSpan clickableSpan_2 = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {

                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(getResources().getColor(R.color.white));
                        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                        textPaint.setTypeface(usefullData.get_montserrat_bold());
                        textPaint.setUnderlineText(false);
                    }
                };
                spannableString_2.setSpan(clickableSpan_2, condition_pre_txt.length(),
                        condition_pre_txt.length() + temp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                header_text.setText(spannableString_2, TextView.BufferType.SPANNABLE);
//                condition_mRecyclerView.setItemChecked(0,false);

//                condition_mRecyclerView.setSelector(R.color.white);
//                condition_mRecyclerView.clearChoices();
//                c_adapter.notifyDataSetInvalidated();
//                condition_mRecyclerView.requestLayout();
                if(isFirstClick) {
                    setSnapanable(saveData.getConditonName("condition_name"));
                    get_presentation(Definitions.PRESENTATION, saveData.getConditionID("condition_id"));
                }
                switch_advert();

            }

        });


        treatment_option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usefullData.isNetworkConnected()) {
                    if (isFirstClick) {
                        layoutTop3Bar.setVisibility(View.VISIBLE);
                    } else {
                        layoutTop3Bar.setVisibility(View.GONE);
                    }
                }

                for(int i = 0; i < listDataHeader.size(); i++){
                    treatment_mRecyclerView.collapseGroup(i);
                }



//                if(isGroup1Clicked) {
//                    quickLink1True();
//                    treatment_mRecyclerView.expandGroup(0);
//                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(0));
//                }else if(isGroup2Clicked){
//                    quickLink1True();
//                    treatment_mRecyclerView.expandGroup(1);
//                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(1));
//                }else if(isGroup3Clicked){
//                    quickLink1True();
//                    treatment_mRecyclerView.expandGroup(2);
//                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(2));
//                }





                if(treatment_option_txt.getText().toString().equalsIgnoreCase("0")){
                    layoutTop3Bar.setVisibility(View.GONE);
                }




                //condition_mRecyclerView.setSelector(R.color.white);
//                treatment_mRecyclerView.setSelector(R.color.white);
//                treatment_mRecyclerView.setItemChecked(0,false);
//                treatment_mRecyclerView.clearChoices();
//                treatment_mRecyclerView.requestLayout();
//                t_adapter.notifyDataSetInvalidated();
                is_advert_visible=false;
                //((Tab_activity)getActivity()).ll_email.setBackground(getResources().getDrawable(R.drawable.transparent_with_white_border));
                isTabOpened=true;
                if (treatment.isEmpty()) {
                    treatment_mRecyclerView.setVisibility(View.GONE);
                    not_found_data.setVisibility(View.VISIBLE);
                    not_found_data.setText(getResources().getString(R.string.no_treatment));
                } else {
                    not_found_data.setVisibility(View.GONE);
                    treatment_mRecyclerView.setVisibility(View.VISIBLE);
                }

                treatment_option_btn.setBackground(getResources().getDrawable(R.drawable.condition_white));
                condition_video_btn.setBackground(getResources().getDrawable(R.drawable.condition_gradient));
//                condition_video_txt.setTextColor(getResources().getColor(R.color.white));
//                treatment_option_txt.setTextColor(getResources().getColor(R.color.black));

                treatment_option_txt.setTextColor(getResources().getColor(R.color.stats));
                tv_treatment_option.setTextColor(getResources().getColor(R.color.stats));
                condition_video_txt.setTextColor(getResources().getColor(R.color.white));
                tv_condition_video.setTextColor(getResources().getColor(R.color.white));

                //treatment_mRecyclerView.setVisibility(View.VISIBLE);
                treatmentExpandableAdapter.notifyDataSetChanged();
                condition_mRecyclerView.setVisibility(View.GONE);



//                send_email_click.setVisibility(View.VISIBLE);


                setScrollbuttonVisibility(treatment.size());

                String temp=condition_name;
                if (temp.length() > 35) {
                    temp = temp.substring(0, 35)+ "...";

                }
                SpannableString spannableString_2 = new SpannableString(treatment_pre_txt + temp);
                ClickableSpan clickableSpan_2 = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {

                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(getResources().getColor(R.color.white));
                        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                        textPaint.setTypeface(usefullData.get_montserrat_bold());
                        textPaint.setUnderlineText(false);
                    }
                };
                spannableString_2.setSpan(clickableSpan_2, treatment_pre_txt.length(),
                        treatment_pre_txt.length() + temp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                header_text.setText(spannableString_2, TextView.BufferType.SPANNABLE);

                switch_advert();
            }
        });

        llBarElement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_advert_visible = false;
                quickLinkSelected();
                llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));


                treatment_option_btn.setBackground(getResources().getDrawable(R.drawable.condition_white));
                condition_video_btn.setBackground(getResources().getDrawable(R.drawable.condition_gradient));
                treatment_option_txt.setTextColor(getResources().getColor(R.color.stats));
                tv_treatment_option.setTextColor(getResources().getColor(R.color.stats));
                condition_video_txt.setTextColor(getResources().getColor(R.color.white));
                tv_condition_video.setTextColor(getResources().getColor(R.color.white));
                //treatment_mRecyclerView.setVisibility(View.VISIBLE);
                treatmentExpandableAdapter.notifyDataSetChanged();
                condition_mRecyclerView.setVisibility(View.GONE);

                treatment_mRecyclerView.setVisibility(View.VISIBLE);

                if(isConditionShown){
                    setSnapanable(listDataHeader.get(0));
                    dataId = listDataId.get(0);
                    isGroup1Clicked = true;
                    isGroup2Clicked = false;
                    isGroup3Clicked = false;
                    isChild = false;
                    isElement1 = false;
                    isElement2 = false;
                    isElement3 = false;
                    //saveData.saveConditionID("condition_id", listDataId.get(0));
                    treatment_mRecyclerView.expandGroup(0);
                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(0), listDataHeader.size());

                }else{
                    setSnapanable(childIdName1);
                    isElement1 = true;
                    isElement2 = false;
                    isElement3 = false;
                    get_presentationForChild(Definitions.CHILD_GALLERY, childId1);
                    whichGroupClicked();


                }
            }
        });

        llBarElement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_advert_visible = false;
                quickLinkSelected();
                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_select));


                treatment_option_btn.setBackground(getResources().getDrawable(R.drawable.condition_white));
                condition_video_btn.setBackground(getResources().getDrawable(R.drawable.condition_gradient));
                treatment_option_txt.setTextColor(getResources().getColor(R.color.stats));
                tv_treatment_option.setTextColor(getResources().getColor(R.color.stats));
                condition_video_txt.setTextColor(getResources().getColor(R.color.white));
                tv_condition_video.setTextColor(getResources().getColor(R.color.white));
                //treatment_mRecyclerView.setVisibility(View.VISIBLE);
                treatmentExpandableAdapter.notifyDataSetChanged();
                condition_mRecyclerView.setVisibility(View.GONE);

                treatment_mRecyclerView.setVisibility(View.VISIBLE);

                if(isConditionShown){
                    setSnapanable(listDataHeader.get(1));
                    dataId = listDataId.get(1);
                    isChild = false;
                    isGroup1Clicked = false;
                    isGroup2Clicked = true;
                    isGroup3Clicked = false;
                    isElement1 = false;
                    isElement2 = false;
                    isElement3 = false;
                    treatment_mRecyclerView.expandGroup(1);
                    //saveData.saveConditionID("condition_id", listDataId.get(1));
                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(1), listDataHeader.size());
                }else{
                    setSnapanable(childIdName2);
                    isElement2 = true;
                    isElement1 = false;
                    isElement3 = false;
                    get_presentationForChild(Definitions.CHILD_GALLERY, childId2);
                    whichGroupClicked();
                }
            }
        });

        llBarElement3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_advert_visible = false;
                quickLinkSelected();
                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_select));


                treatment_option_btn.setBackground(getResources().getDrawable(R.drawable.condition_white));
                condition_video_btn.setBackground(getResources().getDrawable(R.drawable.condition_gradient));
                treatment_option_txt.setTextColor(getResources().getColor(R.color.stats));
                tv_treatment_option.setTextColor(getResources().getColor(R.color.stats));
                condition_video_txt.setTextColor(getResources().getColor(R.color.white));
                tv_condition_video.setTextColor(getResources().getColor(R.color.white));
                //treatment_mRecyclerView.setVisibility(View.VISIBLE);
                treatmentExpandableAdapter.notifyDataSetChanged();
                condition_mRecyclerView.setVisibility(View.GONE);

                treatment_mRecyclerView.setVisibility(View.VISIBLE);

                if(isConditionShown){
                    setSnapanable(listDataHeader.get(2));
                    dataId = listDataId.get(2);
                    isChild = false;
                    isGroup1Clicked = false;
                    isGroup2Clicked = false;
                    isGroup3Clicked = true;
                    isElement1 = false;
                    isElement2 = false;
                    isElement3 = false;
                    treatment_mRecyclerView.expandGroup(2);
                    //saveData.saveConditionID("condition_id", listDataId.get(2));
                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(2), listDataHeader.size());

                }else{
                    setSnapanable(childIdName3);
                    isElement3 = true;
                    isElement1 = false;
                    isElement2 = false;
                    get_presentationForChild(Definitions.CHILD_GALLERY, childId3);
                    whichGroupClicked();
                }
            }
        });


        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_image.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(mContext)) {
                        setAutoOrientationEnabled(mContext.getContentResolver(), true);


                        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        Permissions.check(getActivity(), permissions, null, null, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                Bitmap bitmap = getScreenShot(draweeView);
                                SaveImage(bitmap, "/" + "temps" + ".jpg");
                                // Stuff that updates the UI

                            }
                        });

                    } else {
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }
                edit_image.setEnabled(true);
            }

        });


        treatment_mRecyclerView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                treatment_mRecyclerView.smoothScrollToPosition(groupPosition);
                if (groupPosition != previousItem)
                    treatment_mRecyclerView.collapseGroup(previousItem);
                previousItem = groupPosition;

            }
        });


        treatment_mRecyclerView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                is_advert_visible = false;
                isFirstClick = true;
                isChild = false;
                int childCount = 0;
                int groupCount = 0;

                //when group closed
                if(parent.isGroupExpanded(groupPosition)){
                    parent.collapseGroup(groupPosition);
                    childCount = 0;
                    groupCount = 0;
                    isGroup1Clicked = false;
                    isGroup2Clicked = false;
                    isGroup3Clicked = false;
                    isElement1 = false;
                    isElement2 = false;
                    isElement3 = false;
                    setScrollbuttonVisibility(listDataHeader.size());
                }
                //when group is expanded
                else {
                    dataId = listDataId.get(groupPosition);
                    setSnapanable(listDataHeader.get(groupPosition));
                    groupCount = listDataHeader.size();
                    quickLinkAllFalse();
                    isPortfolio = false;
                    isGroup = true;
                    if(groupPosition == 0){
                        isGroup1Clicked = true;
                        isGroup2Clicked = false;
                        isGroup3Clicked = false;
                        isElement1 = false;
                        isElement2 = false;
                        isElement3 = false;
                    }else if(groupPosition == 1){
                        isGroup1Clicked = false;
                        isGroup2Clicked = true;
                        isGroup3Clicked = false;
                        isElement1 = false;
                        isElement2 = false;
                        isElement3 = false;

                    }else if(groupPosition == 2){
                        isGroup1Clicked = false;
                        isGroup2Clicked = false;
                        isGroup3Clicked = true;
                        isElement1 = false;
                        isElement2 = false;
                        isElement3 = false;

                    }else{
                        isGroup1Clicked = false;
                        isGroup2Clicked = false;
                        isGroup3Clicked = false;
                        isElement1 = false;
                        isElement2 = false;
                        isElement3 = false;
                    }

                    get_presentationForSubTreatment(Definitions.GALLERY, listDataId.get(groupPosition), groupCount);
                    parent.expandGroup(groupPosition);
                }

                return true;
            }
        });


        treatment_mRecyclerView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });


        //            if (selectedCondition.equals("")) {
        //            } else {
        //                treatment_mRecyclerView.expandGroup(selectedGroupPos);
        //            }

        treatment_mRecyclerView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //listDataSubHeadings.get(listDataHeader.get(groupPosition)).get(childPosition).setSelected(true);
                //treatmentExpandableAdapter.notifyDataSetChanged();
                quickLinkAllFalse();
                is_advert_visible = false;
                layoutTop3Bar.setVisibility(View.VISIBLE);
                isPortfolio = false;
                isGroup = false;
                isChild = true;
                isChildClick = true;
                childId = childId;
                setSnapanable(listSubDataHeader.get(childPosition));
                if(childPosition == 0){
                    isElement1 = true;
                    isElement2 = false;
                    isElement3 = false;
                    llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                    llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                    llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                }else if(childPosition == 1){
                    isElement2 = true;
                    isElement1 = false;
                    isElement3 = false;
                    llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_select));
                    llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                    llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                }else if(childPosition == 2){
                    isElement3 = true;
                    isElement1 = false;
                    isElement2 = false;
                    llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_select));
                    llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                    llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                }else{
                    isElement3 = false;
                    isElement1 = false;
                    isElement2 = false;
                    llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                    llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                    llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                }
                get_presentationForChild(Definitions.CHILD_GALLERY,  listSubDataId.get(childPosition));
                childIdOther = listSubDataId.get(childPosition);
                childIdOtherName = listSubDataHeader.get(childPosition);

                for (int i = 1; i < parent.getChildCount(); i++) {
                    View child = (View) parent.getChildAt(i);
                    child.setBackgroundColor(Color.WHITE);
                }


                parent.setSelected(true);
                if(parent.isSelected()){
                    v.setBackgroundColor(getResources().getColor(R.color.list_bg_color));
                }else {
                    v.setBackgroundColor(getResources().getColor(R.color.white));
                }
                return true;
            }
        });

        barPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_advert_visible = false;
                quickLinkinitialize();
                barPreview.setBackground(getResources().getDrawable(R.drawable.preview_on));
                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                //treatmentExpandableAdapter.notifyDataSetChanged();
                get_presentationForPreview(Definitions.GALLERY, dataId);
                setSnapanable("Portfolio");
            }
        });

    }

    private void setScrollbuttonVisibility(int totalCount) {
        if (totalCount > 7) {
            bottom_scroll_treatment.setVisibility(View.VISIBLE);
        } else {
            bottom_scroll_treatment.setVisibility(View.GONE);
        }
    }

    public void whichGroupClicked(){
        if(isGroup1Clicked) {
            //isGroup1Clicked = false;
            treatment_mRecyclerView.expandGroup(0);
        }else if(isGroup2Clicked){
            //isGroup2Clicked = false;
            treatment_mRecyclerView.expandGroup(1);
        }else if(isGroup3Clicked){
            //isGroup3Clicked = false;
            treatment_mRecyclerView.expandGroup(2);
        }
    }


    public static Bitmap getScreenShot(View view) {
//        View screenView = view.getRootView();

        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        int height = bitmap.getHeight(), width = bitmap.getWidth();
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void SaveImage(Bitmap finalBitmap, String fname) {

        paint = Bitmap.createScaledBitmap(finalBitmap, 1920, 1200, true);

//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/Temp");
//        myDir.mkdirs();
//        File file = new File (myDir, fname);
//        if (file.exists ())
//            file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//        File final_file = new File(root + "/Jukepad");
//        final_file.mkdirs();


//        beginCrop(Uri.parse("file://"+file));
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("request", "normal");
        startActivity(intent);

    }



//    private void initPlayer( final String url) {
//
//        try {
//            if (usefullData.isNetworkConnected()) {
////                player.play(url);
//
//
//                releasePlayer();
//                video_finished=false;
//                simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
//                simpleExoPlayerView.requestFocus();
//                simpleExoPlayerView.showController();
//                simpleExoPlayerView.setUseController(true);
//
////                simpleExoPlayerView.setBackground(getResources().getDrawable(R.drawable.loader_background));
//                tv_replay.setVisibility(View.GONE);
//
//
//                TrackSelection.Factory videoTrackSelectionFactory =
//                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
//
//                trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
////                exo_player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
//                exo_player =  ExoPlayerFactory.newSimpleInstance(getActivity(),
//                        new DefaultRenderersFactory(getActivity()),
//                        trackSelector, new DefaultLoadControl());
//
//                simpleExoPlayerView.setPlayer(exo_player);
//
//                shouldAutoPlay=true;
//                exo_player.setPlayWhenReady(shouldAutoPlay);
//
////                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
////                extractorsFactory.setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_IGNORE_H264_STREAM);
//
//
//                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
//                        mediaDataSourceFactory, extractorsFactory, null, null);
//
//
//                exo_player.prepare(mediaSource);
//                simpleExoPlayerView.hideController();
//
//
//
//                simpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
//                    @Override
//                    public void onVisibilityChange(int i) {
//                        if(i == 0) {
//                            is_controller_visible=true;
//                        }else {
//                            is_controller_visible=false;
//                        }
//                    }
//                });
//
//                exo_player.addListener(new ExoPlayer.EventListener() {
////                    @Override
////                    public void onTimelineChanged(Timeline timeline, Object manifest) {
////
////                    }
//
//                    @Override
//                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//                    }
//
//                    @Override
//                    public void onLoadingChanged(boolean isLoading) {
//
//                        if (isLoading){
//                            video_loader.setVisibility(View.VISIBLE);
//                            tv_replay.setVisibility(View.GONE);
//                        } else {
//                            video_loader.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
////                        if (playbackState == ExoPlayer.STATE_ENDED) {
////                            simpleExoPlayerView.hideController();
////                            simpleExoPlayerView.setUseController(false);
////                            tv_replay.setVisibility(View.VISIBLE);
////                            video_finished=true;
////                            video_loader.setVisibility(View.GONE);
////
////                        } else if (playbackState == ExoPlayer.STATE_READY) {
////                            tv_replay.setVisibility(View.GONE);
////                            simpleExoPlayerView.showController();
////                            simpleExoPlayerView.setUseController(true);
////                            video_finished=false;
////                            video_loader.setVisibility(View.GONE);
////                        }
////
////                        if (playbackState == ExoPlayer.STATE_BUFFERING){
////                            video_loader.setVisibility(View.VISIBLE);
////                            tv_replay.setVisibility(View.GONE);
////                        } else {
////                            video_loader.setVisibility(View.GONE);
////                        }
//
//                        switch (playbackState) {
//                            case Player.STATE_ENDED:
//                                Log.i("EventListenerState", "Playback ended!");
//                                simpleExoPlayerView.hideController();
//                                simpleExoPlayerView.setUseController(false);
//                                tv_replay.setVisibility(View.VISIBLE);
//                                video_finished=true;
//                                video_loader.setVisibility(View.GONE);
//
//                                break;
//                            case Player.STATE_READY:
//                                Log.i("EventListenerState", "Playback State Ready!");
//                                tv_replay.setVisibility(View.GONE);
//                                simpleExoPlayerView.showController();
//                                simpleExoPlayerView.setUseController(true);
//                                video_finished=false;
//                                video_loader.setVisibility(View.GONE);
//                                break;
//                            case Player.STATE_BUFFERING:
//                                Log.i("EventListenerState", "Playback buffering");
//                                video_loader.setVisibility(View.VISIBLE);
//                                tv_replay.setVisibility(View.GONE);
//
//                                break;
//                            case Player.STATE_IDLE:
//
//                                break;
//
//                        }
//                    }
//
//                    @Override
//                    public void onRepeatModeChanged(int repeatMode) {
//
//                    }
//
//                    @Override
//                    public void onPlayerError(ExoPlaybackException error) {
////                        if(exo_player!=null){
////                            exo_player.retry();
////                        }
//
////                        if(usefullData.indexExists(presentation_List,current_presentation_position)) {
////                            player_handler(current_presentation_position);
////                        }else {
////                            usefullData.make_alert(getResources().getString(R.string.wrong), false, mContext);
////
////                        }
////                        if (error.getCause() instanceof MediaCodecRenderer.DecoderInitializationException) {
////                            workaroundRequired = true;
////                        }
////                        simpleExoPlayerView.useDummySurfaceWorkaround(true);
//
//                    }
//
//                    //                    @Override
////                    public void onPositionDiscontinuity() {
////
////                    }
//                    @Override
//                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//                    }
//
//
//                    @Override
//                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//                    }
//
//                    @Override
//                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//                    }
//                    @Override
//                    public void onPositionDiscontinuity(int reason) {
//
//                    }
//
//                    @Override
//                    public void onSeekProcessed() {
//
//                    }
//                });
//
//
//                simpleExoPlayerView.setOnTouchListener(new OnSwipeTouchListener(mContext) {
//
//                    public void onSwipeRight() {
//                        if(!video_full_screen) {
//                            initPrev();
//
//                        }
//                    }
//
//                    public void onClick() {
//
//                        if(is_controller_visible) {
//                            simpleExoPlayerView.hideController();
//                        }else {
//                            simpleExoPlayerView.showController();
//                        }
//                    }
//
//                    public void onSwipeLeft() {
//
//                        if(!video_full_screen) {
//                            initNext();
//                        }
//                    }
//
//
//                });
//
//
//            } else {
//                usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);
//            }
//
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public void initPlayer(String url,String thumb){

        //Not saving the state of video....
        Jzvd.clearSavedProgress(getActivity(), null);
        //Jzvd.releaseAllVideos();

        Glide.with(mContext)
                .load(thumb)
                .override(20,20)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(temp_layer);

        Glide.with(mContext)
                .load(thumb)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(temp_layer);


        jzvdStd = (JzvdStd) view.findViewById(R.id.player_view);
        jzvdStd.bottomProgressBar.setVisibility(View.GONE);
        jzvdStd.setVisibility(View.VISIBLE);
        tv_replay.setVisibility(View.GONE);
        temp_layer.setVisibility(View.GONE);
        jzvdStd.setUp(url,
                "",
                0);
        jzvdStd.startVideo();
        jzvdStd.setStart(new OnStart() {
            @Override
            public void OnStart() {
                temp_layer.setVisibility(View.GONE);
                jzvdStd.onClickUiToggle();
                jzvdStd.bottomProgressBar.setVisibility(View.VISIBLE);
                jzvdStd.startDismissControlViewTimer();
            }
        });


        jzvdStd.setListener(new OnComplete() {
            @Override
            public void OnComplete() {
                temp_layer.setVisibility(View.VISIBLE);
                jzvdStd.setVisibility(View.GONE);
                tv_replay.setVisibility(View.VISIBLE);
            }
        });

        jzvdStd.setOnTouchListener(new OnSwipeTouchListener(mContext) {

            public void onSwipeRight() {
                if(!video_full_screen && !is_advert_visible) {
                    jzvdStd.bottomProgressBar.setVisibility(View.GONE);
                    initPrev();
                }
            }
            public void onClick() {
                jzvdStd.onClickUiToggle();
                jzvdStd.startDismissControlViewTimer();
            }

            public void onSwipeLeft() {
                jzvdStd.bottomProgressBar.setVisibility(View.GONE);
                if(!video_full_screen && !is_advert_visible) {
                    initNext();
                }
            }

        });

        temp_layer.setOnTouchListener(new OnSwipeTouchListener(mContext) {

            public void onSwipeRight() {
                if(!video_full_screen && !is_advert_visible) {
                    jzvdStd.bottomProgressBar.setVisibility(View.GONE);
                    initPrev();

                }
            }
            public void onClick() {
                jzvdStd.onClickUiToggle();
                jzvdStd.startDismissControlViewTimer();

//                if (is_controller_visible) {
//                    jzvdStd.startDismissControlViewTimer();
//                    simpleExoPlayerView.hideController();
//                } else {
//                    simpleExoPlayerView.showController();
//                }
            }

            public void onSwipeLeft() {
                if(!video_full_screen && !is_advert_visible) {
                    jzvdStd.bottomProgressBar.setVisibility(View.GONE);
                    initNext();
                }
            }

        });

        //JZMediaManager.instance().jzMediaInterface.setVolume(0f, 0f);

    }




    //change
    private void set_up_values(JSONObject response) {
        try {
            actorsList.clear();
            treatment.clear();
            campaigns_list.clear();
            if (!response.isNull("campaigns")) {
                JSONArray campaigns = response.getJSONArray("campaigns");
                for (int i = 0; i < campaigns.length(); i++) {
                    JSONObject in = campaigns.getJSONObject(i);

                    String thumbnail_url = in.optString("thumbnail_url");
                    campaigns_list.add(thumbnail_url);
                    if (i == 0) {
                        Glide.with(mContext)
                                .load(thumbnail_url)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                    }
                }
            }
            if (!response.isNull("individual_condition_text")) {
                condition_pre_txt = response.optString("individual_condition_text");
                treatment_pre_txt = response.optString("treatment_text");
            }
            JSONArray conditions = response.getJSONArray("conditions_android");
            for (int i = 0; i < conditions.length(); i++) {


                if (current_condition == i) {
                    JSONObject in = conditions.getJSONObject(i);
                    int treatment_options_count = in.optInt("treatment_options_count");
                    int condition_videos_count = in.optInt("presentations_count");
                    condition_name = " " + in.optString("name");


                    String temp = condition_name;
                    if (temp.length() > 27) {
                        temp = temp.substring(0, 27)+ "...";

                    }


                    treatment_option_txt.setText("" + treatment_options_count);
                    condition_video_txt.setText("" + condition_videos_count);
                    SpannableString spannableString_2 = new SpannableString(condition_pre_txt + temp);
                    ClickableSpan clickableSpan_2 = new ClickableSpan() {
                        @Override
                        public void onClick(View textView) {

                        }

                        @Override
                        public void updateDrawState(final TextPaint textPaint) {
                            textPaint.setColor(getResources().getColor(R.color.white));
                            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                            textPaint.setTypeface(usefullData.get_montserrat_bold());
                            textPaint.setUnderlineText(false);
                        }
                    };
                    spannableString_2.setSpan(clickableSpan_2, condition_pre_txt.length(),
                            condition_pre_txt.length() + temp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    header_text.setText(spannableString_2, TextView.BufferType.SPANNABLE);



//                    if (!in.isNull("videos")) {
//                        JSONArray videos = in.getJSONArray("videos");
//                        for (int j = 0; j < videos.length(); j++) {
//                            JSONObject videos_in = videos.getJSONObject(j);
//                            Actors actor = new Actors();
//
//                            String thumbnail_url = videos_in.optString("thumbnail_url_medium");
//                            String url = videos_in.optString("url");
//                            String name = videos_in.optString("name");
//                            String duration = videos_in.optString("duration");
//                            int id = videos_in.optInt("id");
//
//
//                            actor.setImage(thumbnail_url);
//                            actor.setdescription(url);
//                            actor.setItem_id(id);
//                            actor.setcomnt(usefullData.capitalize(name));
//                            actor.setpic_id(duration);
//
//                            all_video_urls.add(url);
//
//                            actorsList.add(actor);
//                        }
//                    }


                    if (!in.isNull("presentations")) {
                        JSONArray presentations = in.getJSONArray("presentations");
                        for (int j = 0; j < presentations.length(); j++) {
                            JSONObject videos_in = presentations.getJSONObject(j);
                            Actors actor = new Actors();

                            String thumbnail_url = videos_in.optString("presentation_image_url");
//                            String url = videos_in.optString("url");
                            String name = videos_in.optString("title");
                            String duration = videos_in.optString("sub_title");
                            int id = videos_in.optInt("id");


                            actor.setImage(thumbnail_url);
//                          actor.setdescription(url);
                            actor.setItem_id(id);

                            actor.setcomnt(usefullData.capitalize(name.trim()));
                            actor.setpic_id(duration);

//                            all_video_urls.add(url);

                            actorsList.add(actor);
                        }
                    }
                    if (!in.isNull("condition_images_android")) {
                        JSONArray condition_images_android = in.getJSONArray("condition_images_android");
                        for (int k = 0; k < condition_images_android.length(); k++) {
                            JSONObject treatment_options_in = condition_images_android.getJSONObject(k);
                            Actors actor = new Actors();
                            String thumbnail_url = treatment_options_in.optString("thumbnail_url");
                            String thumbnail_url_large = treatment_options_in.optString("thumbnail_url_large");
                            String thumbnail_url_blurred = treatment_options_in.optString("thumbnail_url_blurred");
                            String name = treatment_options_in.optString("name");

                            actor.setcreated_date(thumbnail_url);
                            actor.setpicture(thumbnail_url_large);
                            actor.settitle(thumbnail_url_blurred);
                            actor.setid("is_image");
                            actor.setItem_id(-1);
                            actor.setImage(thumbnail_url);
                            actor.setcomnt(usefullData.capitalize(name.trim()));
                            actor.setpic_id("");
                            Glide.with(mContext)
                                    .load(thumbnail_url_large)
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);

                            condition_image.add(actor);
                            actorsList.add(actor);

                        }
                    }
                    if (!in.isNull("treatment_options")) {

                        JSONArray treatment_options = in.getJSONArray("treatment_options");
                        for (int k = 0; k < treatment_options.length(); k++) {
                            JSONObject treatment_options_in = treatment_options.getJSONObject(k);
                            Actors actor = new Actors();
                            String thumbnail_url = treatment_options_in.optString("thumbnail_url");
                            String thumbnail_url_large = treatment_options_in.optString("thumbnail_url_large");
                            String thumbnail_url_blurred = treatment_options_in.optString("thumbnail_url_blurred");
                            String name = treatment_options_in.optString("name");
                            String sub_title = treatment_options_in.optString("sub_title");
                            int id=treatment_options_in.optInt("id");

//                            if (!treatment_options_in.isNull("risk_and_benifits_title")) {
//                                String risk_and_benifits_title = treatment_options_in.optString("risk_and_benifits_title", null);
//                                actor.setlikes(risk_and_benifits_title);
//                            }
//                            if (!treatment_options_in.isNull("risk_and_benifits_description")) {
//                                String risk_and_benifits_description = treatment_options_in.optString("risk_and_benifits_description", null);
//                                actor.setusername(risk_and_benifits_description);
//                            }
//                            if (!treatment_options_in.isNull("longevity_title")) {
//                                String longevity_title = treatment_options_in.optString("longevity_title", null);
//                                actor.setdata(longevity_title);
//                            }
//                            if (!treatment_options_in.isNull("longevity_description")) {
//                                String longevity_description = treatment_options_in.optString("longevity_description", null);
//                                actor.setcomnt(longevity_description);
//                            }
//                            if (!treatment_options_in.isNull("cost_title")) {
//                                String cost_title = treatment_options_in.optString("cost_title", null);
//                                actor.setdescription(cost_title);
//                            }
//                            if (!treatment_options_in.isNull("cost_description")) {
//                                String cost_description = treatment_options_in.optString("cost_description", null);
//                                actor.setImage(cost_description);
//                            }


                            actor.setcreated_date(thumbnail_url);
                            actor.setpicture(thumbnail_url_large);
                            actor.settitle(thumbnail_url_blurred);
                            actor.setlikes2(usefullData.capitalize(name.trim()));
                            actor.setpic_id(sub_title);
                            actor.setid("is_image");
                            actor.setItem_id(id);

                            Glide.with(mContext)
                                    .load(thumbnail_url_large)
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);

//                            image_viewer.add(thumbnail_url_large);

                            treatment.add(actor);


                            listDataHeader.add(name);
                            listDataHeaderImage.add(thumbnail_url);
                            listDataId.add(id);


                        }
                    }
//                    if(!in.isNull("dynamic_statistics")) {
//                        JSONArray dynamic_statistics = in.getJSONArray("dynamic_statistics");
//                        for (int k = 0; k < dynamic_statistics.length(); k++) {
//                            JSONObject treatment_options_in = dynamic_statistics.getJSONObject(k);
//
//                            Actors actor = new Actors();
//                            String thumbnail_url = treatment_options_in.optString("thumbnail_url");
//                            String thumbnail_url_large = treatment_options_in.optString("thumbnail_url_large");
//                            String thumbnail_url_blurred = treatment_options_in.optString("thumbnail_url_blurred");
//
//                            actor.setcreated_date(thumbnail_url);
//                            actor.setpicture(thumbnail_url_large);
//                            actor.settitle(thumbnail_url_blurred);
//                            actor.setid("is_gif");
//
//                            Glide.with(mContext)
//                                    .load(thumbnail_url_large)
//                                    .asBitmap()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
//
////                            image_viewer.add(thumbnail_url_large);
//                            treatment.add(actor);
//                        }
//                    }
                }
            }

            if (!campaigns_list.isEmpty()) {
                Collections.shuffle(campaigns_list);
            }
            start_campaigns();
            if (actorsList.size() > 7) {
                bottom_scroll_condition.setVisibility(View.VISIBLE);
            } else {
                bottom_scroll_condition.setVisibility(View.GONE);
            }

            //setScrollbuttonVisibility(treatment.size());


//            if (treatment.size() < 6) {
//                bottom_scroll_treatment.setVisibility(View.GONE);
//            }
//            switch_player_or_treatment(false);
//            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (treatment.isEmpty()) {
//            not_found.setVisibility(View.VISIBLE);
//            b_list.setVisibility(View.GONE);
//        }else {
//            not_found.setVisibility(View.GONE);
//            b_list.setVisibility(View.VISIBLE);
//        }
//
        if (actorsList.isEmpty()) {
            not_found_data.setVisibility(View.VISIBLE);
            not_found_data.setText(getResources().getString(R.string.no_condtion));
            condition_mRecyclerView.setVisibility(View.GONE);
        } else {
            not_found_data.setVisibility(View.GONE);
            condition_mRecyclerView.setVisibility(View.VISIBLE);
        }


    }

//    public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {
//
//        private ArrayList<Actors> items;
//
//        public AdapterList(ArrayList<Actors> items) {
//            this.items = items;
//        }
//
//        public  class ViewHolder extends RecyclerView.ViewHolder {
//            public RoundedImageView imageview;
//            public TextView title;
//            public ImageView thumb;
//            public LinearLayout click_here;
//
//            private ViewHolder(View view) {
//                super(view);
//                imageview = (RoundedImageView) view.view.findViewById(R.id.thumbnail);
//                title = (TextView) view.view.findViewById(R.id.title);
//                click_here = (LinearLayout) view.view.findViewById(R.id.video_click_here);
//            }
//        }
//
//        @Override
//        public AdapterList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_one_condition, null);
//            AdapterList.ViewHolder viewHolder = new AdapterList.ViewHolder(v);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(final AdapterList.ViewHolder holder, final int position) {
//
//            //            holder.imageview.setImageURI(actorList.get(position).getImage());
//
////            MultiTransformation multi = new MultiTransformation(
////                    new BlurTransformation(25),
////                    new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL));
////            Glide.with(mContext).load(items.get(position).getImage())
////                    .apply(bitmapTransform(new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL)))
////                    .into(holder.imageview);
////            Picasso.with(mContext)
////                    .load(items.get(position).getImage())
////                    .placeholder(R.drawable.profile)
////                    .error(R.drawable.profile)
////                    .transform(new RoundedTransformation(50, 4))
////                    .resizeDimen(100, 100)
////                    .centerCrop()
////                    .into(holder.imageview);
//
//            Glide.with(mContext)
//                    .load(items.get(position).getImage())
//                    .placeholder(R.mipmap.loader_background)
//                    .error(R.mipmap.loader_background)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.imageview);
//            holder.title.setText(items.get(position).getcomnt());
//            holder.title.setTypeface(usefullData.get_montserrat_regular());
////            if(items.get(position).getItem_id()==-1){
////                holder.thumb.setVisibility(View.INVISIBLE);
////            }else {
////                holder.thumb.setVisibility(View.VISIBLE);
////            }
////            if(position==0){
////                holder.padding_layout.setVisibility(View.VISIBLE);
////            }else {
////                holder.padding_layout.setVisibility(View.GONE);
////            }
//
//            holder.click_here.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if(items.get(position).getItem_id()==-1){
//                        switch_player_or_treatment(false);
//                        Glide.with(mContext)
//                                .load(items.get(position).gettitle())
//                                .asBitmap()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                        // Do something with bitmap here.
//                                        advert_and_treatments.setImageBitmap(bitmap);
//
//                                        Glide.with(mContext)
//                                                .load(actorsList.get(position).getpicture())
//                                                .asBitmap()
//                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                                .into(new SimpleTarget<Bitmap>() {
//                                                    @Override
//                                                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                                        // Do something with bitmap here.
//                                                        advert_and_treatments.setImageBitmap(bitmap);
//
//                                                    }
//                                                });
//                                    }
//                                });
//
//                    }else {
//                        switch_player_or_treatment(true);
//                        initPlayer(actorsList.get(position).getdescription());
//
//                    }
//
////                    if(usefullData.isNetworkConnected()){
////                        if(actorsList.get(position).getItem_id()==-1){
////                            Intent edit = new Intent(mContext, Media_viewer.class);
////                            TabGroupActivity parentActivity = (TabGroupActivity) mContext;
////                            edit.putExtra("current_index",position-(actorsList.size()-condition_image.size()));
////                            edit.putExtra("request","Condition Image Viewed");
////                            parentActivity.startChildActivity("viewer_Activity", edit);
////                        }else {
////                            Intent edit = new Intent(mContext, CustomPlayerControlActivity.class);
////                            edit.putExtra("current_video_id", actorsList.get(position).getItem_id());
////                            edit.putStringArrayListExtra("campaigns_list", campaigns_list);
////                            edit.putExtra("sky_news", false);
////                            edit.putExtra("request_from", "condition");
////                            edit.putExtra("track_name",actorsList.get(position).getcomnt());
////                            edit.putStringArrayListExtra("all_video_urls", all_video_urls);
////                            edit.putExtra("current_postion", position);
////                            startActivity(edit);
////                        }
////                    }else {
////                        usefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);
////                    }
//
//                }
//
//
//            });
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//
//    }
//    public class AdapterHorizontalList extends RecyclerView.Adapter<AdapterHorizontalList.ViewHolder> {
//
//        private ArrayList<Actors> items;
//
//        public AdapterHorizontalList(ArrayList<Actors> items) {
//            this.items = items;
//        }
//
//        public  class ViewHolder extends RecyclerView.ViewHolder {
//            public RoundedImageView imageview;
//            public TextView title;
//            public LinearLayout click_here;
//
//            private ViewHolder(View view) {
//                super(view);
//                imageview = (RoundedImageView) view.view.findViewById(R.id.thumbnail);
//                title=(TextView) view.view.findViewById(R.id.title);
//                click_here = (LinearLayout) view.view.findViewById(R.id.video_click_here);
//            }
//        }
//
//        @Override
//        public AdapterHorizontalList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_one_condition, null);
//            AdapterHorizontalList.ViewHolder viewHolder = new AdapterHorizontalList.ViewHolder(v);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(final AdapterHorizontalList.ViewHolder holder, final int position) {
//
////            Glide.with(mContext)
////                    .load(items.get(position).gettitle())
////                    .asBitmap()
////                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                    .into(new SimpleTarget<Bitmap>() {
////                        @Override
////                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
////                            // Do something with bitmap here.
////                            holder.imageview.setImageBitmap(bitmap);
////
////                            Glide.with(mContext)
////                                    .load(items.get(position).getcreated_date())
////                                    .asBitmap()
////                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                                    .into(new SimpleTarget<Bitmap>() {
////                                        @Override
////                                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
////                                            // Do something with bitmap here.
////                                            holder.imageview.setImageBitmap(bitmap);
////
////                                        }
////                                    });
////                        }
////                    });
////            Glide.with(mContext)
////                    .load(items.get(position).getcreated_date())
////                    .placeholder(R.mipmap.loader_background)
////                    .error(R.mipmap.loader_background)
////                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                    .into(holder.imageview);
////            MultiTransformation multi = new MultiTransformation(
////                    new BlurTransformation(25),
////                    new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL));
////            Glide.with(mContext).load(items.get(position).getcreated_date())
////                    .apply(bitmapTransform(multi))
////                    .into(holder.imageview);
//            Glide.with(mContext).load(items.get(position).getcreated_date())
////                    .apply(bitmapTransform(new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL)))
//                    .into(holder.imageview);
//            holder.title.setText(items.get(position).getlikes2());
//            holder.title.setTypeface(usefullData.get_montserrat_regular());
////            if(position==0){
////                holder.padding_layout.setVisibility(View.VISIBLE);
////            }else {
////                holder.padding_layout.setVisibility(View.GONE);
////            }
//
//            holder.click_here.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    switch_player_or_treatment(false);
//                    Glide.with(mContext)
//                            .load(items.get(position).gettitle())
//                            .asBitmap()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                    // Do something with bitmap here.
//                                    advert_and_treatments.setImageBitmap(bitmap);
//
//                                    Glide.with(mContext)
//                                            .load(items.get(position).getpicture())
//                                            .asBitmap()
//                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                            .into(new SimpleTarget<Bitmap>() {
//                                                @Override
//                                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                                    // Do something with bitmap here.
//                                                    advert_and_treatments.setImageBitmap(bitmap);
//
//                                                }
//                                            });
//                                }
//                            });
//
//
//                }
//            });
//
////            holder.click_here.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    // TODO Auto-generated method stub
////                    holder.click_here.performClick();
////                    return true;
////                }
////            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//
//    }


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
        if(is_screen_visible) {
            Activity activity = getActivity();
            if (activity != null) {
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                // Do something with bitmap here.
//                                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//                                advert.setBackgroundDrawable(drawable);
                                advert.setImageBitmap(bitmap);
                                Log.e("GalleryAdapter", "Glide getLarge ");
                            }
                        });
            }
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
        timer = new CounterClass(21000, 1000);
        timer.start();   //Start the timer
        isRunning = true;
        set_addons(temp_addons_list.get(count));


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
            Log.e("Timer condition-->", "" + time_left);
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
                if(!usefullData.indexExists(temp_addons_list,current_add_index)){
                    current_add_index=0;
                }

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
//                BitmapFactory.Options o = new BitmapFactory.Options();
//                o.inJustDecodeBounds = true;
                // Convert the BufferedInputStream to a Bitmap
//                Bitmap bMap = BitmapFactory.decodeStream(buf, null, o);
                Bitmap bMap = BitmapFactory.decodeStream(buf);

//                mBitmapInsurance = BitmapFactory.decodeFile(mCurrentPhotoPath,options);

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
        advert.setBackgroundDrawable(drawable);
    }

    public void switch_player_or_treatment(boolean player_visible) {
        if (player_visible) {
            treatments_layer.setVisibility(View.GONE);
            mk_player_layout.setVisibility(View.VISIBLE);
        } else {
            treatments_layer.setVisibility(View.VISIBLE);
            mk_player_layout.setVisibility(View.GONE);

        }
        advert.setVisibility(View.GONE);
//        if (isRunning) {
//            timer.cancel();
//            isRunning = false;
//            advert.setBackground(null);
//
//        }
    }

    public void switch_advert() {

        treatments_layer.setVisibility(View.GONE);
        mk_player_layout.setVisibility(View.GONE);
        advert.setVisibility(View.VISIBLE);
        circles.setVisibility(View.GONE);
    }

    public void handle_volume_btn(int current_volume) {


        if (getActivity() != null) {
            switch (current_volume) {
                case 0:
                    low_vol.setAlpha(.5f);
                    low_vol.setEnabled(false);
                    high_vol.setAlpha(1f);
                    high_vol.setEnabled(true);
                    mute.setText(getResources().getString(R.string.ic_mute));
                    break;

                case 15:
                    high_vol.setAlpha(.5f);
                    high_vol.setEnabled(false);
                    low_vol.setAlpha(1f);
                    low_vol.setEnabled(true);
                    mute.setText(getResources().getString(R.string.ic_unmute));
                    break;

                default:
                    low_vol.setAlpha(1f);
                    low_vol.setEnabled(true);
                    high_vol.setAlpha(1f);
                    high_vol.setEnabled(true);
                    try {
                        mute.setText(getResources().getString(R.string.ic_unmute));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            timer.cancel();
            isRunning = false;
        }
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }


    }

    private void releasePlayer() {
//        if (exo_player != null) {
//            shouldAutoPlay = exo_player.getPlayWhenReady();
//            exo_player.release();
//            exo_player = null;
//            trackSelector = null;
//
//
//        }
        if(jzvdStd!=null){
            jzvdStd.release();
        }
    }

    public class condition_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> data_items;

        LayoutInflater vi;
        int Resource;
        ViewHolder holder;


        public condition_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            data_items = objects;

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                holder.imageview = (ImageView) v.findViewById(R.id.thumbnail);
                holder.title = (TextView) v.findViewById(R.id.title);
                holder.duration = (TextView) v.findViewById(R.id.duration);

                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            Glide.with(mContext).load(data_items.get(position).getImage())
                    .placeholder(R.drawable.loader_background)
//                    .error(R.mipmap.loader_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageview);
            holder.title.setText(data_items.get(position).getcomnt());
            holder.title.setTypeface(usefullData.get_montserrat_semibold());
            holder.duration.setText(data_items.get(position).getpic_id());
            holder.duration.setTypeface(usefullData.get_montserrat_regular());

            if (data_items.get(position).getpic_id().equalsIgnoreCase("") || data_items.get(position).getpic_id().equalsIgnoreCase("null")) {
                holder.duration.setVisibility(View.GONE);
            } else {
                holder.duration.setVisibility(View.VISIBLE);
            }


            return v;

        }

        class ViewHolder {
            public ImageView imageview;
            public TextView title, duration;
            public ImageView thumb;

        }


    }






    //gaurav

    public class treatment_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> t_items;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;

        public treatment_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            t_items = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
                holder.imageview = (ImageView) v.findViewById(R.id.thumbnail);
                holder.title = (TextView) v.findViewById(R.id.title);
                holder.duration = (TextView) v.findViewById(R.id.duration);

                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            Glide.with(mContext).load(t_items.get(position).getcreated_date())
                    .placeholder(R.drawable.loader_background)
//                    .error(R.mipmap.loader_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageview);
            holder.title.setText(t_items.get(position).getlikes2());
            holder.title.setTypeface(usefullData.get_montserrat_regular());
            holder.duration.setTypeface(usefullData.get_montserrat_regular());

            if (t_items.get(position).getpic_id().equalsIgnoreCase("null") || t_items.get(position).getpic_id().equalsIgnoreCase("")) {
                holder.duration.setVisibility(View.GONE);
            } else {
                holder.duration.setVisibility(View.VISIBLE);
                holder.duration.setText(t_items.get(position).getpic_id());
            }


            return v;

        }

        class ViewHolder {
            public ImageView imageview;
            public TextView title, duration;


        }

    }

    public void player_full_screen(boolean full_screens) {
        if (full_screens) {
            video_full_screen = true;
            //Tab_activity.ll_bottombar.setVisibility(View.GONE);
            full_screen.setText(getResources().getString(R.string.ic_zoom_in));
            header_view.setVisibility(View.GONE);
            treatment_view.setVisibility(View.GONE);
            video_view.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            video_view.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) video_view.getLayoutParams();
            int v_m = getResources().getDimensionPixelSize(R.dimen._32sdp);
            params.setMargins(0, 0, 0, v_m);
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) video_view.getLayoutParams();
            video_view.setLayoutParams(params);
            video_view.requestLayout();
            circles.setVisibility(View.GONE);
            if(jzvdStd!=null){
                jzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
            }
        } else {
            video_full_screen = false;
            //Tab_activity.ll_bottombar.setVisibility(View.VISIBLE);
            full_screen.setText(getResources().getString(R.string.ic_zoom_out));
            header_view.setVisibility(View.VISIBLE);
            treatment_view.setVisibility(View.VISIBLE);
            int v_w = getResources().getDimensionPixelSize(R.dimen._335sdp);
            int v_h = getResources().getDimensionPixelSize(R.dimen._188sdp);
            int v_m = getResources().getDimensionPixelSize(R.dimen._5sdp);
            video_view.getLayoutParams().height = v_h;
            video_view.getLayoutParams().width = v_w;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) video_view.getLayoutParams();
            params.setMargins(v_m, 0, v_m, v_m);
            video_view.setLayoutParams(params);
            video_view.requestLayout();
            circles.setVisibility(View.VISIBLE);
            if(jzvdStd!=null){
                jzvdStd.setVideoImageDisplayType(Jzvd.CURRENT_STATE_AUTO_COMPLETE);
            }
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

    private void initNext() {

        if (usefullData.isNetworkConnected()) {
            if(presentation_List.size() > 1) {
                current_presentation_position++;
                if (usefullData.indexExists(presentation_List, current_presentation_position)) {
                    set_slider_view(current_presentation_position);

                } else {
                    current_presentation_position = 0;
                    set_slider_view(current_presentation_position);

                }
            }
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }
    }

    private void initPrev() {

        if (usefullData.isNetworkConnected()) {
            if(presentation_List.size()>1) {
                current_presentation_position--;
                if (usefullData.indexExists(presentation_List, current_presentation_position)) {
                    set_slider_view(current_presentation_position);

                } else {
                    current_presentation_position = presentation_List.size() - 1;
                    set_slider_view(current_presentation_position);

                }
            }
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }
    }


    private void buildCircles(){

        int padding = getResources().getDimensionPixelSize(R.dimen._2sdp);
        int h = getResources().getDimensionPixelSize(R.dimen._5sdp);
        circles.removeAllViews();
        for(int i = 0 ; i < presentation_List.size() ; i++){
            Button circle = new Button(getActivity());
            circle.setBackground(getResources().getDrawable(R.drawable.grey_circle_bg));
            circle.setLayoutParams(new ViewGroup.LayoutParams(h, h));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(h,h);
            params.setMargins(padding, 0, padding, 0);
            circle.setLayoutParams(params);
            circles.addView(circle);
        }
        circles.setVisibility(View.VISIBLE);
        setIndicator(0);
    }
    private void setIndicator(int index){
        if(index < presentation_List.size() && presentation_List.size()>1){
            for(int i = 0 ; i < presentation_List.size() ; i++){
                Button circle = (Button) circles.getChildAt(i);
                if(i == index){
                    circle.setBackground(getResources().getDrawable(R.drawable.grident_circle_bg));
                }else {
                    circle.setBackground(getResources().getDrawable(R.drawable.grey_circle_bg));
                }
            }
        }
    }


    private void get_presentation(String url, int id) {

        if (usefullData.isNetworkConnected()) {

//            usefullData.showProgress();

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", Definitions.VERSION);
            headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
            headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

            UserAPI.get_JsonObjResp(url+"/"+id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("TAG response", response.toString());
                            set_up_values_presentation(response, id);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    if(response.statusCode == 500){
                                        usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                    }
                                }
                                else{
                                    usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                                }
                            }catch (Exception e){

                            }
//                            usefullData.dismissProgress();


                        }
                    });
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }
    }

    private void set_up_values_presentation(JSONObject response, int idPresentation) {
        isTreatmentShown = false;
        isConditionShown = true;
        barPreview.setVisibility(View.GONE);
        int id = 0;
        try {
            if (!response.isNull("result")) {
                presentation_List.clear();
                JSONArray presentations = response.getJSONArray("result");
                for (int j = 0; j < presentations.length(); j++) {
                    JSONObject videos_in = presentations.getJSONObject(j);

                    if (videos_in.optString("media_url").contains("http:")) {
                        Actors actor = new Actors();

                        String media_url = videos_in.optString("media_url");
                        String content_type = videos_in.optString("content_type");
                        String blurred = videos_in.optString("blurred");
                        id = videos_in.optInt("id");
                        String name = videos_in.optString("name");
                        String video_thumbnail = videos_in.optString("video_thumbnail");

                        actor.setpicture(media_url);
                        actor.setItem_id(id);
                        actor.setpic_id(content_type);
                        actor.settitle(blurred);
                        actor.setusername(name);
                        actor.setcreated_date(video_thumbnail);

                        presentation_List.add(actor);
                    }
                    if (presentation_List.size() > 1) {
                        buildCircles();
                    } else {
                        circles.removeAllViews();
                    }

                }
                if (presentation_List.isEmpty()) {

                    usefullData.make_alert("There is no item in this presentation", false, getActivity());

                } else {

                    current_presentation_position = 0;
                    set_slider_view(current_presentation_position);
                }

            } else {
                usefullData.make_alert("There is no item in this presentation", false, getActivity());
            }


            // presentation_treatment_option_list
            if (!response.isNull("treatment_options")) {
                JSONArray presentations_treatment_option = response.getJSONArray("treatment_options");
                listDataHeader.clear();
                listDataHeaderImage.clear();
                listDataId.clear();
                if (presentations_treatment_option.length() != 0) {
                    layoutTop3Bar.setVisibility(View.VISIBLE);
                    for (int j = 0; j < presentations_treatment_option.length(); j++) {
                        JSONObject treatmentList = presentations_treatment_option.getJSONObject(j);
                        if (presentations_treatment_option.length() == 1) {
                            if (j == 0) {
                                llBarElement1.setVisibility(View.VISIBLE);
                                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                barElement1.setText(treatmentList.optString("name"));
                                llBarElement2.setVisibility(View.GONE);
                                llBarElement3.setVisibility(View.GONE);
                                if(isGroup1Clicked){
                                    //llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                }
                            }
                        } else if (presentations_treatment_option.length() == 2) {
                            if (j == 0) {
                                llBarElement1.setVisibility(View.VISIBLE);
                                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                barElement1.setText(treatmentList.optString("name"));
                                if(isGroup1Clicked){
                                    // llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                }
                            }
                            if (j == 1) {
                                llBarElement2.setVisibility(View.VISIBLE);
                                llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                barElement2.setText(treatmentList.optString("name"));
                                if(isGroup2Clicked){
                                    // llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                }
                            }
                            llBarElement3.setVisibility(View.GONE);
                        } else if (presentations_treatment_option.length() >= 3) {
                            if (j == 0) {
                                llBarElement1.setVisibility(View.VISIBLE);
                                llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                barElement1.setText(treatmentList.optString("name"));
                                if(isGroup1Clicked){
                                    //llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                }
                            }
                            if (j == 1) {
                                llBarElement2.setVisibility(View.VISIBLE);
                                llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                barElement2.setText(treatmentList.optString("name"));
                                if(isGroup2Clicked){
                                    //llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                }
                            }
                            if (j == 2) {
                                llBarElement3.setVisibility(View.VISIBLE);
                                llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                barElement3.setText(treatmentList.optString("name"));
                                if(isGroup3Clicked){
                                    // llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                }
                            }
                        }

                        listDataHeader.add(treatmentList.optString("name"));
                        listDataHeaderImage.add(treatmentList.optString("thumbnail_url"));
                        listDataId.add(treatmentList.optInt("id"));
                    }
                }
            } else {
                layoutTop3Bar.setVisibility(View.INVISIBLE);
            }


            // presentation_treatment_count
            if (!response.isNull("treatment_options_count")) {
                treatment_option_txt.setText("" + response.optInt("treatment_options_count"));
            } else {
                treatment_option_txt.setText("" + "0");
            }

//                 listSubDataHeader = new ArrayList<>() ;
//                 listSubDataId = new ArrayList<>();

//                treatmentExpandableAdapter = new TreatmentExpandableAdapter(getActivity(), listDataHeader, listDataId, listSubDataHeader, listSubDataId, this);
//                treatment_mRecyclerView.setAdapter(treatmentExpandableAdapter);

            treatmentExpandableAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
//        usefullData.dismissProgress();

    }


    private void get_presentationForSubTreatment(String url, int id, int groupCount) {
        //barPreview.setVisibility(View.VISIBLE);
        listSubDataHeader.clear();
        listSubDataHeaderImage.clear();
        listSubDataId.clear();
        isTreatmentShown = true;

        if (usefullData.isNetworkConnected()) {

//            usefullData.showProgress();

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", Definitions.VERSION);
            headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
            headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

            UserAPI.get_JsonObjResp(url+"/"+id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //treatment click treatment Expandable View
                            try {

                                if (!response.isNull("sub_treatment_options")) {
                                    try {
                                        JSONArray presentations_treatment_option = response.getJSONArray("sub_treatment_options");
                                        if (presentations_treatment_option.length() != 0) {
                                            isConditionShown =false;
                                            layoutTop3Bar.setVisibility(View.VISIBLE);
                                            for (int j = 0; j < presentations_treatment_option.length(); j++) {
                                                JSONObject treatmentList = presentations_treatment_option.getJSONObject(j);
                                                String name = "";
                                                try {
                                                    name = new String(treatmentList.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                if (presentations_treatment_option.length() == 1) {
                                                    if (j == 0) {
                                                        llBarElement1.setVisibility(View.VISIBLE);
                                                        llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                                        barElement1.setText(Html.fromHtml(name).toString());
                                                        llBarElement2.setVisibility(View.GONE);
                                                        llBarElement3.setVisibility(View.GONE);
                                                        childId1 = treatmentList.optInt("id");
                                                        childIdName1 = treatmentList.optString("name");
                                                        if(isElement1){
                                                            llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                                        }
                                                    }
                                                }
                                                else if (presentations_treatment_option.length() == 2) {
                                                    if (j == 0) {
                                                        llBarElement1.setVisibility(View.VISIBLE);
                                                        llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                                        barElement1.setText(Html.fromHtml(name).toString());
                                                        childId1 = treatmentList.optInt("id");
                                                        childIdName1 = treatmentList.optString("name");
                                                        if(isElement1){
                                                            llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                                        }
                                                    }
                                                    if (j == 1) {
                                                        llBarElement2.setVisibility(View.VISIBLE);
                                                        llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                                        barElement2.setText(Html.fromHtml(name).toString());
                                                        childId2 = treatmentList.optInt("id");
                                                        childIdName2 = treatmentList.optString("name");
                                                        if(isElement2){
                                                            llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                                        }
                                                    }
                                                    llBarElement3.setVisibility(View.GONE);
                                                }


                                                else if (presentations_treatment_option.length() >= 3) {
                                                    if (j == 0) {
                                                        llBarElement1.setVisibility(View.VISIBLE);
                                                        llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                                        barElement1.setText(Html.fromHtml(name).toString());
                                                        childId1 = treatmentList.optInt("id");
                                                        childIdName1 = treatmentList.optString("name");
                                                        if(isElement1){
                                                            llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                                        }
                                                    }
                                                    if (j == 1) {
                                                        llBarElement2.setVisibility(View.VISIBLE);
                                                        llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                                        barElement2.setText(Html.fromHtml(name).toString());
                                                        childId2 = treatmentList.optInt("id");
                                                        childIdName2 = treatmentList.optString("name");
                                                        if(isElement2){
                                                            llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                                        }
                                                    }
                                                    if (j == 2) {
                                                        llBarElement3.setVisibility(View.VISIBLE);
                                                        llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
                                                        barElement3.setText(Html.fromHtml(name).toString());
                                                        childId3 = treatmentList.optInt("id");
                                                        childIdName3 = treatmentList.optString("name");
                                                        if(isElement3){
                                                            llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_select));
                                                        }
                                                    }

                                                }
                                                listSubDataHeader.add(treatmentList.optString("name"));
                                                listSubDataHeaderImage.add(treatmentList.optString("video_thumbnail"));
                                                listSubDataId.add(treatmentList.optInt("id"));
                                            }

                                            //setScrollbuttonVisibility();
                                            treatmentExpandableAdapter.notifyDataSetChanged();
                                            setScrollbuttonVisibility(presentations_treatment_option.length() + groupCount);
                                        }else{
                                            layoutTop3Bar.setVisibility(View.INVISIBLE);
                                            usefullData.make_alert("No sub-treatment associated with this treatment", false, getActivity());
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }

                                if(response.isNull("sub_treatment_options")) {
                                    layoutTop3Bar.setVisibility(View.INVISIBLE);
                                    usefullData.make_alert("No sub-treatment associated with this treatment", false, getActivity());
                                }


                                //treatment list image swapping
                                if(!response.isNull("result")) {
                                    try{
                                        JSONArray treatmentList = response.getJSONArray("result");
                                        presentation_List.clear();
                                        for (int j = 0; j < treatmentList.length(); j++) {
                                            JSONObject videos_in = treatmentList.getJSONObject(j);

                                            if (videos_in.optString("media_url").contains("http:")) {
                                                Actors actor = new Actors();

                                                String media_url = videos_in.optString("media_url");
                                                String content_type = videos_in.optString("content_type");
                                                String blurred = videos_in.optString("blurred");
                                                int id = videos_in.optInt("id");
                                                String name = videos_in.optString("name");
                                                String video_thumbnail = videos_in.optString("video_thumbnail");

                                                actor.setpicture(media_url);
                                                actor.setItem_id(id);
                                                actor.setpic_id(content_type);
                                                actor.settitle(blurred);
                                                actor.setusername(name);
                                                actor.setcreated_date(video_thumbnail);

                                                presentation_List.add(actor);
                                                listDataSubHeadings.add(actor);

                                            }
                                            if (presentation_List.size() > 1) {
                                                buildCircles();
                                            } else {
                                                circles.removeAllViews();
                                            }
                                        }
                                        if (presentation_List.isEmpty()) {
                                            usefullData.make_alert("There is no item in this presentation", false, getActivity());
                                        } else {
                                            if(isElement1){
                                                setSnapanable(childIdName1);
                                                get_presentationForChild(Definitions.CHILD_GALLERY, childId1);
                                            }else if(isElement2){
                                                setSnapanable(childIdName2);
                                                get_presentationForChild(Definitions.CHILD_GALLERY, childId2);
                                            }else if(isElement3){
                                                setSnapanable(childIdName3);
                                                get_presentationForChild(Definitions.CHILD_GALLERY, childId3);
                                            }else if(isChild){
                                                isChildClick = false;
                                                setSnapanable(childIdOtherName);
                                                get_presentationForChild(Definitions.CHILD_GALLERY, childIdOther);
                                            } else {
                                                current_presentation_position = 0;
                                                set_slider_view(current_presentation_position);
                                            }
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                if(response.isNull("result")) {
                                    {
                                        usefullData.make_alert("There is no item in this presentation", false, getActivity());
                                    }
                                }



                                if (!response.isNull("dentist_works")) {
                                    try{
                                        JSONArray dentistList = response.getJSONArray("dentist_works");
                                        if(dentistList.length() != 0){
                                            barPreview.setVisibility(View.VISIBLE);
                                        }else{
                                            barPreview.setVisibility(View.GONE);
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                } else {
                                    barPreview.setVisibility(View.GONE);
                                    //usefullData.make_alert("There is no item in this presentation", false, getActivity());
                                }





                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    if(response.statusCode == 500){
                                        usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                    }
                                }
                                else{
                                    usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                                }
                            }catch (Exception e){

                            }
//                            usefullData.dismissProgress();


                        }
                    });
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }
    }


    private void get_presentationForChild(String url, int id) {
//        }
        if (usefullData.isNetworkConnected()) {

//            usefullData.showProgress();

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", Definitions.VERSION);
            headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
            headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

            UserAPI.get_JsonObjResp(url+"/"+id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //treatment click treatment Expandable View
                            if (!response.isNull("result")) {

                                //treatment list image swapping
                                try{
                                    JSONArray treatmentList = response.getJSONArray("result");
                                    presentation_List.clear();
                                    for (int j = 0; j < treatmentList.length(); j++) {
                                        JSONObject videos_in = treatmentList.getJSONObject(j);

                                        if (videos_in.optString("media_url").contains("http:")) {
                                            Actors actor = new Actors();

                                            String media_url = videos_in.optString("media_url");
                                            String content_type = videos_in.optString("content_type");
                                            String blurred = videos_in.optString("blurred");
                                            int id = videos_in.optInt("id");
                                            String name = videos_in.optString("name");
                                            String video_thumbnail = videos_in.optString("video_thumbnail");

                                            actor.setpicture(media_url);
                                            actor.setItem_id(id);
                                            actor.setpic_id(content_type);
                                            actor.settitle(blurred);
                                            actor.setusername(name);
                                            actor.setcreated_date(video_thumbnail);

                                            presentation_List.add(actor);

                                        }
                                        if (presentation_List.size() > 1) {
                                            buildCircles();
                                        } else {
                                            circles.removeAllViews();
                                        }
                                    }
                                    treatmentExpandableAdapter.notifyDataSetChanged();
                                    if (presentation_List.isEmpty()) {
                                        usefullData.make_alert("There is no item in this sub-treatment", false, getActivity());
                                    } else {
                                        current_presentation_position = 0;
                                        set_slider_view(current_presentation_position);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            } else {
                                usefullData.make_alert("There is no item in this sub-treatment", false, getActivity());
                                //draweeView.setImageResource;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    if(response.statusCode == 500){
                                        usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                    }
                                }
                                else{
                                    usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                                }
                            }catch (Exception e){

                            }
//                            usefullData.dismissProgress();


                        }
                    });
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }
    }


    private void get_presentationForPreview(String url, int id) {
        if (usefullData.isNetworkConnected()) {

//           usefullData.showProgress();

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", Definitions.VERSION);
            headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
            headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

            UserAPI.get_JsonObjResp(url+"/"+id, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //treatment click treatment Expandable View
                            if (!response.isNull("dentist_works")) {

                                //treatment list image swapping
                                try{
                                    JSONArray treatmentList = response.getJSONArray("dentist_works");
                                    presentation_List.clear();
                                    if(treatmentList.length() != 0){
                                        //barPreview.setVisibility(View.VISIBLE);
                                        for (int j = 0; j < treatmentList.length(); j++) {
                                            JSONObject videos_in = treatmentList.getJSONObject(j);
                                            Actors actor = new Actors();
                                            String media_url = videos_in.optString("media_url");
                                            String content_type = videos_in.optString("content_type");
                                            int id = videos_in.optInt("id");
                                            actor.setpicture(media_url);
                                            actor.setItem_id(id);
                                            actor.settitle(media_url);
                                            actor.setpic_id(content_type);
                                            presentation_List.add(actor);

                                            if (presentation_List.size() > 1) {
                                                buildCircles();
                                            } else {
                                                circles.removeAllViews();
                                            }
                                        }


                                        if (presentation_List.size() == 0) {
                                            usefullData.make_alert("There is no item in this portfolio", false, getActivity());
                                        } else {
                                            current_presentation_position = 0;
                                            set_slider_view(current_presentation_position);
                                        }
                                    }else{
                                        //barPreview.setVisibility(View.GONE);
                                        usefullData.make_alert("There is no item in this portfolio", false, getActivity());
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            } else {
                                barPreview.setVisibility(View.GONE);
                                usefullData.make_alert("There is no item in this portfolio", false, getActivity());
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try{
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    if(response.statusCode == 500){
                                        usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                    }
                                }
                                else{
                                    usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                                }
                            }catch (Exception e){

                            }
//                            usefullData.dismissProgress();


                        }
                    });
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }
    }

    private void set_slider_view(int position) {
        if (usefullData.isNetworkConnected()) {
            setIndicator(current_presentation_position);
            if(usefullData.indexExists(presentation_List,position)) {
                if (presentation_List.get(position).getpic_id().contains("video")) {
                    player_handler(position);
                } else {
                    releasePlayer();
                    //treatment_image.setImageResource(android.R.color.transparent);
                    switch_player_or_treatment(false);

//                    int width = 50, height = 50;
//                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                            .setResizeOptions(new ResizeOptions(width, height))
//                            .build();
//                    PipelineDraweeController controller = Fresco.newDraweeControllerBuilder()
//                            .setOldController(mDraweeView.getController())
//                            .setImageRequest(request)
//                            .build();
//                    mSimpleDraweeView.setController(controller);

                    try{

                        uriHigh = Uri.parse(presentation_List.get(position).getpicture());
                        uriLow = Uri.parse(presentation_List.get(position).gettitle());

                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setLowResImageRequest(ImageRequest.fromUri(uriLow))
                                .setImageRequest(ImageRequest.fromUri(uriHigh))
                                .setOldController(draweeView.getController())
                                .build();
                        draweeView.setController(controller);


                        edit_image.setVisibility(View.VISIBLE);

                        new BackTask().execute();
                        //draweeView.setImageURI(uri);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    RetainingDataSourceSupplier<CloseableReference<CloseableImage>> retainingSupplier = new RetainingDataSourceSupplier<>();
                    PipelineDraweeControllerBuilder builder = (PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder();
                    DraweeController controller = builder
                            .setLowResImageRequest(ImageRequest.fromUri(uriLow))
                            //.setImageRequest(ImageRequest.fromUri(uriHigh))
                            .setOldController(draweeView.getController())
                            .build();
                    draweeView.setController(controller);
                    builder.setDataSourceSupplier(retainingSupplier);
                    int width = 50, height = 50;
                    ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uriHigh)
                            .setResizeOptions(new ResizeOptions(width, height));
                    ImageRequest request = requestBuilder.build();
//
                    retainingSupplier.setSupplier(Fresco.getImagePipeline().getDataSourceSupplier(request, null, ImageRequest.RequestLevel.FULL_FETCH));



//                    Glide.with(mContext)
////                        .load("https://s3.eu-west-2.amazonaws.com/sydevideobucket/treatment_options/treatment_option_images/000/000/066/blurred/Jukepada7ec05c91b1.png")
//                            .load(presentation_List.get(position).gettitle())
//                            .asBitmap()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                    // Do something with bitmap here.
//                                    edit_image.setVisibility(View.GONE);
//                                    treatment_image.setImageBitmap(bitmap);
//
//                                    Glide.with(mContext)
//                                            .load(presentation_List.get(position).getpicture())
//                                            .asBitmap()
//                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                            .into(new SimpleTarget<Bitmap>() {
//                                                @Override
//                                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                                    // Do something with bitmap here.
//                                                    treatment_image.setImageBitmap(bitmap);
//                                                    edit_image.setVisibility(View.VISIBLE);
//                                                }
//                                            });
//                                }
//                            });

                }
            }
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);
        }
    }

    @Override
    public void onListItemClick(int childPos, int childId, String childName) {

    }

    public void player_handler(int position){

        if(usefullData.indexExists(presentation_List,position)) {
            File myFile = new File(Definitions.VIDEO_FOLDER + presentation_List.get(position).getusername() + "_" + presentation_List.get(position).getItem_id() + ".mp4");

            if (myFile.exists()) {
                if (usefullData.isValidVideo(Definitions.VIDEO_FOLDER + presentation_List.get(position).getusername() + "_" + presentation_List.get(position).getItem_id() + ".mp4")) {
                    initPlayer(Definitions.VIDEO_FOLDER + presentation_List.get(position).getusername() + "_" + presentation_List.get(position).getItem_id() + ".mp4",presentation_List.get(position).getcreated_date());
                    //usefullData.Mixpanel_events("Education Video Played", "Video Name", presentation_List.get(position).getusername());
                } else {
                    initPlayer(presentation_List.get(position).getpicture(),presentation_List.get(position).getcreated_date());
                    //usefullData.Mixpanel_events("Education Video Played", "Video Name", presentation_List.get(position).getusername());
                }
            } else {
                initPlayer(presentation_List.get(position).getpicture(),presentation_List.get(position).getcreated_date());
                //usefullData.Mixpanel_events("Education Video Played", "Video Name", presentation_List.get(position).getusername());
            }
        }
        switch_player_or_treatment(true);
    }


    private void pausePlayer(){
        exo_player.setPlayWhenReady(false);
        exo_player.getPlaybackState();
    }
    private void startPlayer(){
        exo_player.setPlayWhenReady(true);
        exo_player.getPlaybackState();
    }


    public void Pause_timer() {
        if (isRunning) {
            if (timer != null) {
                timer.cancel();
                timer = null;

                isRunning = false;
            }


        }

    }

    public void Resume() {
        if (!isRunning) {  //This method will execute only when timer is not running
            timer = new CounterClass(remainMilli, 1000); //resume timer from where it is paused
            timer.start();  //Start the timer
            isRunning = true;

        }
    }

    public void manage_timer(){
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (Jzvd.backPress()) {
            return;
        }
    }


    class BackTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(uriHigh.toString());
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //treatment_image.setVisibility(View.GONE);
            draweeView.setImageBitmap(image);
        }
    }



    public void iniExoPlayer(View view){
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view_exo);
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player_view_exo = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        simpleExoPlayerView.setPlayer(player_view_exo);

        player_view_exo.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player_view_exo.prepare(mediaSource);
    }


    /**
     *
     * @param url
     * @param fileName
     * gaurav downloading the file to save it in the memory
     */


    private void downloadFile(String url, String fileName) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "/Syde_conditions_video/");
            if (!root.exists()) {
                root.mkdirs();
            }
            myFile = new File(root, fileName);

//            if (myFile.exists()) {
//                myFile.delete();
//            }
            URL u = new URL(url);
            URLConnection ucon = u.openConnection();

            //this timeout affects how long it takes for the app to realize there's a connection problem
            ucon.setReadTimeout(TIMEOUT_CONNECTION);
            ucon.setConnectTimeout(TIMEOUT_SOCKET);


            //Define InputStreams to read from the URLConnection.
            // uses 3KB download buffer
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(myFile);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1)
            {
                outStream.write(buff,0,len);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    class VideoTask extends AsyncTask<Void, Void, Void> {

        String fileName;
        String url;
        String userName;
        String completeFileName;
        String createdDate;

        VideoTask(String userName, String fileName, String url, String createdDate){
            this.fileName = fileName;
            this.url = url;
            this.userName = userName;
            this.createdDate = createdDate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                completeFileName = userName + "_" + fileName;
                downloadFile(url
                        , completeFileName);

            } catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //treatment_image.setVisibility(View.GONE);
            if (myFile.exists()) {
                if (usefullData.isValidVideo(userName + "_" + fileName)) {
                    initPlayer(Definitions.VIDEO_FOLDER + userName + "_" + fileName,createdDate);
                    //usefullData.Mixpanel_events("Education Video Played", "Video Name", userName);
                } else {
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Syde_conditions_video/"+completeFileName);
                    initPlayer(uri.toString(), createdDate);
                    //usefullData.Mixpanel_events("Education Video Played", "Video Name", userName);
                }
            } else {
                initPlayer(url, createdDate);
                //usefullData.Mixpanel_events("Education Video Played", "Video Name", userName);
            }
        }
    }

    @Override
    public void onChildListItemClick(int groupPos, int groupId) {

    }



    public void setSnapanable(String string){
        String temp=string;
        if (temp.length() > 35) {
            temp = temp.substring(0, 35)+ "...";

        }
//        SpannableString spannableString_2 = new SpannableString(treatment_pre_txt + temp);
//        ClickableSpan clickableSpan_2 = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//
//            }
//
//            @Override
//            public void updateDrawState(final TextPaint textPaint) {
//                textPaint.setColor(getResources().getColor(R.color.white));
//                final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
//                textPaint.setTypeface(usefullData.get_montserrat_bold());
//                textPaint.setUnderlineText(false);
//            }
//        };
//        spannableString_2.setSpan(clickableSpan_2, treatment_pre_txt.length(),
//                treatment_pre_txt.length() + temp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        header_text.setText(string);
        header_text.setTypeface(usefullData.get_montserrat_regular());
    }


    public void quickLinkinitialize(){
        isPortfolio = false;
        isElement1 = false;
        isElement2 = false;
        isElement3 = false;
        isGroup = false;
        isChild = false;
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }


    public void quickLinkinitializeEverything(){
        isPortfolio = false;
        isElement1 = false;
        isElement2 = false;
        isElement3 = false;
        isGroup = false;
        isChild = false;
        isGroup1Clicked = false;
        isGroup2Clicked = false;
        isGroup3Clicked = false;
        llBarElement1.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
        llBarElement2.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
        llBarElement3.setBackground(getResources().getDrawable(R.drawable.tap_unselect));
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }

    public void quickLinkSelected(){
        isPortfolio = false;
        isGroup = false;
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }



    public void quickLink1True(){
//        isElement1 = true;
//        isElement2 = false;
//        isElement3 = false;
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }


    public void quickLink2True(){
//        isElement1 = false;
//        isElement2 = true;
//        isElement3 = false;
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }

    public void quickLink3True(){
//        isElement1 = false;
//        isElement2 = false;
//        isElement3 = true;
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }


    public void quickLinkAllFalse(){
        isElement1 = false;
        isElement2 = false;
        isElement3 = false;
        barPreview.setBackground(getResources().getDrawable(R.drawable.tap_prev));
    }

}
