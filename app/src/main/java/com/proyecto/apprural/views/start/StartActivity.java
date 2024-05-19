package com.proyecto.apprural.views.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;

import com.proyecto.apprural.databinding.StartActivityBinding;
import com.proyecto.apprural.views.home.HomeActivityRouter;

public class StartActivity extends AppCompatActivity {

    // Properties
    private StartActivityBinding binding;

    // Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StartActivityBinding.inflate(getLayoutInflater());

        // Content
        setContentView(binding.getRoot());

        // Setup
        Handler handler = new Handler();

        // Ejecuta la función después de un retraso de 2 segundos (2000 milisegundos)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setup();
                data();
            }
        }, 2000);

    }

    // Private
    private void setup() {
        //getSupportActionBar().hide();

        // Remote notifications
        //Firebase.messaging.setAutoInitEnabled(true);
    }

    private void data() {
        showHome();
    }

    private void showHome() {
        new HomeActivityRouter().launch(this);
        finish();
    }
}
