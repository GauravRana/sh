package com.sydehealth.sydehealth.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.Manifest;

import com.google.android.material.textfield.TextInputLayout;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.volley.UserAPI;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.database.DatabaseHelper;
import com.sydehealth.sydehealth.utility.App_startup;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.LockableScrollView;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.WIFI_SERVICE;
import static org.webrtc.ContextUtils.getApplicationContext;

public class Setting_activity extends Fragment implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener{

    EditText user_name, user_email, new_pswd, confirm_pswd ;
    UsefullData objUsefullData;
    SaveData save_data;
    LinearLayout sign_out_layer, profile_layer, device_layer, about_layer, profile_data, about_data, device_data;
    int Brightness;
    SeekBar brightness_seekBar;
    TextView  bright_label, wifi_name;
    LinearLayout wifi_btn, bluetooth_btn;
    View view;
    Context mContext;
    TextView app_version, app_desc;
    TextView info_user, info_pswd, title;
    Button save;
    TextInputLayout user_name_layer, user_email_layer, new_pswd_layer, confirm_pswd_layer;
    LockableScrollView main_scrollview;
    static DatabaseHelper db;
    LinearLayout keyboard_layout;
    RelativeLayout root;
    boolean email_focus=false;
    Button yahoo_txt, outlook_txt, gmail_txt;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout extra_space;

    private int initial = 0;
    private static final int LOCATION = 1;

    private TextView info_pswd_txt, unInfo;

    private TextView tvProfile, tvDevice, tvAbout, tvSignOut, tvWifi, tvB, titleT;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Tab_activity.ll_bottombar.setVisibility(View.VISIBLE);
        if(view==null){
            view=inflater.inflate(R.layout.activity_setting_activity,container,false);
            initView();
        }
        return view;
    }





    /**
     * Method to initialize views
     */
    private void initView() {
        mContext=getActivity();

        save_data = new SaveData(mContext);
        objUsefullData = new UsefullData(mContext);
        root=(RelativeLayout) view.findViewById(R.id.root);
        keyboard_layout = (LinearLayout) view.findViewById(R.id.keyboard_extra_key);
        yahoo_txt=(Button) view.findViewById(R.id.yahoo_txt);
        outlook_txt=(Button) view.findViewById(R.id.outlook_txt);
        gmail_txt=(Button) view.findViewById(R.id.gmail_txt);
        yahoo_txt.setOnClickListener(this);
        outlook_txt.setOnClickListener(this);
        gmail_txt.setOnClickListener(this);

        db = new DatabaseHelper(getActivity());
        main_scrollview=(LockableScrollView) view.findViewById(R.id.main_scrollview);
        main_scrollview.setScrollingEnabled(false);
        title=(TextView) view.findViewById(R.id.title);
        save=(Button) view.findViewById(R.id.save_details_btn);
        save.setAlpha(.4f);
        save.setEnabled(false);
        profile_data=(LinearLayout) view.findViewById(R.id.profile_data);
        about_data=(LinearLayout) view.findViewById(R.id.about_data);
        device_data=(LinearLayout) view.findViewById(R.id.device_data);
        profile_layer=(LinearLayout) view.findViewById(R.id.profile_layer);
        device_layer=(LinearLayout) view.findViewById(R.id.device_layer);
        about_layer=(LinearLayout) view.findViewById(R.id.about_layer);
        info_pswd=(TextView) view.findViewById(R.id.info_pswd);
        info_user=(TextView) view.findViewById(R.id.info_user);
        user_name_layer=(TextInputLayout) view.findViewById(R.id.user_name_layer);
        user_email_layer=(TextInputLayout) view.findViewById(R.id.user_email_layer);
        new_pswd_layer=(TextInputLayout) view.findViewById(R.id.new_pswd_layer);
        confirm_pswd_layer=(TextInputLayout) view.findViewById(R.id.confirm_pswd_layer);
        info_pswd_txt = (TextView) view.findViewById(R.id.info_pswd_txt);
        unInfo = (TextView) view.findViewById(R.id.unInfo);


        brightness_seekBar = (SeekBar)view.findViewById(R.id.brightness_seekBar);
        app_version=(TextView) view.findViewById(R.id.app_version);
        app_desc=(TextView) view.findViewById(R.id.app_desc);
        wifi_btn=(LinearLayout) view.findViewById(R.id.wifi_btn);
        bluetooth_btn=(LinearLayout) view.findViewById(R.id.bluetooth_btn);

        bright_label=(TextView) view.findViewById(R.id.bright_label);
        wifi_name=(TextView) view.findViewById(R.id.wifi_name);


        user_name=(EditText) view.findViewById(R.id.user_name);
        user_email=(EditText) view.findViewById(R.id.user_email);
        new_pswd=(EditText) view.findViewById(R.id.new_pswd);
        confirm_pswd=(EditText) view.findViewById(R.id.confirm_pswd);
        sign_out_layer=(LinearLayout) view.findViewById(R.id.sign_out_layer);

        tvProfile =  view.findViewById(R.id.tv_profile);
        tvDevice =  view.findViewById(R.id.tv_Device);
        tvAbout =  view.findViewById(R.id.tv_About);
        tvSignOut =  view.findViewById(R.id.tv_signOut);

        tvWifi = view.findViewById(R.id.tv_wifi);
        tvB =  view.findViewById(R.id.tv_Bluetooth);

        extra_space = (LinearLayout) view.findViewById(R.id.extra_space);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.setting_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.switch_color);
        swipeRefreshLayout.setSoundEffectsEnabled(true);


        user_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        user_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        new_pswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirm_pswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        setFontStyle();
        listeners();

        if (objUsefullData.isNetworkConnected()) {
            get_details(true);
        } else {
            objUsefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);

        }

    }


    /**
     * Method to initialize listeners
     */
    private void listeners() {

        bluetooth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent bluetoothPicker = new Intent("android.bluetooth.devicepicker.action.LAUNCH");
//                startActivity(bluetoothPicker);

//                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
//                intent.setComponent(cn);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity( intent);

                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);

