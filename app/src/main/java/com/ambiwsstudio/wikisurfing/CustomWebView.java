package com.ambiwsstudio.wikisurfing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.Toast;

public class CustomWebView extends WebView {

    private final Context context;

    public CustomWebView(Context context) {

        super(context);
        this.context = context;

    }

    public CustomWebView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;

    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    @Override
    public boolean performClick() {

        super.performClick();
        return true;

    }

    public void disableTouch() {

        this.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Toast.makeText(context, "Please, tap 'Surf!' button first.", Toast.LENGTH_SHORT).show();
                performClick();
                return true;

            }

            return false;

        });

    }

    public void enableTouch() {

        this.setOnTouchListener((v, event) -> {

            performClick();
            return false;

        });

    }

}
