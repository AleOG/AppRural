package com.proyecto.apprural.views.owner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.apprural.databinding.OwnerViewActivityBinding;
import com.proyecto.apprural.views.owner.alta.OwnerAltaRouter;

public class OwnerViewActivity extends AppCompatActivity {

    private OwnerViewActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OwnerViewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtén el intent que inició esta actividad
        Intent intent = getIntent();
        // Recupera el Bundle de extras del intent
        Bundle bundle = intent.getExtras();

        // Verifica que el bundle no sea null
        if (bundle != null) {
            String email = bundle.getString("email");
            String proveedor = bundle.getString("proveedor");
            setup(email, proveedor);
        } else {
            setup(null, null);
        }


        // Manejar el botón de retroceso
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logoutAndFinish();
            }
        });
    }

    private void setup(String email, String provider) {

        binding.altaBtn.setOnClickListener(event -> {
            new OwnerAltaRouter().launch(this);
        });

        //binding.ofertaBtn.setOnClickListener();

        //binding.editaBtn.setOnClickListener();

        binding.logoutBtn.setOnClickListener(event -> {
            FirebaseAuth.getInstance().signOut();
            logoutAndFinish();
        });
    }

    private void logoutAndFinish() {
        FirebaseAuth.getInstance().signOut();
        finish(); // Close the activity
    }

}
