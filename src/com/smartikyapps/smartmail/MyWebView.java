package com.smartikyapps.smartmail;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

public class MyWebView extends WebView {
    public static final String TAG = "MyWebView";

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Warning! This will cause the WebView to continuously be redrawn
        // and will drain the devices battery while the view is displayed!
        invalidate();
    }

}
