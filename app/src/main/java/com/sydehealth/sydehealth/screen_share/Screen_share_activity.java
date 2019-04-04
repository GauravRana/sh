package com.sydehealth.sydehealth.screen_share;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.sydehealth.sydehealth.loader.CallPhoneDialog;
import com.sydehealth.sydehealth.loader.Youtube_loader;
import com.sydehealth.sydehealth.model.Classified;
import com.sydehealth.sydehealth.retrofit.MyApiEndpointInterface;
import com.sydehealth.sydehealth.model.PowerAIRes;
import com.sydehealth.sydehealth.retrofit.NetworkHandler;
import com.sydehealth.sydehealth.mail_upload.Screenshare_screenshot_service;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.drawingview.DrawingView;
import com.sydehealth.sydehealth.utility.CameraSound;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.Normal_toast;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Screen_share_activity extends AppCompatActivity implements
        WebServiceCoordinator.Listener,
        Session.SessionListener,
        SubscriberKit.SubscriberListener,
        View.OnClickListener{

    private static final String LOG_TAG = Screen_share_activity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")
    private WebServiceCoordinator mWebServiceCoordinator;
    private Session mSession;
    // private Publisher mPublisher;
    private Subscriber mSubscriber;

    // private FrameLayout mPublisherViewContainer;
    private RelativeLayout mSubscriberViewContainer;

    static UsefullData usefullData;
    SaveData saveData;
    CameraSound mCameraSound;
    private ProgressDialog loader;
    Button fab;
    static Button back;

    protected PowerManager.WakeLock mWakeLock;
    public DrawingView mDrawingView;
    public static Bitmap final_bitmap;
    Button undo_btn, red_color_btn, blue_color_btn, back_btn;
    public static Button save_btn;
    public static Button upload_btn;
    Dialog dialog, dialogLoader;
    SaveData save_data;;
    String fromWhere;
    RelativeLayout rl_screenshare;
    RelativeLayout rl_ai;



    //AI POWER VIEW
    private String document_ai;
    private ImageView imageView_ai;
    private LinearLayout llBack_ai;
    private ImageView tvBack_ai;
    private Bitmap bitmap_ai;
    private Classified classified_ai;
    private File imgFile_ai;
    UsefullData usefullData_ai;
    private Button fab_ai, back_btn_ai;
    private  Button undo_btn_ai, red_color_btn_ai, blue_color_btn_ai;
    private Button onOff_ai, save_btn_ai;

    private boolean isShowingMarks = false;
    public static DrawingView mDrawingView_ai;
    private SaveData saveData_ai;
    public Bitmap finalBitmap_ai;
    //private RelativeLayout mSubscriberViewContainer;
    public static Bitmap final_bitmap_ai;
    Dialog dialog_ai;
    public Bitmap mutableBitmap_ai;
    public LinearLayout lay_btn_ai;


    private Classified classified;
    public ArrayList<PowerAIRes.Classified> classifiedArrayList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private String screenShareId;

    @Subscribe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_share);
        EventBus.getDefault().register(this);
        usefullData = new UsefullData(this);
        saveData = new SaveData(this);
        //iniIntent();
        findViewsAI();
        init();
        upload_btn.setVisibility(View.INVISIBLE);
        upload_btn.setEnabled(false);
        saveData.save(Definitions.ANNOTATION_ENABLE,true);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
        back = (Button) findViewById(R.id.back_screenshare);
        back.setTypeface(usefullData.get_awosome_font());
        mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriber_container);
        fab = (Button) findViewById(R.id.fab);
        fab.setTypeface(usefullData.get_awosome_font());

        save_data = new SaveData(this);
        screenShareId = save_data.getPowerAIURL("PowerAIURL");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usefullData.isNetworkConnected()) {
                    if (mSubscriber == null) {
                        alert(getResources().getString(R.string.capture));
                        return;
                    } else {

                        mCameraSound = new CameraSound();
//                        final View screen = mSubscriber.getRenderer().getView();
//                        screen.setDrawingCacheEnabled(true);

//                        ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).saveScreenshot(true);
                        BasicCustomVideoRenderer.paint = ((BasicCustomVideoRenderer)mSubscriber.getRenderer()).captureScreenshot();
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something here
                                mCameraSound.playSound(CameraSound.SHUTTER_CLICK);

//                                BasicCustomVideoRenderer.paint = Bitmap.createBitmap(screen.getDrawingCache());
//                                screen.setDrawingCacheEnabled(false);

//                                Intent intent = new Intent(Screen_share_activity.this, MainActivity.class);
//                                intent.putExtra("request", "screen_share");
//                                startActivity(intent);

//                                mDrawingView.loadImage(BasicCustomVideoRenderer.paint);
                            }
                        }, 400);

                    }
                } else {
                    usefullData.make_alert(getResources().getString(R.string.no_internet), true, Screen_share_activity.this);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });


        hide_draw_view();
        get_details();

        //POWER AI buttons
        mDrawingView_ai.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                check_activity(DrawingView.savePath.size()+1);
                return false;
            }
        });


        onOff_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowingMarks){
                    mDrawingView_ai.undoAll();
                    mDrawingView_ai.loadImage(bitmap_ai);
                    onOff_ai.setText("ON");
                    onOff_ai.setAlpha(1f);
                    isShowingMarks = false;
                }else{
                    mDrawingView_ai.undoAll();
                    onOff_ai.setText("OFF");
                    showMarksOnImage();
                    onOff_ai.setAlpha(1f);
                }
            }
        });

        back_btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_ai.setVisibility(View.GONE);
                rl_screenshare.setVisibility(View.VISIBLE);
            }
        });

        undo_btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_activity(DrawingView.savePath.size()-1);
                mDrawingView_ai.undo();
                if(mDrawingView_ai.getPenColor()==getResources().getColor(R.color.screeshot_red)){
                    blue_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.blue_color_without_white_border));
                    red_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.red_color_with_white_border));
                }else {
                    blue_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.blue_color_with_white_border));
                    red_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.red_color_without_white_border));
                }
            }
        });

        red_color_btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawingView_ai.setPenColor(getResources().getColor(R.color.screeshot_red));
                blue_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.blue_color_without_white_border));
                red_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.red_color_with_white_border));

            }
        });

        blue_color_btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawingView_ai.setPenColor(getResources().getColor(R.color.screeshot_blue));
                blue_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.blue_color_with_white_border));
                red_color_btn_ai.setBackground(getResources().getDrawable(R.drawable.red_color_without_white_border));
            }
        });

        save_btn_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (save_btn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.ic_tick))) {
                    takeScreenshot();
                    Normal_toast.show(Screen_share_activity.this, "Screenshot saved",  false);

                    rl_screenshare.setVisibility(View.VISIBLE);
                    rl_ai.setVisibility(View.GONE);

//
//                    Intent intent = new Intent(Screen_share_activity.this, Screen_share_activity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("from", "AIPicViewer");
//                    startActivity(intent);

                    //finish();
                }else {
                    finish();
//                    finalBitmap=cropImageView.getCroppedImage();
//                    new final_save_image().execute("1");
                }
            }
        });


    }

    private void init() {

        mDrawingView = (DrawingView) findViewById(R.id.img_screenshot);
        mDrawingView.initializePen();
        mDrawingView.setPenSize(20);
        mDrawingView.setPenColor(getResources().getColor(R.color.screeshot_red));

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        undo_btn=(Button) findViewById(R.id.undo_btn);
        rl_ai = (RelativeLayout) findViewById(R.id.rl_ai);
        rl_screenshare = (RelativeLayout) findViewById(R.id.rl_screenshare);
        undo_btn.setTypeface(usefullData.get_awosome_font());
        red_color_btn=(Button) findViewById(R.id.red_color_btn);
        red_color_btn.setTypeface(usefullData.get_awosome_font());
        blue_color_btn=(Button) findViewById(R.id.blue_color_btn);
        blue_color_btn.setTypeface(usefullData.get_awosome_font());
        save_btn=(Button) findViewById(R.id.save_btn);
        upload_btn = (Button) findViewById(R.id.upload);
        save_btn.setTypeface(usefullData.get_awosome_font());
        upload_btn.setTypeface(usefullData.get_awosome_font());
        back_btn=(Button) findViewById(R.id.back_btn);
        back_btn.setTypeface(usefullData.get_awosome_font());








        undo_btn.setOnClickListener(this);
        red_color_btn.setOnClickListener(this);
        blue_color_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        upload_btn.setOnClickListener(this);
        save_btn.setEnabled(true);
        save_btn.setAlpha(1f);

        mDrawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                check_activity(DrawingView.savePath.size()+1);
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSession != null) {
            mSession.disconnect();
        }
        dismiss_alert();
        //this.mWakeLock.release();
    }

    private void get_details() {
        alert(getResources().getString(R.string.share_start));

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

        UserAPI.get_JsonObjResp(Definitions.SCREEN_SHARE_DETAILS, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        usefullData.make_toast("Something went wrong");
                    }
                });
    }

    private void subscriber_hit(String url) {

        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));


        UserAPI.get_JsonObjResp(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("TAG response", "");
                    }
                });
    }


    private void set_up_values(JSONObject response) {
        try {
            OpenTokConfig.TOKEN = response.optString("token");
            OpenTokConfig.SESSION_ID = response.optString("session_id");
            requestPermissions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Activity lifecycle methods */
//    @Override
//    protected void onPause() {
//        Log.d(LOG_TAG, "onPause");
//        super.onPause();
//        if (mSession != null) {
//            mSession.onPause();
//        }
//    }
    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }
        usefullData = new UsefullData(Screen_share_activity.this);
    }

    private void requestPermissions() {
            // if there is no server URL set
            if (OpenTokConfig.CHAT_SERVER_URL == null) {
                // use hard coded session values
                if (OpenTokConfig.areHardCodedConfigsValid()) {
                    initializeSession(OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID, OpenTokConfig.TOKEN);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.hardCodedConfigErrorMessage);
                }
            } else {
                // otherwise initialize WebServiceCoordinator and kick off request for session data
                // session initialization occurs once data is returned, in onSessionConnectionDataReady
                if (OpenTokConfig.isWebServerConfigUrlValid()) {
                    mWebServiceCoordinator = new WebServiceCoordinator(this, this);
                    mWebServiceCoordinator.fetchSessionConnectionData(OpenTokConfig.SESSION_INFO_ENDPOINT);
                } else {
                    showConfigError("Configuration Error", OpenTokConfig.webServerConfigErrorMessage);
                }
            }

    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        try{
            mSession = new Session.Builder(this, apiKey, sessionId).build();
            mSession.setSessionListener(this);
            mSession.connect(token);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /* Web Service Coordinator delegate methods */

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
        Log.d(LOG_TAG, "ApiKey: " + apiKey + " SessionId: " + sessionId + " Token: " + token);
        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
//        Toast.makeText(this, "Web Service error: " + error.getMessage(), Toast.LENGTH_LONG).show();
//        finish();
        onBackPressed();
    }

    /* Session Listener methods */

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: " + session.getSessionId());
        // initialize Publisher and set this object to listen to Publisher events
