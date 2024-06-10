package com.proyecto.apprural.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;

public class OfferService {

    private DatabaseReference databaseReference;

    public OfferService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    /**
     * Función usada para guardar el alojamiento ofertado en base de datos
     *
     * @param offer
     * @param callback
     */
    public void saveFullAccommodationOffer(FullAccommodationOffer offer, SaveCallback callback) {
        String propertyID = offer.getIdProperty();
        databaseReference.child("offers").child(propertyID).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("bien", "Oferta añadida con éxito.");
                    callback.onOfferSaved(true);
                } else {
                    Log.w("mal", "Error al añadir la oferta.", task.getException());
                    callback.onOfferSaved(false);
                }
            }
        });
    }

    /**
     * Función que sirve para eliminar el alojamiento ofertado de base de datos
     *
     * @param propertyID
     * @param callback
     */
    public void deleteFullAccommodationOffer(String propertyID, DeleteCallback callback) {
        databaseReference.child("offers").child(propertyID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.d("bien", "Oferta eliminada con éxito.");
                    callback.onOfferDeleted(true);
                }else {
                    Log.w("mal", "Error al eliminar la oferta.", task.getException());
                    callback.onOfferDeleted(false);
                }
            }
        });
    }

    public interface SaveCallback  {
        void onOfferSaved(boolean result);
    }

    public interface DeleteCallback {
        void onOfferDeleted(boolean result);
    }
}
