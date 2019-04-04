package com.sydehealth.sydehealth.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.PortfolioAdapter;
import com.sydehealth.sydehealth.adapters.RecyclerviewOnItemClickListener;
import com.sydehealth.sydehealth.model.portfolio.InstaPhotoResponse;
import com.sydehealth.sydehealth.model.portfolio.PortfolioImage;
import com.sydehealth.sydehealth.model.portfolio.PortfolioResponse;
import com.sydehealth.sydehealth.utility.CustomSwipeToRefresh;
import com.sydehealth.sydehealth.utility.Definitions;
import com.sydehealth.sydehealth.utility.LinearLayoutPagerManager;
import com.sydehealth.sydehealth.utility.SaveData;
import com.sydehealth.sydehealth.utility.TabGroupActivity;
import com.sydehealth.sydehealth.utility.TouchImageView;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.volley.InitializeActivity;
import com.sydehealth.sydehealth.volley.UserAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MyPortfolioActivity extends Fragment implements View.OnClickListener {

    private CustomSwipeToRefresh swipe_refresh_layout;
    private ImageView iv_insta;
    private TouchImageView iv_preview_image;
    private RecyclerView rv_portfolio;
    private TextView txt_no_images;
    private ImageButton iv_left;
    private ImageButton iv_right;
    private ImageButton iv_back;
    private Context mContext;
    private UsefullData usefullData;
    private SaveData saveData;
    private ArrayList<PortfolioImage> mainPhotoArr;
    private PortfolioAdapter portfolioAdapter;
    private static final String TAG = "MyPortfolioActivity";
    private TextView txtPort;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //((Tab_activity)getActivity()).ll_email.setBackground(null);
        mContext = getActivity();
        Tab_activity.ll_bottombar.setVisibility(View.GONE);
        if(view==null){
            view=inflater.inflate(R.layout.activity_my_portfolio,container,false);
            init(view);
            getPortfolioData();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(InitializeActivity.isIsRadioPlay()){
//            Tab_activity.jcplayerView.continueAudio();
//        }else{
//            Tab_activity.jcplayerView.pause();
//        }
    }

    private void init(View view) {
        usefullData = new UsefullData(mContext);
        saveData = new SaveData(mContext);

        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layout);
        iv_insta = view.findViewById(R.id.iv_insta);
        iv_preview_image = view.findViewById(R.id.iv_preview_image);
        rv_portfolio = view.findViewById(R.id.rv_portfolio);
        txt_no_images = view.findViewById(R.id.txt_no_images);
        txtPort = view.findViewById(R.id.txtPort);
        iv_left = view.findViewById(R.id.iv_left);
        iv_right = view.findViewById(R.id.iv_right);
        iv_back = view.findViewById(R.id.iv_back);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        txtPort.setTypeface(usefullData.get_montserrat_regular());
        swipe_refresh_layout.setColorSchemeResources(R.color.switch_color);
        swipe_refresh_layout.setSoundEffectsEnabled(true);

        //init views end

        portfolioAdapter = new PortfolioAdapter(getActivity(), null);

        LinearLayoutPagerManager linearLayoutPagerManager
                = new LinearLayoutPagerManager(getActivity(), LinearLayoutManager.HORIZONTAL, false, 4.5f);

        rv_portfolio.setLayoutManager(linearLayoutPagerManager);
        rv_portfolio.setAdapter(portfolioAdapter);


        portfolioAdapter.setOnItemClickListener(new RecyclerviewOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setPreviewPhoto(portfolioAdapter.getmDataSet().get(position).getImageUrl());
                changeLeftRightButtonViews(position);
            }
        });

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(false);

                getPortfolioData();
            }
        });
    }


    private void getPortfolioData() {


        if (usefullData.isNetworkConnected()) {

            usefullData.showProgress();

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", Definitions.VERSION);
            headers.put("X-User-Email", saveData.get(Definitions.USER_EMAIL));
            headers.put("X-User-Token", saveData.get(Definitions.USER_TOKEN));

            UserAPI.get_JsonObjResp(Definitions.GETPORTFOLIOIMAGES, headers, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("TAG response", response.toString());

                            usefullData.dismissProgress();
                            swipe_refresh_layout.setRefreshing(false);

                            Gson gson = new Gson();
                            PortfolioResponse portfolioResponse =
                                    gson.fromJson(response.toString(), PortfolioResponse.class);


                            if (portfolioResponse.getAccessToken() != null && !portfolioResponse.getAccessToken().isEmpty()) {
                                // instagram photo fetching
                                iv_insta.setVisibility(View.GONE);

                                mainPhotoArr = new ArrayList<>();

                                getInstagramPhotos(portfolioResponse.getAccessToken(), "");

                            } else if (portfolioResponse.getResult() != null) {
                                //server photos
                                iv_insta.setVisibility(View.GONE);

                                mainPhotoArr = new ArrayList<>();

                                try {
                                    for (int i = 0; i < portfolioResponse.getResult().size(); i++) {
                                        mainPhotoArr.add(new PortfolioImage(portfolioResponse.getResult().get(i).getImageUrl()));
                                    }

                                    if (!mainPhotoArr.isEmpty()) {
                                        setPreviewPhoto(mainPhotoArr.get(0).getImageUrl());
                                        portfolioAdapter.updateDataSet(mainPhotoArr);
                                        txt_no_images.setVisibility(View.GONE);
                                        changeLeftRightButtonViews(0);
                                        rv_portfolio.setVisibility(View.VISIBLE);

                                    } else {
                                        emptyPhotoListing();
                                    }
                                } catch (Exception e) {
                                    usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                    emptyPhotoListing();
                                }

                            } else {
                                usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                emptyPhotoListing();
                                //result and access_token; both are null; give error
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                emptyPhotoListing();
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                } else {
                                    usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                                }

                                usefullData.dismissProgress();
                                swipe_refresh_layout.setRefreshing(false);

                            } catch (Exception e) {

                            }

                        }
                    });
        } else {
            usefullData.make_alert(getResources().getString(R.string.no_internet), false, mContext);
        }

    }


    private void getInstagramPhotos(final String accessToken, final String nextMaxId) {

        if (!usefullData.isDialogShowing())
            usefullData.showProgress();

        //Define Headers
        Map<String, String> headers = new HashMap<>();

        String url = Definitions.INSTAGRAM_DOMAIN + accessToken + "&max_id=" + nextMaxId;

        UserAPI.get_JsonObjRespCustom(url, headers, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);

                        if (mainPhotoArr == null)
                            mainPhotoArr = new ArrayList<>();

                        try {
                            Gson gson = new Gson();
                            InstaPhotoResponse instaPhotoResponse =
                                    gson.fromJson(response.toString(), InstaPhotoResponse.class);

                            if (instaPhotoResponse.getData() != null && instaPhotoResponse.getMeta().getCode() == 200) {

                                for (int i = 0; i < instaPhotoResponse.getData().size(); i++) {
                                    InstaPhotoResponse.Data tmpPost = instaPhotoResponse.getData().get(i);
                                    if (tmpPost.getType().equalsIgnoreCase("carousel")) {
                                        for (int j = 0; j < tmpPost.getCarouselMedia().size(); j++) {
                                            InstaPhotoResponse.CarouselMedium tmpPhotoIncarousel
                                                    = tmpPost.getCarouselMedia().get(j);

                                            mainPhotoArr.add(new PortfolioImage(tmpPhotoIncarousel.getImages().getStandardResolution().getUrl()));
                                        }
                                    } else if (tmpPost.getType().equalsIgnoreCase("image")) {
                                        mainPhotoArr.add(new PortfolioImage(tmpPost.getImages().getStandardResolution().getUrl()));
                                    }
                                }

                                // if pagination has data
                                // else set photo

                                if (instaPhotoResponse.getPagination().getNextMaxId() != null
                                        && !instaPhotoResponse.getPagination().getNextMaxId().isEmpty()) {

                                    getInstagramPhotos(accessToken, instaPhotoResponse.getPagination().getNextMaxId());
                                } else {
                                    usefullData.dismissProgress();
                                    setPreviewPhoto(mainPhotoArr.get(0).getImageUrl());
                                    portfolioAdapter.updateDataSet(mainPhotoArr);
                                    txt_no_images.setVisibility(View.GONE);
                                    changeLeftRightButtonViews(0);
                                    rv_portfolio.setVisibility(View.VISIBLE);

                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            emptyPhotoListing();
                            usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Log.v("TAG response", error.toString());
                            usefullData.dismissProgress();

                            emptyPhotoListing();

                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                if (response.statusCode == 500) {
                                    usefullData.make_alert(getResources().getString(R.string.no_api_hit), false, mContext);
                                }
                            } else {
                                usefullData.make_alert(getResources().getString(R.string.no_internet_speed), false, mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //finish();
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.iv_left:
                previewPreviousImage();
                break;
            case R.id.iv_right:
                previewNextImage();
                break;
        }
    }

    private void setPreviewPhoto(String url) {

        if (url == null || url.isEmpty()) return;

        Glide.with(mContext)
                .load(url)
                .override(20, 20)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_preview_image);

        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .error(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_preview_image);
    }

    private void emptyPhotoListing() {
        rv_portfolio.setVisibility(View.GONE);
        txt_no_images.setVisibility(View.VISIBLE);
        iv_preview_image.setImageResource(R.drawable.image_placeholder);
    }

    private void previewPreviousImage() {
        if (portfolioAdapter.getmDataSet() == null) return;

        int currentPosition = portfolioAdapter.getSelectedImgPosition();
        if (currentPosition == 0) {
            portfolioAdapter.setSelectedImgPosition(portfolioAdapter.getItemCount() - 1);
            rv_portfolio.scrollToPosition(portfolioAdapter.getSelectedImgPosition());
        } else if (currentPosition > 0) {
            portfolioAdapter.setSelectedImgPosition(--currentPosition);
            rv_portfolio.scrollToPosition(portfolioAdapter.getSelectedImgPosition());
        }
        changeLeftRightButtonViews(currentPosition);
    }

    private void previewNextImage() {
        if (portfolioAdapter.getmDataSet() == null) return;

        int currentPosition = portfolioAdapter.getSelectedImgPosition();

        if (currentPosition == portfolioAdapter.getItemCount() - 1) {
            portfolioAdapter.setSelectedImgPosition(0);
            rv_portfolio.scrollToPosition(portfolioAdapter.getSelectedImgPosition());
        } else if (currentPosition < portfolioAdapter.getmDataSet().size() - 1) {
            portfolioAdapter.setSelectedImgPosition(++currentPosition);
            rv_portfolio.scrollToPosition(portfolioAdapter.getSelectedImgPosition());
        }
        changeLeftRightButtonViews(currentPosition);
    }

    private void changeLeftRightButtonViews(int currentPosition) {
//        iv_left.setAlpha(1f);
//        iv_right.setAlpha(1f);
//        iv_left.setEnabled(true);
//        iv_right.setEnabled(true);
//
//        if (currentPosition == 0) {
//            iv_left.setAlpha(0.3f);
//            iv_left.setEnabled(false);
//        } else if (currentPosition == portfolioAdapter.getItemCount() - 1) {
//            iv_right.setAlpha(0.3f);
//            iv_right.setEnabled(false);
//        } else if (portfolioAdapter.getItemCount() == 0) {
//            iv_left.setAlpha(0.3f);
//            iv_left.setEnabled(false);
//            iv_right.setAlpha(0.3f);
//            iv_right.setEnabled(false);
//        }

        int adapterCount = portfolioAdapter.getItemCount();
        if (adapterCount == 0 || adapterCount == 1) {
            iv_left.setAlpha(0.3f);
            iv_left.setEnabled(false);
            iv_right.setAlpha(0.3f);
            iv_right.setEnabled(false);
        } else {
            iv_left.setAlpha(1f);
            iv_left.setEnabled(true);
            iv_right.setAlpha(1f);
            iv_right.setEnabled(true);
        }

        iv_preview_image.resetZoom();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tab_activity.ll_bottombar.setVisibility(View.VISIBLE);
    }

}



