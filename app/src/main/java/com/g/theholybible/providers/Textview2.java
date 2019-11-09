package com.g.theholybible.providers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class Textview2 extends AppCompatTextView {

    public Textview2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Textview2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Textview2(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Deutsch.ttf");
            setTypeface(tf);
        }
    }

}
