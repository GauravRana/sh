package com.sydehealth.sydehealth.utility;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.sydehealth.sydehealth.loader.CallPhoneDialog;
import com.sydehealth.sydehealth.loader.Youtube_loader;
import com.sydehealth.sydehealth.mail_upload.Video_download_service;
import com.sydehealth.sydehealth.model.TreatmentImages;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.volley.UserAPI;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.Actors;
import com.sydehealth.sydehealth.main.Condition_page;
import com.sydehealth.sydehealth.main.LastModifiedFileComparator;
import com.sydehealth.sydehealth.main.Screen_saver_activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import wseemann.media.FFmpegMediaMetadataRetriever;


public class UsefullData {
    static public Context _context;
    Dialog dialog;
    Dialog dialog2;
    static CounterClass timer;
    long remainMilli = 0;
    boolean isRunning = false;
    boolean ispause = false;
    Boolean isFinished = true;
    public static Drawable screen_save_image;
    ProgressDialog loader;
    private int current_screensaver_id = 0;
    private int ScreenSaverTimer = 120;

    public UsefullData(Context c) {
        _context = c;
    }

    // ================== DEVICE INFORMATION ============//

    public static String getCountryCodeFromDevice() {
        String countryCode = Locale.getDefault().getCountry();
        if (countryCode.equals("")) {
            countryCode = "IN";
        }
        return countryCode;
    }

    // ================== CREATE FILE AND RELATED ACTION ============//

    public File getRootFile() {

        File f = new File(Environment.getExternalStorageDirectory(), _context
                .getString(R.string.app_name).toString());
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        return f;
    }

