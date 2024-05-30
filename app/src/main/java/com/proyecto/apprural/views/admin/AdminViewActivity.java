package com.proyecto.apprural.views.admin;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.AdminViewActivityBinding;
import com.proyecto.apprural.databinding.DialogViewPropertyBinding;
import com.proyecto.apprural.model.beans.Client;
import com.proyecto.apprural.model.beans.Property;
import com.proyecto.apprural.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class AdminViewActivity extends AppCompatActivity implements PropertyAdapter.OnPropertyActionListener {

    private AdminViewActivityBinding binding;
    private PropertyAdapter propertyAdapter;
    private List<Property> propertyList;
    private DatabaseReference mDatabase;
    private SharedPreferences.Editor miEditor;
    private SharedPreferences misDatos;
    private Util utils = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminViewActivityBinding.inflate(getLayoutInflater());
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

        // Configurar el RecyclerView
        setupRecyclerView();

        // Inicializa la referencia de la base de datos
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Lee los datos desde la base de datos
        mDatabase.child("propertiesValidation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                propertyList.clear(); // Limpia la lista antes de agregar nuevos datos
                for (DataSnapshot ownerSnapshot : dataSnapshot.getChildren()) {
                    // Itera sobre cada propiedad en propertiesOwner
                    DataSnapshot propertiesOwnerSnapshot = ownerSnapshot.child("propertiesOwner");
                    for (DataSnapshot propertySnapshot : propertiesOwnerSnapshot.getChildren()) {
                        Property property = propertySnapshot.getValue(Property.class);
                        if (property != null) {
                            propertyList.add(property); // Agrega la propiedad a la lista
                            Log.d("Property", "Property Name: " + property.getName());
                        }
                    }
                }
                propertyAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja el error
                Log.w("DatabaseError", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Manejar el botón de retroceso
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logoutAndFinish();
            }
        });
    }

    private void setupRecyclerView() {
        propertyList = new ArrayList<>();
        propertyAdapter = new PropertyAdapter(propertyList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(propertyAdapter);
    }

    private void setup() {

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

    @Override
    public void onViewProperty(Property property) {
        DialogViewPropertyBinding dialogBinding = DialogViewPropertyBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());

        // Establecer los datos de la propiedad en el diálogo usando databinding
        dialogBinding.setProperty(property);
        // Obtener y establecer los datos del cliente
        getClient(property.getOwnerId(), client -> {
            if (client != null) {
                dialogBinding.setClient(client);
            }
        });

        // Configurar el botón de cierre
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        // Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getClient(String userId, OnClientRetrievedListener listener) {
        //mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client client = dataSnapshot.getValue(Client.class);
                listener.onClientRetrieved(client);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja los errores
                Log.e("firebase", "Error getting data", databaseError.toException());
                listener.onClientRetrieved(null);
            }
        });
    }

    @Override
    public void onAcceptProperty(Property property) {
        String propertyId = property.getPropertyId();
        String ownerId = property.getOwnerId();

        DatabaseReference properyRef = mDatabase.child("properties").child(ownerId).child("propertiesOwner").child(propertyId);

        properyRef.child("validated").setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DatabaseReference propertyValidation = mDatabase.child("propertiesValidation").child(ownerId).child("propertiesOwner").child(propertyId);

                propertyValidation.removeValue().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        // La eliminación fue exitosa, ahora elimina la propiedad de la lista local
                        propertyList.remove(property);
                        propertyAdapter.notifyDataSetChanged();
                        Log.d("Property", "Property deleted successfully");
                        // Muestra un mensaje al usuario
                        utils.showAlert(this, "Éxito", "La propiedad ha sido validada exitosamente.");
                    } else {
                        // Manejar el error
                        Log.e("Property", "Failed to delete property", task2.getException());
                        // Muestra un mensaje al usuario
                        utils.showAlert(this, "Error", "No se pudo validar la propiedad. Inténtalo de nuevo.");
                    }
                });

            } else {
                // Manejar el error
                Log.e("Property", "Failed to validate property", task.getException());
                // Muestra un mensaje al usuario
                utils.showAlert(this, "Error", "No se pudo validar la propiedad. Inténtalo de nuevo.");
            }
        });
    }

    @Override
    public void onBlockProperty(Property property) {
        String propertyId = property.getPropertyId();
        String ownerId = property.getOwnerId();

        DatabaseReference properyRef = mDatabase.child("properties").child(ownerId).child("propertiesOwner").child(propertyId);
        DatabaseReference propertyValidation = mDatabase.child("propertiesValidation").child(ownerId).child("propertiesOwner").child(propertyId);

        properyRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                propertyValidation.removeValue().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        // La eliminación fue exitosa, ahora elimina la propiedad de la lista local
                        propertyList.remove(property);
                        propertyAdapter.notifyDataSetChanged();
                        Log.d("Property", "Property blocked successfully");
                        // Muestra un mensaje al usuario
                        utils.showAlert(this, "Éxito", "La propiedad ha sido bloqueada con éxito.");
                    } else {
                        // Manejar el error
                        Log.e("Property", "Failed to delete property", task2.getException());
                        // Muestra un mensaje al usuario
                        utils.showAlert(this, "Error", "No se pudo bloquear la propiedad. Inténtalo de nuevo.");
                    }
                });
            } else {
                // Manejar el error
                Log.e("Property", "Failed to delete property", task.getException());
                // Muestra un mensaje al usuario
                utils.showAlert(this, "Error", "No se pudo bloquear la propiedad. Inténtalo de nuevo.");
            }
        });

    }

    // Interfaz de callback para obtener el cliente
    private interface OnClientRetrievedListener {
        void onClientRetrieved(Client client);
    }
}
