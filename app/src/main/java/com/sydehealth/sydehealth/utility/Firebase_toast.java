package com.sydehealth.sydehealth.utility;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sydehealth.sydehealth.R;

public class Firebase_toast {

    public static void show(Context context, String title,String desc, boolean isLong, boolean show_bottom_rrow) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        UsefullData usefullData=new UsefullData(context);
        TextView firebase_title = (TextView) layout.findViewById(R.id.firebase_title);
        firebase_title.setText(title);
        firebase_title.setTypeface(usefullData.get_montserrat_regular());
        TextView firebase_desc = (TextView) layout.findViewById(R.id.firebase_desc);
        firebase_desc.setText(desc);
        firebase_desc.setTypeface(usefullData.get_montserrat_regular());

        RelativeLayout bottom_arrow=(RelativeLayout) layout.findViewById(R.id.triangle_view);

            if (show_bottom_rrow) {
                bottom_arrow.setVisibility(View.VISIBLE);
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER, 350, 350);
                toast.setDuration((isLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

            } else {
                bottom_arrow.setVisibility(View.GONE);
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.TOP, 600, 0);
                toast.setDuration((isLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }

    }
}
