package com.sydehealth.sydehealth.utility;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sydehealth.sydehealth.R;

public class EmailToast {

    public static void show(Context context, String title, boolean isLong) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.email_toast, null);

        UsefullData usefullData=new UsefullData(context);

        TextView firebase_title = (TextView) layout.findViewById(R.id.normal_title);
        firebase_title.setText(title);
        firebase_title.setTypeface(usefullData.get_montserrat_regular());

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP|Gravity.LEFT, 20, 50);
        toast.setDuration((isLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
}
