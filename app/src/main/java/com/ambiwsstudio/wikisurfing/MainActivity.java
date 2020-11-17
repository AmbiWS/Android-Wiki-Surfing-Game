package com.ambiwsstudio.wikisurfing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.ambiwsstudio.wikisurfing.databinding.ActivityMainBinding;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        client.getIsWin().observe(this, this::setupWin);

    }

    @SuppressLint("SetTextI18n")
    private void setupWin(String link) {

        viewModel.stopTimer();
        binding.webView.loadUrl(link);
        binding.webView.disableTouch();
        binding.textViewGoal.setText("You Win! Well Played:)");
        binding.progressBar2.setProgress(binding.progressBar2.getProgress() + 1);
        binding.textViewTimer.setText(R.string.tap_surf_button_to_start);
        binding.button2.setEnabled(true);

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
        binding.progressBar2.setProgress(0);
        binding.button2.setEnabled(true);

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

        binding.progressBar2.setProgress(0);
        String init = "https://en.m.wikipedia.org/wiki/" + stack.get(0);
        binding.webView.loadUrl(init);
        binding.webView.enableTouch();
        binding.button2.setEnabled(false);

        try {

            binding.textViewGoal.setText(URLDecoder.decode("F: " + stack.get(0).replaceAll("_", " ")
                    + "\n" +
                    "L: " + stack.peek().replaceAll("_", " "), "UTF-8"));

        } catch (Exception e) {

            e.printStackTrace();

        }

        client.setLinks(init, stack.peek());

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

        binding.progressBar2.setMax(AsyncWikiTaskGenerator.linksToFinish);

    }

}