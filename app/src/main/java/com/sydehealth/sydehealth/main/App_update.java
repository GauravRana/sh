package com.sydehealth.sydehealth.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by POPLIFY on 11/7/2017.
 */

public class App_update extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        get_conditions();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        return super.onStartCommand(intent, flags, startId);
    }

    private void get_conditions() {

        UsefullData usefullData=new UsefullData(this);
        final SaveData saveData=new SaveData(this);
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put( "X-User-Email", saveData.get(Definitions.USER_EMAIL) );
        headers.put( "X-User-Token", saveData.get(Definitions.USER_TOKEN) );

        UserAPI.get_JsonObjResp(Definitions.CONDITIONS, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());
                        saveData.save(Definitions.CONDITIONS_JSON_DATA,response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
    }
}