//                    public static final String EXTRA_NEED_AUTH = "android.bluetooth.devicepicker.extra.NEED_AUTH";
//                    public static final String EXTRA_FILTER_TYPE = "android.bluetooth.devicepicker.extra.FILTER_TYPE";
//                    public static final String EXTRA_LAUNCH_PACKAGE = "android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE";
//                    public static final String EXTRA_LAUNCH_CLASS = "android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS";

//                Intent settingsIntent = new Intent();
//                settingsIntent.setClassName("com.android.settings",      "com.android.settings.bluetooth.BluetoothSettings");
//                settingsIntent.putExtra("android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE", "mypackage.bttoggle");
//                settingsIntent.putExtra("android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS", "mypackage.bttoggle.BluetoothWidget");
////                PendingIntent settingsPendingIntent = PendingIntent.getActivity(getActivity(), 0, settingsIntent, 0);
////                views.setOnClickPendingIntent(R.id.btnSettings, settingsPendingIntent);
//                startActivity(settingsIntent);
            }

        });
        wifi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));

            }
        });

        user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    Rect r = new Rect();
//                    root.getWindowVisibleDisplayFrame(r);
//                    int screenHeight = root.getRootView().getHeight();
//                    int keyboardHeight = screenHeight - (r.bottom);
//                    int h = getResources().getDimensionPixelSize(R.dimen._20sdp);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
//                    params.setMargins(0, 0, 0, keyboardHeight);
//                    keyboard_layout.setLayoutParams(params);
//                    show_keyboard_layout();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        user_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {

                email_focus = hasfocus;
                if(hasfocus) {
                    show_keyboard_layout();
                    Rect r = new Rect();
                    root.getWindowVisibleDisplayFrame(r);
                    int screenHeight = root.getRootView().getHeight();
                    int keyboardHeight = screenHeight - (r.bottom);
                    int h = getResources().getDimensionPixelSize(R.dimen._20sdp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                    params.setMargins(0, 0, 0, 430);
                    keyboard_layout.setLayoutParams(params);
                    show_keyboard_layout();
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,
//                            (int)getResources().getDimension(R.dimen._165sdp)); // or set height to any fixed value you want
//                    extra_space.setLayoutParams(lp);
                }else {
                    keyboard_layout.setVisibility(View.GONE);

                }
            }
        });

