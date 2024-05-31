package com.proyecto.apprural.views.registration;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.RegistrationActivityBinding;
import com.proyecto.apprural.model.beans.Client;
import com.proyecto.apprural.utils.Util;

import java.time.LocalDateTime;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private RegistrationActivityBinding binding;
    private Client client;

    private Util utils = new Util();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        binding = DataBindingUtil.setContentView(this, R.layout.registration_activity);
        client = new Client();

        // Obtén el intent que inició esta actividad
        Intent intent = getIntent();
        // Recupera el Bundle de extras del intent
        Bundle bundle = intent.getExtras();

        // Verifica que el bundle no sea null
        if (bundle != null) {
            client = createClient(client);

        } else {
        }
        binding.setClient(client);
        setup();
    }


    private void setup() {

        TextView selectedDate = binding.dateOfBirth;

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        binding.birthdayBtn.setOnClickListener(event -> {
            Calendar calendar = Calendar.getInstance();
            long today = calendar.getTimeInMillis();
            utils.showDatePickerDialog(selectedDate, this, 0, today);
        });

        //registro del usuario
        binding.registerButton.setOnClickListener(event -> {
            registerUser(client);
        });
    }



    // Método para registrar un nuevo usuario y guardar su rol en Firestore
    private void registerUser(Client theClient) {
        if(theClient.getEmail() != null && theClient.getPassword() != null) {
            Log.e("cliente", client.toString());
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(client.getEmail(), client.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                // Obtener el email del usuario registrado
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                //generar el id en base al email
                                String userId = utils.generateID(userEmail);
                                client.setUserId(userId);
                                String fecha = binding.dateOfBirth.getText().toString().trim();
                                if(!fecha.isEmpty()) {
                                    LocalDateTime dateTime = utils.formatStringDateToLocalDateTime(fecha);
                                    client.setDateOfBirth(dateTime);
                                }

                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference usuarioRef = database.child("users").child(userId);
                                usuarioRef
                                        .setValue(client)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                utils.showAlert(RegistrationActivity.this, "Exito", "Registro guardado y completado.");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                utils.showAlert(RegistrationActivity.this, "Error", "Fallo al guardar usuario.");
                                            }
                                        });
                            }else {
                                utils.showAlert(RegistrationActivity.this, "Error", "Registro fallido.");
                            }
                        }
                    });
        } else {
            utils.showAlert(RegistrationActivity.this, "Error", "Email o contraseña no válidos");
        }
    }

    private Client createClient(Client client) {
        client.setRole("client");
        client.setNumberOfProperties(0);
        client.setFidelityPoints(0);
        client.setMember(true);
        client.setMemberCategory("BLUE");
        return client;
    }


}