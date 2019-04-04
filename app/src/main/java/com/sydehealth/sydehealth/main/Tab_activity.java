
package com.sydehealth.sydehealth.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jean.jcplayer.view.JcPlayerButtonClickListener;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sydehealth.sydehealth.model.TreatmentImages;
import com.sydehealth.sydehealth.screen_share.Screen_share_activity;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.TreatmentImageSelectListener;
import com.sydehealth.sydehealth.adapters.TreatmentScreenAdapter;
import com.sydehealth.sydehealth.drawingview.MainActivity;
import com.sydehealth.sydehealth.service.AlarmReceiver;
import com.sydehealth.sydehealth.utility.CameraSound;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.wunderlist.slidinglayer.LayerTransformer;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.AlphaTransformer;
import com.wunderlist.slidinglayer.transformer.RotationTransformer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class Tab_activity extends FragmentActivity implements View.OnClickListener, TreatmentImageSelectListener, View.OnTouchListener, View.OnLongClickListener {
    SaveData save_data;
    UsefullData objUsefullData;
    TabHost tabHost;
    TabHost.TabSpec spec;
    View m_view;
    boolean shot_click = false;
    CameraSound mCameraSound;
    PopupWindow pwindo;
    public static ImageView crossRadio;
    //    public static Bitmap paint;
    //    ImageView setting_icon;
    private static final String TAG = "Tab_activity";
    private final int ANIMATION_DURATION = 500;

    public Runnable update;


    ImageView iv_settings;
    TextView portfolio;
    LinearLayout ll_condition, ll_screenshare, ll_media;
    public static LinearLayout ll_email;
    TextView tv_conditions, tv_screenshare, tv_media, tv_email, tv_back, conditions, screenshare, media, email, smallRadioText;
    public static TextView name;
    RelativeLayout rl_back;
    int noOfScreensShots = 0;
    public static TextView tv_count;
    //    private PendingIntent pendingIntent;
    ArrayList<TreatmentImages> alTreatmentImages = new ArrayList<>();
    public static ArrayList<TreatmentImages> alSelectedImages = new ArrayList<>();
    public static ArrayList<TreatmentImages> alTempSelectedImages = new ArrayList<>();
    TreatmentScreenAdapter treatmentScreenAdapter;
    ImageLoader imageLoader;
    Button btn_add_photos;
    public static ImageView surgery_logo;
    public static LinearLayout ll_bottombar;
    public static JcPlayerView jcplayerView;
    private TextView tv_portfolio;


    private int _xDelta;
    private int _yDelta;

    private ImageView mImageView;
    private ViewGroup mRrootLayout;


    int windowwidth; // Actually the width of the RelativeLayout.
    int windowheight; // Actually the height of the RelativeLayout.

    //gaurav
    LinearLayout llPortfolio;
    private RadioAudioAdapter audioAdapter;
    private RecyclerView recyclerView;

    public static FrameLayout jcFrameLayout;

    private TextView discard_txt;

    private ImageView radio_floating;

    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;


    private static FrameLayout layout_radio_floating;
    public AudioManager audioManager;


    public FrameLayout layout_cross;
    public ImageView arrow;

    private boolean isFabMenuOpen = false;

    public ImageView btnLowMinus, btnHighPlus;
    public static RelativeLayout radioLayoutMin;
    Handler handler3 = new Handler();


    private int openRadioTime  = 10000;
    private int timerInc = 0;



    private SlidingLayer mSlidingLayer;
    private TextView swipeText;

    public static LinearLayout playerLayout;

    private Handler handler = new Handler();
    private Runnable runnable;


    public static boolean isRadioOpen  = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        save_data = new SaveData(Tab_activity.this);
        objUsefullData = new UsefullData(Tab_activity.this);
        setContentView(R.layout.activity_test_tabs);

        ComponentName receiver = new ComponentName(Tab_activity.this, AlarmReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        /* Retrieve a PendingIntent that will perform a broadcast */
//        Intent alarmIntent = new Intent(Tab_activity.this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(Tab_activity.this, 0, alarmIntent, 0);
        initView();

        listeners();
        bindViews();
        initState();


        /**
         * gaurav the service downloads the videos (condition and screensaver at 1:00 PM to 4:00 PM)
         */
        startmultiple();


//        ArrayList<JcAudio> jcAudios = new ArrayList<>();
//        jcAudios.add(JcAudio.createFromURL("Heart London","http://media-ice.musicradio.com/HeartLondonMP3"));
//        jcAudios.add(JcAudio.createFromURL("Heart Harlow","http://media-ice.musicradio.com/HeartHarlowMP3"));
//        jcAudios.add(JcAudio.createFromURL("Heart Glasgow","http://media-ice.musicradio.com/HeartGlasgowMP3"));
//
//        jcplayerView.initPlaylist(jcAudios, null);


//        if (jcplayerView.getVisibility() != View.GONE) {
//            jcplayerView.setVisibility(View.VISIBLE);
        // jcFrameLayout.setOnTouchListener(this);
//        } else {
//            jcplayerView.setVisibility(View.GONE);
//        }

//        jcFrameLayout.setOnTouchListener(this);
//        jcFrameLayout.setOnLongClickListener(this);
//        final Handler handler2 = new Handler();
//        if (!jcplayerView.isPlaying()) {
//            handler2.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    jcplayerView.setVisibility(View.GONE);
//                    radioLayoutMin.setVisibility(View.VISIBLE);
//
//                    jcFrameLayout.setOnLongClickListener(null);
////                   Definitions.ISJCPLAYERCLICK=false;
//                }
//            }, 3000);
//
//        }


        // Capture the width of the RelativeLayout once it is laid out.

        layout_radio_floating.setOnTouchListener(this);

//        mRrootLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                windowwidth = mRrootLayout.getWidth();
//                windowheight = mRrootLayout.getHeight();
//            }
//        });


//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent edit = new Intent(Tab_activity.this, Settings_group_activity.class);
//                startActivity(edit);
//            }
//        });
//        final LinearLayout screen_share = (LinearLayout) findViewById(R.id.juckpad_screen_share);
//        final LinearLayout screen_shoot = (LinearLayout) findViewById(R.id.juckpad_screenshot);
//        setting_icon = (ImageView) findViewById(R.id.setting_icon);


        /*screenshare_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objUsefullData.isNetworkConnected()) {
                    if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save_data.getInt(Definitions.ID)))) {
                        InitializeActivity.mMixpanel.track("Screenshare Button Clicked");
                    }
                    Intent edit = new Intent(Tab_activity.this, Screen_share_activity.class);
                    startActivity(edit);
                } else {
                    objUsefullData.make_alert(getResources().getString(R.string.no_internet), true, Tab_activity.this);
                }
            }
        });*/
//        screenshare_layer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (Settings.System.canWrite(Tab_activity.this)) {
//                        setAutoOrientationEnabled(getContentResolver(), true);
//                        Ask.on(Tab_activity.this)
//                                .forPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                .withRationales("In order to save screenshot you will need to grant storage permission")
//                                .go();
//                        m_view = view;
//                        mCameraSound = new CameraSound();
//                        Bitmap bitmap = Screenshot.getInstance().takeScreenshotForScreen(Tab_activity.this);
//                        SaveImage(bitmap, "/" + "temps" + ".jpg");
//                    } else {
//                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                        intent.setData(Uri.parse("package:" + Tab_activity.this.getPackageName()));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
        /*email_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mail_click=true;
                objUsefullData.showProgress();
                if (objUsefullData.isNetworkConnected()) {
                    Intent edit = new Intent(Tab_activity.this, EmailActivity.class);
                    edit.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(edit);
                } else {
                    objUsefullData.make_alert(getResources().getString(R.string.no_internet), true, Tab_activity.this);
                }
            }
        });
*/
        //setTabs();

//        jcplayerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(getApplicationContext(),"shdjshdh",Toast.LENGTH_SHORT).show();
//
//                getTimer();
//                return true;
//            }
//        });


        jcplayerView.onClickAction(new JcPlayerView.JcButtonClicked() {
             @Override
             public void buttonClicked() {
                 //Toast.makeText(getApplicationContext(), "ButtonClicked", Toast.LENGTH_SHORT).show();
                 //InitializeActivity.setIsRadioPlay(true);
                 getTimer();
             }
        });



        jcplayerView.onJcClickPLay(new JcPlayerView.JcButtonPlayClicked() {


            @Override
            public void buttonPauseClicked() {

            }

            @Override
            public void buttonPlayClicked() {
                InitializeActivity.setIsRadioPlay(0);
                InitializeActivity.setIsRadioPause(-1);

            }
        });

        jcplayerView.onJcClickPause(new JcPlayerView.JcButtonPlayClicked() {
            @Override
            public void buttonPlayClicked() {

            }

            @Override
            public void buttonPauseClicked() {
                InitializeActivity.setIsRadioPause(1);
                InitializeActivity.setIsRadioPlay(-1);
            }
        });



//        playerLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getTimer();
//            }
//        });
    }



    // radio closes after 20 sec

    private  CountDownTimer waitTimer ;

    public void getTimer() {

        if (waitTimer != null) {
            //Toast.makeText(getApplicationContext(),"timer cancal",Toast.LENGTH_SHORT).show();
            waitTimer.cancel();
            waitTimer = null;
        }
        waitTimer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Toast.makeText(getApplicationContext(),String.valueOf(millisUntilFinished),Toast.LENGTH_SHORT).show();
            }
            public void onFinish() {
                mSlidingLayer.closeLayer(true);
                arrow.setImageResource(R.drawable.left_a);
            }

        }.start();

    }