//        user_email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                keyboard_layout.setVisibility(View.VISIBLE);
//            }
//        });


        KeyboardVisibilityEvent.setEventListener(getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            main_scrollview.setScrollingEnabled(true);
                            if(email_focus) {
                                Rect r = new Rect();
                                root.getWindowVisibleDisplayFrame(r);
                                int screenHeight = root.getRootView().getHeight();
                                int keyboardHeight = screenHeight - (r.bottom);
                                int h = getResources().getDimensionPixelSize(R.dimen._20sdp);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                                if(initial == 0){
                                    initial = 1;
                                    if(keyboardHeight < 600) {
                                        save_data.saveKeyBHeight("key", keyboardHeight);
                                        params.setMargins(0, 0, 0, 430);
                                        keyboard_layout.setLayoutParams(params);
                                        //show_keyboard_layout();
                                    }else{
                                        keyboard_layout.setVisibility(View.GONE);
//                                        user_email.clearFocus();
//                                        user_email.setFocusable(true);
//                                        user_email.requestFocus();
                                    }
                                }
                                else if(keyboardHeight > save_data.getKeyBHeight("key")) {
                                    params.setMargins(0, 0, 0, 430);
                                    keyboard_layout.setLayoutParams(params);
                                    show_keyboard_layout();
                                }else{
                                    params.setMargins(0, 0, 0, 430);
                                    keyboard_layout.setLayoutParams(params);
                                    show_keyboard_layout();
                                }

                            }

                        }else {
                            keyboard_layout.setVisibility(View.GONE);
                            main_scrollview.scrollTo(0,0);
                            main_scrollview.setScrollingEnabled(false);
                        }

                    }
                });


        brightness_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(mContext)) {
                        // Do stuff here
                        Settings.System.putInt(mContext.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,i);
                    }
                    else {
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        user_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()&& !TextUtils.isEmpty(user_name.getText().toString().trim())) {
                    save.setAlpha(1f);
                    save.setEnabled(true);
                }else {
                    save.setAlpha(0.4f);
                    save.setEnabled(false);
                }

            }
        });
        user_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()&& !TextUtils.isEmpty(user_name.getText().toString().trim())) {
                    save.setAlpha(1f);
                    save.setEnabled(true);
                }else {
                    save.setAlpha(0.4f);
                    save.setEnabled(false);
                }

            }
        });
        new_pswd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()&& !TextUtils.isEmpty(user_name.getText().toString().trim())) {
                    save.setAlpha(1f);
                    save.setEnabled(true);
                }else {
                    save.setAlpha(0.4f);
                    save.setEnabled(false);
                }

            }
        });
        confirm_pswd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()&& !TextUtils.isEmpty(user_name.getText().toString().trim())) {
                    save.setAlpha(1f);
                    save.setEnabled(true);
                }else {
                    save.setAlpha(0.4f);
                    save.setEnabled(false);
                }

            }
        });

        user_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.user_email || id == EditorInfo.IME_ACTION_DONE) {

                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((getActivity().getWindow().getDecorView().getApplicationWindowToken()), 0);


                    return true;
                }
                return false;
            }
        });

        user_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        user_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    user_email.requestFocus();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
        new_pswd.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        new_pswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    confirm_pswd.requestFocus();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });



