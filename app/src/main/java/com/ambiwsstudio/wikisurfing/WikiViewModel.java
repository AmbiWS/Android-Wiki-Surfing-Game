package com.ambiwsstudio.wikisurfing;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WikiViewModel extends ViewModel {

    MutableLiveData<Stack<String>> wikiLink = new MutableLiveData<>();
    MutableLiveData<String> timerUpd = new MutableLiveData<>();
    MutableLiveData<Boolean> isReadyForRestart = new MutableLiveData<>();
    MutableLiveData<Boolean> isGenerating = new MutableLiveData<>();
    MutableLiveData<Boolean> isNeedPreviousPage = new MutableLiveData<>();
    CountDownTimer timer;
    Stack<String> wikiLinks = new Stack<>();

    public void stopTimer() {

        timer.cancel();
        initTimer();

    }

    public void initTimer() {

        timer = new CountDownTimer(300_000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timerUpd.postValue("Time left: " + time);

            }

            @Override
            public void onFinish() {

                timerUpd.postValue("Tap 'Surf!' button to start");
                isReadyForRestart.postValue(true);

            }

        };

    }

    public void backClick(View view) {

        Log.i("WikiViewModel", "Back Button Click: " + view.toString());
        isNeedPreviousPage.setValue(true);

    }

    public void surfClick(View view) {

        Log.i("WikiViewModel", "Surf Button Click: " + view.toString());

        initTimer();
        isGenerating.setValue(true);

        new Thread() {

            @Override
            public void run() {

                wikiLinks.clear();
                wikiLinks = new AsyncWikiTaskGenerator().generateGoal();
                wikiLink.postValue(wikiLinks);

                new Handler(Looper.getMainLooper()).post(() -> timer.start());

            }

        }.start();

    }

}
