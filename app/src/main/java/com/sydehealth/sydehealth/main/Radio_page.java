package com.sydehealth.sydehealth.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.firebase.client.annotations.Nullable;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.Actors;
import com.sydehealth.sydehealth.adapters.OnItemClickListener;
import com.sydehealth.sydehealth.adapters.RadioGridAdapter;
import com.sydehealth.sydehealth.adapters.RecycleBean;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.HeaderDecoration;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.UserAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Radio_page extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    UsefullData usefullData;
    SaveData saveData;
    ArrayList<String> campaigns_list = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    View header;
    TextView not_found;
    RecyclerView mRecyclerView= null;
    private RadioGridAdapter adapter = null;
    private GridLayoutManager mGridLayoutManager = null;
    List<RecycleBean> mList = new ArrayList<>();
    View view;
    Context mContext;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.activity_radio_page,container,false);

            initView();
        }
        return view;
    }


    /**
     * Method to initialize views
     */
    private void initView() {

        mContext=getActivity();
        usefullData=new UsefullData(mContext);
        saveData=new SaveData(mContext);


        not_found=(TextView) view.findViewById(R.id.not_found_radio);
        not_found.setText(R.string.no_playlist);
        not_found.setTypeface(usefullData.get_montserrat_regular());
//        header_layer=(LinearLayout) findViewById(R.id.header_layer);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout_radio);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.switch_color);
        swipeRefreshLayout.setSoundEffectsEnabled(true);
//        back=(LinearLayout) findViewById(R.id.main_back);
//        back.setBackgroundResource(R.mipmap.playlist_background);
//        LayoutInflater layoutInflater = LayoutInflater.from(getParent());
//        header = (View) layoutInflater.inflate(R.layout.listview_header, null);
//        gv=(GridViewWithHeaderAndFooter) findViewById(R.id.playlist_condition);
//        gv.setFriction(ViewConfiguration.getScrollFriction() * 20);
//        gv.setNumColumns(4);
//        surgery_logo=(ImageView) findViewById(R.id.surgery_logo);
//        page_logo=(ImageView) findViewById(R.id.page_logo);
//        page_logo.setImageResource(R.mipmap.playlist_big);
//        title=(TextView) findViewById(R.id.condition_title);
//        title.setTypeface(usefullData.get_semibold());
//        title.setText(R.string.play_list);
//        adapter = new Playlist_adapter(this, R.layout.row_condition, actorsList);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        header = (View) layoutInflater.inflate(R.layout.listview_header, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_radio);
        HeaderDecoration headerDecoration = new HeaderDecoration(header,false,10f,10f,4);
        mRecyclerView.addItemDecoration(headerDecoration);

        mGridLayoutManager = new GridLayoutManager(mContext, 4);

//        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
//        layoutManager.setFlexDirection(FlexDirection.COLUMN);
//        layoutManager.setJustifyContent(JustifyContent.CENTER);
//        mRecyclerView.setLayoutManager(layoutManager);


        //mGridLayoutManager.setSpanSizeLookup(new MySizeLookup());
        adapter = new RadioGridAdapter(mContext, mList);
        adapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        get_playlist(true);


        if (saveData.isExist(Definitions.PLAYLIST_JSON_DATA)) {
            try {
                JSONObject jsonObj = new JSONObject(saveData.getString(Definitions.PLAYLIST_JSON_DATA));
                set_up_values(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            if(usefullData.isNetworkConnected()) {
                get_playlist(true);
            }else {
                usefullData.dismissProgress();
                swipeRefreshLayout.setRefreshing(false);
                usefullData.make_alert(getResources().getString(R.string.no_internet),false, mContext);
                check_data();
            }
        }




//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                    long id)
//            {
//
//                if(position-1==0) {
//                    Intent edit = new Intent(getParent(), CustomPlayerControlActivity.class);
//                    edit.putExtra("current_video_id", actorsList.get(position-1).getItem_id());
//                    edit.putStringArrayListExtra("campaigns_list", campaigns_list);
//
//                    edit.putExtra("sky_news", true);
//                    edit.putExtra("request_from", "playlist");
//                    startActivity(edit);
//                }else {
//                    Intent edit = new Intent(getParent(), CustomPlayerControlActivity.class);
//                    edit.putExtra("current_video_id", actorsList.get(position-1).getItem_id());
//                    edit.putStringArrayListExtra("campaigns_list", campaigns_list);
//                    edit.putExtra("sky_news", false);
//                    edit.putExtra("request_from", "playlist");
//                    edit.putExtra("track_name",actorsList.get(position).gettitle());
//                    edit.putStringArrayListExtra("all_video_urls",actorsList.get(position-1).get_urls());
//                    edit.putExtra("current_postion",position-1);
//                    startActivity(edit);
//                }
//            }
//        });

//        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState)
//            {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount)
//            {
//
//                int lastItem = firstVisibleItem + visibleItemCount;
//
//                if (firstVisibleItem == 0) {
//                    //first item visible
//                    header_layer.setBackgroundColor(Color.TRANSPARENT);
//                }else {
//
//                    header_layer.setBackgroundColor(getResources().getColor(R.color.transparentGray));
//                }
//
//                gv.invalidateViews();
//            }
//        });


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

            if(usefullData.isNetworkConnected()) {
//                Bundle bundle=new Bundle();
//                bundle.putString("title", bean.getTitle());
//                bundle.putInt("current_condition", bean.getType());
//                Radio_page condition_player=new Radio_page();
//                condition_player.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,condition_player).addToBackStack(null).commit();

                if (!bean.getCount().equalsIgnoreCase("null")) {
                    Intent edit = new Intent(mContext, Radio_player.class);
                    edit.putExtra("current_video_id", bean.getId());
                    if (bean.getTitle().equalsIgnoreCase("News")) {

                        edit.putExtra("sky_news", true);

                    } else {

                        edit.putExtra("sky_news", false);

                    }
                    edit.putStringArrayListExtra("campaigns_list", campaigns_list);
                    edit.putExtra("request_from", "playlist");
                    edit.putExtra("track_name", bean.getTitle());
                    edit.putExtra("video_url", bean.getCount());
                    edit.putExtra("current_postion", bean.getType());
                    startActivity(edit);
                }else {
                    usefullData.make_alert(getResources().getString(R.string.no_playlist_found),false,mContext);
                }
            }else {
                usefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);
            }
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    };


    private void get_playlist(final boolean loader) {

        if (loader) {
            usefullData.showProgress();

        }
        Map<String,String> headers = new HashMap<>();

        headers.put("Accept", Definitions.VERSION);
        headers.put( "X-User-Email", saveData.get(Definitions.USER_EMAIL) );
        headers.put( "X-User-Token", saveData.get(Definitions.USER_TOKEN) );


        UserAPI.get_JsonObjResp(Definitions.PLAYLIST, headers, null,
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
                            if (loader) {
                                usefullData.dismissProgress();
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if(response.statusCode == 500){
                                    usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                }
                            }else{
                                usefullData.make_alert(getResources().getString(R.string.no_internet_speed),false,mContext);
                            }


                            mRecyclerView.setEnabled(true);
                            check_data();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
    }

    private void set_up_values(JSONObject response) {

        saveData.save(Definitions.PLAYLIST_JSON_DATA,response.toString());
        mList.clear();
        try {
//            if(gv.getHeaderViewCount()==0) {
//                gv.removeHeaderView(header);
//                gv.setAdapter(null);
//                gv.addHeaderView(header, null, false);
//            }

            if(!response.isNull("campaigns")) {
                JSONArray campaigns = response.getJSONArray("campaigns");
                for (int i = 0; i < campaigns.length(); i++) {
                    JSONObject in = campaigns.getJSONObject(i);

                    String thumbnail_url = in.optString("thumbnail_url");
                    campaigns_list.add(thumbnail_url);
                    if(i==0){
//                        Glide.with(getParent())
//                                .load(thumbnail_url)
//                                .asBitmap()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                    }
                }
            }
            if(!response.isNull("playlists")) {
                JSONArray conditions = response.getJSONArray("playlists");

                for (int i = 0; i < conditions.length(); i++) {
                    JSONObject in = conditions.getJSONObject(i);

//                    Actors actor = new Actors();

                    String title = in.optString("name");
                    String thumbnail_url = in.optString("thumbnail_url");
                    String playlist_url = in.optString("playlist_url");
                    int id = in.optInt("id");

//                    actor.settitle(title);
//                    actor.setImage(thumbnail_url);
//                    actor.setItem_id(id);

//                    JSONArray video_urls = in.getJSONArray("video_urls");
//                    ArrayList<String> all_urls = new ArrayList<String>();
//
//                    for (int j = 0; j < video_urls.length(); j++) {
//                        String url = video_urls.getString(j);
//                        all_urls.add(url);
//
//                    }
                    RecycleBean bean = new RecycleBean();
                    bean.setTitle(title);
                    bean.setUrl(thumbnail_url);
                    bean.setCount(playlist_url);
                    bean.setId(id);
                    bean.setType(i);
                    mList.add(bean);
//                    actor.set_urls(all_urls);
//                    actorsList.add(actor);

                }
            }

//            if(!response.isNull("surgery_logo")){
//                saveData.save(Definitions.SURGERY_LOGO,response.optString("surgery_logo"));
//                Glide.with(getParent())
//                        .load(response.optString("surgery_logo"))
//                        .asBitmap()
//                        .placeholder(R.mipmap.jukepad_text)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(surgery_logo);
//            }
            mRecyclerView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();






        } catch (JSONException e) {
            e.printStackTrace();
        }

        check_data();
//        if(saveData.isExist(Definitions.SURGERY_LOGO)) {
//            Glide.with(getParent())
//                    .load(saveData.getString(Definitions.SURGERY_LOGO))
//                    .asBitmap()
//                    .placeholder(R.mipmap.jukepad_text)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(surgery_logo);
//        }

    }

    private void check_data() {
        if (mList.isEmpty()) {
            not_found.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            not_found.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {

        if(usefullData.isNetworkConnected()) {

            mRecyclerView.setEnabled(false);
            swipeRefreshLayout.setRefreshing(true);
            get_playlist(false);


        }else {
            usefullData.dismissProgress();
            swipeRefreshLayout.setRefreshing(false);
            usefullData.make_alert(getResources().getString(R.string.no_internet),false,mContext);
        }
    }


    public class Playlist_adapter extends ArrayAdapter<Actors> {
        ArrayList<Actors> actorList;

        LayoutInflater vi;
        int Resource;
        ViewHolder holder;
        Context _context;


        public Playlist_adapter(Context context, int resource, ArrayList<Actors> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            actorList = objects;
            _context = context;


        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);
//                holder.imageview = (ImageView) v.findViewById(R.id.imageView17);
//                holder.click_here = (LinearLayout) v.findViewById(R.id.click_here);
//                holder.click_here.setLongClickable(true);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

//            Glide.with(getParent())
//                    .load(actorList.get(position).getImage())
//                    .asBitmap()
//                    .placeholder(R.mipmap.condition_thumb)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.imageview);

////            holder.click_here.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                    if(usefullData.isNetworkConnected()) {
////
////                        if (actorsList.get(position).gettitle().equals("News")) {
////                            Intent edit = new Intent(getParent(), CustomPlayerControlActivity.class);
////                            edit.putExtra("current_video_id", actorsList.get(position).getItem_id());
////                            edit.putStringArrayListExtra("campaigns_list", campaigns_list);
////
////                            edit.putExtra("sky_news", true);
////                            edit.putExtra("request_from", "playlist");
////                            startActivity(edit);
////                        } else {
////                            Intent edit = new Intent(getParent(), CustomPlayerControlActivity.class);
////                            edit.putExtra("current_video_id", actorsList.get(position).getItem_id());
////                            edit.putStringArrayListExtra("campaigns_list", campaigns_list);
////                            edit.putExtra("sky_news", false);
////                            edit.putExtra("request_from", "playlist");
////                            edit.putExtra("track_name", actorsList.get(position).gettitle());
////                            edit.putStringArrayListExtra("all_video_urls", actorsList.get(position).get_urls());
////                            edit.putExtra("current_postion", position);
////                            startActivity(edit);
////                        }
////                    }else {
////                        usefullData.make_alert(getResources().getString(R.string.no_internet),false,getParent());
////                    }
////
////
////                }
//
//
//            });

//            holder.click_here.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    // TODO Auto-generated method stub
//                    holder.click_here.performClick();
//                    return true;
//                }
//            });


            return v;

        }

        class ViewHolder {
            public ImageView imageview;
            public LinearLayout click_here;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(usefullData.playlist_update_time()) {
            onRefresh();
        }
//        if (saveData.isExist(Definitions.SURGERY_LOGO)){
//            Glide.with(getParent())
//                    .load(saveData.getString(Definitions.SURGERY_LOGO))
//                    .asBitmap()
//                    .placeholder(R.mipmap.jukepad_text)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(surgery_logo);
//        }

    }



    public static class MySizeLookup extends GridLayoutManager.SpanSizeLookup {

        public int getSpanSize(int position) {
            if(position >= 0 && position <= 2) {
                return 2;
            } else if (position == 3 || position == 6) {
                return 1;
            } else {
                return 2;
            }
        }
    }






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


}
