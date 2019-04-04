package com.sydehealth.sydehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sydehealth.sydehealth.R;
import com.sydehealth.sydehealth.utility.UsefullData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RadioGridAdapter extends RecyclerView.Adapter<RadioGridAdapter.TextHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener = null;
    private Context mContext = null;
    private List<RecycleBean> mList = null;
    UsefullData usefullData;
    RecyclerView.ViewHolder holder;
    RecycleBean bean;


    int[] myImageList = new int[]{R.drawable.absoluteradio1, R.drawable.capital1,
            R.drawable.heart1, R.drawable.kiss1,
            R.drawable.radio11, R.drawable.radio21, R.drawable.radio41, R.drawable.magicradio1};



    int[] myImageList2 = new int[]{R.drawable.absoluteradio_logo, R.drawable.capitalfm_logo,
            R.drawable.heartfm_logo, R.drawable.kissradio_logo,
            R.drawable.radio_1_logo, R.drawable.radio_2_logo, R.drawable.radio_4_logo, R.drawable.magicradio_logo};


    public RadioGridAdapter(Context context) {
        this.mContext = context;
    }



    public RadioGridAdapter(Context context, List<RecycleBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void replace(List<RecycleBean> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RadioGridAdapter.TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_playlist, null);
        view.setOnClickListener(this);
        TextHolder holder = new TextHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TextHolder holder, int position) {
        if(null != mList) {
                bean = mList.get(position);
                if (null != bean) {
                    holder.itemView.setTag(bean);
                    TextHolder textHolder = (TextHolder) holder;
                    textHolder.imageview.setImageResource(myImageList2[position]);
//                    Glide.with(mContext)
//                            .load(bean.getUrl())
//                            .asBitmap()
//                            .placeholder(R.mipmap.condition_thumb)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(textHolder.imageview);
                }

        }

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, bean);
            }
        });
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
            //mOnItemClickListener.onItemClickComplete(v, (RecycleBean) v.getTag(), 0);
        }
    }

    public class TextHolder extends RecyclerView.ViewHolder {
        private ImageView imageview = null;
        public TextHolder(View itemView) {
            super(itemView);
            usefullData = new UsefullData(mContext);
            imageview = (ImageView) itemView.findViewById(R.id.iv_playlist_img);

        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

