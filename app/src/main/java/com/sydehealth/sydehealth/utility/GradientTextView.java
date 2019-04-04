package com.sydehealth.sydehealth.utility;

/**
 * Created by Lenovo on 07-02-2018.
 */

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.util.AttributeSet;
import android.widget.TextView;

import com.sydehealth.sydehealth.R;

import androidx.core.content.ContextCompat;

/**
 * Created by rahulchowdhury on 04/08/16.
 */

public class GradientTextView extends TextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public float getLetterSpacing() {
        return super.getLetterSpacing();
    }

    @Override
    public void setLetterSpacing(float letterSpacing) {
        super.setLetterSpacing(letterSpacing);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //Setting the gradient if layout is changed
        if (changed) {
            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    ContextCompat.getColor(getContext(), R.color.yellow),
                    ContextCompat.getColor(getContext(), R.color.pink),
                    Shader.TileMode.CLAMP));
        }
    }
}