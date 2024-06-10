package com.proyecto.apprural.views.owner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.OwnerViewActivityBinding;
import com.proyecto.apprural.views.owner.alta.OwnerAltaRouter;
import com.proyecto.apprural.views.owner.publish.OwnerPublishRouter;

public class OwnerViewActivity extends AppCompatActivity {

    private OwnerViewActivityBinding binding;
    private SharedPreferences.Editor miEditor;
    private SharedPreferences misDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OwnerViewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String email = null;
        if (bundle != null) {
            email = bundle.getString("email");
        }

        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        miEditor = misDatos.edit();
        miEditor.putString("email", email);
        miEditor.apply();

        setup();

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logoutAndFinish();
            }
        });
    }

    /**
     * Función que configura los elementos de la actividad
     */
    private void setup() {

        binding.altaBtn.setOnClickListener(event -> {
            new OwnerAltaRouter().launch(this);
        });

        binding.ofertaBtn.setOnClickListener(event -> {
            new OwnerPublishRouter().launch(this);
        });

        binding.logoutBtn.setOnClickListener(event -> {
            FirebaseAuth.getInstance().signOut();
            logoutAndFinish();
        });
    }

    /**
     * Función que elimina los datos de sesión de SharePreferences, finaliza sesión en firebase y finaliza la actividad.
     */
    private void logoutAndFinish() {
        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        miEditor = misDatos.edit();
        miEditor.clear();
        miEditor.apply();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}