    public void deleteRootDir(File root) {

        if (root.isDirectory()) {
            String[] children = root.list();
            for (int i = 0; i < children.length; i++) {
                File f = new File(root, children[i]);
                Log("file name:" + f.getName());
                if (f.isDirectory()) {
                    deleteRootDir(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    // ================ DOWNLOAD ============================//

    public void downloadAndDisplayImage(final String image_url,
                                        final ImageView v, int type) {

        new Thread() {

            public void run() {
                try {

                    InputStream in = new URL(image_url).openConnection()
                            .getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(in);
                    File fileUri = new File(getRootFile(),
                            getNameFromURL(image_url));
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(fileUri);
                    bm.compress(Bitmap.CompressFormat.JPEG, 75, outStream);
                    outStream.flush();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                    File f = new File(getRootFile(), "aa.jpg");
                    if (f.exists()) {
                        final Bitmap bmp = BitmapFactory
                                .decodeFile(f.getPath());

                        v.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                v.setImageBitmap(bmp);
                            }
                        });

                        Log("download images and showing ,,,,");

                    }
                }
            }

        }.start();
    }

    public String getNameFromURL(String url) {

        String fileName = "item_image.jpg";
        if (url != null) {
            fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        }
        return fileName;
    }

    // ================== LOG AND TOAST====================//

    public static void Log(final String msg) {

//		if (SHOW_LOG) {
//			android.util.Log.e(LOG_TAG, msg);
//		}

    }


    public void make_alert(final String msg, boolean activity, final Context contxt) {

        SaveData save = new SaveData(_context);
        if (!activity) {
            ((Activity) contxt).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loader = MyCustomProgressDialog.ctor(contxt, msg);
                    loader.show();

                    if (save.isExist(Definitions.USER_TOKEN)) {
                        Start_timer();
                    }
                }
            });
        } else {

            loader = MyCustomProgressDialog.ctor(contxt, msg);
            if (!((Activity) contxt).isFinishing()) {
                loader.show();

                if (save.isExist(Definitions.USER_TOKEN)) {
                    Start_timer();
                }
            }
        }

    }


    public void make_alert_with_action(final String msg, boolean activity, final Context contxt) {
        loader = MyCustomProgressDialogAction.ctor(contxt, msg);
        loader.show();

    }


    public void cancel_alert() {

        ((Activity) _context).runOnUiThread(new Runnable() {

            @Override
            public void run() {


                try {
                    if ((loader != null) && loader.isShowing()) {
                        loader.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    // =================== INTERNET ===================//
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

    // ==================== PROGRESS DIALOG ==================//

    public void showProgress() {

        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            dialog = new CallPhoneDialog(_context);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowing() {
        try {
            return dialog.isShowing();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void showProgress2() {
        try {
            if ((dialog2 != null) && dialog2.isShowing()) {
                dialog2.dismiss();
                dialog2 = null;
            }
            dialog2 = new Youtube_loader(_context);
            dialog2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void dismissProgress2() {

        try {
            if (dialog2 != null) {
                if (dialog2.isShowing()) {
                    dialog2.dismiss();
                    dialog2 = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void forceCloseDismissProgress() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dismissProgress() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // ====================SET FONT SIZE==================//
    public static Typeface get_semibold() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Montserrat-Regular.ttf");
        return raleway_font;
    }

    public static Typeface get_helvetica_regular() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Montserrat-Regular.ttf");
        return raleway_font;
    }

    public static Typeface get_google_bold() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Montserrat-Regular.ttf");
        return raleway_font;
    }

    public static Typeface get_montserrat_regular() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Montserrat-Regular.ttf");
        return raleway_font;
    }

    public Typeface get_montserrat_bold() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Montserrat-Bold.ttf");
        return raleway_font;
    }

    public static Typeface get_montserrat_semibold() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Montserrat-SemiBold.ttf");
        return raleway_font;
    }

    public static Typeface get_awosome_font() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Font Awesome 5 Pro-Light-300.otf");
        return raleway_font;
    }

    public Typeface get_awosome_font_solid() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Font Awesome 5 Pro-Solid-900.otf");
        return raleway_font;
    }

    public Typeface get_awosome_font_400() {
        Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
                "Font Awesome 5 Pro-Regular-400.otf");
        return raleway_font;
    }


    public File createFile(String fileName) {
        File f = null;
        try {
            f = new File(getRootFile(), fileName);
            if (f.exists()) {
                f.delete();
            }

            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static String getLocation(Context context, double latitude,
                                     double longitude) throws IOException {

        String address;
        String city;
        String country;

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getAddressLine(1);
            country = addresses.get(0).getAddressLine(2);

        } catch (Exception e) {
            address = "";
            city = "";
            country = "";
        }
        return address + ", " + city;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        // sdf.applyPattern("dd MMM yyyy");
        String strDate = sdf.format(cal.getTime());
        return strDate;
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) _context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }


    public static int randInt(int min, int max) {
        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, ByteStream);
        bitmap.recycle();
        bitmap = null;
        byte[] b = ByteStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);


        return temp;


    }


    public Bitmap getBitmap(Uri uri) {

        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 102400; // 200kb
            in = _context.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = _context.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }


    public boolean has_image(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }


    public boolean password_Validator(String pswd) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^(?=.*\\d).{8,}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(pswd);
        return matcher.matches();
    }

    public void Mixpanel_events(String parent, String property, String property_name) {
        SaveData save = new SaveData(_context);
        if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save.getInt(Definitions.ID)))) {
            try {
                JSONObject props = new JSONObject();
                props.put(property, property_name);

                Log.e("TAG mix panel event", props.toString());
                InitializeActivity.mMixpanel.track(parent, props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        }
    }


    public void Mixpanel_events_email(String parent, String condition_name, String property_condition_name
                                                    ,String sub_condition_name, String property_sub_condition_name
                                                    ,String treatment_name, String property_treatment_name
                                                    ,String sub_treatment_name, String property_sub_treatment_name) {
        SaveData save = new SaveData(_context);
        if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save.getInt(Definitions.ID)))) {
            try {
                JSONObject props = new JSONObject();
                props.put(condition_name, property_condition_name);
                props.put(sub_condition_name, property_sub_condition_name);
                props.put(treatment_name, property_treatment_name);
                props.put(sub_treatment_name, property_sub_treatment_name);


                Log.e("TAG mix panel event", props.toString());
                InitializeActivity.mMixpanel.track(parent, props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        }
    }


    public void Mixpanel_events_VideoCount(String parent, String property, String property_name,String playlist,String playlist_Name) {
        SaveData save = new SaveData(_context);
        if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save.getInt(Definitions.ID)))) {
            try {
                JSONObject props = new JSONObject();
                props.put(property, property_name);
                props.put(playlist,playlist_Name);

                Log.e("TAG mix panel event", props.toString());
                InitializeActivity.mMixpanel.track(parent, props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        }
    }



    public void Mixpanel_multiple_events(String parent, String property1, String property_name1, String property2, String property_name2) {
        SaveData save = new SaveData(_context);
        if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save.getInt(Definitions.ID)))) {
            try {
                JSONObject props = new JSONObject();
                props.put(property1, property_name1);
                props.put(property2, property_name2);

                Log.e("TAG mix panel event", props.toString());
                InitializeActivity.mMixpanel.track(parent, props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        }
    }

    public void Mixpanel_time_event() {
        SaveData save = new SaveData(_context);
        if (!Definitions.NOT_ALLOWED_EMAIL.contains(String.valueOf(save.getInt(Definitions.ID)))) {
            InitializeActivity.mMixpanel.timeEvent(Definitions.MIXPANEL_TIME);
            InitializeActivity.mMixpanel.track(Definitions.MIXPANEL_TIME);

        }
    }

    public boolean condition_update_time() {
        boolean update = false;
        SaveData save = new SaveData(_context);

        long currentTime = new Date().getTime();

        if (save.isExist(Definitions.CONDITION_LAST_UPDATE)) {
            if (currentTime - save.getLong(Definitions.CONDITION_LAST_UPDATE) >= Definitions.MAX_TIME) // number of milliseconds in a day
            {
                save.save(Definitions.CONDITION_LAST_UPDATE, currentTime);
                update = true;
            }
        } else {
            save.save(Definitions.CONDITION_LAST_UPDATE, currentTime);
        }
        Log.e("currentTime--", "" + currentTime);
        Log.e("savetTime--", "" + save.getLong(Definitions.CONDITION_LAST_UPDATE));
        Log.e("C.refresh time left--->", "" + (currentTime - save.getLong(Definitions.CONDITION_LAST_UPDATE)));
        Log.e("max--", "" + Definitions.MAX_TIME);
        return update;
    }

    public boolean playlist_update_time() {
        boolean update = false;
        SaveData save = new SaveData(_context);

        long currentTime = new Date().getTime();

        if (save.isExist(Definitions.PLAYLIST_LAST_UPDATE)) {
            if (currentTime - save.getLong(Definitions.PLAYLIST_LAST_UPDATE) >= Definitions.MAX_TIME) // number of milliseconds in a day
            {
                save.save(Definitions.PLAYLIST_LAST_UPDATE, currentTime);
                update = true;
            }
        } else {
            save.save(Definitions.PLAYLIST_LAST_UPDATE, currentTime);
        }
        Log.e("currentTime--", "" + currentTime);
        Log.e("savetTime--", "" + save.getLong(Definitions.PLAYLIST_LAST_UPDATE));
        Log.e("P.refresh time left--->", "" + (currentTime - save.getLong(Definitions.PLAYLIST_LAST_UPDATE)));
        Log.e("max--", "" + Definitions.MAX_TIME);
        return update;
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
            Log.e("screen saver timer -->", "" + time_left);
        }

        //When time is finished, what should happen: will go in this method
        @Override
        public void onFinish() {
            // reset all variables

//          timer_pic.setImageResource(R.mipmap.p15);
            isRunning = false;
            ispause = false;
            remainMilli = 0;
            isFinished = false;

            if (isNetworkConnected()) {
                SaveData save = new SaveData(_context);
                get_screensaver();

                //old code for image
//                if (save.isExist(Definitions.SCREENSAVER_IMAGE)) {
//                    if (screen_save_image != null) {
//                        if (timer != null) {
//                            timer.cancel();
//                            timer = null;
//                        }
//                        Intent edit = new Intent(_context, Screen_saver_activity.class);
//                        _context.startActivity(edit);
//                    } else {
//                        new DownloadImage().execute(save.getString(Definitions.SCREENSAVER_IMAGE));
//                    }

//                } else {
//                    if (timer != null) {
//                        timer.cancel();
//                        timer = null;
//                    }
//                    Start_timer();
//                }
            } else {
                if (timer != null) {
                    timer.cancel();
                    timer = null;

                }
                Start_timer();
            }
        }
    }


    private void get_screensaver() {

        SaveData save_data = new SaveData(_context);
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", save_data.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", save_data.get(Definitions.USER_TOKEN));


        UserAPI.get_JsonObjResp(Definitions.SCREENSAVER_FETCH + current_screensaver_id, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        try {
                            if (!response.isNull("screensaver_videos")) {

                                Intent edit = new Intent(_context, Screen_saver_activity.class);
                                edit.putExtra("response", response.toString());
                                _context.startActivity(edit);

//                                if (response.getJSONObject("result").has("id")) {
//
//                                    Mixpanel_events("Screensaver", "Advert Name", response.getJSONObject("result").optString("name"));
//
//                                    current_screensaver_id = response.getJSONObject("result").optInt("id");
//                                    String videoName = response.getJSONObject("result").optString("name");
//                                    String media_url = response.getJSONObject("result").optString("media_url");
//
////                                    String filePath = Definitions.VIDEO_FOLDER + videoName
////                                            + "_" + current_screensaver_id + ".mp4";
////                                    File file = new File(filePath);
////
////                                    if (file.exists() && isValidVideo(filePath)) {
//
////                                        Intent edit = new Intent(_context, Screen_saver_activity.class);
////                                        edit.putExtra("screensaver_name", videoName);
////                                        edit.putExtra("screensaver_id",""+current_screensaver_id);
////                                        edit.putExtra("url","http://sydevideobucket.s3-eu-west-2.amazonaws.com/presentation_items/media/000/000/081/original/asian-dentist-visiting-girl-in-dental-studio-with-tools-and-equipment-people-and-oral-hygiene-health-care-and-medicine-in-clinic-lab_4ykt6x6z__PM.mp4?1539756998");
////                                        _context.startActivity(edit);
//
////                                    }
//
//                                }
                            } else {
                                Start_timer();
                            }
//                            if(!isRunning){
//                                start_adds_play();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.v("TAG response", error.toString());
//                        if (isNetworkConnected()) {
//                            get_screensaver();
//                        }
                    }
                });

    }

    public void Start_timer() {


        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        remainMilli = 0;
        timer = new CounterClass(ScreenSaverTimer * 1000, 1000);
        timer.start();   //Start the timer
        isRunning = true;
        ispause = false;

    }

    public void Resume() {
        if (!isRunning) {  //This method will execute only when timer is not running
            timer = new CounterClass(remainMilli, 1000); //resume timer from where it is paused
            timer.start();  //Start the timer
            isRunning = true;

        }
    }

    public void Pause_timer() {
        if (isRunning) {
            if (timer != null) {
                timer.cancel();
                timer = null;
                ispause = true;
                isRunning = false;
            }


        }

    }

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
//            try {
//                screen_save_image = image;
//                if (!isRunning) {
//                    Intent edit = new Intent(_context, Screen_saver_activity.class);
//                    _context.startActivity(edit);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
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

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }


    public boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }

    public void schedule_time_events() {
        SaveData save = new SaveData(_context);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (save.isExist(Definitions.RECENT_DATE)) {
            if (save.getInt(Definitions.RECENT_DATE) != day) {
                save.save(Definitions.RECENT_DATE, day);
                Mixpanel_time_event();
            }
        } else {
            save.save(Definitions.RECENT_DATE, day);
            Mixpanel_time_event();
        }

    }

    public void make_visible(View view) {
        int vt = view.getTop();
        int vb = view.getBottom();

        View v = view;

        for (; ; ) {
            ViewParent vp = v.getParent();

            if (vp == null || !(vp instanceof ViewGroup))
                break;

            ViewGroup parent = (ViewGroup) vp;

            if (parent instanceof ScrollView) {
                ScrollView sv = (ScrollView) parent;

                // Code based on ScrollView.computeScrollDeltaToGetChildRectOnScreen(Rect rect) (Android v5.1.1):

                int height = sv.getHeight();
                int screenTop = sv.getScrollY();
                int screenBottom = screenTop + height;

                int fadingEdge = sv.getVerticalFadingEdgeLength();

                // leave room for top fading edge as long as rect isn't at very top
                if (vt > 0)
                    screenTop += fadingEdge;

                // leave room for bottom fading edge as long as rect isn't at very bottom
                if (vb < sv.getChildAt(0).getHeight())
                    screenBottom -= fadingEdge;

                int scrollYDelta = 0;

                if (vb > screenBottom && vt > screenTop) {
                    // need to move down to get it in view: move down just enough so
                    // that the entire rectangle is in view (or at least the first
                    // screen size chunk).

                    if (vb - vt > height) // just enough to get screen size chunk on
                        scrollYDelta += (vt - screenTop);
                    else              // get entire rect at bottom of screen
                        scrollYDelta += (vb - screenBottom);

                    // make sure we aren't scrolling beyond the end of our content
                    int bottom = sv.getChildAt(0).getBottom();
                    int distanceToBottom = bottom - screenBottom;
                    scrollYDelta = Math.min(scrollYDelta, distanceToBottom);
                } else if (vt < screenTop && vb < screenBottom) {
                    // need to move up to get it in view: move up just enough so that
                    // entire rectangle is in view (or at least the first screen
                    // size chunk of it).

                    if (vb - vt > height)    // screen size chunk
                        scrollYDelta -= (screenBottom - vb);
                    else                  // entire rect at top
                        scrollYDelta -= (screenTop - vt);

                    // make sure we aren't scrolling any further than the top our content
                    scrollYDelta = Math.max(scrollYDelta, -sv.getScrollY());
                }

                sv.smoothScrollBy(0, scrollYDelta);
                break;
            }

            // Transform coordinates to parent:
            int dy = parent.getTop() - parent.getScrollY();
            vt += dy;
            vb += dy;

            v = parent;
        }
    }

    public static class MyCustomProgressDialog extends ProgressDialog {


        static String set_msg;


        public static ProgressDialog ctor(Context context, String msg) {
            MyCustomProgressDialog dialog = new MyCustomProgressDialog(context, R.style.CustomDialog_new);
            dialog.setIndeterminate(true);

            dialog.setCancelable(false);
            set_msg = msg;
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
            Button back = (Button) findViewById(R.id.back_alert);
//            FrameLayout root = (Button) findViewById(R.id.pop_back_root);
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

                    dismiss();
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

    public void delete_screenshot_folder() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Jukepad");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    public void saveuserList(ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getuserList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public ArrayList<TreatmentImages> get_screenshots() {
        ArrayList<TreatmentImages> al_screenshots = new ArrayList<TreatmentImages>();// list of file paths

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(_context, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

                String root = Environment.getExternalStorageDirectory().toString();
                File file = new File(root + "/Jukepad");

                if (file.isDirectory()) {
                    File[] listFile = file.listFiles();
                    if (listFile != null) {
                        Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                        for (int i = 0; i < listFile.length; i++) {
                            TreatmentImages treatmentImages = new TreatmentImages();
                            treatmentImages.setPath(listFile[i].getAbsolutePath());
                            treatmentImages.setSelected(false);
                            al_screenshots.add(treatmentImages);
                        }
                    }
                }

            }
        });
        return al_screenshots;
    }

    public ArrayList<Actors> get_video_name() {
        ArrayList<Actors> device_list = new ArrayList<Actors>();
        File[] listFile;
        File file = new File(Environment.getExternalStorageDirectory() + "/Syde_conditions_video");
        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                Actors actor = new Actors();

                actor.setusername(listFile[i].getName());
                actor.setdata(listFile[i].getAbsolutePath());
                device_list.add(actor);
            }
        }
        return device_list;
    }

    public void deleteFile(File root) {

        if (root.exists()) {
            root.delete();
        }
    }


    public String capitalize(@NonNull String input) {


        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }

        return builder.toString();
    }

    public void time_check() {

        SaveData saveData = new SaveData(_context);

        if (saveData.isExist(Definitions.USER_NAME_HEADER)) {

            Calendar c = Calendar.getInstance();
            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
            String time_event = "Morning";

            if (timeOfDay >= 0 && timeOfDay < 12) {
                time_event = "Morning";
            } else if (timeOfDay >= 12 && timeOfDay < 18) {
                time_event = "Afternoon";
            } else if (timeOfDay >= 18 && timeOfDay < 24) {
                time_event = "Evening";
            }

            String pre_user_name = " " + time_event + " ";
            String temp = saveData.getString(Definitions.USER_NAME_HEADER).trim();
            if (temp.length() > 25) {
                temp = temp.substring(0, 25) + "...";

            }
            String user_name = temp + " ";
            String post_user_name = "What conditions are we treating today?";
            String boldName = user_name.replace(" .", ".");
            SpannableString spannableString_2 = new SpannableString(pre_user_name + user_name + post_user_name);
            ClickableSpan clickableSpan_2 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {

                }

                @Override
                public void updateDrawState(final TextPaint textPaint) {
                    textPaint.setColor(_context.getResources().getColor(R.color.white));
                    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

                    textPaint.setTypeface(get_montserrat_bold());
                    textPaint.setUnderlineText(false);
                }
            };
            spannableString_2.setSpan(clickableSpan_2, pre_user_name.length(),
                    pre_user_name.length() + user_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            try {
                if (Condition_page.txt_welcome_msg != null) {
                    Condition_page.txt_welcome_msg.setText(spannableString_2, TextView.BufferType.SPANNABLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isValidVideo(String filePath) {
        boolean isVideo = false;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(_context, Uri.fromFile(new File(filePath)));
            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
            isVideo = "yes".equals(hasVideo);
        } catch (Exception e) {
            e.printStackTrace();
            isVideo = false;
        }

        return isVideo;
    }


    public Bitmap getImageFrame(String filePath, long timeInMilli) {
        timeInMilli = (timeInMilli * 1000);
        try {
            FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
            if(filePath.contains("http")){
                retriever.setDataSource(filePath);
            }else {
                retriever.setDataSource(_context, Uri.fromFile(new File(filePath)));
            }
            Bitmap temp = retriever.getFrameAtTime(timeInMilli - 1000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            retriever.release();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run_video_downloader_service() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        switch (timeOfDay) {
            //At 1:00 PM noon Download all videos by start service.......
            case 13:
                runDownloadService();
                break;

            case 14:
                runDownloadService();
                break;

            case 15:
                runDownloadService();
                break;

            //At 4:00 PM noon stop download service......
            default:
                stopDownloadService();
                break;
        }
    }

    public void runDownloadService() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                _context.startForegroundService(new Intent(_context, Video_download_service.class));
            } else {
                _context.startService(new Intent(_context, Video_download_service.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopDownloadService() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent stop_service = new Intent(_context, Video_download_service.class);
                _context.stopService(stop_service);
            } else {
                Intent stop_service = new Intent(_context, Video_download_service.class);
                _context.stopService(stop_service);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class MyCustomProgressDialogAction extends ProgressDialog {

        static String set_msg;

        public static ProgressDialog ctor(Context context, String msg) {
            MyCustomProgressDialogAction dialog = new MyCustomProgressDialogAction(context, R.style.CustomDialog_new);
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


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("hjshd", "click");
                    _context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    dismiss();
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

}