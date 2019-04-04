package com.sydehealth.sydehealth.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.LockableScrollView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Forgot_password extends Activity implements View.OnClickListener{

    static LinearLayout back;
    EditText edit_email;
    static UsefullData objUsefullData;
    Button forgot;
    TextView email_error,email_label;

    ProgressDialog loader;
    TextInputLayout email_layer;
    LinearLayout keyboard_layout;
    RelativeLayout root;
    Button yahoo_txt, outlook_txt, gmail_txt;
    LockableScrollView main_scrollview;
    private int initial = 0;
    private SaveData saveData;

    TextView backB, forgot_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        objUsefullData = new UsefullData(Forgot_password.this);
        saveData = new SaveData(this);

        main_scrollview=(LockableScrollView) findViewById(R.id.forgot_scrollview);
        main_scrollview.setScrollingEnabled(false);

        root=(RelativeLayout) findViewById(R.id.root);
        keyboard_layout = (LinearLayout)findViewById(R.id.keyboard_extra_key);
        yahoo_txt=(Button) findViewById(R.id.yahoo_txt);
        outlook_txt=(Button) findViewById(R.id.outlook_txt);
        gmail_txt=(Button) findViewById(R.id.gmail_txt);
        backB = (TextView) findViewById(R.id.backB);
        forgot_label = (TextView) findViewById(R.id.backB);
        yahoo_txt.setOnClickListener(this);
        outlook_txt.setOnClickListener(this);
        gmail_txt.setOnClickListener(this);


        forgot=(Button) findViewById(R.id.forgot);
        edit_email=(EditText) findViewById(R.id.edit_email);
        email_error=(TextView) findViewById(R.id.edit_email_error);
        email_label=(TextView) findViewById(R.id.email_label);
        email_layer=(TextInputLayout) findViewById(R.id.email_layer);

        email_layer.setTypeface(objUsefullData.get_montserrat_regular());
        email_error.setTypeface(objUsefullData.get_helvetica_regular());
        edit_email.setTypeface(objUsefullData.get_helvetica_regular());
        email_label.setTypeface(objUsefullData.get_helvetica_regular());
        forgot.setTypeface(objUsefullData.get_google_bold());

        backB.setTypeface(objUsefullData.get_awosome_font_400());
        forgot.setTypeface(objUsefullData.get_montserrat_regular());


        edit_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);


        forgot.setAlpha(0.4f);
        forgot.setEnabled(false);

        back=(LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objUsefullData.hideKeyboardFrom(Forgot_password.this, v);
                finish();

            }


        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_error.setText("");

                valid_forgot();

            }


        });



        edit_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                email_error.setText("");
                if(edit_email.getText().toString().length()==0){
                    forgot.setAlpha(0.4f);
                    forgot.setEnabled(false);
                }else {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email.getText().toString().trim()).matches()) {
                        forgot.setAlpha(1f);
                        forgot.setEnabled(true);
                    }else {
                        forgot.setAlpha(0.4f);
                        forgot.setEnabled(false);
                    }
                }
            }
        });
        edit_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.edit_email || id == EditorInfo.IME_ACTION_DONE) {

                    valid_forgot();

                    return true;
                }
                return false;
            }
        });




        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){

                            main_scrollview.setScrollingEnabled(true);

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


                        }else {
                            main_scrollview.scrollTo(0,0);
                            main_scrollview.setScrollingEnabled(false);
                            keyboard_layout.setVisibility(View.GONE);
                        }

                    }
                });



    }

    public void valid_forgot(){
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((getWindow().getDecorView().getApplicationWindowToken()), 0);

        email_error.setText("");
        if(objUsefullData.isNetworkConnected())
        {
        if (TextUtils.isEmpty(edit_email.getText().toString().trim())) {
            Animation shake = AnimationUtils.loadAnimation(Forgot_password.this, R.anim.shake);
            edit_email.startAnimation(shake);
            email_error.setText(getResources().getString(R.string.req));

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit_email.getText().toString().trim()).matches()) {
            Animation shake = AnimationUtils.loadAnimation(Forgot_password.this, R.anim.shake);
            edit_email.startAnimation(shake);
            email_error.setText(getResources().getString(R.string.email_valid));

        }else{
                attemptforgot(edit_email.getText().toString().trim());
                   }
        }else {
            objUsefullData.make_alert(getResources().getString(R.string.no_internet),true,Forgot_password.this);
        }
    }

    private void attemptforgot(String email) {



        objUsefullData.showProgress();
        JSONObject request = new JSONObject();
        JSONObject user_info = new JSONObject();

        try {
            user_info.put("email", email);

            request.put("user", user_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        UserAPI.post_JsonReq_JsonResp(Definitions.FORGOT_PASSWORD, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        objUsefullData.dismissProgress();
                        Log.e("-----response--",""+response);
                        alert(getResources().getString(R.string.forgot_pswd_msg));



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        objUsefullData.dismissProgress();
                        objUsefullData.make_alert(getResources().getString(R.string.no_email),true,Forgot_password.this);


                    }
                });


    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View view = getCurrentFocus();
//        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            view.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + view.getTop() - scrcoords[1];
//            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
//                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

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


    public static class MyCustomProgressDialog extends ProgressDialog {


        static String set_msg;


        public  static ProgressDialog ctor(Context context, String msg) {
            MyCustomProgressDialog dialog = new MyCustomProgressDialog(context,R.style.CustomDialog_new);
            dialog.setIndeterminate(true);

            dialog.setCancelable(false);

            set_msg=msg;
            return dialog;
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
            final Button back_pop = (Button) findViewById(R.id.back_alert);

            pop_msg.setText(set_msg);

            pop_title.setTypeface(objUsefullData.get_semibold());
            pop_msg.setTypeface(objUsefullData.get_semibold());
            back_pop.setTypeface(objUsefullData.get_google_bold());


            back_pop.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dismiss();
                    back.performClick();
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

    public void alert(final String msg) {
        loader = MyCustomProgressDialog.ctor(Forgot_password.this,msg);

        if(!((Activity) this).isFinishing())
        {
            loader.show();
        }

    }

    public void show_keyboard_layout(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                keyboard_layout.setVisibility(View.VISIBLE);
            }
        }, 100);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yahoo_txt:
                edit_email.setText(edit_email.getText().toString()+getResources().getString(R.string.yahoo));
                edit_email.setSelection(edit_email.getText().length());
                break;
            case R.id.outlook_txt:
                edit_email.setText(edit_email.getText().toString()+getResources().getString(R.string.outlook));
                edit_email.setSelection(edit_email.getText().length());
                break;
            case R.id.gmail_txt:
                edit_email.setText(edit_email.getText().toString()+getResources().getString(R.string.gmail));
                edit_email.setSelection(edit_email.getText().length());
                break;
        }
    }
}
