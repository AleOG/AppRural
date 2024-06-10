package com.proyecto.apprural.views.client.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.AccommodationDetailsActivityBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.views.client.ClientViewLoginRouter;
import com.google.firebase.storage.StorageReference;

public class AccommodationDetailsActivity extends AppCompatActivity {

    private AccommodationDetailsActivityBinding binding;
    private FullAccommodationOffer offer =null;
    ShapeableImageView foto_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.accommodation_details_activity);
        binding.getLifecycleOwner();

        FullAccommodationOffer offerAux = (FullAccommodationOffer) getIntent().getSerializableExtra("offer");

        if (offerAux != null) {
            Log.e("offer en accomodation", offerAux.toString());
            binding.setOffer(offerAux);
            offer = offerAux;
        }

        foto_usuario = binding.imagenUsuario;

        StorageReference refImagenes = FirebaseStorage.getInstance().getReference().child("images").child(offer.getIdProperty());

        /**
         * Función que recupera la imagen del alojamiento
         */
        refImagenes.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                if (listResult.getItems().size() > 0) {
                    StorageReference refPrimeraImagen = listResult.getItems().get(0);

                    refPrimeraImagen.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(AccommodationDetailsActivity.this)
                                    .load(uri)
                                    .into(foto_usuario);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", "Error al obtener la URL de descarga: " + e.getMessage());
                        }
                    });
                } else {
                    Log.e("Error", "No se encontraron imágenes en la carpeta");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error", "Error al listar las imágenes: " + e.getMessage());
            }
        });


        setup();
    }

    /**
     * Función que configura los elementos de esta actividad
     */
    private void setup() {

        binding.buttonExit.setOnClickListener(event -> {
            finish();
        });

        binding.buttonBooking.setOnClickListener(event -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("offer", offer);
            new ClientViewLoginRouter().launch(this, bundle);
        });
    }

}