//    public void getTimer() {
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.removeCallbacks(this);
//                mSlidingLayer.closeLayer(true);
//            }
//        };
//
//        handler.postDelayed(runnable, 5000);
//
//
////        if (waitTimer != null) {
////            Toast.makeText(getApplicationContext(),"timer cancal",Toast.LENGTH_SHORT).show();
////            waitTimer.cancel();
////            waitTimer = null;
////        }
////        new CountDownTimer(5000, 1000) {
////
////            public void onTick(long millisUntilFinished) {
////
////            }
////            public void onFinish() {
////                mSlidingLayer.closeLayer(true);
////                arrow.setImageResource(R.drawable.left_a);
////            }
////
////        }.start();
//
//    }

//    public void getTimer(){
//        new CountDownTimer(5000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//
//            }
//            public void onFinish() {
//                mSlidingLayer.closeLayer(true);
//                arrow.setImageResource(R.drawable.left_a);
//            }
//
//        }.start();
//    }

    @Override
    public boolean onLongClick(View v) {

        return true;
    }


    /**
     * Initialize Listeners
     */
    private void listeners() {
        ll_condition.setOnClickListener(this);
        ll_screenshare.setOnClickListener(this);
        ll_media.setOnClickListener(this);
        ll_email.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
        llPortfolio.setOnClickListener(this);

        //tv_back.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        ll_email.setLongClickable(true);
        crossRadio.setOnClickListener(this);
        jcFrameLayout.setOnClickListener(this);
//        jcFrameLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });


       // jcplayerView.setOnTouchListener(this);


//        jcplayerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//             public boolean onTouch(View v, MotionEvent event) {
//                getTimer();
//                return false;
//            }
//        });



//        ll_email.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
////                Intent start_service = new Intent(Tab_activity.this, Video_download_service.class);
////                startService(start_service);
//                if (objUsefullData.get_screenshots().size() == 0 || objUsefullData.get_screenshots().equals(null)) {
//                    objUsefullData.make_alert(getResources().getString(R.string.screenshot_error), true, Tab_activity.this);
//                } else {
//                    openAddPhotoDialog();
//                }
//                return true;
//            }
//
//        });

    }


    /**
     * Initialize Views
     */
    private void initView() {
        jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);
        surgery_logo = (ImageView) findViewById(R.id.surgery_logo);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        tv_back = (TextView) findViewById(R.id.tv_back);
        ll_condition = (LinearLayout) findViewById(R.id.ll_condition);
        ll_screenshare = (LinearLayout) findViewById(R.id.ll_screenshare);
        ll_media = (LinearLayout) findViewById(R.id.ll_media);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);
        tv_conditions = (TextView) findViewById(R.id.tv_conditions);
        tv_screenshare = (TextView) findViewById(R.id.tv_screenshare);
        tv_media = (TextView) findViewById(R.id.tv_media);
        portfolio = (TextView) findViewById(R.id.portfolio);
        tv_email = (TextView) findViewById(R.id.tv_email);
        conditions = (TextView) findViewById(R.id.conditions);
        screenshare = (TextView) findViewById(R.id.screenshare);
        media = (TextView) findViewById(R.id.media);
        email = (TextView) findViewById(R.id.email);
        tv_portfolio = (TextView) findViewById(R.id.tv_portfolio);
        name = (TextView) findViewById(R.id.name);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_count = (TextView) findViewById(R.id.tv_count);
        crossRadio = (ImageView) findViewById(R.id.crossRadio);
        playerLayout = (LinearLayout) findViewById(R.id.playerLayout);
        layout_radio_floating = (FrameLayout) findViewById(R.id.layout_radio_floating);
        radio_floating = (ImageView) findViewById(R.id.radio_floating);
        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open_anim);
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close_anim);
        ll_bottombar = (LinearLayout) findViewById(R.id.ll_bottombar);
        jcFrameLayout = (FrameLayout) findViewById(R.id.jcFrameLayout);
        layout_cross = (FrameLayout) findViewById(R.id.layout_cross);
        tv_conditions.setTypeface(objUsefullData.get_awosome_font_400());
        tv_screenshare.setTypeface(objUsefullData.get_awosome_font_400());
        tv_media.setTypeface(objUsefullData.get_awosome_font_400());
        tv_email.setTypeface(objUsefullData.get_awosome_font_400());
        tv_portfolio.setTypeface(objUsefullData.get_awosome_font_400());
        tv_back.setTypeface(objUsefullData.get_awosome_font_400());
        conditions.setTypeface(objUsefullData.get_montserrat_regular());
        screenshare.setTypeface(objUsefullData.get_montserrat_regular());
        screenshare.setTypeface(objUsefullData.get_montserrat_regular());
        media.setTypeface(objUsefullData.get_montserrat_regular());
        portfolio.setTypeface(objUsefullData.get_montserrat_regular());
        email.setTypeface(objUsefullData.get_montserrat_regular());
        name.setTypeface(objUsefullData.get_montserrat_regular());
        tv_count.setTypeface(objUsefullData.get_montserrat_regular());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Condition_page()).commit();
        //gauarv
        llPortfolio = (LinearLayout) findViewById(R.id.ll_portfolio);
        arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setOnClickListener(this);

        //mRrootLayout = (ViewGroup) findViewById(R.id.root);

        //Rahul Fm Radio Volume Up and Low Control
