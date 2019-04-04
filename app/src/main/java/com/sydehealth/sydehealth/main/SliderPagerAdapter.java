package com.sydehealth.sydehealth.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sydehealth.sydehealth.adapters.Actors;
import com.sydehealth.sydehealth.utility.UsefullData;
import com.sydehealth.sydehealth.R;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by POPLIFY on 10/20/2017.
 */

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Context activity;
    ArrayList<Actors> image_arraylist;
    UsefullData usefullData;



    public SliderPagerAdapter(Context activity, ArrayList<Actors> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        usefullData=new UsefullData(activity);


    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_cover, container, false);
        final ImageView img_slider = (ImageView) view.findViewById(R.id.img_item);
//        img_slider.setBorderColor(activity.getResources().getColor(R.color.white));
//        img_slider.setBorderWidth(5.0f);


        if(image_arraylist.get(position).getid().equals("is_image")) {
//            Glide.with(activity)
//                    .load(image_arraylist.get(position).getpicture())
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(img_slider);


//            Glide.with(activity)
//                    .load(image_arraylist.get(position).gettitle())
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                            // Do something with bitmap here.
//                            img_slider.setImageBitmap(bitmap);
//
//                            Glide.with(activity)
//                                    .load(image_arraylist.get(position).getpicture())
//                                    .asBitmap()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                    .into(new SimpleTarget<Bitmap>() {
//                                        @Override
//                                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//                                            // Do something with bitmap here.
//                                            img_slider.setImageBitmap(bitmap);
//
//                                        }
//                                    });
//                        }
//                    });

        }else {

//            Glide.with(activity).load(image_arraylist.get(position).getpicture()).into(new GlideDrawableImageViewTarget(img_slider));

        }


        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}
