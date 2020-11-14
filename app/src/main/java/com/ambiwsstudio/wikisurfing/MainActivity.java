package com.ambiwsstudio.wikisurfing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ambiwsstudio.wikisurfing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    WikiViewModel viewModel;
    ActivityMainBinding binding;

    @SuppressLint("ClickableViewAccessibility")
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

        /*binding.webView.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                Toast.makeText(getApplicationContext(), "Click 'Surf!' to start.", Toast.LENGTH_SHORT).show();
                return true;

            }

            return false;

        });*/

    }

}