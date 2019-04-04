package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.main.Condition_player;
import com.sydehealth.sydehealth.model.SubHeadings;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.core.content.ContextCompat;

public class TreatmentExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> expandableListTitle;
    private ArrayList<Integer> expandableListId;

    private ArrayList<String> listSubDataHeader = new ArrayList<>();
    private ArrayList<Integer> listSubDataId = new ArrayList<>();

    private ArrayList<String> listDataHeaderImage = new ArrayList<>();
    private ArrayList<String> listSubDataHeaderImage = new ArrayList<>();
    ;
    private TreatmentExpandableListListener mListener;

    private TreatmentExpandableChildListListener mChildListener;

    UsefullData objUsefullData;


    public TreatmentExpandableAdapter(Context context, ArrayList<String> expandableListTitle,
                                      ArrayList<Integer> expandableListId,ArrayList<String> listDataHeaderImage
                                    ,ArrayList<String> listSubDataHeader, ArrayList<Integer> listSubDataId, ArrayList<String> listSubDataHeaderImage
                                    ,TreatmentExpandableListListener mListener) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.mChildListener = mChildListener;
        this.mListener = mListener;
        this.listSubDataHeader = listSubDataHeader;
        this.listSubDataId =  listSubDataId;
        this.expandableListId = expandableListId;
        this.listDataHeaderImage = listDataHeaderImage;
        this.listSubDataHeaderImage = listSubDataHeaderImage;
        objUsefullData = new UsefullData(context);
    }



    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //final String strChildName = (String) getChild(listPosition, expandedListPosition);

//        String groupId = expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                .get(expandedListPosition).getGroupId();
//
//        String childId = expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                .get(expandedListPosition).getChildId();

        //View view = super.getChildView(listPosition, expandedListPosition, isLastChild, convertView, parent);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.treatment_child_item, null);
        }

        try{
            LinearLayout ll_subtitle = (LinearLayout) convertView.findViewById(R.id.ll_subtitle);
            TextView txt_condition_subtitle = (TextView) convertView.findViewById(R.id.txt_condition_subtitle);

            txt_condition_subtitle.setTypeface(objUsefullData.get_montserrat_regular());

            txt_condition_subtitle.setText(listSubDataHeader.get(expandedListPosition));

            ImageView iv_select = (ImageView) convertView.findViewById(R.id.iv_select);

            iv_select.setVisibility(View.GONE);


            ImageView thumb_icon =  (ImageView) convertView
                    .findViewById(R.id.thumb_icon);


            Glide.with(context).load(listSubDataHeaderImage.get(expandedListPosition))
                    .placeholder(R.drawable.loader_background)
//                    .error(R.mipmap.loader_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumb_icon);

        }catch (Exception e){
            e.printStackTrace();
        }


//        if (listSubDataHeader.get(expandedListPosition).isSelected()) {
//            iv_select.setBackgroundResource(R.drawable.grident_circle_white_border);
//            iv_select.setImageResource(R.drawable.ic_check_white);
//            ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
//        } else {
//            iv_select.setBackgroundResource(R.drawable.circle_grey_outline);
//            ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }


        //.setBackgroundResource(isExpanded?R.drawable.grident_circle_white_border:R.drawable.grey_circle_bg);

//        ll_subtitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Log.e("data--", expandableListTitle.get(listPosition) + "(" + strChildName + ")");
////                for (int i = 0; i < expandableListDetail.get(expandableListTitle.get(listPosition)).size(); i++) {
////                    if (i == expandedListPosition) {
////                        if (expandableListDetail.get(expandableListTitle.get(listPosition))
////                                .get(expandedListPosition).isSelected()) {
////                            expandableListDetail.get(expandableListTitle.get(listPosition))
////                                    .get(expandedListPosition).setSelected(false);
////
////                        } else {
////                            expandableListDetail.get(expandableListTitle.get(listPosition)).get(expandedListPosition).setSelected(true);
////                        }
////                    } else {
////                        expandableListDetail.get(expandableListTitle.get(listPosition)).get(i).setSelected(false);
////                    }
////                }
//                notifyDataSetChanged();
//
//                //mListener.onListItemClick(listPosition, groupId, childId, expandableListDetail.get(expandableListTitle.get(listPosition)).get(expandedListPosition).isSelected());
//            }
//        });

//        for (int i = 0; i < listSubDataHeader.size(); i++) {
//            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//        }


        if(Condition_player.isPortfolio || Condition_player.isGroup){
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }


        if(Condition_player.isElement1) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (expandedListPosition == 0) {
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.list_bg_color));
            }
            if(expandedListPosition == 1){
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            if(expandedListPosition == 2){
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }


        else if(Condition_player.isElement2) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (expandedListPosition == 1) {
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.list_bg_color));
            }
            if(expandedListPosition == 0){
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            if(expandedListPosition == 2){
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }


        else if(Condition_player.isElement3) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (expandedListPosition == 2) {
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.list_bg_color));
            }
            if(expandedListPosition == 1){
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            if(expandedListPosition == 0){
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }

        else {
            if(!Condition_player.isChild) {
                convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }

//        ll_subtitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onListItemClick(expandedListPosition, listSubDataId.get(expandedListPosition), listSubDataHeader.get(expandedListPosition));
////                if(listSubDataHeader.get(expandedListPosition).isSelected()) {
////                    ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
////                }
//            }
//        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if(listSubDataHeader == null){
            return 0;
        }
        return this.listSubDataHeader.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.listSubDataHeader.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
       // String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.treatment_parent_item, null);
        }
        ((ImageView) convertView.findViewById(R.id.iv_expand_status)).setImageResource(isExpanded ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
//        ((ImageView) convertView.findViewById(R.id.iv_expand_status))
//                .setBackgroundResource(isExpanded?R.drawable.grident_circle_white_border:R.drawable.grey_circle_bg);

        LinearLayout ll_title = (LinearLayout) convertView.findViewById(R.id.ll_title);

        if (isExpanded) {
            ll_title.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
        } else {
            ll_title.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        TextView txt_condition_title = (TextView) convertView
                .findViewById(R.id.txt_condition_title);


        ImageView thumb_icon =  (ImageView) convertView
                .findViewById(R.id.thumb_icon);



        Glide.with(context).load(listDataHeaderImage.get(listPosition))
                .placeholder(R.drawable.loader_background)
//                    .error(R.mipmap.loader_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumb_icon);



        txt_condition_title.setText(expandableListTitle.get(listPosition));
        txt_condition_title.setTypeface(objUsefullData.get_montserrat_semibold());

        if(Condition_player.isGroup1Clicked) {
            ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (listPosition == 0) {
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.list_bg_color));
            }
            if(listPosition == 1){
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            if(listPosition == 2){
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }

        if(Condition_player.isGroup2Clicked) {
            ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (listPosition == 1) {
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.list_bg_color));
            }
            if(listPosition == 0){
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            if(listPosition == 2){
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }


        if(Condition_player.isGroup3Clicked) {
            ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (listPosition == 2) {
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.list_bg_color));
            }
            if(listPosition == 1){
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            if(listPosition == 0){
                ll_title.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }

        //
        // txt_condition_title.setText(objUsefullData.capitalize(listTitle.trim()));

        //mListener.onListItemClick(listPosition, expandableListId.get(listPosition));


//        ll_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                if(isExpanded){
////
////                }
//            }
//        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
