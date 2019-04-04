package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sydehealth.sydehealth.model.TreatmentImages;
import com.sydehealth.sydehealth.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class TreatmentScreenAdapter extends RecyclerView.Adapter<TreatmentScreenAdapter.ViewHolder> {

    ArrayList<TreatmentImages> al_images;
    private LayoutInflater mInflater;
    private Context mContext;
    ImageLoader imageLoader;
    TreatmentImageSelectListener mListener;
    String key;

    public TreatmentScreenAdapter(ArrayList<TreatmentImages> al_images, Context mContext, ImageLoader imageLoader,
                                  TreatmentImageSelectListener mListener, String key) {
        this.al_images = al_images;
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.imageLoader = imageLoader;
        this.mListener = mListener;
        this.key = key;
    }

    @Override
    public TreatmentScreenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_treatment_screen, parent, false);
        return new TreatmentScreenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /*imageLoader.displayImage("file://" + al_images.get(position).getPath(),
                holder.iv_image, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        *//*holder.iv_image
                                .setImageResource(R.drawable.email_image);*//*
                        super.onLoadingStarted(imageUri, view);
                    }
                });*/

        Glide.with(mContext)
                .load(al_images.get(position).getPath())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loader_background).into(holder.iv_image);


        if (key.equalsIgnoreCase("add")) {
            if (!al_images.get(position).isSelected()) {
                holder.iv_select.setBackgroundResource(R.drawable.circle_white_outline);
                holder.iv_select.setImageResource(android.R.color.transparent);
            } else {
                holder.iv_select.setBackgroundResource(R.drawable.grident_circle_white_border);
                holder.iv_select.setImageResource(R.drawable.ic_check_white);
            }
        } else {
            holder.iv_select.setVisibility(View.GONE);
        }

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equalsIgnoreCase("add")) {
                    if (al_images.get(position).isSelected()) {
                        al_images.get(position).setSelected(false);
                    } else {
                        al_images.get(position).setSelected(true);
                    }
                    notifyDataSetChanged();

                    mListener.onTreatmentSelect(al_images.get(position).getPath(), al_images.get(position).isSelected());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return al_images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image, iv_select;

        ViewHolder(View itemView) {
            super(itemView);
            iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
