package com.japho.campus.center.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



public class MyTextView_GreatVibes_Regular extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView_GreatVibes_Regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView_GreatVibes_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView_GreatVibes_Regular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/GreatVibes-Regular.ttf");
            setTypeface(tf);
        }
    }

}