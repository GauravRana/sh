package com.sydehealth.sydehealth.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.sydehealth.sydehealth.adapters.UserAdapter;
import com.sydehealth.sydehealth.database.DatabaseHelper;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class User_account extends AppCompatActivity {


    ExpandableHeightListView listView;
    UserAdapter adapter;
    UsefullData usefullData;
    SaveData saveData;
    static DatabaseHelper db;
    TextView label;
    Button add_account;
    TextView tv_wifi;
    LinearLayout wifi_btn;
    private static final int LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        usefullData=new UsefullData(User_account.this);
        saveData=new SaveData(User_account.this);
        listView=(ExpandableHeightListView) findViewById(R.id.listView);
        label=(TextView) findViewById(R.id.label) ;
        label.setTypeface(usefullData.get_montserrat_regular());
        add_account=(Button) findViewById(R.id.add_account) ;
        wifi_btn = findViewById(R.id.wifi_btn);
        tv_wifi = findViewById(R.id.tv_wifi);
        add_account.setTypeface(usefullData.get_montserrat_regular());
        db = new DatabaseHelper(getApplicationContext());

        db.openDB();
        if(!db.Execute_recent_user().isEmpty()) {

            adapter = new UserAdapter(getApplicationContext(), R.layout.row_user, db.Execute_recent_user());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        db.closeDB();

        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot=new Intent(User_account.this,Login_activity.class);
                forgot.putExtra("request","none");
                startActivity(forgot);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //finish();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent forgot=new Intent(User_account.this,Login_activity.class);
                db.openDB();
                if(!db.Execute_recent_user().isEmpty()) {
                    forgot.putExtra("request","user");
                    forgot.putExtra("email",db.Execute_recent_user().get(position).getusername());
                    forgot.putExtra("pswd",db.Execute_recent_user().get(position).gettitle());
                    forgot.putExtra("remember",db.Execute_recent_user().get(position).getselected());
                }
                db.closeDB();

                startActivity(forgot);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
               // finish();

            }
        });

        wifi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == LOCATION){
            //User allowed the location and you can read it now
            tryToReadSSID();
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
    protected void onResume() {
        super.onResume();
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
}