//        btnHighPlus = findViewById(R.id.minRadioBtnhigh);
//        btnLowMinus = findViewById(R.id.minRadioBtnLow);
//        radioLayoutMin = findViewById(R.id.minRadioLayout);
//        smallRadioText = findViewById(R.id.small_radio_txt);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //mImageView = (ImageView) mRrootLayout.findViewById(R.id.im_move_zoom_rotate);

    }



    /*private void setTabs() {

        addTab(getResources().getString(R.string.condition), R.drawable.media_icon, Conditions_group_activity.class);
        addTab(getResources().getString(R.string.media), R.drawable.media_icon, Playlist_group_activity.class);
        addTab(getResources().getString(R.string.sett), R.drawable.setting_icon, Settings_group_activity.class);

    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);

        title.setText(labelId);
        title.setTypeface(objUsefullData.get_montserrat_semibold());
//        title.setLetterSpacing(5);

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        tabHost.addTab(spec);
    }
*/


    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
//        View view = getCurrentFocus();
//        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            view.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + view.getTop() - scrcoords[1];
//            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
//                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
//        }
        //getTimer();
        objUsefullData.Start_timer();
        objUsefullData.schedule_time_events();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//               hideSystemUI();
//            }
//        }, 2000);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_email:
                //objUsefullData.showProgress();
