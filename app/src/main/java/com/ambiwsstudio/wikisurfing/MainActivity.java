package com.ambiwsstudio.wikisurfing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.ambiwsstudio.wikisurfing.databinding.ActivityMainBinding;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final String wikiMainPageUrl = "https://www.wikipedia.org/";
    CustomWebViewClient client;
    WikiViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setupActivity();

        viewModel.wikiLink.observe(this, this::setupTheGame);
        viewModel.timerUpd.observe(this, this::updateTheTimer);
        viewModel.isReadyForRestart.observe(this, this::resetUi);
        viewModel.isNeedPreviousPage.observe(this, this::loadPrev);
        viewModel.isGenerating.observe(this, this::setupGeneration);
        client.getIsPageLoaded().observe(this, this::updateProgress);

    }

    private void loadPrev(Boolean bool) {

        String link = client.returnPrevPage();

        if (link != null)
            binding.webView.loadUrl(link);

    }

    private void updateProgress(Integer integer) {

        binding.progressBar2.setProgress(integer);

    }

    @SuppressLint("SetTextI18n")
    private void resetUi(Boolean bool) {

        binding.webView.disableTouch();
        binding.webView.loadUrl(wikiMainPageUrl);
        binding.textViewGoal.setText("You lose. Better luck next time! :)");
        binding.button2.setEnabled(true);
        client.setGameInit(false, null);

    }

    private void updateTheTimer(String data) {

        binding.textViewTimer.setText(data);

    }

    @SuppressLint("SetTextI18n")
    private void setupGeneration(Boolean bool) {

        binding.webView.enableTouch();
        binding.button2.setEnabled(false);
        binding.textViewGoal.setText("Goal generation...");

    }

    @SuppressLint("SetTextI18n")
    private void setupTheGame(Stack<String> stack) {

        String init = "https://en.m.wikipedia.org/wiki/" + stack.get(0);
        binding.webView.loadUrl(init);
        binding.webView.enableTouch();
        binding.button2.setEnabled(false);
        binding.textViewGoal.setText("F: " + stack.get(0).replaceAll("_", " ")
                + "\n" +
                "L: " + stack.peek().replaceAll("_", " "));
        client.setGameInit(true, init);

    }

    private void setupActivity() {

        viewModel = new ViewModelProvider(this).get(WikiViewModel.class);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.webView.loadUrl(wikiMainPageUrl);
        client = new CustomWebViewClient();
        binding.webView.setWebViewClient(client);
        binding.webView.disableTouch();

    }

}