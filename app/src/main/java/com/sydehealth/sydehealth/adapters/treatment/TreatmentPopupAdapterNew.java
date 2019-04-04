package com.sydehealth.sydehealth.adapters.treatment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.adapters.ExpandableListClickListener;
import com.sydehealth.sydehealth.model.TreatmentDataset;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.ArrayList;

public class TreatmentPopupAdapterNew extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<TreatmentDataset> expandableListDataset;
    private ExpandableListClickListener mListener;
    UsefullData objUsefullData;

    public TreatmentPopupAdapterNew(Context context, ArrayList<TreatmentDataset> expandableListTitle,
                                    ExpandableListClickListener mListener) {
        this.context = context;
        this.expandableListDataset = expandableListTitle;
        this.mListener = mListener;
        objUsefullData = new UsefullData(context);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDataset.get(listPosition).getChildDataSet().get(expandedListPosition).getName();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String strChildName = (String) getChild(listPosition, expandedListPosition);

//        String groupId = expandableListDetail.get(this.expandableListDataset.get(listPosition).getHeadingName())
//                .get(expandedListPosition).getGroupId();
//
//        String childId = expandableListDetail.get(this.expandableListDataset.get(listPosition).getHeadingName())
//                .get(expandedListPosition).getChildId();


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.treatment_popup_child_item, null);
        }


        LinearLayout ll_subtitle = (LinearLayout) convertView.findViewById(R.id.ll_subtitle);
        TextView txt_condition_subtitle = (TextView) convertView.findViewById(R.id.txt_condition_subtitle);

        txt_condition_subtitle.setTypeface(objUsefullData.get_montserrat_regular());

        txt_condition_subtitle.setText(objUsefullData.capitalize(strChildName.trim()));

        ImageView iv_select = (ImageView) convertView.findViewById(R.id.iv_select);

        if (expandableListDataset.get(listPosition).getChildDataSet()
                .get(expandedListPosition).isSelected()) {
            iv_select.setBackgroundResource(R.drawable.grident_circle_white_border);
            iv_select.setImageResource(R.drawable.ic_check_white);
            ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
        } else {
            iv_select.setBackgroundResource(R.drawable.circle_grey_outline);
            ll_subtitle.setBackgroundColor(context.getResources().getColor(R.color.white));
        }


        //.setBackgroundResource(isExpanded?R.drawable.grident_circle_white_border:R.drawable.grey_circle_bg);

//        ll_subtitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("data--", expandableListDataset.get(listPosition) + "(" + strChildName + ")");
////                for (int i = 0; i < expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName()).size(); i++) {
////                    if (i == expandedListPosition) {
////                        if (expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName())
////                                .get(expandedListPosition).isSelected()) {
////                            expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName())
////                                    .get(expandedListPosition).setSelected(false);
////
////                        } else {
////                            expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName()).get(expandedListPosition).setSelected(true);
////                        }
////                    } else {
////                        expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName()).get(i).setSelected(false);
////                    }
////                }
//
//                boolean isSelected = expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName())
//                        .get(expandedListPosition).isSelected();
//
//                expandableListDetail.get(expandableListDataset.get(listPosition).getHeadingName())
//                        .get(expandedListPosition).setSelected(!isSelected);
//
//
//                notifyDataSetChanged();
//
//                //mListener.onListItemClick(listPosition, groupId, childId,expandableListDetail.get(expandableListDataset.get(listPosition)).get(expandedListPosition).isSelected());
//            }
//        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDataset.get(listPosition).getChildDataSet().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListDataset.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListDataset.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        TreatmentDataset headingObj = (TreatmentDataset) getGroup(listPosition);
        String listTitle = headingObj.getHeadingName();
        boolean isSelected = headingObj.isSelected();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.treatment_popup_header_item, null);
        }

        LinearLayout ll_title = (LinearLayout) convertView.findViewById(R.id.ll_title);
        ImageView iv_selected = convertView.findViewById(R.id.iv_expand_status);


        //((ImageView) convertView.findViewById(R.id.iv_expand_status)).setImageResource(isExpanded ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
        /*((ImageView) convertView.findViewById(R.id.iv_expand_status))
                .setBackgroundResource(isExpanded?R.drawable.grident_circle_white_border:R.drawable.grey_circle_bg);
*/

        if (isSelected) {
            ll_title.setBackgroundColor(context.getResources().getColor(R.color.list_bg_color));
            iv_selected.setBackgroundResource(R.drawable.grident_circle_white_border);
            iv_selected.setImageResource(R.drawable.ic_check_white);

            //onGroupExpanded(listPosition);
        } else {
            ll_title.setBackgroundColor(context.getResources().getColor(R.color.white));
            iv_selected.setBackgroundResource(R.drawable.circle_grey_outline);

            //onGroupCollapsed(listPosition);
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
