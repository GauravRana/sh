package com.sydehealth.sydehealth.utility;

import android.content.Context;
import android.os.Environment;

import com.sydehealth.sydehealth.BuildConfig;
import com.sydehealth.sydehealth.volley.VolleyWebLayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Definitions {

    //MixPanel

    //ACTUAL KEYS
    //MixPanel KEYS

    // a9c47489333e16f593e076a494d398ea // staging
    // 10b85de6e37fb8603b76968980f7ab0b // live

    // ====================================== Definitions ====================================== //
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String SURGERY_ID = "surgery_id";
    public static final String SURGERY_NAME = "surgery_name";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_REM_EMAIL = "user_rem_email";
    public static final String USER_REM_PSWD = "user_rem_pswd";
    public static final String GALLERY = "select_treatment_presentation_items";
    public static final String CHILD_GALLERY = "select_sub_treatment_presentation_items";
    public static final String CONDITIONS_JSON_DATA = "conditions_json_data";
    public static final String PLAYLIST_JSON_DATA = "playlist_json_data";
    public static final String SURGERY_LOGO = "surgery_logo";
    public static final String ID = "user_id";
    public static final String USER_NAME_HEADER = "user_name_header";
    public static final String VERSION = "application/json;version=1";
    public static final String SIGN_IN = "users/sign_in";
    public static final String SIGN_OUT = "users/sign_out";
    public static final String CONDITIONS = "conditions";
    public static final String PRESENTATION = "select_presentation_items";
    public static final String GET_USER_DETAILS = "basic_details";
    public static final String FORGOT_PASSWORD = "users/password";
    public static final String PLAYLIST = "playlists";
    public static final String UPDATE_USER_DETAILS = "update_basic_details";
    public static final String CHANGE_PASSWORD = "change_password";
    public static final String CONDITIONS_ADVERT_PART_ONE = "conditions/";
    public static final String CONDITIONS_ADVERT_PART_TWO = "/select_adverts_conditions/";
    public static final String SUBMIT_ROUTE = "submit_adverts";
    public static final String SCREEN_SHARE_DETAILS = "screenshare";
    public static final String SUBSCRIBE_STATUS = "subscriber_status";
    public static final String SUBSCRIBE_STATUS_BACK = "terminate_screenshare";
    public static final String CONDITION_LAST_UPDATE = "condition_last_update";
    public static final String PLAYLIST_LAST_UPDATE = "playlist_last_update";
    public static final String SUBHEADING = "select_condition_video_subheadings";
    public static final String PLAYLIST_ADVERT_PART_ONE = "playlists/";
    public static final String PLAYLIST_ADVERT_PART_TWO = "/select_adverts/";
    public static final String RECENT_DATE = "recent_date";
    public static final String MAIL_IN_DRAFT = "mail_in_draft";
    public static final String SCREENSHARE_NOTIFICATION = "screenshare_notification";
    public static final String NEWS_ADVERT = "select_news_adverts/";
    public static final String SEND_EMAIL = "/send_email";
    public static final String SHOW_FIREBASE_TOP = "show_firebase_top";
    public static final String MIXPANEL_NAME = "name";
    public static final String MIXPANEL_ID = "id";
    public static final String MIXPANEL_EMAIL = "email";
    public static final String MIXPANEL_TIME = "App Used";
    public static final String ALL_VIDEO_DOWNLOAD = "get_videos";
    public static final long MAX_TIME = 24 * 60 * 60 * 1000;
    public static final String ANNOTATION_ENABLE = "annotation_enable";
    public static final String SCREENSAVER_FETCH = "select_screensaver/";
    public static final String SCREENSAVER_SUBMIT = "submit_screensavers";
    public static final String SCREENSAVER_ID = "screensaver_id";
    public static final String SCREENSAVER_NAME = "screensaver_name";
    public static final String SCREENSAVER_IMAGE = "screensaver_image";
    public static final String GOOGLE_API_KEY = "AIzaSyCsZtTnOmf0bvtcXX8c23MMmej8A-8GCUM";
    public static final String VIDEO_FOLDER = Environment.getExternalStorageDirectory().toString() + "/Syde_conditions_video/";
    //    public static final String USERS = "users" ;
//    public static final Set<String> ALLOW_EMAIL = new HashSet<String>(Arrays.asList(new String[]
//            { "65","63","69","72","70","71","68","64","37"}));

    public static final Set<String> NOT_ALLOWED_EMAIL = new HashSet<String>(Arrays.asList(new String[]
            {"1", "6", "15", "16", "28"}));

//    public static final Set<String> NOT_ALLOWED_EMAIL = new HashSet<String>(Arrays.asList(new String[]
//            {}));


    public static final String YOUTUBE_DOMAIN = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=";
    public static final String INSTAGRAM_DOMAIN = "https://api.instagram.com/v1/users/self/media/recent?access_token=";

//  public static final String APIdomain = "https://jukepad2.poplify.com";//staging
//  public static final String MIXPANEL_TOKEN  = "10b85de6e37fb8603b76968980f7ab0b";  //live
//  public static final String MIXPANEL_SECRET  = "1c2565338c17f6a2dd876686d5633771"; //staging
    //   public static final String APIdomain = "http://192.168.1.61:3000";//development
    //public static final String MIXPANEL_TOKEN = "a9c47489333e16f593e076a494d398ea";  //staging
//  public static final String MIXPANEL_SECRET  = "1840d138bacbdea7d2af01d55428cc89";


    //  public static final String APIdomain = "https://sydehealth.com";//live
//  public static final String APIdomain = "http://sydehealth2.poplify.com";//live testing
    public static final String APIdomain = "https://sydehealth.com";//live testing
    //    public static final String APIdomain = "http://192.168.1.13:3000";//live testing
//        public static final String APIdomain = "http://192.168.1.16:3000";//live testing
//  public static final String APIdomain = "https://jukepad.poplify.com";//live
    public static final String MIXPANEL_TOKEN  = "10b85de6e37fb8603b76968980f7ab0b";
    //  public static final String MIXPANEL_SECRET  = "6578c5ad3e74e11d6daafcc45e400080";
//https://github.com/manikanta110/PrDownloader
    // ============================================ Others ============================================ //
    public static Context appContext;
    // global topic to receive app wide push notifications

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String FIREBASE_TOKEN = "firebase_token";
    public static final String TOPIC_GLOBAL = "global";
    public static final String IS_TOKEN_AVAILABLE = "isTokenAvailable";

    public static final  String PowerAI = "/powerai-vision/api/dlapis/";
    public static final  String PowerAIURL = "/powerai-vision/api/dlapis/ef6e7760-71ff-4ecd-b719-d63c8d863ded";
    public static final String GETPORTFOLIOIMAGES = "/dentist_works_new";
    public static final String GET_TREATMENT_DATA = "/email_data";


    // ============================================ Initializer ============================================ //
    public static void initializeApplication(Context _appContext) {
        appContext = _appContext;
        VolleyWebLayer.initialize();
    }
}