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
        setupRecyclerView();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        /**
         * Se recuperan los datos de la colección propertiesValidation. Esas son las propiedades que el administrador tiene que validar.
         */
        mDatabase.child("propertiesValidation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                propertyList.clear();
                for (DataSnapshot ownerSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot propertiesOwnerSnapshot = ownerSnapshot.child("propertiesOwner");
                    for (DataSnapshot propertySnapshot : propertiesOwnerSnapshot.getChildren()) {
                        Property property = propertySnapshot.getValue(Property.class);
                        if (property != null) {
                            propertyList.add(property);
                            Log.d("Property", "Property Name: " + property.getName());
                        }
                    }
                }
                propertyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseError", "loadPost:onCancelled", databaseError.toException());
            }
        });

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logoutAndFinish();
            }
        });
    }

    /**
     * Función donde se configura el recyclerview para esta actividad
     */
    private void setupRecyclerView() {
        propertyList = new ArrayList<>();
        propertyAdapter = new PropertyAdapter(propertyList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(propertyAdapter);
    }

    /**
     * Función donde se nicializan y configuran elementos para esta actividad
     */
    private void setup() {

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

    /**
     * Función que sirve para mostrar las caracteristicas de la propiedad en una ventana emergente.
     *
     * @param property
     */
    @Override
    public void onViewProperty(Property property) {
        DialogViewPropertyBinding dialogBinding = DialogViewPropertyBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());

        dialogBinding.setProperty(property);
        getClient(property.getOwnerId(), client -> {
            if (client != null) {
                dialogBinding.setClient(client);
            }
        });

        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Función que recupera el cliente de base de datos en base a su id
     *
     * @param userId
     * @param listener
     */
    private void getClient(String userId, OnClientRetrievedListener listener) {
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client client = dataSnapshot.getValue(Client.class);
                listener.onClientRetrieved(client);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("firebase", "Error getting data", databaseError.toException());
                listener.onClientRetrieved(null);
            }
        });
    }

    /**
     * Función que actualiza una propiedad en base de datos como validad.
     *
     * @param property
     */
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
                        propertyList.remove(property);
                        propertyAdapter.notifyDataSetChanged();
                        utils.showAlert(this, "Éxito", "La propiedad ha sido validada exitosamente.");
                    } else {
                        utils.showAlert(this, "Error", "No se pudo validar la propiedad. Inténtalo de nuevo.");
                    }
                });

            } else {
                utils.showAlert(this, "Error", "No se pudo validar la propiedad. Inténtalo de nuevo.");
            }
        });
    }

    /**
     * Funciñon que elimina la propiedad de base de datos al considerarse no válida
     *
     * @param property
     */
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
                        propertyList.remove(property);
                        propertyAdapter.notifyDataSetChanged();
                        utils.showAlert(this, "Éxito", "La propiedad ha sido bloqueada con éxito.");
                    } else {
                        utils.showAlert(this, "Error", "No se pudo bloquear la propiedad. Inténtalo de nuevo.");
                    }
                });
            } else {
                utils.showAlert(this, "Error", "No se pudo bloquear la propiedad. Inténtalo de nuevo.");
            }
        });

    }

    private interface OnClientRetrievedListener {
        void onClientRetrieved(Client client);
    }
}
