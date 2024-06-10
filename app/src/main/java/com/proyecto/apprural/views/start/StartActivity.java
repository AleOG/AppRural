package com.proyecto.apprural.views.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;

import com.proyecto.apprural.databinding.StartActivityBinding;
import com.proyecto.apprural.views.home.HomeActivityRouter;

public class StartActivity extends AppCompatActivity {

    private StartActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StartActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                innit();
            }
        }, 2000);

    }


    private void innit() {
        showHome();
    }

    private void showHome() {
        new HomeActivityRouter().launch(this);
        finish();
    }
}
