package com.sydehealth.sydehealth.adapters;

import android.view.View;

/**
 * Created by docking on 16/4/26 10:11.
 */
public interface OnItemClickListener {

    public void onItemClickMe(View view, int position);

    public void onItemClick(View view, RecycleBean bean );

    public void onItemClickComplete(View view, RecycleBean bean, int position );

    public void onItemLongClick(View view, int position);
}