//        edit_basic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancel_password();
//                user_name.setTextColor(getResources().getColor(R.color.white));
//                user_email.setTextColor(getResources().getColor(R.color.white));
//                if (edit_basic.getText().toString().equals(getResources().getString(R.string.save))) {
//
//                    name_error.setText("");
//                    email_error.setText("");
//
//                    if (objUsefullData.isNetworkConnected()) {
//
//
//                        if (TextUtils.isEmpty(user_name.getText().toString())) {
////                            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
////                            user_name.startAnimation(shake);
//                            name_error.setText(getResources().getString(R.string.req));
//                        } else if (TextUtils.isEmpty(user_email.getText().toString())) {
////                            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
////                            user_email.startAnimation(shake);
//                            email_error.setText(getResources().getString(R.string.req));
//                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()) {
////                            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
////                            user_email.startAnimation(shake);
//                            email_error.setText(getResources().getString(R.string.email_valid));
//                        } else {
//                            update_user_details(user_name.getText().toString(), user_email.getText().toString());
//                            cancel_password();
//
//                        }
//                    } else {
//                        objUsefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);
//
//                    }
//                } else {
//                    cancel_basic.setVisibility(View.VISIBLE);
//                    edit_basic.setText(getResources().getString(R.string.save));
//                    user_name.setEnabled(true);
//                    user_email.setEnabled(true);
//                    name_error.setText("");
//                    email_error.setText("");
//
//
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txt.getLayoutParams();
//                    int margin = getResources().getDimensionPixelSize(R.dimen._311sdp);
//                    params.width = margin;
//                    txt.setLayoutParams(params);
//
//                }
//
//
//            }
//        });
//        edit_pswd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                cancel_basic();
//                edit_pswd.requestFocus();
//
//                if(edit_pswd.getText().toString().equals(getResources().getString(R.string.save))){
//
//                    pswd_error.setText("");
//                    confirm_error.setText("");
//
//                    if(objUsefullData.isNetworkConnected())
//                    {
//                        if(new_pswd.getText().toString().trim().equals("")) {
//                            pswd_error.setText(getResources().getString(R.string.req));
//                        }else if(confirm_pswd.getText().toString().trim().equals("")) {
//                            confirm_error.setText(getResources().getString(R.string.req));
//                        }else{
//
//                            if (new_pswd.getText().toString().equals(confirm_pswd.getText().toString().trim())) {
//                                if(objUsefullData.password_Validator(new_pswd.getText().toString().trim()) && objUsefullData.password_Validator(new_pswd.getText().toString().trim())) {
//                                    change_password(new_pswd.getText().toString().trim(), confirm_pswd.getText().toString().trim());
//                                    cancel_password();
//                                }else {
//                                    objUsefullData.make_alert(getResources().getString(R.string.pswd_valid),false,mContext);
//                                }
//
//                            } else {
//                                objUsefullData.make_alert(getResources().getString(R.string.pswd_not_match),false,mContext);
//                            }
//                        }
//                    }else {
//                        objUsefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);
//                    }
//
//
//
//
//
//                }else {
//                    cancel_pswd.setVisibility(View.VISIBLE);
//                    edit_pswd.setText(getResources().getString(R.string.save));
//                    new_pswd.setEnabled(true);
//                    confirm_pswd.setEnabled(true);
//                    pswd_error.setText("");
//                    confirm_error.setText("");
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chng_pswd_txt.getLayoutParams();
//                    int margin = getResources().getDimensionPixelSize(R.dimen._295sdp);
//                    params.width = margin;
//                    chng_pswd_txt.setLayoutParams(params);
//                }
//
//
//            }
//        });


        confirm_pswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.confirm_pswd || id == EditorInfo.IME_ACTION_DONE) {

                    ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((getActivity().getWindow().getDecorView().getApplicationWindowToken()), 0);


                    return true;
                }
                return false;
            }
        });

        sign_out_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_out_layer.setEnabled(false);
                logout();

            }
        });
        profile_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device_layer.setBackgroundColor(getResources().getColor(R.color.white));
                about_layer.setBackgroundColor(getResources().getColor(R.color.white));
                profile_layer.setBackgroundColor(getResources().getColor(R.color.divider));
                profile_data.setVisibility(View.VISIBLE);
                about_data.setVisibility(View.GONE);
                device_data.setVisibility(View.GONE);
                title.setText("Profile");
                swipeRefreshLayout.setEnabled(true);
                hideKeyboard(getActivity());
            }
        });
        device_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile_layer.setBackgroundColor(getResources().getColor(R.color.white));
                about_layer.setBackgroundColor(getResources().getColor(R.color.white));
                device_layer.setBackgroundColor(getResources().getColor(R.color.divider));
                profile_data.setVisibility(View.GONE);
                device_data.setVisibility(View.VISIBLE);
                about_data.setVisibility(View.GONE);
                title.setText("Device");
                swipeRefreshLayout.setEnabled(false);
                hideKeyboard(getActivity());

            }
        });
        about_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device_layer.setBackgroundColor(getResources().getColor(R.color.white));
                profile_layer.setBackgroundColor(getResources().getColor(R.color.white));
                about_layer.setBackgroundColor(getResources().getColor(R.color.divider));
                profile_data.setVisibility(View.GONE);
                about_data.setVisibility(View.VISIBLE);
                device_data.setVisibility(View.GONE);
                title.setText("About");
                swipeRefreshLayout.setEnabled(true);
                hideKeyboard(getActivity());

            }
        });

        profile_layer.performClick();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (objUsefullData.isNetworkConnected()) {
                    if (TextUtils.isEmpty(user_name.getText().toString().trim())) {
                        objUsefullData.make_alert(getResources().getString(R.string.req),false,mContext);
                    } else if (TextUtils.isEmpty(user_email.getText().toString().trim())) {
                        objUsefullData.make_alert(getResources().getString(R.string.req),false,mContext);
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString().trim()).matches()) {
                        objUsefullData.make_alert(getResources().getString(R.string.email_valid),false,mContext);
                    }else if (new_pswd.getText().toString().equals(confirm_pswd.getText().toString().trim())) {
                        if(new_pswd.getText().toString().trim().length()==0){
                            update_user_details(user_name.getText().toString().trim(), user_email.getText().toString().trim(),new_pswd.getText().toString().trim(), confirm_pswd.getText().toString().trim(),false);
                        }else {
                            if (objUsefullData.password_Validator(new_pswd.getText().toString().trim()) && objUsefullData.password_Validator(new_pswd.getText().toString().trim())) {
                                update_user_details(user_name.getText().toString().trim(), user_email.getText().toString().trim(), new_pswd.getText().toString().trim(), confirm_pswd.getText().toString().trim(),true);
                            } else {
                                objUsefullData.make_alert(getResources().getString(R.string.pswd_valid), false, mContext);
                            }
                        }

                    } else {
                        objUsefullData.make_alert("New password and confirm password does not match",false,mContext);
                    }

                } else {
                    objUsefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);

                }


            }
        });

    }

    /**
     * Set Fonts
     */
    private void setFontStyle() {

        user_name.setTypeface(objUsefullData.get_montserrat_regular());
        user_email.setTypeface(objUsefullData.get_montserrat_regular());
        new_pswd.setTypeface(objUsefullData.get_montserrat_regular());
        confirm_pswd.setTypeface(objUsefullData.get_montserrat_regular());

        wifi_name.setTypeface(objUsefullData.get_montserrat_regular());
        bright_label.setTypeface(objUsefullData.get_montserrat_regular());
        app_version.setTypeface(objUsefullData.get_montserrat_regular());
        app_desc.setTypeface(objUsefullData.get_montserrat_regular());

        info_pswd.setTypeface(objUsefullData.get_awosome_font_solid());
        info_user.setTypeface(objUsefullData.get_awosome_font_solid());

        save.setTypeface(objUsefullData.get_montserrat_regular());
        user_name_layer.setTypeface(objUsefullData.get_montserrat_regular());
        user_email_layer.setTypeface(objUsefullData.get_montserrat_regular());

        new_pswd_layer.setTypeface(objUsefullData.get_montserrat_regular());
        confirm_pswd_layer.setTypeface(objUsefullData.get_montserrat_regular());
        info_pswd_txt.setTypeface(objUsefullData.get_montserrat_regular());
        unInfo.setTypeface(objUsefullData.get_montserrat_regular());
        tvProfile.setTypeface(objUsefullData.get_montserrat_regular());
        tvDevice.setTypeface(objUsefullData.get_montserrat_regular());
        tvAbout.setTypeface(objUsefullData.get_montserrat_regular());
        tvSignOut.setTypeface(objUsefullData.get_montserrat_regular());
        tvWifi.setTypeface(objUsefullData.get_montserrat_regular());
        tvB.setTypeface(objUsefullData.get_montserrat_regular());
        title.setTypeface(objUsefullData.get_montserrat_regular());
    }


    @Override
    public void onResume() {
        super.onResume();

//        if(InitializeActivity.isIsRadioPlay()){
//            Tab_activity.jcplayerView.continueAudio();
//        }else{
//            Tab_activity.jcplayerView.pause();
//        }

        try {
            Brightness = Settings.System.getInt(mContext.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        brightness_seekBar.setProgress(Brightness);
        if(objUsefullData.isNetworkConnected()){
            wifi_btn.setAlpha(1f);
            String net=objUsefullData.getWifiName(mContext);
            if(net!=null){
                //wifi_name.setText(objUsefullData.getWifiName(mContext)+" Connected");
               String wifiName = tryToReadSSID().replace("\"","");
               wifi_name.setText(wifiName);
            }else {
                wifi_name.setText(" Connected");
            }
        }else {
            wifi_name.setText(" Not Connected");
            wifi_btn.setAlpha(.5f);
        }

//        if (save_data.isExist(Definitions.SURGERY_LOGO)){
//            Glide.with(mContext)
//                    .load(save_data.getString(Definitions.SURGERY_LOGO))
////                    .asBitmap()
////                    .placeholder(R.mipmap.jukepad_text)
////                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(surgery_logo);
//        }
    }

    private void get_details(boolean loader) {

        if (loader) {
            objUsefullData.showProgress();

        }
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put( "X-User-Email", save_data.get(Definitions.USER_EMAIL) );
        headers.put( "X-User-Token", save_data.get(Definitions.USER_TOKEN) );

        UserAPI.get_JsonObjResp(Definitions.GET_USER_DETAILS, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        set_up_values(response);
                        if (loader) {
                            objUsefullData.dismissProgress();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            if (loader) {
                                objUsefullData.dismissProgress();
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if(response.statusCode == 500){
                                    objUsefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                }
                            }else {
                                objUsefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });

    }

    private void set_up_values(JSONObject response)
    {
        try {

            user_name.setText(response.getJSONObject("user").getString("name"));
            user_email.setText(response.getJSONObject("user").getString("email"));
//            if(!response.getJSONObject("about_sydehealth").isNull("version")) {
//                app_version.setText("App Version : " + response.getJSONObject("about_sydehealth").getString("version"));
//            }
            if(!response.getJSONObject("about_sydehealth").isNull("description")){
             app_desc.setVisibility(View.VISIBLE);
             app_desc.setText(response.getJSONObject("about_sydehealth").getString("description"));

            }else {
                app_desc.setVisibility(View.GONE);
            }

            objUsefullData.dismissProgress();
            save.setAlpha(0.4f);
            save.setEnabled(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void update_user_details(String name, String email, String new_pswd_str, String confirm_pswd_str, boolean pswd_update)
    {
        objUsefullData.showProgress();
        //Define Headers
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept", Definitions.VERSION);
        headers.put( "X-User-Email", save_data.get(Definitions.USER_EMAIL) );
        headers.put( "X-User-Token", save_data.get(Definitions.USER_TOKEN) );

        JSONObject request = new JSONObject();
        JSONObject user = new JSONObject();
        try {
            user.put("name", name);
            user.put("email", email);
            user.put("password", new_pswd_str);
            user.put("password_confirmation", confirm_pswd_str);

            request.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TAG", request.toString());

        UserAPI.post_JsonResp(Definitions.UPDATE_USER_DETAILS, request, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("-----response--",""+response);
                        objUsefullData.dismissProgress();
                        objUsefullData.make_alert(getResources().getString(R.string.user_update),true,mContext);

                        try {
                            save_data.save(Definitions.USER_NAME, response.getJSONObject("user").getString("name"));
                            save_data.save(Definitions.USER_EMAIL, response.getJSONObject("user").getString("email"));
                            user_name.setText(response.getJSONObject("user").getString("name"));
                            user_email.setText(response.getJSONObject("user").getString("email"));
                            db.openDB();
                            db.updateTableContent("user_name",response.getJSONObject("user").getString("name"),response.getJSONObject("user").getString("email"));
                            db.updateTableContent("email",response.getJSONObject("user").getString("email"),response.getJSONObject("user").getString("email"));
                            if(pswd_update){
                                db.updateTableContent("user_password",new_pswd.getText().toString().trim(),response.getJSONObject("user").getString("email"));
                            }
                            db.closeDB();
                            new_pswd.setText("");
                            confirm_pswd.setText("");
                            save.setAlpha(.4f);
                            save.setEnabled(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            objUsefullData.dismissProgress();
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if(response.statusCode == 500){
                                    objUsefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                }
                            }else {
                                objUsefullData.make_alert(getResources().getString(R.string.no_internet_speed), true, mContext);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });

    }

    public void change_password(String new_pswd,String conf_pswd)
    {

        objUsefullData.showProgress();
        //Define Headers
        Map<String,String> headers = new HashMap<>();
        headers.put("Accept", Definitions.VERSION);
        headers.put( "X-User-Email", save_data.get(Definitions.USER_EMAIL) );
        headers.put( "X-User-Token", save_data.get(Definitions.USER_TOKEN) );

        JSONObject request = new JSONObject();
        JSONObject user = new JSONObject();
        try {


            user.put("password", new_pswd);
            user.put("confirmation_password", conf_pswd);


            request.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TAG", request.toString());

        UserAPI.put_StringResp(Definitions.CHANGE_PASSWORD, request, headers,null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("TAG", response);
                        objUsefullData.dismissProgress();
                        objUsefullData.make_alert(getResources().getString(R.string.pswd_update),true,mContext);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 500:
                                        objUsefullData.make_alert(getResources().getString(R.string.no_internet_speed),true,mContext);
                                        break;
                                    case 422:
                                        objUsefullData.make_alert(getResources().getString(R.string.error_code),true,mContext);
                                        break;

                                }
                            }else{
                                objUsefullData.make_alert(getResources().getString(R.string.error_code),true,mContext);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        objUsefullData.dismissProgress();
                    }
                });
    }


    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getActivity().getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getActivity().getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.getActivity().dispatchTouchEvent(ev);
    }*/
//    @Override
//    public void onBackPressed() {
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

    private void logout()
    {

        if(!objUsefullData.isNetworkConnected())
        {
            objUsefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);
            sign_out_layer.setEnabled(true);
        }
        else
        {
            objUsefullData.showProgress();
            //Define Headers
            Map<String,String> headers = new HashMap<>();
            headers.put("Accept", Definitions.VERSION);
            headers.put( "X-User-Email", save_data.get(Definitions.USER_EMAIL) );
            headers.put( "X-User-Token", save_data.get(Definitions.USER_TOKEN) );
            Map<String,String> request = new HashMap<>();
            try {
                request.put("token", save_data.getString(Definitions.FIREBASE_TOKEN));
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserAPI.delete_StringResp(Definitions.SIGN_OUT,headers, request,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            objUsefullData.Pause_timer();
                            save_data.remove(Definitions.ID);
                            save_data.remove(Definitions.USER_NAME);
                            save_data.remove(Definitions.USER_EMAIL);
                            save_data.remove(Definitions.SURGERY_ID);
                            save_data.remove(Definitions.USER_TOKEN);
                            save_data.remove(Definitions.CONDITIONS_JSON_DATA);
                            save_data.remove(Definitions.PLAYLIST_JSON_DATA);
                            save_data.remove(Definitions.SURGERY_LOGO);
                            save_data.remove(Definitions.CONDITION_LAST_UPDATE);
                            save_data.remove(Definitions.PLAYLIST_LAST_UPDATE);
                            save_data.remove(Definitions.MAIL_IN_DRAFT);
                            save_data.remove(Definitions.USER_NAME_HEADER);

                            objUsefullData.schedule_time_events();
                            objUsefullData.Pause_timer();
                            sign_out_layer.setEnabled(true);
                            objUsefullData.dismissProgress();
                            getActivity().finish();
                            Intent forgot=new Intent(getActivity(),App_startup.class);
                            startActivity(forgot);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            objUsefullData.dismissProgress();
                            sign_out_layer.setEnabled(true);


                        }
                    });
        }
    }

    public void show_keyboard_layout(){
        try{
            keyboard_layout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yahoo_txt:
                user_email.setText(user_email.getText().toString()+getResources().getString(R.string.yahoo));
                user_email.setSelection(user_email.getText().length());
                break;
            case R.id.outlook_txt:
                user_email.setText(user_email.getText().toString()+getResources().getString(R.string.outlook));
                user_email.setSelection(user_email.getText().length());
                break;
            case R.id.gmail_txt:
                user_email.setText(user_email.getText().toString()+getResources().getString(R.string.gmail));
                user_email.setSelection(user_email.getText().length());
                break;
        }
    }


    @Override
    public void onRefresh() {
        if (objUsefullData.isNetworkConnected()) {
            main_scrollview.setScrollingEnabled(false);
            swipeRefreshLayout.setRefreshing(true);
            hideKeyboard(getActivity());
            user_name.clearFocus();
            user_email.clearFocus();
            new_pswd.clearFocus();
            confirm_pswd.clearFocus();
            get_details(false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            objUsefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);
        }
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private static void showKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert methodManager != null && view != null;
        methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


    private String tryToReadSSID() {
        String ssid = "";
        //If requested permission isn't Granted yet
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request permission from user
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION);
        }else{//Permission already granted
            try{
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    ssid = info.getExtraInfo();
                    Log.d("TAG", "WiFi SSID: " + ssid);
                }
                return ssid;


//                WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
//                if (manager.isWifiEnabled()) {
//                    WifiInfo wifiInfo = manager.getConnectionInfo();
//                    if (wifiInfo != null) {
//                        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
//                        if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
//                            return wifiInfo.getSSID();
//                        }
//                    }
//                }
//                return null;


//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                if(wifiInfo.getSupplicantState() == SupplicantState.COMPLETED){
//                    ssid = wifiInfo.getSSID();//Here you can access your SSID
//                    System.out.println(ssid);
//                }
//                return ssid;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == LOCATION){
            //User allowed the location and you can read it now
            tryToReadSSID();
        }
    }


}
