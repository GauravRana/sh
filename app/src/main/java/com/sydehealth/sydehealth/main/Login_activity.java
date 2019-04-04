package com.sydehealth.sydehealth.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sydehealth.sydehealth.database.DatabaseHelper;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.LockableScrollView;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.volley.UserAPI;
import com.sydehealth.sydehealth.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.ActivityCompat;

import static com.sydehealth.sydehealth.utility.UsefullData.get_montserrat_regular;


/**
 * Created by POPLIFY on 8/11/2017.
 */

public class Login_activity extends Activity implements View.OnClickListener{

    TextView email_error,pswd_error;
    EditText email,pswd;
    Button login, forgot_txt;
    UsefullData usefullData;
    SaveData saveData;
    CheckBox remember;
    LockableScrollView main_scrollview;
    static DatabaseHelper db;
    LinearLayout login_view, sign_in_btn_layer, forgot_layout;
    Intent intent;
    TextInputLayout email_layer, pswd_layer;
    LinearLayout back;
    LinearLayout keyboard_layout;
    RelativeLayout root;
    boolean email_focus=false;
    Button yahoo_txt, outlook_txt, gmail_txt;
    private int initial = 0;
    TextView ic_back, forgot_label;
    private static final int LOCATION = 1;

    private LinearLayout wifi_btn;
    private TextView tv_wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        root=(RelativeLayout) findViewById(R.id.root);
        keyboard_layout = (LinearLayout)findViewById(R.id.keyboard_extra_key);
        yahoo_txt=(Button) findViewById(R.id.yahoo_txt);
        ic_back = (TextView) findViewById(R.id.ic_back);
        outlook_txt=(Button) findViewById(R.id.outlook_txt);
        gmail_txt=(Button) findViewById(R.id.gmail_txt);
        forgot_label = findViewById(R.id.forgot_label);
        wifi_btn = findViewById(R.id.wifi_btn);
        tv_wifi = findViewById(R.id.tv_wifi);
        yahoo_txt.setOnClickListener(this);
        outlook_txt.setOnClickListener(this);
        gmail_txt.setOnClickListener(this);
        wifi_btn.setOnClickListener(this);


        usefullData=new UsefullData(Login_activity.this);
        saveData=new SaveData(Login_activity.this);
        login_view=(LinearLayout) findViewById(R.id.login_view);
        db = new DatabaseHelper(getApplicationContext());
        main_scrollview=(LockableScrollView) findViewById(R.id.main_scrollview);
        main_scrollview.setScrollingEnabled(false);
        email_layer=(TextInputLayout) findViewById(R.id.email_layer);
        pswd_layer=(TextInputLayout) findViewById(R.id.pswd_layer);
        forgot_txt=(Button) findViewById(R.id.forgot_txt);
        email=(EditText) findViewById(R.id.email_field);
        email_error=(TextView) findViewById(R.id.email_field_error);
        pswd=(EditText) findViewById(R.id.pswd);
        pswd_error=(TextView) findViewById(R.id.pswd_error);
        login=(Button) findViewById(R.id.login_btn);
        sign_in_btn_layer=(LinearLayout) findViewById(R.id.sign_in_btn_layer);
        forgot_layout=(LinearLayout) findViewById(R.id.forgot_layout);
        remember=(CheckBox) findViewById(R.id.switch_view);

        remember.setTypeface(get_montserrat_regular());
        forgot_txt.setTypeface(get_montserrat_regular());
        email.setTypeface(get_montserrat_regular());
        email_layer.setTypeface(get_montserrat_regular());
        pswd_layer.setTypeface(get_montserrat_regular());
        pswd.setTypeface(get_montserrat_regular());
        login.setTypeface(get_montserrat_regular());
        email_error.setTypeface(get_montserrat_regular());
        pswd_error.setTypeface(get_montserrat_regular());
        tv_wifi.setTypeface(get_montserrat_regular());
        ic_back.setTypeface(usefullData.get_awosome_font_400());
        forgot_label.setTypeface(get_montserrat_regular());


        email.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI |
                EditorInfo.IME_FLAG_NO_FULLSCREEN);


        pswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);



