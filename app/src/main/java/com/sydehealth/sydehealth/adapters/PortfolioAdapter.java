package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.model.portfolio.PortfolioImage;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class PortfolioAdapter extends RecyclerView.Adapter {

    private RecyclerviewOnItemClickListener mOnItemClickListener = null;
    private Context mContext = null;
    private ArrayList<PortfolioImage> mDataSet = null;
    UsefullData usefullData;
    private RecyclerView recyclerView;
    private int selectedImgPosition = 0;

    public PortfolioAdapter(Context context, ArrayList<PortfolioImage> mDataSet) {
        selectedImgPosition = 0;
        this.mContext = context;
        this.mDataSet = mDataSet;
    }

    public void updateDataSet(ArrayList<PortfolioImage> mDataSet) {
        selectedImgPosition = 0;
        this.mDataSet = mDataSet;
        this.notifyDataSetChanged();
    }

    public ArrayList<PortfolioImage> getmDataSet() {
        return mDataSet;
    }

    public int getSelectedImgPosition() {
        return selectedImgPosition;
    }

    public void setSelectedImgPosition(int selectedImgPosition) {
        this.selectedImgPosition = selectedImgPosition;

        if (mOnItemClickListener != null) {
            notifyDataSetChanged();
            mOnItemClickListener.onItemClick(selectedImgPosition);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.portfolio_item, parent, false);

//        int itemWidth = parent.getWidth() / 4;
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.width = itemWidth;
//        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        ViewHolder holder = (ViewHolder) holder1;

        holder.itemView.getLayoutParams().width = recyclerView.getWidth() / 4;

        if (!mDataSet.get(position).getImageUrl().isEmpty()) {

            Glide.with(mContext)
                    .load(mDataSet.get(position).getImageUrl())
                    .override(20, 20)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_portfolio);

            Glide.with(mContext)
                    .load(mDataSet.get(position).getImageUrl())
                    .asBitmap()
                    .error(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_portfolio);
        }

        if (position == selectedImgPosition) {
            holder.iv_selection.setBackgroundResource(R.drawable.transparent_stroke);
        } else {
            holder.iv_selection.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                    selectedImgPosition = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        }
        return mDataSet.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_portfolio;
        private ImageView iv_selection;

        private ViewHolder(View itemView) {
            super(itemView);
            usefullData = new UsefullData(mContext);
            iv_portfolio = itemView.findViewById(R.id.iv_portfolio);
            iv_selection = itemView.findViewById(R.id.iv_selection);
        }
    }

    public void setOnItemClickListener(RecyclerviewOnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
