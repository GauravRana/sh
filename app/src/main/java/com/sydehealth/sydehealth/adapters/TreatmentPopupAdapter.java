package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sydehealth.sydehealth.model.Treatments;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TreatmentPopupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Treatments> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    ListItemClickListener mListener;
    UsefullData objUsefullData;

    // data is passed into the constructor
    public TreatmentPopupAdapter(Context context, ArrayList<Treatments> data, ListItemClickListener mListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        this.mListener = mListener;
        objUsefullData = new UsefullData(context);
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // R.layout.condition_popup_child_item =// also used in conditions popup expandable list
        View view = mInflater.inflate(R.layout.treatment_popup_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ViewHolder holder = ((ViewHolder) holder1);

        if (mData.get(position).isSelected()) {
            holder.iv_select.setBackgroundResource(R.drawable.grident_circle_white_border);
            holder.iv_select.setImageResource(R.drawable.ic_check_white);
            holder.ll_subtitle.setBackgroundColor(mContext.getResources().getColor(R.color.list_bg_color));
        } else {
            holder.iv_select.setBackgroundResource(R.drawable.circle_grey_outline);
            holder.ll_subtitle.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        holder.txt_condition_subtitle.setTypeface(objUsefullData.get_montserrat_regular());
        holder.txt_condition_subtitle.setText(objUsefullData.capitalize(mData.get(position).getName().trim()));
        holder.ll_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mData.get(position).isSelected()) {
                    mData.get(position).setSelected(false);
                } else {
                    mData.get(position).setSelected(true);
                }

                notifyDataSetChanged();
                mListener.onItemClick(mData.get(position).getId(), mData.get(position).isSelected());
            }
        });
    }


    // total number of cells
    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        //        LinearLayout main_item;
        TextView txt_condition_subtitle;
        LinearLayout ll_subtitle;
        ImageView iv_select;

        ViewHolder(View itemView) {
            super(itemView);
//            main_item = (LinearLayout) itemView.findViewById(R.id.main_item);
            txt_condition_subtitle = (TextView) itemView.findViewById(R.id.txt_condition_subtitle);
            ll_subtitle = (LinearLayout) itemView.findViewById(R.id.ll_subtitle);
            iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
        }
    }

    // convenience method for getting data at click position
    Object getItem(int id) {
        return mData.get(id);
    }
}