//        remember.setVisibility(View.GONE);
//        int top = getResources().getDimensionPixelSize(R.dimen._60sdp);
//        LinearLayout.LayoutParams forgot = (LinearLayout.LayoutParams)forgot_layout.getLayoutParams();
//        forgot.setMargins(0, top, 0, 0);
//        forgot_layout.setLayoutParams(forgot);

        login.setAlpha(.4f);
        login.setEnabled(false);

        intent=getIntent();
        if(intent.getStringExtra("request").equals("user")){
            email.setText(intent.getStringExtra("email"));
            email_layer.setVisibility(View.GONE);
            if(intent.getBooleanExtra("remember",false)){
                pswd.setText(intent.getStringExtra("pswd"));
                remember.setChecked(true);
                login.setAlpha(1f);
                login.setEnabled(true);
            }
//            remember.setVisibility(View.GONE);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)sign_in_btn_layer.getLayoutParams();
//            params.setMargins(0, 0, 0, 0);
//            sign_in_btn_layer.setLayoutParams(params);
            int top1 = getResources().getDimensionPixelSize(R.dimen._80sdp);
            LinearLayout.LayoutParams param1 = (LinearLayout.LayoutParams)forgot_layout.getLayoutParams();
            param1.setMargins(0, top1, 0, 0);
            forgot_layout.setLayoutParams(param1);



        }

//        if(saveData.isExist(Definitions.USER_REM_EMAIL)){
//            remember.setChecked(saveData.getBoolean(Definitions.REMEMBER));
//            email.setText(saveData.getString(Definitions.USER_REM_EMAIL));
//            pswd.setText(saveData.getString(Definitions.USER_REM_PSWD));
//            email.setSelection(email.getText().length());
//            pswd.setSelection(pswd.getText().length());
//
//        }

        back=(LinearLayout) findViewById(R.id.back);
        if(intent.getStringExtra("request").equals("hide_back")){
            back.setVisibility(View.INVISIBLE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((getWindow().getDecorView().getApplicationWindowToken()), 0);

                finish();

            }


        });

        pswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.pswd || id == EditorInfo.IME_ACTION_DONE) {

                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((getWindow().getDecorView().getApplicationWindowToken()), 0);

                    login.performClick();

                    return true;
                }
                return false;
            }
        });


        forgot_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot=new Intent(Login_activity.this,Forgot_password.class);
                startActivity(forgot);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usefullData.isNetworkConnected())
                {

                    email_error.setText("");
                    pswd_error.setText("");


                    // Check for a valid email address.
                    if (TextUtils.isEmpty(email.getText().toString().trim())) {
                        Animation shake = AnimationUtils.loadAnimation(Login_activity.this, R.anim.shake);
                        email.startAnimation(shake);
                        email_error.setText(getResources().getString(R.string.req));


                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        Animation shake = AnimationUtils.loadAnimation(Login_activity.this, R.anim.shake);
                        email.startAnimation(shake);
                        email_error.setText(getResources().getString(R.string.email_valid));


                    }else if (TextUtils.isEmpty(pswd.getText().toString().trim())) {
                        Animation shake = AnimationUtils.loadAnimation(Login_activity.this, R.anim.shake);
                        pswd.startAnimation(shake);
                        pswd_error.setText(getResources().getString(R.string.req));


                    }else if (!usefullData.password_Validator(pswd.getText().toString().trim())) {
                        Animation shake = AnimationUtils.loadAnimation(Login_activity.this, R.anim.shake);
                        pswd.startAnimation(shake);
                        pswd_error.setText(getResources().getString(R.string.invalid_pswd));


                    }else {

                        if(!email.getText().toString().trim().equals("")&&!pswd.getText().toString().trim().equals("")) {
                            if (email.getText().toString().charAt(0) == ' ' || pswd.getText().toString().charAt(0) == ' ') {

                                email.setText(email.getText().toString().trim());
                                pswd.setText(pswd.getText().toString().trim());
                            }
                        }

                        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        Permissions.check(Login_activity.this, permissions, null, null, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                attemptLogin(email.getText().toString().trim(),pswd.getText().toString().trim());
                            }
                        });
                    }
                }else {
                    usefullData.make_alert(getResources().getString(R.string.no_internet),true,Login_activity.this);
                }

            }


        });

