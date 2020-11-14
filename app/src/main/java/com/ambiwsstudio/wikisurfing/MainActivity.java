package com.ambiwsstudio.wikisurfing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.ambiwsstudio.wikisurfing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    WikiViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WikiViewModel.class);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.webView.loadUrl("https://www.wikipedia.org/");
        CustomWebViewClient client = new CustomWebViewClient();
        binding.webView.setWebViewClient(client);
        binding.webView.disableTouch();

        viewModel.wikiLink.observe(this, this::setupTheGame);

    }

    @SuppressLint("SetTextI18n")
    private void setupTheGame(String link) {

        binding.webView.loadUrl(link);
        binding.webView.enableTouch();

        binding.button2.setEnabled(false);
        binding.textViewGoal.setText("Goal generation...");
        binding.textViewTimer.setText("Time left: 5:00");

    }

}