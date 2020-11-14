package com.ambiwsstudio.wikisurfing;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return true;

    }

    @Override
    public void onLoadResource(WebView view, String url) {

        Log.i("CustomWebViewClient", "Url: " + url);

    }
}
