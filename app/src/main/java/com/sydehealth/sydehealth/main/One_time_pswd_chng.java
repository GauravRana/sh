package com.sydehealth.sydehealth.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.LockableScrollView;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class One_time_pswd_chng extends Activity {


    UsefullData objUsefullData;
    SaveData save_data;
    TextInputLayout new_pswd_layer, confirm_pswd_layer;
    EditText new_pswd, confirm_pswd;
    TextView new_pswd_error, confirm_pswd_error, tv_first, tv_sec;
    Button set_pswd;
    LockableScrollView main_scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pwd);

        save_data = new SaveData(this);
        objUsefullData = new UsefullData(this);

        init();

        set_pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_pswd_error.setText("");
                confirm_pswd_error.setText("");

                if(objUsefullData.isNetworkConnected())
                {
                    if(new_pswd.getText().toString().trim().equals("")) {
                        new_pswd_error.setText(getResources().getString(R.string.req));
                    }else if(confirm_pswd.getText().toString().trim().equals("")) {
                        confirm_pswd_error.setText(getResources().getString(R.string.req));
                    }else{

                        if (new_pswd.getText().toString().equals(confirm_pswd.getText().toString().trim())) {
                            if(objUsefullData.password_Validator(new_pswd.getText().toString().trim()) && objUsefullData.password_Validator(new_pswd.getText().toString().trim())) {
                                change_password(new_pswd.getText().toString().trim(), confirm_pswd.getText().toString().trim());

                            }else {
                                objUsefullData.make_alert(getResources().getString(R.string.pswd_valid),false,One_time_pswd_chng.this);
                            }

                        } else {
                            objUsefullData.make_alert(getResources().getString(R.string.pswd_not_match),false,One_time_pswd_chng.this);
                        }
                    }
                }else {
                    objUsefullData.make_alert(getResources().getString(R.string.no_internet),false,One_time_pswd_chng.this);
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
                new_pswd_error.setText("");
                confirm_pswd_error.setText("");

                if(objUsefullData.password_Validator(confirm_pswd.getText().toString().trim())) {
                    set_pswd.setAlpha(1f);
                    set_pswd.setEnabled(true);
                }
            }
        });

        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){

                            main_scrollview.setScrollingEnabled(true);

                        }else {
                            main_scrollview.scrollTo(0,0);
                            main_scrollview.setScrollingEnabled(false);
                        }

                    }
                });

    }

    private void init() {

        main_scrollview=(LockableScrollView) findViewById(R.id.main_scrollview_one);
        main_scrollview.setScrollingEnabled(false);
        new_pswd_layer=(TextInputLayout) findViewById(R.id.new_pswd_layer);
        confirm_pswd_layer=(TextInputLayout) findViewById(R.id.confirm_pswd_layer);
        new_pswd_layer.setTypeface(objUsefullData.get_montserrat_regular());
        confirm_pswd_layer.setTypeface(objUsefullData.get_montserrat_regular());

        new_pswd=(EditText) findViewById(R.id.new_pswd);
        new_pswd_error=(TextView) findViewById(R.id.new_pswd_error);
        confirm_pswd=(EditText) findViewById(R.id.confirm_pswd);
        confirm_pswd_error=(TextView) findViewById(R.id.confirm_pswd_error);
        new_pswd.setTypeface(objUsefullData.get_montserrat_regular());
        new_pswd_error.setTypeface(objUsefullData.get_montserrat_regular());
        confirm_pswd.setTypeface(objUsefullData.get_montserrat_regular());
        confirm_pswd_error.setTypeface(objUsefullData.get_montserrat_regular());


        set_pswd=(Button) findViewById(R.id.set_pwd);
        set_pswd.setAlpha(.4f);
        set_pswd.setEnabled(false);
        tv_first=(TextView) findViewById(R.id.tv_first);
        tv_sec=(TextView) findViewById(R.id.tv_sec);

        tv_first.setTypeface(objUsefullData.get_montserrat_regular());
        tv_sec.setTypeface(objUsefullData.get_montserrat_regular());
        set_pswd.setTypeface(objUsefullData.get_montserrat_regular());


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
                        Intent forgot=new Intent(One_time_pswd_chng.this,Screenshare_instruction_page.class);
                        forgot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(forgot);
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            objUsefullData.dismissProgress();


                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {

                                    case 500:

                                        objUsefullData.make_alert(getResources().getString(R.string.no_api_hit),true,One_time_pswd_chng.this);
                                        break;
                                    case 422:

                                        objUsefullData.make_alert(getResources().getString(R.string.error_code),true,One_time_pswd_chng.this);
                                        break;

                                    default:
                                        objUsefullData.make_alert(getResources().getString(R.string.no_internet_speed),true,One_time_pswd_chng.this);
                                        break;

                                }
                            }else{
                                objUsefullData.make_alert(getResources().getString(R.string.no_internet_speed),true,One_time_pswd_chng.this);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
    }

}