//        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//                if (isChecked) {
//                    saveData.save(Definitions.REMEMBER,isChecked);
//
//                } else {
//                    saveData.remove(Definitions.USER_REM_EMAIL);
//                    saveData.remove(Definitions.USER_REM_PSWD);
//
//                    saveData.save(Definitions.REMEMBER,isChecked);
//                }
//
//            }
//        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {

                email_focus=hasfocus;
                if(hasfocus) {
                    Rect r = new Rect();

                    root.getWindowVisibleDisplayFrame(r);

                    int screenHeight = root.getRootView().getHeight();
                    int keyboardHeight = screenHeight - (r.bottom);

                    int h = getResources().getDimensionPixelSize(R.dimen._20sdp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
                    params.setMargins(0, 0, 0, keyboardHeight);
                    keyboard_layout.setLayoutParams(params);
                    show_keyboard_layout();


                }else {

                    keyboard_layout.setVisibility(View.GONE);


                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                email_error.setText("");
                if(email.getText().toString().length()==0){
                    login.setAlpha(0.4f);
                    login.setEnabled(false);
                }else {
                    if (usefullData.password_Validator(pswd.getText().toString().trim()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        login.setAlpha(1f);
                        login.setEnabled(true);
                    }else {
                        login.setAlpha(0.4f);
                        login.setEnabled(false);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    pswd.requestFocus();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
        pswd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                pswd_error.setText("");

                if(pswd.getText().toString().length()==0){
                    login.setAlpha(0.4f);
                    login.setEnabled(false);
                }else {
                    if (usefullData.password_Validator(pswd.getText().toString().trim()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        login.setAlpha(1f);
                        login.setEnabled(true);
                    }else {
                        login.setAlpha(0.4f);
                        login.setEnabled(false);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        KeyboardVisibilityEvent.setEventListener(this,
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
                                        saveData.saveKeyBHeight("key", keyboardHeight);
                                        params.setMargins(0, 0, 0, keyboardHeight);
                                        keyboard_layout.setLayoutParams(params);
                                        show_keyboard_layout();
                                    }else{
                                        keyboard_layout.setVisibility(View.GONE);
                                    }
                                }
                                else if(keyboardHeight > saveData.getKeyBHeight("key")) {
                                    params.setMargins(0, 0, 0, saveData.getKeyBHeight("key"));
                                    keyboard_layout.setLayoutParams(params);
                                    show_keyboard_layout();
                                }else{
                                    params.setMargins(0, 0, 0, keyboardHeight);
                                    keyboard_layout.setLayoutParams(params);
                                    show_keyboard_layout();
                                }
                            }

                        }else {
                            main_scrollview.scrollTo(0,0);
                            main_scrollview.setScrollingEnabled(false);
                            keyboard_layout.setVisibility(View.GONE);
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        usefullData=new UsefullData(Login_activity.this);
        if(usefullData.isNetworkConnected()){
            wifi_btn.setAlpha(1f);
            String net = usefullData.getWifiName(this);
            if(net!=null){
                try{
                    String wifiName = tryToReadSSID().replace("\"","");
                    tv_wifi.setText(wifiName);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                tv_wifi.setText(" Connected");
            }
        }else {
            tv_wifi.setText("Not Connected");
            wifi_btn.setAlpha(.5f);
            usefullData.make_alert_with_action("You are not connected to wifi. Please connect now to start using the app", true, this);
        }

    }


    private void attemptLogin(final String email,final String password) {
        ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        usefullData.showProgress();
        JSONObject request = new JSONObject();
        try {
            request.put("email", email);
            request.put("password", password);
            request.put("firebase_token",saveData.getString(Definitions.FIREBASE_TOKEN));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TAG", request.toString());
        UserAPI.post_JsonReq_JsonResp(Definitions.SIGN_IN, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (saveData.isExist(Definitions.ID)){
                            saveData.remove(Definitions.ID);
                        }
                        usefullData.dismissProgress();
                        Log.e("-----response--",""+response);
//                        if(saveData.getBoolean(Definitions.REMEMBER)){
//                            saveData.save(Definitions.USER_REM_EMAIL,email);
//                            saveData.save(Definitions.USER_REM_PSWD,password);
//                        }else {
//                            saveData.remove(Definitions.USER_REM_EMAIL);
//                            saveData.remove(Definitions.USER_REM_PSWD);
//                        }
                        store_values(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            usefullData.dismissProgress();
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {

                                    case 500:
                                        usefullData.make_alert(getResources().getString(R.string.no_api_hit),true,Login_activity.this);
                                        break;
                                    case 401:
                                        usefullData.make_alert("Invalid email address or password",true,Login_activity.this);
                                        break;
                                    case 404:
                                        usefullData.make_alert("Please try signing in on the web App",true,Login_activity.this);
                                        break;
                                }
                            }else{
                                usefullData.make_alert(getResources().getString(R.string.no_internet_speed),true,Login_activity.this);
                            }
                        }catch (Exception e){

                        }

                    }
                });


    }

    private void store_values(JSONObject response) {

        try {
            saveData.save(Definitions.USER_TOKEN,response.getJSONObject("user").optString("authentication_token"));
            saveData.save(Definitions.USER_NAME,response.getJSONObject("user").optString("name"));
            saveData.save(Definitions.USER_EMAIL,response.getJSONObject("user").optString("email"));
//          saveData.save(Definitions.SURGERY_ID,response.getJSONObject("user").optInt("SURGERY_ID"));
            saveData.save(Definitions.SURGERY_NAME,response.optString("surgery_name"));
            saveData.save(Definitions.ID,response.getJSONObject("user").optInt("id"));


            if(!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(saveData.getInt(Definitions.ID))))  {

                InitializeActivity.mMixpanel.identify(String.valueOf(response.getJSONObject("user").optInt("id")));
                InitializeActivity.mMixpanel.getPeople().identify(String.valueOf(response.getJSONObject("user").optInt("id")));
                InitializeActivity.mMixpanel.getPeople().set(Definitions.MIXPANEL_NAME, response.getJSONObject("user").optString("name"));
                InitializeActivity.mMixpanel.getPeople().set(Definitions.MIXPANEL_ID, response.getJSONObject("user").optInt("id"));
                InitializeActivity.mMixpanel.getPeople().set(Definitions.MIXPANEL_EMAIL, response.getJSONObject("user").optString("email"));
                usefullData.schedule_time_events();
            }


            db.openDB();
            db.insert_user(response.getJSONObject("user").optString("name"), response.getJSONObject("user").optString("email"),pswd.getText().toString().trim(),remember.isChecked());
            db.closeDB();


            if(response.optBoolean("reset_password")){
                Intent forgot = new Intent(Login_activity.this, One_time_pswd_chng.class);
                forgot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(forgot);
                finish();
            }else {
                Intent forgot = new Intent(Login_activity.this, Screenshare_instruction_page.class);
                forgot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(forgot);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//
//        switch(ev.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                x1 = ev.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = ev.getX();
//                float deltaX = x2 - x1;
//                if (Math.abs(deltaX) > MIN_DISTANCE)
//                {
////                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
//                }
//                else
//                {
////                    ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
//                }
//                break;
//            default:
//                                    ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
//
//                break;
//
//        }
////        return super.onTouchEvent(event);
////        View view = getCurrentFocus();
////        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
////            int scrcoords[] = new int[2];
////            view.getLocationOnScreen(scrcoords);
////            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
////            float y = ev.getRawY() + view.getTop() - scrcoords[1];
////            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
////                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
////        }
//        return super.dispatchTouchEvent(ev);
//    }




    @Override
    protected void onPause() {
        super.onPause();

//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
    }


    public void show_keyboard_layout(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                keyboard_layout.setVisibility(View.VISIBLE);
            }
        }, 200);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yahoo_txt:
                email.setText(email.getText().toString()+getResources().getString(R.string.yahoo));
                email.setSelection(email.getText().length());
                break;
            case R.id.outlook_txt:
                email.setText(email.getText().toString()+getResources().getString(R.string.outlook));
                email.setSelection(email.getText().length());
                break;
            case R.id.gmail_txt:
                email.setText(email.getText().toString()+getResources().getString(R.string.gmail));
                email.setSelection(email.getText().length());
                break;
            case R.id.wifi_btn:
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                break;

        }
    }


    private String tryToReadSSID() {
        String ssid = "";
        //If requested permission isn't Granted yet
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Request permission from user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION);
        }else{//Permission already granted
            try{
                ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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



    public class MyCustomProgressDialogAction extends ProgressDialog {


         String set_msg;


        public ProgressDialog ctor(Context context, String msg) {
            UsefullData.MyCustomProgressDialog dialog = new UsefullData.MyCustomProgressDialog(context, R.style.CustomDialog_new);
            dialog.setIndeterminate(true);

            dialog.setCancelable(false);
            set_msg = msg;
            return dialog;
        }

        public MyCustomProgressDialogAction(Context context) {
            super(context);
        }

        public MyCustomProgressDialogAction(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.custom_progress_wifi);

            TextView pop_title = (TextView) findViewById(R.id.pop_title_alert);
            TextView pop_msg = (TextView) findViewById(R.id.pop_msg_alert);
            Button back = (Button) findViewById(R.id.back_alert);
//            FrameLayout root = (Button) findViewById(R.id.pop_back_root);
            back.setText("Connect");
            pop_msg.setText(set_msg);

            pop_title.setTypeface(get_montserrat_regular());
            pop_msg.setTypeface(get_montserrat_regular());
            back.setTypeface(get_montserrat_regular());

//            root.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    return false;
//                }
//            });


            back.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    tryToReadSSID();
                    return false;
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

    public void make_alert_with_action(final String msg, boolean activity, final Context contxt) {
        new MyCustomProgressDialogAction(this).show();

    }
}