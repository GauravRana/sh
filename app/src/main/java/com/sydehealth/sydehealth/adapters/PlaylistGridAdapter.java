package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class PlaylistGridAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext = null;
    private List<RecycleBean> mList = null;
    UsefullData usefullData;
    RecycleBean bean =  null;

    public PlaylistGridAdapter(Context context) {
        this.mContext = context;
    }

    public PlaylistGridAdapter(Context context, List<RecycleBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void replace(List<RecycleBean> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_playlist, null);
        view.setOnClickListener(this);
        RecyclerView.ViewHolder holder = new TextHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextHolder textHolder = (TextHolder) holder;
        if(position < mList.size()) {
            if (null != mList) {
                 bean = mList.get(position);
                if (null != bean) {
                    holder.itemView.setTag(bean);
                    Glide.with(mContext)
                            .load(bean.getUrl())
                            .asBitmap()
                            .placeholder(R.mipmap.condition_thumb)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(textHolder.imageview);
                }

            }
        }else{
//            Glide.with(mContext)
//                    .load(R.drawable.radio_pic)
//                    .asBitmap()
//                    .placeholder(R.mipmap.condition_thumb)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(textHolder.imageview);
        }


    }

    @Override
    public int getItemCount() {
        if(null == mList) {
            return 0;
        }
//        return mList.size() + 1;
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if(null != mOnItemClickListener) {
            //mOnItemClickListener.onItemClick(v, (RecycleBean) v.getTag());
        }
    }

    public class TextHolder extends RecyclerView.ViewHolder {
        private ImageView imageview = null;
        public TextHolder(View itemView) {
            super(itemView);
            usefullData = new UsefullData(mContext);
            imageview = (ImageView) itemView.findViewById(R.id.iv_playlist_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClickComplete(v, (RecycleBean) v.getTag(), getAdapterPosition());
                }
            });

        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }


}
