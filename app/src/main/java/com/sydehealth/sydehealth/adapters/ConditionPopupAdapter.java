package com.sydehealth.sydehealth.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.model.SubHeadings;
import com.sydehealth.sydehealth.utility.UsefullData;

public class ConditionPopupAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<SubHeadings>> expandableListDetail;
    private ExpandableListClickListener mListener;
    UsefullData objUsefullData;

    public ConditionPopupAdapter(Context context, ArrayList<String> expandableListTitle,
                                 HashMap<String, ArrayList<SubHeadings>> expandableListDetail, ExpandableListClickListener mListener) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.mListener = mListener;
        objUsefullData = new UsefullData(context);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).getName();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String strChildName = (String) getChild(listPosition, expandedListPosition);

        String groupId = expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).getGroupId();


        String groupName = expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).getGroupName();

        String childId = expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).getChildId();

        String ChildName = expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).getName();


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.condition_popup_child_item, null);
        }


        LinearLayout ll_subtitle = (LinearLayout) convertView.findViewById(R.id.ll_subtitle);
        TextView txt_condition_subtitle = (TextView) convertView.findViewById(R.id.txt_condition_subtitle);

        txt_condition_subtitle.setTypeface(objUsefullData.get_montserrat_regular());

        txt_condition_subtitle.setText(objUsefullData.capitalize(strChildName.trim()));

        ImageView iv_select = (ImageView) convertView.findViewById(R.id.iv_select);



        if (expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition).isSelected()) {
            iv_select.setBackgroundResource(R.drawable.grident_circle_white_border);
            iv_select.setImageResource(R.drawable.ic_check_white);
            ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
        } else {
            iv_select.setBackgroundResource(R.drawable.circle_grey_outline);
            ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.white));
        }


        //.setBackgroundResource(isExpanded?R.drawable.grident_circle_white_border:R.drawable.grey_circle_bg);

        ll_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("data--", expandableListTitle.get(listPosition) + "(" + strChildName + ")");
                for (int i = 0; i < expandableListDetail.get(expandableListTitle.get(listPosition)).size(); i++) {
                    if (i == expandedListPosition) {
                        if (expandableListDetail.get(expandableListTitle.get(listPosition))
                                .get(expandedListPosition).isSelected()) {
                            expandableListDetail.get(expandableListTitle.get(listPosition))
                                    .get(expandedListPosition).setSelected(false);

                        } else {
                            expandableListDetail.get(expandableListTitle.get(listPosition)).get(expandedListPosition).setSelected(true);
                        }
                    } else {
                        expandableListDetail.get(expandableListTitle.get(listPosition)).get(i).setSelected(false);
                    }
                }
                notifyDataSetChanged();

                mListener.onListItemClick(listPosition, groupId, groupName, childId, ChildName,expandableListDetail.get(expandableListTitle.get(listPosition)).get(expandedListPosition).isSelected());
            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
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
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.condition_popup_header_item, null);
        }
        ((ImageView) convertView.findViewById(R.id.iv_expand_status)).setImageResource(isExpanded ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
        /*((ImageView) convertView.findViewById(R.id.iv_expand_status))
                .setBackgroundResource(isExpanded?R.drawable.grident_circle_white_border:R.drawable.grey_circle_bg);
*/

        LinearLayout ll_title=(LinearLayout)convertView.findViewById(R.id.ll_title);
        if(isExpanded){
            ll_title.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
        }else{
            ll_title.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        TextView txt_condition_title = (TextView) convertView
                .findViewById(R.id.txt_condition_title);

        txt_condition_title.setTypeface(objUsefullData.get_montserrat_semibold());
        txt_condition_title.setText(objUsefullData.capitalize(listTitle.trim()));
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
