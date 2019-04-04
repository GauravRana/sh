package com.example.jean.jcplayer;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    static public Context _context;

    public Utils(Context c) {
        _context = c;
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
                "Montserrat-Regular.ttf");
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

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
