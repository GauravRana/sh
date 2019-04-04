package com.sydehealth.sydehealth.adapters.treatment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.model.EmailTreatmentResponse;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class SubTreatmentScreenAdapter extends RecyclerView.Adapter<SubTreatmentScreenAdapter.ViewHolder> {

    private ArrayList<EmailTreatmentResponse.SubTreatmentOption_> mDataset;
    private LayoutInflater mInflater;
    private Context mContext;
    private UsefullData objUsefullData;

    public SubTreatmentScreenAdapter(Context mContext, ArrayList<EmailTreatmentResponse.SubTreatmentOption_> mDataset) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mDataset = mDataset;
        objUsefullData = new UsefullData(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sub_treatment_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!mDataset.get(position).getSubTreatmentOptionImage().isEmpty()) {
            Glide.with(mContext)
                    .load(mDataset.get(position).getSubTreatmentOptionImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loader_background).into(holder.iv_image);
        }

        holder.txt_sub_treatment.setText(mDataset.get(position).getName());
        holder.txt_sub_treatment.setTypeface(objUsefullData.get_montserrat_bold());
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView txt_sub_treatment;

        ViewHolder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            txt_sub_treatment = itemView.findViewById(R.id.txt_sub_treatment);
        }
    }
}
