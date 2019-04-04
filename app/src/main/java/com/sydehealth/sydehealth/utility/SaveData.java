package com.sydehealth.sydehealth.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveData {

    private Context _context;
    private SharedPreferences shared;
    private String SHARED_NAME = "Shelf";
    private String PLAYLIST_SHARED_NAME = "media_playlist";
    private String KeyBack = "fromAIBack";
    private Editor edit;

    private int condition_id;

    private String keyBgeightPref;
    private int keyHeight = 0;

    private boolean isScreenShareActive;

    public String powerAIURL = "";

    public SaveData(Context c) {
        _context = c;
        shared = _context.getSharedPreferences(SHARED_NAME,
                Context.MODE_PRIVATE);
        edit = shared.edit();
    }

    // ============================================//
    public void save(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }


    public void saveConditionID(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }


    public int getConditionID(String key) {
        return shared.getInt(key, 0);

    }


    public void saveTreatmentID(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }


    public int getTreatmentID(String key) {
        return shared.getInt(key, 0);

    }


    public void saveTreatmentGroup(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }


    public int getTreatmentGroup(String key) {
        return shared.getInt(key, 0);

    }


    public void saveKeyBHeight(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }


    public int getKeyBHeight(String key) {
        return shared.getInt(key, 0);

    }



    public void saveConditionName(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }



    public String getConditonName(String key) {
        return shared.getString(key, "");
    }


    public void savePowerAIURL(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }



    public String getPowerAIURL(String key) {
        return shared.getString(key, "");
    }


    // ============================================//
    public void saveProgress(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }


    public String getProgress(String key) {
        return shared.getString(key, key);

    }



    public void saveScreenShareActive(String key, boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }


    public boolean getScreenShareActive(String key) {
        return shared.getBoolean(key, true);

    }


    public void save(String key, float value) {
        edit.putFloat(key, value);
        edit.commit();
    }


    // ============================================//
    public void save(String key, boolean value) {
        edit.putBoolean(key, value);
        edit.commit();
    }

    // ============================================//
    public void save(String key, int value) {
        edit.putInt(key, value);
        edit.commit();
    }

    public void save(String key, long value) {
        edit.putLong(key, value);
        edit.commit();
    }

    // ============================================//
    public String getString(String key) {
        return shared.getString(key, key);

    }


    // ============================================//
    public int getInt(String key) {
        return shared.getInt(key, 0);

    }

    // ============================================//
    public float getFloat(String key) {
        return shared.getFloat(key, 0);

    }

    // ============================================//
    public boolean getBoolean(String key) {
        return shared.getBoolean(key, true);

    }

    public long getLong(String key) {
        return shared.getLong(key, 0);

    }

    // ============================================//
    public boolean isExist(String key) {
        return shared.contains(key);

    }

    // ============================================//
    public void remove(String key) {

        edit.remove(key);
        edit.commit();

    }

    // ============================================//
    public String get(String key) {
        return shared.getString(key, key);

    }

    public void clearAll() {
        shared = _context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        shared.edit().clear().commit();

    }


    //

    public void storePlayList(String playListId, ArrayList<String> videoIds) {
        SharedPreferences sharedPrefs = _context.getSharedPreferences(PLAYLIST_SHARED_NAME,
                Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(videoIds);

        editor.putString(playListId, json);
        editor.commit();
    }

    public ArrayList<String> getPlayList(String playListId) {

        SharedPreferences sharedPrefs = _context.getSharedPreferences(PLAYLIST_SHARED_NAME,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(playListId, "[]");
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);

        return arrayList;
    }

    public void clearPlaylistData() {
        SharedPreferences sharedPrefs = _context.getSharedPreferences(PLAYLIST_SHARED_NAME,
                Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().apply();

    }


}