//                objUsefullData.Mixpanel_events("Email Viewed", "Email Viewed", "Email");
//                ll_email.setBackground(null);
//                if (objUsefullData.isNetworkConnected()) {
//                    ll_email.setBackground(null);
//                    Fragment currentFragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                    if (currentFragment1 instanceof EmailActivity) {
//
//                    } else {
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmailActivity()).addToBackStack(null).commit();
//                    }
//                    break;
//                } else {
//                    objUsefullData.make_alert(getResources().getString(R.string.no_internet), true, Tab_activity.this);
//                }
                break;
            case R.id.ll_screenshare:
                //ll_email.setBackground(null);
                if (objUsefullData.isNetworkConnected()) {
                    if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save_data.getInt(Definitions.ID)))) {
                        InitializeActivity.mMixpanel.track("Screenshare Button Clicked");
                    }
                    Intent edit = new Intent(Tab_activity.this, Screen_share_activity.class);
                    edit.putExtra("from", "Tab_activity");
                    startActivity(edit);
                } else {
                    objUsefullData.make_alert(getResources().getString(R.string.no_internet), true, Tab_activity.this);
                }
                break;
            case R.id.ll_media:
                //ll_email.setBackground(null);
                Fragment playlist_fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (playlist_fragment instanceof Playlist_activity) {

                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Playlist_activity()).addToBackStack(null).commit();
                }
                break;
            case R.id.rl_back:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                    if (Condition_player.isTabOpened) {
                        //ll_email.setBackground(getResources().getDrawable(R.drawable.transparent_with_white_border));
                    } else {
                        //ll_email.setBackground(null);
                    }
                } else {
                    finishAffinity();
                }
                break;
            case R.id.ll_condition:
                //ll_email.setBackground(null);
                try {
                    Condition_player.isTabOpened = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof Condition_page) {

                } else {

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Condition_page()).commit();
                }
                break;
            case R.id.iv_settings:
                //ll_email.setBackground(null);
                Fragment currentFragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment1 instanceof Setting_activity) {

                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Setting_activity()).addToBackStack(null).commit();
                }
                break;

            case R.id.ll_portfolio:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                objUsefullData.Mixpanel_events("Portfolio", "Portfolio Viewed", "Portfolio");
                if (fragment instanceof MyPortfolioActivity) {

                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyPortfolioActivity()).addToBackStack(null).commit();
                }
                break;

            case R.id.crossRadio:
                if(jcplayerView.isPlaying()){
                    jcFrameLayout.setVisibility(View.GONE);
                    jcplayerView.pause();
                    jcplayerView.kill();
                }else{
                    jcFrameLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.arrow:
                if(mSlidingLayer.isOpened()){
                    mSlidingLayer.closeLayer(true);
                    arrow.setImageResource(R.drawable.left_a);
                }
                else if(mSlidingLayer.isClosed()){
                    isRadioOpen = true;
                    handler.removeCallbacks(runnable);
                    getTimer();
                    mSlidingLayer.openLayer(true);
                    arrow.setImageResource(R.drawable.right_a);
                }
                break;

            case R.id.jcplayer:
                //Toast.makeText(this,"sdsdsd", Toast.LENGTH_LONG).show();
                //getTimer();
                break;
        }
    }

    @Override
    public void onTreatmentSelect(String path, boolean isSelected) {
        for (int i = 0; i < alTempSelectedImages.size(); i++) {
            if (alTempSelectedImages.get(i).getPath().equalsIgnoreCase(path)) {
                if (!isSelected) {
                    alTempSelectedImages.remove(i);
                    if (alTempSelectedImages.size() == 0) {
                        btn_add_photos.setAlpha(0.4f);
                        btn_add_photos.setEnabled(false);
                    }
                }
            }
        }

        if (isSelected) {
            TreatmentImages treatmentImages = new TreatmentImages();
            treatmentImages.setSelected(isSelected);
            treatmentImages.setPath(path);
            alTempSelectedImages.add(treatmentImages);
            btn_add_photos.setAlpha(1f);
            btn_add_photos.setEnabled(true);
        }

    }

