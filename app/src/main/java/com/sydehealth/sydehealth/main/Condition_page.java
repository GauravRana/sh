package com.sydehealth.sydehealth.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sydehealth.sydehealth.adapters.ConditionGridAdapter;
import com.sydehealth.sydehealth.adapters.OnItemClickListener;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.RecycleBean;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.SaveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Condition_page extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    UsefullData usefullData;
    SaveData saveData;
    RecyclerView mRecyclerView= null;
    private ConditionGridAdapter adapter = null;
    private GridLayoutManager mGridLayoutManager = null;
    List<RecycleBean> mList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView not_found;
    public static TextView txt_welcome_msg;
    View view;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //((Tab_activity)getActivity()).ll_email.setBackground(null);
        Tab_activity.ll_bottombar.setVisibility(View.VISIBLE);
        if(view==null){
            view=inflater.inflate(R.layout.activity_conitions_page,container,false);
            initView();
        }
        return view;
    }


    /**
     * Method to initialize views
     */
    private void initView() {
        mContext=getActivity();
        usefullData = new UsefullData(mContext);
        saveData = new SaveData(mContext);
        txt_welcome_msg = (TextView) view.findViewById(R.id.txt_welcome_msg);
        txt_welcome_msg.setTypeface(usefullData.get_montserrat_regular());
        not_found = (TextView) view.findViewById(R.id.not_found);
        not_found.setTypeface(usefullData.get_montserrat_regular());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.switch_color);
        swipeRefreshLayout.setSoundEffectsEnabled(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_conditions);
        mGridLayoutManager = new GridLayoutManager(mContext, 5);
        adapter = new ConditionGridAdapter(mContext, mList);
        adapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        if (saveData.isExist(Definitions.CONDITIONS_JSON_DATA)) {
            try {
                JSONObject jsonObj = new JSONObject(saveData.getString(Definitions.CONDITIONS_JSON_DATA));
                set_up_values(jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            if (usefullData.isNetworkConnected()) {
                get_conditions(true);
            } else {
                usefullData.dismissProgress();
                swipeRefreshLayout.setRefreshing(false);
                usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);
                check_data();
            }
        }

    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickMe(View view, int position) {

        }

        @Override
        public void onItemClickComplete(View view, RecycleBean bean, int position) {

        }

        @Override
        public void onItemClick(View view, RecycleBean bean) {
            /*Intent edit = new Intent(mContext, Condition_player.class);
            TabGroupActivity parentActivity = (TabGroupActivity) mContext;
            edit.putExtra("title", bean.getTitle());
            edit.putExtra("current_condition", bean.getType());
            parentActivity.startChildActivity("video_index_Activity", edit);
*/
            Bundle bundle=new Bundle();
            bundle.putString("title", bean.getTitle());
            bundle.putInt("current_condition", bean.getType());
            bundle.putInt("condition_id", bean.getId());
            Condition_player condition_player=new Condition_player();
            condition_player.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,condition_player).addToBackStack(null).commit();
            usefullData.Mixpanel_events("Condition Viewed", "Condition Name", bean.getTitle());
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    };

    public void check_data() {
        if (mList.isEmpty()) {
            not_found.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            not_found.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (usefullData.condition_update_time()) {
            onRefresh();
        }
    }

    private void get_conditions(final boolean loader) {

        if (loader) {
            usefullData.showProgress();

        }
        Map<String, String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
        headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));


        UserAPI.get_JsonObjResp(Definitions.CONDITIONS, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("TAG response", response.toString());

                        set_up_values(response);
                        if (loader) {
                            usefullData.dismissProgress();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        mRecyclerView.setEnabled(true);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try{
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if(response.statusCode == 500){
                                    usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                }
                            }else{
                                usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                            }

                            if (loader) {
                                usefullData.dismissProgress();
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            mRecyclerView.setEnabled(true);
                            check_data();
                        }catch (Exception e){

                        }

                    }
                });


    }

    private void set_up_values(JSONObject response) {
        saveData.save(Definitions.CONDITIONS_JSON_DATA, response.toString());
        mList.clear();

        try {

            saveData.save(Definitions.SURGERY_NAME,response.optString("surgery_name"));
            //sample = sample.replaceAll(" ,", ",").replaceAll(" )", ")");
            Tab_activity.name.setText(usefullData.capitalize(response.optString("surgery_name").trim()));


            if (!response.isNull("link")) {
                String url = response.optString("link");
                saveData.savePowerAIURL("PowerAIURL",url);

            }

            if (!response.isNull("user_name")) {
                String user_name = response.optString("user_name");
                saveData.save(Definitions.USER_NAME_HEADER,user_name);
                usefullData.time_check();

            }
            if (!response.isNull("surgery_logo")) {
                saveData.save(Definitions.SURGERY_LOGO, response.optString("surgery_logo"));
                Glide.with(getActivity())
                        .load(response.optString("surgery_logo"))
                        .asBitmap()
                        .placeholder(R.drawable.logo_white)
                        .error(R.drawable.logo_white)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(Tab_activity.surgery_logo);
            }

//            if (!response.isNull("screensaver")) {
//
//                saveData.save(Definitions.SCREENSAVER_ID, response.getJSONObject("screensaver").optInt("id"));
//                saveData.save(Definitions.SCREENSAVER_NAME, response.getJSONObject("screensaver").optString("name"));
//                saveData.save(Definitions.SCREENSAVER_IMAGE, response.getJSONObject("screensaver").optString("thumbnail_url"));
//            }



            if (!response.isNull("conditions_android")) {
                JSONArray conditions = response.getJSONArray("conditions_android");

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject in = conditions.getJSONObject(i);

                    String title = in.optString("name");
                    String thumbnail_url = in.optString("thumbnail_url");
                    int treatment_options_count= in.optInt("treatment_options_count");
                    int condition_videos_count= in.optInt("presentations_count");
                    int id=in.optInt("id");

                    RecycleBean bean = new RecycleBean();
                    bean.setTitle(title);
                    bean.setUrl(thumbnail_url);
                    bean.setCount(""+treatment_options_count);
                    if(in.isNull("presentations_count")){
                        bean.setTag(""+0);
                    }else {
                        bean.setTag(""+condition_videos_count);
                    }

                    bean.setType(i);
                    bean.setId(id);
                    mList.add(bean);
                }
            }
            mRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        check_data();

    }

    @Override
    public void onRefresh() {
        if (usefullData.isNetworkConnected()) {
//            gv.setEnabled(false);
            swipeRefreshLayout.setRefreshing(true);
            get_conditions(false);
        } else {
            usefullData.dismissProgress();
            swipeRefreshLayout.setRefreshing(false);
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tab_activity.ll_bottombar.setVisibility(View.VISIBLE);
    }
}
