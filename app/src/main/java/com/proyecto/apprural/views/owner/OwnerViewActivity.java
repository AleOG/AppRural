package com.proyecto.apprural.views.owner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.OwnerViewActivityBinding;
import com.proyecto.apprural.views.owner.alta.OwnerAltaRouter;
import com.proyecto.apprural.views.owner.publish.OwnerPublishRouter;
import com.proyecto.apprural.views.owner.publish.OwnerViewPublishActivity;

public class OwnerViewActivity extends AppCompatActivity {

    private OwnerViewActivityBinding binding;
    private SharedPreferences.Editor miEditor;
    private SharedPreferences misDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OwnerViewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtén el intent que inició esta actividad
        Intent intent = getIntent();
        // Recupera el Bundle de extras del intent
        Bundle bundle = intent.getExtras();
        String email = null;
        // Verifica que el bundle no sea null
        if (bundle != null) {
            email = bundle.getString("email");
        }

        //guardado de datos
        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        miEditor = misDatos.edit();
        miEditor.putString("email", email);
        miEditor.apply();

        setup();

        // Manejar el botón de retroceso
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logoutAndFinish();
            }
        });
    }

    private void setup() {

        binding.altaBtn.setOnClickListener(event -> {
            new OwnerAltaRouter().launch(this);
        });

        binding.ofertaBtn.setOnClickListener(event -> {
            new OwnerPublishRouter().launch(this);
        });

        //binding.editaBtn.setOnClickListener();

        binding.logoutBtn.setOnClickListener(event -> {
            FirebaseAuth.getInstance().signOut();
            logoutAndFinish();
        });
    }

    private void logoutAndFinish() {
        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        miEditor = misDatos.edit();
        miEditor.clear();
        miEditor.apply();
        FirebaseAuth.getInstance().signOut();
        finish(); // Close the activity
    }

}
