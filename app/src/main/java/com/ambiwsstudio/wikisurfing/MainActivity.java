package com.ambiwsstudio.wikisurfing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.ambiwsstudio.wikisurfing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String wikiMainPageUrl = "https://www.wikipedia.org/";
    WikiViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setupActivity();

        viewModel.wikiLink.observe(this, this::setupTheGame);
        viewModel.timerUpd.observe(this, this::updateTheTimer);
        viewModel.isReadyForRestart.observe(this, this::resetUi);

    }

    @SuppressLint("SetTextI18n")
    private void resetUi(Boolean bool) {

        binding.webView.disableTouch();
        binding.webView.loadUrl(wikiMainPageUrl);
        binding.textViewGoal.setText("You lose. Better luck next time! :)");
        binding.button2.setEnabled(true);

    }

    private void updateTheTimer(String data) {

        binding.textViewTimer.setText(data);

    }

    @SuppressLint("SetTextI18n")
    private void setupTheGame(String link) {

        binding.webView.loadUrl(link);
        binding.webView.enableTouch();
        binding.button2.setEnabled(false);
        binding.textViewGoal.setText("Goal generation...");

    }

    private void setupActivity() {

        viewModel = new ViewModelProvider(this).get(WikiViewModel.class);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.webView.loadUrl(wikiMainPageUrl);
        binding.webView.setWebViewClient(new CustomWebViewClient());
        binding.webView.disableTouch();

    }

}