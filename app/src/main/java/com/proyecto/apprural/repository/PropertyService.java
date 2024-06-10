package com.proyecto.apprural.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.model.beans.Property;

import java.util.HashMap;
import java.util.Map;

public class PropertyService {

    private DatabaseReference databaseReference;

    public PropertyService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    /**
     * Función que sirve para recuperar la propiedad de base de datos en función a su id
     *
     * @param ownerID
     * @param propertyID
     * @param callback
     */
    public void getPropertyById(String ownerID, String propertyID, PropertyCallback callback) {
        databaseReference.child("properties").child(ownerID)
                .child("propertiesOwner").child(propertyID).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Property property = snapshot.getValue(Property.class);
                        if (property != null) {
                            Log.e("propiedad recuperada", property.toString());
                            callback.onCheckComplete(property);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Error en getPropertyById", "Fallo al recuperar propiedad");
                        callback.onCheckComplete(null);
                    }
                });
    }

    /**
     * Función que modifica el campo "published" a true en una propiedad en base de datos cuando esta se oferta
     *
     * @param ownerID
     * @param propertyID
     * @param state
     * @param callback
     */
    public void updatePublishedProperty(String ownerID, String propertyID, Boolean state, UpdateCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("published", state);

        databaseReference.child("properties").child(ownerID)
                .child("propertiesOwner").child(propertyID)
                .updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onUpdateComplete(true);
            } else {
                callback.onUpdateComplete(false);
            }
        });
    }

    /**
     * Función que modifica el campo "published" a false en una propiedad en base de datos cuando esta se deja de ser ofertada
     *
     * @param ownerID
     * @param propertyID
     * @param state
     * @param callback
     */
    public void updatePublishedPropertyToFalse(String ownerID, String propertyID, Boolean state, UpdateCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("published", state);

        databaseReference.child("properties").child(ownerID)
                .child("propertiesOwner").child(propertyID)
                .updateChildren(updates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onUpdateComplete(true);
                    } else {
                        callback.onUpdateComplete(false);
                    }
                });
    }

    public interface PropertyCallback {
        void onCheckComplete(Property property);

    }

    public interface UpdateCallback {
        void onUpdateComplete(boolean result);
    }
}
