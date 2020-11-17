package com.ambiwsstudio.wikisurfing;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Stack;

import androidx.lifecycle.MutableLiveData;

public class CustomWebViewClient extends WebViewClient {

    private final Stack<String> wikiLinks = new Stack<>();
    private final MutableLiveData<Integer> isPageLoaded = new MutableLiveData<>();
    private final MutableLiveData<String> isWin = new MutableLiveData<>();
    private String initLink = null;
    private String winLink = null;

    public void setLinks(String wikiLink, String wikiLastLink) {

        wikiLinks.clear();
        initLink = wikiLink;
        wikiLinks.push(initLink);
        winLink = wikiLastLink;

    }

    public MutableLiveData<String> getIsWin() {

        return isWin;

    }

    public MutableLiveData<Integer> getIsPageLoaded() {

        return isPageLoaded;

    }

    public String returnPrevPage() {

        if (wikiLinks.size() > 1) {

            isPageLoaded.postValue(wikiLinks.size() - 2);
            wikiLinks.pop();
            return wikiLinks.peek();

        }

        return null;

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        if (request.getUrl().toString().contains("wikipedia.org/wiki/" + winLink)) {

            isWin.postValue("https://en.m.wikipedia.org/wiki/" + winLink);

        } else if (request.getUrl().toString().contains("wikipedia.org/wiki/")
                && wikiLinks.size() < (AsyncWikiTaskGenerator.linksToFinish + 1)) {

            if (!initLink.equals(request.getUrl().toString().substring(request.getUrl().toString().lastIndexOf('/')))) {

                wikiLinks.add(request.getUrl().toString());
                isPageLoaded.postValue(wikiLinks.size() - 1);

            }

            return false;

        }

        return true;

    }

}
