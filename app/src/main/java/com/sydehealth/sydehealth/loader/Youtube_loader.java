package com.sydehealth.sydehealth.loader;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sydehealth.sydehealth.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * Created by Administrator on 2015/5/6.
 */
public class Youtube_loader extends Dialog implements View.OnClickListener {

    private Context mContext;

    public Youtube_loader(Context context) {
        super(context, R.style.colorAccentTheme);
        mContext = context;
        initView();
    }

    public Youtube_loader(Context context, int theme) {
        super(context, R.style.colorAccentTheme);
        mContext = context;
        initView();
    }

    protected Youtube_loader(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        initView();
    }

    private void initView() {
        // 全屏显示 ： root layout下的子view width设置为match_parent
        setContentView(R.layout.youtube_loading);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(mContext.getResources().getColor(R.color.black));
        }
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initData();
    }

    private void initData() {
//        ImageView findViewById(R.id.loader_image).setOnClickListener(this);

//        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.loaderss);
//        avi.show();
//        Animation startRotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
//        my_image.startAnimation(startRotateAnimation);
//        findViewById(R.id.tv_call).setOnClickListener(this);
//        findViewById(R.id.tv_add_contact).setOnClickListener(this);
//        findViewById(R.id.tv_copy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void show() {
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.CustomDialog);  //添加动画
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