//        mPublisher = new Publisher.Builder(this).build();
//        mPublisher.setPublisherListener(this);
        // set publisher video style to fill view
//        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
//                BaseVideoRenderer.STYLE_VIDEO_FILL);
//        mPublisherViewContainer.addView(mPublisher.getView());
//        if (mPublisher.getView() instanceof GLSurfaceView) {
//            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
//        }

//        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {

        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
//        onBackPressed();
        //usefullData.make_alert("Something went wrong", true, Screen_share_activity.this);
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
        hidedismissProgress();
        upload_btn.setVisibility(View.VISIBLE);
        upload_btn.setEnabled(true);
        if (mSubscriber == null) {
            mSubscriber = new Subscriber
                    .Builder(this, stream)
                    .renderer(new BasicCustomVideoRenderer(this))
                    .build();
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
            dismiss_alert();
            subscriber_hit(Definitions.SUBSCRIBE_STATUS);
            show_draw_view();
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
        //alert(getResources().getString(R.string.share_error));
        hide_draw_view();
        finish();

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());

//        showOpenTokError();
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());
//        showOpenTokError();
    }

//    private void showOpenTokError() {
//
//        usefullData.make_toast("Something went wrong");
//      Toast.makeText(this, opentokError.getErrorDomain().name() +": " +opentokError.getMessage() + " Something went wrong ", Toast.LENGTH_LONG).show();
//        onBackPressed();
//        back.performClick();
//
//    }

    private void showConfigError(String alertTitle, final String errorMessage) {
        Log.e(LOG_TAG, "Error " + alertTitle + ": " + errorMessage);

        alert("Something went wrong");
    }


    public void alert(final String msg) {
//        loader = MyCustomProgressDialog.makeProgress(Screen_share_activity.this, msg, "Please wait...");

        show_dialog(msg,"Please wait...");
        if (!((Activity) this).isFinishing()) {
            dialog.show();
        }




    }

    public void dismiss_alert() {
        usefullData.dismissProgress();
        ((Activity) this).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try {
                    if ((dialog != null) && dialog.isShowing()) {
//                        m_dialog.dismiss();
                        dialog.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void onPause() {

        super.onPause();
        if (mSession != null) {
            mSession.onPause();
        }
        usefullData.dismissProgress();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
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
    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }
//
//
//    public static class MyCustomProgressDialog extends ProgressDialog {
//        static String set_title, set_msg;
//
//        public static ProgressDialog makeProgress(Context context, String title, String msg) {
//            MyCustomProgressDialog dialog = new MyCustomProgressDialog(context, R.style.CustomDialog_new);
//            dialog.setIndeterminate(true);
//
//            dialog.setCancelable(false);
//            set_title = title;
//            set_msg = msg;
//            return dialog;
//        }
//
//        public MyCustomProgressDialog(Context context) {
//            super(context);
//        }
//
//        public MyCustomProgressDialog(Context context, int theme) {
//            super(context, theme);
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.custom_progress);
//
//            TextView pop_title = (TextView) findViewById(R.id.pop_title);
//            TextView pop_msg = (TextView) findViewById(R.id.pop_msg);
//            Button back_pop = (Button) findViewById(R.id.back);
//
//            pop_title.setText(set_title);
//            pop_msg.setText(set_msg);
//
//            pop_title.setTypeface(usefullData.get_semibold());
//            pop_msg.setTypeface(usefullData.get_montserrat_semibold());
//            back_pop.setTypeface(usefullData.get_google_bold());
//
//
//            back_pop.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    dismiss();
////                    back_handler();
////                    onBackPressed();
//
//                    return false;
//                }
//            });
//
//        }
//
////        @Override
////        public void onBackPressed() {
////            back_handler();
////            super.onBackPressed();
////        }
//
//        @Override
//        public void show() {
//            super.show();
//
//        }
//
//        @Override
//        public void dismiss() {
//            super.dismiss();
//
//        }
//    }

    public void back_handler() {
//        back.performClick();
        subscriber_hit(Definitions. SUBSCRIBE_STATUS_BACK);
        dismiss_alert();
        if (mSession != null) {
            mSession.disconnect();
        }
        this.mWakeLock.release();
        onBackPressed();
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                back_handler();
                break;
            case R.id.blue_color_btn:
                mDrawingView.setPenColor(getResources().getColor(R.color.screeshot_blue));
                blue_color_btn.setBackground(getResources().getDrawable(R.drawable.blue_color_with_white_border));
                red_color_btn.setBackground(getResources().getDrawable(R.drawable.red_color_without_white_border));
                break;
            case R.id.undo_btn:
                check_activity(DrawingView.savePath.size()-1);
                mDrawingView.undo();
                if(mDrawingView.getPenColor()==getResources().getColor(R.color.screeshot_red)){
                    blue_color_btn.setBackground(getResources().getDrawable(R.drawable.blue_color_without_white_border));
                    red_color_btn.setBackground(getResources().getDrawable(R.drawable.red_color_with_white_border));
                }else {
                    blue_color_btn.setBackground(getResources().getDrawable(R.drawable.blue_color_with_white_border));
                    red_color_btn.setBackground(getResources().getDrawable(R.drawable.red_color_without_white_border));
                }

                break;
            case R.id.save_btn:
                if(saveData.getBoolean(Definitions.ANNOTATION_ENABLE)) {
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    Permissions.check(this, permissions, null, null, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            if (usefullData.isNetworkConnected()) {
                                if (mSubscriber == null) {
                                    alert(getResources().getString(R.string.capture));
                                    return;
                                } else {
                                    save_btn.setEnabled(false);
                                    save_btn.setAlpha(.4f);
                                    saveData.save(Definitions.ANNOTATION_ENABLE,false);
                                    BasicCustomVideoRenderer.paint = ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).captureScreenshot();

                                    mDrawingView.saveImage(Environment.getExternalStorageDirectory().toString(), "Drawing_temp", Bitmap.CompressFormat.PNG, 70);
                                    Bitmap drawBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/" + "Drawing_temp" + ".png");
                                    final_bitmap = overlay(BasicCustomVideoRenderer.paint, drawBitmap);
                                    try{
                                        Intent i = new Intent(Screen_share_activity.this, Screenshare_screenshot_service.class);
                                        startService(i);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    mDrawingView.clear();

                                }
                            }
                        }
                    });

                }

                break;

            case R.id.upload:
                usefullData.showProgress();
                if(saveData.getBoolean(Definitions.ANNOTATION_ENABLE)) {
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    Permissions.check(this, permissions, null, null, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            if (usefullData.isNetworkConnected()) {
                                if (mSubscriber == null) {
                                    usefullData.dismissProgress();
                                    alert(getResources().getString(R.string.capture));
                                    return;
                                } else {
                                    upload_btn.setEnabled(false);
                                    upload_btn.setAlpha(.4f);
                                    saveData.save(Definitions.ANNOTATION_ENABLE,false);
                                    BasicCustomVideoRenderer.paint = ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).captureScreenshot();
                                    mDrawingView.saveImage(Environment.getExternalStorageDirectory().toString(), "Drawing_temp", Bitmap.CompressFormat.PNG, 70);
                                    Bitmap drawBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/" + "Drawing_temp" + ".png");
                                    final_bitmap = overlay(BasicCustomVideoRenderer.paint, drawBitmap);
                                    takeScreenshotForPowerAI();

//                                    Intent i = new Intent(Screen_share_activity.this, Screenshare_screenshot_service_power_api.class);
//                                    startService(i);
                                    mDrawingView.clear();
                                }
                            }
                        }
                    });

                }
                break;
            case R.id.back:

                onBackPressed();
                break;
            case R.id.red_color_btn:

                mDrawingView.setPenColor(getResources().getColor(R.color.screeshot_red));
                blue_color_btn.setBackground(getResources().getDrawable(R.drawable.blue_color_without_white_border));
                red_color_btn.setBackground(getResources().getDrawable(R.drawable.red_color_with_white_border));

                break;
            default:
                break;
        }
    }

    public  void check_activity(int size){

        if (size>0) {

//            save_btn.setText(getResources().getString(R.string.ic_tick));
//            save_btn.setBackground(getResources().getDrawable(R.drawable.grident_circle_bg));
            undo_btn.setBackground(getResources().getDrawable(R.drawable.black_circle_layout));

        }else {


//            save_btn.setText(getResources().getString(R.string.ic_cross));
//            save_btn.setBackground(getResources().getDrawable(R.drawable.black_circle_layout));
            undo_btn.setBackground(getResources().getDrawable(R.drawable.undo_start_back));

        }
    }


    private void show_dialog(String title, String msg) {
        dialog = new Dialog(Screen_share_activity.this);
        dialog.setContentView(R.layout.custom_progress);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_translucent)));

        dialog.getWindow().setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.8f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);


        TextView pop_title = (TextView) dialog.findViewById(R.id.pop_title);
        TextView pop_msg = (TextView) dialog.findViewById(R.id.pop_msg);
        Button back_pop = (Button) dialog.findViewById(R.id.back);

        pop_title.setText(title);
        pop_msg.setText(msg);

        pop_title.setTypeface(usefullData.get_semibold());
        pop_msg.setTypeface(usefullData.get_montserrat_semibold());
        back_pop.setTypeface(usefullData.get_google_bold());

        back_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_handler();
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK){
                    back_handler();
                }


                return true;
            }
        });

    }

    public void hide_draw_view(){
        back_btn.setVisibility(View.GONE);
        undo_btn.setVisibility(View.GONE);
        red_color_btn.setVisibility(View.GONE);
        blue_color_btn.setVisibility(View.GONE);
        save_btn.setVisibility(View.GONE);
        mDrawingView.setVisibility(View.GONE);
    }

    public void show_draw_view(){

        back_btn.setVisibility(View.VISIBLE);
        undo_btn.setVisibility(View.VISIBLE);
        red_color_btn.setVisibility(View.VISIBLE);
        blue_color_btn.setVisibility(View.VISIBLE);
        save_btn.setVisibility(View.VISIBLE);
        mDrawingView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onSelectionEvent(Classified selectionEvent) {

    }



    /**
     * gaurav IBM Power AI screenshot and API call. No need to call Service as done before.
     */

    // TAKING SCREENSHOT FOR POWER API
    private void takeScreenshotForPowerAI() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            //usefullData.showProgress();
            // image naming and path  to include sd card  appending name you choose for file
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Jukepad");
            myDir.mkdirs();
            String myFile = "/" + now + ".jpeg";
            File file = new File(myDir, myFile.trim());

            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);

                // quality of image is kept down by
                final_bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                out.flush();
                out.close();
                uploadPowerAI(file);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    // power AI upload screenshare images
    public void uploadPowerAI(File file) {
        MyApiEndpointInterface apiService =
                NetworkHandler.getRetrofit(0).create(MyApiEndpointInterface.class);

        // condition 0 is for IBM POWER URI

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
        Call<PowerAIRes> call = apiService.uploadImage(screenShareId, body);

        call.enqueue(new Callback<PowerAIRes>() {

            @Override
            public void onFailure(Call<PowerAIRes> call, Throwable t) {
                //save_data.saveProgress("isFailurePowerAPI", "true");
                Normal_toast.show(getApplicationContext(), "Upload Failed TimeOut", false);
                //dismissProgress();
                // Enable button
                usefullData.dismissProgress();
                saveData.save(Definitions.ANNOTATION_ENABLE,true);
                if (upload_btn != null ) {
                    upload_btn.setEnabled(true);
                    upload_btn.setAlpha(1f);
                }
                //stopSelf();
            }

            @Override
            public void onResponse(Call<PowerAIRes> call, retrofit2.Response<PowerAIRes> response) {
                //dismissProgress();
                // Enable button
                usefullData.dismissProgress();
                saveData.save(Definitions.ANNOTATION_ENABLE,true);
                if (upload_btn != null ) {
                    upload_btn.setEnabled(true);
                    upload_btn.setAlpha(1f);
                }


                Classified classified = new Classified();
                int statusCode = response.code();
                Log.d("statuscode", String.valueOf(statusCode));
                if (statusCode == 200) {
                    if(file.exists()) {
                        if(response.body().getClassified() != null) {
                            classified.setClassified(response.body().getClassified());
                            classifiedArrayList = response.body().getClassified();
                            Normal_toast.show(getApplicationContext(), "Screenshot Uploaded", false);
                        }else{
                           // Normal_toast.show(getApplicationContext(), "Change PowerAI API KEY", false);
                        }

//                        EventBus.getDefault().postSticky(classified);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("image", file.getAbsolutePath());
//                        Intent intent = new Intent(getApplicationContext(), AIPicViewer.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);


                        rl_ai.setVisibility(View.VISIBLE);
                        rl_screenshare.setVisibility(View.GONE);
                        imgFile_ai = new  File(file.getAbsolutePath());
                        bitmap_ai = BitmapFactory.decodeFile(imgFile_ai.getAbsolutePath());
                        loadImage(bitmap_ai);
                        showMarksOnImage();

                    }

                }else{
                    Normal_toast.show(getApplicationContext(), "Upload Failed" + " " + String.valueOf(statusCode), false);
                }
            }

        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void iniIntent(){
        Intent myIntent = getIntent(); // this getter is just for example purpose, can differ
        if (myIntent !=null && myIntent.getExtras()!=null)
            fromWhere = myIntent.getExtras().getString("from").toString();
    }



    public void showCustomProgress() {

        try {
            if ((dialogLoader != null) && dialogLoader.isShowing()) {
                dialogLoader.dismiss();
                dialogLoader = null;
            }
            dialogLoader = new CallPhoneDialog(this);
            dialogLoader.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void hidedismissProgress() {
        try {
            if (dialogLoader != null) {
                if (dialogLoader.isShowing()) {
                    dialogLoader.dismiss();
                    dialogLoader = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findViewsAI() {
        mDrawingView_ai = (DrawingView) findViewById(R.id.img_screenshot_ai);
        imageView_ai = (ImageView)findViewById( R.id.imageView_ai );
        llBack_ai = (LinearLayout)findViewById( R.id.ll_back_ai );
        back_btn_ai = (Button)findViewById( R.id.back_btn_ai );
        undo_btn_ai = (Button)findViewById( R.id.undo_btn_ai );
        red_color_btn_ai = (Button)findViewById( R.id.red_color_btn_ai );
        blue_color_btn_ai = (Button)findViewById( R.id.blue_color_btn_ai );
        onOff_ai = (Button)findViewById( R.id.onOff_ai );
        save_btn_ai = (Button)findViewById( R.id.save_btn_ai );
        fab_ai = (Button) findViewById(R.id.fab_ai);

        fab_ai.setTypeface(usefullData.get_awosome_font());
        back_btn_ai.setTypeface(usefullData.get_awosome_font());
        undo_btn_ai.setTypeface(usefullData.get_awosome_font());
        red_color_btn_ai.setTypeface(usefullData.get_awosome_font());
        blue_color_btn_ai.setTypeface(usefullData.get_awosome_font());
        onOff_ai.setTypeface(usefullData.get_awosome_font());
        save_btn_ai.setTypeface(usefullData.get_awosome_font());
        initDrawingAI();
        //lay_btn = (LinearLayout) findViewById(R.id.lay_btn);
        //mSubscriberViewContainer = (RelativeLayout) findViewById(R.id.subscriber_container);
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/Jukepad";

            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            mDrawingView_ai.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mDrawingView.getDrawingCache());
            mDrawingView_ai.setDrawingCacheEnabled(false);
            File file = new File (mPath, "/" + now + ".png");
            FileOutputStream outputStream = new FileOutputStream(file);
            int quality = 70;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public void showMarksOnImage(){
        if(classifiedArrayList != null) {
            if (classifiedArrayList.size() != 0) {
                isShowingMarks = true;
                onOff_ai.setVisibility(View.VISIBLE);
                onOff_ai.setEnabled(true);
                mutableBitmap_ai = bitmap_ai.copy(Bitmap.Config.ARGB_8888, true);
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setColor(getResources().getColor(R.color.pink_color));

                Paint paintNew = new Paint();
                paintNew.setColor(getResources().getColor(R.color.pink_color));

                Canvas cnvs = new Canvas(mutableBitmap_ai);
                cnvs.drawBitmap(BitmapFactory.decodeFile(imgFile_ai.getAbsolutePath()), 0, 0, null);

                paint.setTypeface(usefullData.get_montserrat_bold());

                if (classifiedArrayList.size() != 0) {
                    for (int i = 0; i < classifiedArrayList.size(); i++) {
                        //drawRect(xmin, ymin, xmax, ymax)
                        cnvs.drawRect(classifiedArrayList.get(i).getXmin()
                                , classifiedArrayList.get(i).getYmin()
                                , classifiedArrayList.get(i).getXmax()
                                , classifiedArrayList.get(i).getYmax()
                                , paint);

                        paintNew.setTextSize(30);
                        paintNew.setStyle(Paint.Style.FILL);
                        paintNew.setStrokeWidth(40);
                        paintNew.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                        double roundOffConfidence = Math.round(classifiedArrayList.get(i).getConfidence() * 100.0) / 100.0;

                        String label = classifiedArrayList.get(i).getLabel();
                        String text = "";
                        if(!label.equalsIgnoreCase("nerve")){
                            text = classifiedArrayList.get(i).getLabel() +
                                    System.lineSeparator() +
                                    roundOffConfidence;
                        }else{
                            text = "" +
                                    System.lineSeparator() +
                                    roundOffConfidence;
                        }
                        int middle = classifiedArrayList.get(i).getXmax() - classifiedArrayList.get(i).getXmin();
                        cnvs.drawText(text,
                                classifiedArrayList.get(i).getXmin() + 10
                                , (classifiedArrayList.get(i).getYmax() + 35), paintNew);

                        loadImage(mutableBitmap_ai);
                    }
                }
            } else {
                mDrawingView_ai.loadImage(bitmap_ai);
                onOff_ai.setVisibility(View.INVISIBLE);
                onOff_ai.setEnabled(false);
//                lay_btn.setGravity(Gravity.RIGHT);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                params.setMargins(0, 0, 10, 0);
//                lay_btn.setLayoutParams(params);
            }
        }
    }

    public void loadImage(Bitmap bitmap) {
        if(bitmap_ai!=null) {
            mDrawingView_ai.loadImage(bitmap);
        }else {
            finish();
        }
    }

    public void initDrawingAI(){
        mDrawingView_ai.initializePen();
        mDrawingView_ai.setPenSize(20);
        mDrawingView_ai.setPenColor(getResources().getColor(R.color.screeshot_red));
    }



    public void showCustomToast(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 400);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}