package com.proyecto.apprural.views.registration;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            client = createClient(client);

        }
        binding.setClient(client);
        setup();
    }


    /**
     * Función que configura los elementos de la actividad
     */
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

        binding.registerButton.setOnClickListener(event -> {
            if(checkFields()) {
                registerUser(client);
            }
        });
    }

    /**
     * Función que comprueba que todos los campos del formulario de creación de la actividad han sido completados
     *
     * @return
     */
    private boolean checkFields() {
        String name = binding.name.getText().toString();
        String firstLastName = binding.firstLastName.getText().toString();
        String secondLastName = binding.secondLastName.getText().toString();
        String dni = binding.dni.getText().toString();
        String phoneNumber = binding.phoneNumber.getText().toString();
        String dateOfBirth = binding.dateOfBirth.getText().toString();
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        String creditCardNumber = binding.creditCardNumber.getText().toString();
        String bankAccountNumber = binding.bankAccountNumber.getText().toString();

        if (!name.isEmpty() || !firstLastName.isEmpty() || !secondLastName.isEmpty()
                || !dni.isEmpty() || !phoneNumber.isEmpty() || !dateOfBirth.isEmpty()
                || !email.isEmpty() || !password.isEmpty() || !creditCardNumber.isEmpty()
                || !bankAccountNumber.isEmpty()) {
            if (email.contains("@gmail.com")) {
                return true;
            } else {
                utils.showAlert(this, "Aviso", "El email debe contener @gmail.com");
                return false;
            }
        } else {
            utils.showAlert(this, "Aviso", "Debes completar todos los campos.");
            return false;
        }
    }


    // Método para registrar un nuevo usuario y guardar su rol en Firestore

    /**
     * Función que guarda un usuario en base de datos y además almacena sus credenciales email y password en el servicio de identificación de firebase
     *
     * @param theClient
     */
    private void registerUser(Client theClient) {
        if(theClient.getEmail() != null && theClient.getPassword() != null) {
            Log.e("cliente", client.toString());
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(client.getEmail(), client.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                String userId = utils.generateID(userEmail);
                                client.setUserId(userId);
                                String fecha = binding.dateOfBirth.getText().toString().trim();
                                if(!fecha.isEmpty()) {
                                    LocalDateTime dateTime = utils.formatStringDateToLocalDateTime(fecha);
                                    client.setDateOfBirth(dateTime.toString());
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
            utils.showAlert(RegistrationActivity.this, "Error", "Email o contraseña erroneos");
        }
    }

    /**
     * Función que asigna los valores por defecto de un usuario cliente (propietario o huésped)
     *
     * @param client
     * @return
     */
    private Client createClient(Client client) {
        client.setRole("client");
        client.setNumberOfProperties(0);
        client.setFidelityPoints(0);
        client.setMember(true);
        client.setMemberCategory("BLUE");
        return client;
    }


}