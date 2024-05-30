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

    public void getPropertyById(String ownerID, String propertyID, PropertyCallback callback) {
        databaseReference.child("properties").child(ownerID)
                .child("propertiesOwner").child(propertyID).addValueEventListener(new ValueEventListener() {

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

    public void updatePublishedProperty(String ownerID, String propertyID, Boolean state, UpdateCallback callback) {
        // Crea un mapa para actualizar el campo 'published' a 'true'
        Map<String, Object> updates = new HashMap<>();
        updates.put("published", state);

        databaseReference.child("properties").child(ownerID)
                .child("propertiesOwner").child(propertyID)
                .updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // La actualización fue exitosa
                callback.onUpdateComplete(true);
            } else {
                // Hubo un error al actualizar
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
