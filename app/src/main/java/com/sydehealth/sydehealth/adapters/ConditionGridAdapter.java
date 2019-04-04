package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class ConditionGridAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext = null;
    private List<RecycleBean> mList = null;
    UsefullData usefullData;

    public ConditionGridAdapter(Context context) {
        this.mContext = context;
    }

    public ConditionGridAdapter(Context context, List<RecycleBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void replace(List<RecycleBean> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_condition, null);
        view.setOnClickListener(this);
        RecyclerView.ViewHolder holder = new TextHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(null != mList) {

            RecycleBean bean = mList.get(position);
            if(null != bean) {
                holder.itemView.setTag(bean);
                TextHolder textHolder = (TextHolder) holder;
                Glide.with(mContext)
                        .load(bean.getUrl())
                        .asBitmap()
                        .placeholder(R.mipmap.condition_thumb)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(textHolder.imageview);

                textHolder.condition_count.setText(bean.getCount());
                textHolder.treatment_count.setText(bean.getTag());
                textHolder.condition_count.setTypeface(usefullData.get_montserrat_regular());
                textHolder.treatment_count.setTypeface(usefullData.get_montserrat_regular());
                textHolder.condition_icon.setTypeface(usefullData.get_awosome_font_400());
                textHolder.count_icon.setTypeface(usefullData.get_awosome_font_400());
            }
        }


    }

    @Override
    public int getItemCount() {
        if(null == mList) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if(null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(v, (RecycleBean) v.getTag());
        }
    }

    public class TextHolder extends RecyclerView.ViewHolder {
        private ImageView imageview = null;
        private TextView condition_count = null;
        private TextView treatment_count = null;
        private TextView condition_icon;
        private TextView count_icon;
        public TextHolder(View itemView) {
            super(itemView);
            usefullData = new UsefullData(mContext);
            imageview = (ImageView) itemView.findViewById(R.id.iv_condition_img);
            condition_count = (TextView) itemView.findViewById(R.id.condition_count);
            treatment_count = (TextView) itemView.findViewById(R.id.treatment_count);
            condition_icon = (TextView) itemView.findViewById(R.id.condition_icon);
            count_icon = (TextView) itemView.findViewById(R.id.count_icon);

        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
