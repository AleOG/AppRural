package com.proyecto.apprural.views.admin;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.proyecto.apprural.R;

public class AdminViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_activity);

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


        Button button = findViewById(R.id.logout_btn);

        button.setOnClickListener(event -> {
            FirebaseAuth.getInstance().signOut();
            logoutAndFinish();
        });
    }

    private void logoutAndFinish() {
        FirebaseAuth.getInstance().signOut();
        finish(); // Close the activity
    }
}