//    @Override
//    public void onJcPlayerButtonClick() {
//        Toast.makeText(this,"Button Clicked",Toast.LENGTH_SHORT).show();
//    }


    class take_shoot extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {

            objUsefullData.showProgress();

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                getScreen(m_view);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            objUsefullData.dismissProgress();
        }
    }


    public void getScreen(View v) {

        try {
//            View view_screen = v.getRootView();
            Bitmap bitmap = getScreenShot(v);
            SaveImage(bitmap, "/" + "temps" + ".jpg");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void showscreen_shoot(final File file) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.screen_shoot_popup, null, false);
                    pwindo = new PopupWindow(layout, AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT, false);
//                  pwindo.setAnimationStyle(R.style.Animation);
                    pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    pwindo.setOutsideTouchable(true);
                    pwindo.setFocusable(true);
                    LinearLayout cross = (LinearLayout) layout.findViewById(R.id.screen_cross);
                    if (file.exists()) {

//                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


//                        img.setImageBitmap(myBitmap);
                        objUsefullData.dismissProgress();

                    }

                    cross.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pwindo.dismiss();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void SaveImage(Bitmap finalBitmap, String fname) {

//        paint = Bitmap.createScaledBitmap(finalBitmap, 1920, 1200, true);

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
        Intent intent = new Intent(Tab_activity.this, MainActivity.class);
        intent.putExtra("request", "normal");
        startActivity(intent);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something here
                mCameraSound.playSound(CameraSound.SHUTTER_CLICK);
            }
        }, 400);
    }


    class save_shoot extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                Bitmap finalBitmap = BitmapFactory.decodeFile(urls[0]);
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Jukepad");
                myDir.mkdirs();
                File file = new File(myDir, "/" + now + ".jpg");
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment conditionFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (conditionFragment instanceof Condition_player) {
            if (Condition_player.isTabOpened) {
                //ll_email.setBackground(getResources().getDrawable(R.drawable.transparent_with_white_border));
            } else {
                //ll_email.setBackground(null);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        save_data.save(Definitions.SHOW_FIREBASE_TOP, false);
        objUsefullData.time_check();
        Fragment conditionFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (conditionFragment instanceof Condition_player) {
            if (Condition_player.isTabOpened) {
                //ll_email.setBackground(getResources().getDrawable(R.drawable.transparent_with_white_border));
            } else {
                //ll_email.setBackground(null);
            }
        }
        ll_bottombar.setVisibility(View.VISIBLE);

        name.setText(objUsefullData.capitalize(save_data.getString(Definitions.SURGERY_NAME).trim()));

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(Tab_activity.this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Jukepad");
                if (myDir.exists()) {
                    File[] fileList = myDir.listFiles();
                    if (fileList != null) {
                        noOfScreensShots = fileList.length;
                    }
                    if (noOfScreensShots > 0) {
                        tv_count.setVisibility(View.VISIBLE);
                        tv_count.setText(noOfScreensShots + "");
                    } else {
                        tv_count.setText("0");
                        tv_count.setVisibility(View.GONE);
                    }
                } else {
                    tv_count.setText("0");
                    tv_count.setVisibility(View.GONE);
                }
            }
        });

        objUsefullData.Start_timer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        save_data.save(Definitions.SHOW_FIREBASE_TOP, true);
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.moveTaskToFront(getTaskId(), 0);
        objUsefullData.Pause_timer();
        objUsefullData.time_check();

    }

    public static void setAutoOrientationEnabled(ContentResolver resolver, boolean enabled) {
        Settings.System.putInt(resolver, Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }


    public void startmultiple() {

        for (int i = 0; i < 5; ++i) {
            switch (i) {
                case 0:
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 1);
                    Intent myIntent = new Intent(Tab_activity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(Tab_activity.this, i, myIntent, i);
                    alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    break;
                case 1:
                    AlarmManager alarm_afternoon = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar calendar_afternoon = Calendar.getInstance();
                    calendar_afternoon.set(Calendar.HOUR_OF_DAY, 12);
                    calendar_afternoon.set(Calendar.MINUTE, 1);
                    Intent myIntent_afternoon = new Intent(Tab_activity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent_afternoon = PendingIntent.getBroadcast(Tab_activity.this, i, myIntent_afternoon, i);
                    alarm_afternoon.set(AlarmManager.RTC_WAKEUP, calendar_afternoon.getTimeInMillis(), pendingIntent_afternoon);
                    break;
                case 2:
                    AlarmManager alarm_evening = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Calendar calendar_evening = Calendar.getInstance();
                    calendar_evening.set(Calendar.HOUR_OF_DAY, 18);
                    calendar_evening.set(Calendar.MINUTE, 1);
                    Intent myIntent_evening = new Intent(Tab_activity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent_evening = PendingIntent.getBroadcast(Tab_activity.this, i, myIntent_evening, i);
                    alarm_evening.set(AlarmManager.RTC_WAKEUP, calendar_evening.getTimeInMillis(), pendingIntent_evening);
                    break;
                case 3:
                    AlarmManager alarm_service_start = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar calendar_service_start = Calendar.getInstance();
                    calendar_service_start.set(Calendar.HOUR_OF_DAY, 13);
                    calendar_service_start.set(Calendar.MINUTE, 1);
                    Intent myIntent_service_start = new Intent(Tab_activity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent_service_start = PendingIntent.getBroadcast(Tab_activity.this, i, myIntent_service_start, i);
                    alarm_service_start.set(AlarmManager.RTC_WAKEUP, calendar_service_start.getTimeInMillis(), pendingIntent_service_start);
                    break;
                case 4:
                    AlarmManager alarm_service_stop = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Calendar calendar_service_stop = Calendar.getInstance();
                    calendar_service_stop.set(Calendar.HOUR_OF_DAY, 16);
                    calendar_service_stop.set(Calendar.MINUTE, 1);
                    Intent myIntent_service_stop = new Intent(Tab_activity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent_service_stop = PendingIntent.getBroadcast(Tab_activity.this, i, myIntent_service_stop, i);
                    alarm_service_stop.set(AlarmManager.RTC_WAKEUP, calendar_service_stop.getTimeInMillis(), pendingIntent_service_stop);
                    break;

            }
        }
    }

    private void openAddPhotoDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_photos_popup);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_translucent)));

        dialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);

        RecyclerView recyclerview = (RecyclerView) dialog.findViewById(R.id.recylerview);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_add_photos = (Button) dialog.findViewById(R.id.btn_add_photos);
        TextView select_treatment = (TextView) dialog.findViewById(R.id.select_treatment);
        select_treatment.setTypeface(objUsefullData.get_montserrat_regular());
        btn_add_photos.setAlpha(0.5f);
        btn_add_photos.setEnabled(false);

        btn_add_photos.setText("Delete");
        btn_add_photos.setTypeface(objUsefullData.get_montserrat_regular());
        btn_cancel.setTypeface(objUsefullData.get_montserrat_regular());

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        alTreatmentImages = objUsefullData.get_screenshots();

        alTempSelectedImages.clear();
        alTempSelectedImages.addAll(alSelectedImages);
        initImageLoader();


        treatmentScreenAdapter = new TreatmentScreenAdapter(alTreatmentImages, Tab_activity.this, imageLoader, this, "add");
        recyclerview.setAdapter(treatmentScreenAdapter);
        recyclerview.smoothScrollToPosition(0);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btn_add_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alSelectedImages.clear();
                alSelectedImages.addAll(alTempSelectedImages);
                dialog.cancel();
                for (int i = 0; i < alSelectedImages.size(); i++) {
                    File file = new File(alSelectedImages.get(i).getPath());
                    objUsefullData.deleteFile(file);
                }
                onResume();
            }
        });

        dialog.show();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," updateDataSet it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    private boolean isOutReported = false;


    public boolean onTouch(View view, MotionEvent event) {
        FrameLayout.LayoutParams lp,lp1;

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        // Check if the image view is out of the parent view and report it if it is.
        // Only report once the image goes out and don't stack toasts.
        if (isOut(view)) {
            if (!isOutReported) {
                isOutReported = true;
                Toast.makeText(this, "OUT", Toast.LENGTH_SHORT).show();
            }
        } else {
            isOutReported = false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                lp = (FrameLayout.LayoutParams) view
                        .getLayoutParams();
                // _xDelta and _yDelta record how far inside the view we have touched. These
                // values are used to compute new margins when the view is moved.
//                _xDelta = X - view.getLeft();
//                _yDelta = Y - view.getTop();

//                _xDelta = X -  lp.leftMargin;
//                _yDelta = Y - lp.topMargin;

                _xDelta = X - view.getLeft();
                _yDelta = Y - view.getTop();
                break;
            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_MOVE:
                lp = (FrameLayout.LayoutParams) view
                        .getLayoutParams();

                lp1 = (FrameLayout.LayoutParams) view
                        .getLayoutParams();

//                _xDelta = X -  view.getLeft();
//                _yDelta = Y - view.getTop();

                lp.leftMargin = X - _xDelta;
                lp.topMargin = Y - _yDelta;

//                lp1.leftMargin = X - _xDelta;
//                lp1.topMargin = Y - _yDelta;


                // Negative margins here ensure that we can move off the screen to the right
                // and on the bottom. Comment these lines out and you will see that
                // the image will be hemmed in on the right and bottom and will actually shrink.
                lp.rightMargin = view.getWidth() - lp.leftMargin - windowwidth;
                lp.bottomMargin = view.getHeight() - lp.topMargin - windowheight;

                lp1.rightMargin = view.getWidth() - lp1.leftMargin - windowwidth;
                lp1.bottomMargin = view.getHeight() - lp1.topMargin - windowheight;

                view.setLayoutParams(lp);
                jcFrameLayout.setLayoutParams(lp1);
//                layout_cross.setLayoutParams(lp);

//                FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) view
//                        .getLayoutParams();
                //lp.gravity = Gravity.CENTER_HORIZONTAL;

                // Image is centered to start, but we need to unhitch it to move it around.
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
//                    lp.removeRule(RelativeLayout.CENTER_VERTICAL);
//                } else {
//                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
//                    lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
//                }

//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.setMargins(15, 0, 0, 0);
//                layout_radio_floating.setLayoutParams(params);






//                lp1.leftMargin = X - _xDelta;
//                lp1.topMargin = Y - _yDelta;



//                lp1.rightMargin = (view.getWidth() - lp1.leftMargin - windowwidth);
//                lp1.bottomMargin = (view.getHeight() - lp1.topMargin - windowheight);

                //lp1.setMargins(15,0,0,0);


                break;
        }
        // invalidate is redundant if layout params are set or not needed if they are not set.
//        mRrootLayout.invalidate();
        return true;
    }

    private boolean isOut(View view) {
        // Check to see if the view is out of bounds by calculating how many pixels
        // of the view must be out of bounds to and checking that at least that many
        // pixels are out.
        float percentageOut = 0.50f;
        int viewPctWidth = (int) (view.getWidth() * percentageOut);
        int viewPctHeight = (int) (view.getHeight() * percentageOut);

        return ((-view.getLeft() >= viewPctWidth) ||
                (view.getRight() - windowwidth) > viewPctWidth ||
                (-view.getTop() >= viewPctHeight) ||
                (view.getBottom() - windowheight) > viewPctHeight);
    }




    private void removeItem(int position) {
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(true);

        //        jcAudios.remove(position);
        jcplayerView.removeAudio(jcplayerView.getMyPlaylist().get(position));
        audioAdapter.notifyItemRemoved(position);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        jcplayerView.pause();
        jcplayerView.kill();
    }


    //
    private void initState() {
        setupSlidingLayerPosition("right");
        setupSlidingLayerTransform("none");
        setupShadow(false);
        setupLayerOffset(true);
        setupPreviewMode(false);
    }


    private void setupSlidingLayerPosition(String layerPosition) {

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mSlidingLayer.getLayoutParams();
        int textResource;
        Drawable d;

        switch (layerPosition) {
            case "right":
                textResource = R.string.swipe_right_label;
                //d = getResources().getDrawable(R.drawable.container_rocket_right);

                mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);
                break;
            case "left":
                textResource = R.string.swipe_left_label;
                //d = getResources().getDrawable(R.drawable.container_rocket_left);

                mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);
                break;
            case "top":
                textResource = R.string.swipe_up_label;
                ///d = getResources().getDrawable(R.drawable.container_rocket);

                mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_TOP);
                rlp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                rlp.height = getResources().getDimensionPixelSize(R.dimen.layer_size);
                break;
            default:
                textResource = R.string.swipe_down_label;
                //d = getResources().getDrawable(R.drawable.container_rocket);

                mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);
                rlp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                rlp.height = getResources().getDimensionPixelSize(R.dimen.layer_size);
        }

        //d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //swipeText.setCompoundDrawables(null, d, null, null);
        //swipeText.setText(getResources().getString(textResource));
        mSlidingLayer.setLayoutParams(rlp);
    }

    private void setupSlidingLayerTransform(String layerTransform) {

        LayerTransformer transformer;

        switch (layerTransform) {
            case "alpha":
                transformer = new AlphaTransformer();
                break;
            case "rotation":
                transformer = new RotationTransformer();
                break;
            case "slide":
                transformer = new SlideJoyTransformer();
                break;
            default:
                return;
        }
        mSlidingLayer.setLayerTransformer(transformer);
    }

    private void setupShadow(boolean enabled) {
        if (enabled) {
            mSlidingLayer.setShadowSizeRes(R.dimen.shadow_size);
            //mSlidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        } else {
            mSlidingLayer.setShadowSize(0);
            mSlidingLayer.setShadowDrawable(null);
        }
    }

    private void setupLayerOffset(boolean enabled) {
        int offsetDistance = enabled ? getResources().getDimensionPixelOffset(R.dimen.offset_distance) : 0;
        mSlidingLayer.setOffsetDistance(offsetDistance);
    }

    private void setupPreviewMode(boolean enabled) {
        int previewOffset = enabled ? getResources().getDimensionPixelOffset(R.dimen.preview_offset_distance) : -1;
        mSlidingLayer.setPreviewOffsetDistance(previewOffset);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
////                if (mSlidingLayer.isOpened()) {
////                    mSlidingLayer.closeLayer(true);
////                    return true;
////                }
//
//            default:
//                return super.onKeyDown(keyCode, event);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    private void bindViews() {
        mSlidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer1);
        mSlidingLayer.setSlidingEnabled(false);
//        mSlidingLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
//            @Override
//            public void onOpen() {
//                getTimer();
//            }
//
//            @Override
//            public void onShowPreview() {
//
//            }
//
//            @Override
//            public void onClose() {
//
//            }
//
//            @Override
//            public void onOpened() {
//
//            }
//
//            @Override
//            public void onPreviewShowed() {
//
//            }
//
//            @Override
//            public void onClosed() {
//
//            }
//        });
        //swipeText = (TextView) findViewById(R.id.swipeText);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //getTimer();
        return false;
    }


}
