package com.ambiwsstudio.wikisurfing;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WikiViewModel extends ViewModel {

    MutableLiveData<String> wikiLink = new MutableLiveData<>();

    public void surfClick(View view) {

        Log.i("WikiViewModel", "Surf Button Click: " + view.toString());
        wikiLink.setValue("https://www.wikipedia.org/");

    }

}
