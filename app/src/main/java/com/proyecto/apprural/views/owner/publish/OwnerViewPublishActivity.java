package com.proyecto.apprural.views.owner.publish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.OwnerViewPublishActivityBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.Property;
import com.proyecto.apprural.repository.OfferService;
import com.proyecto.apprural.repository.PropertyService;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.common.RecyclerViewItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class OwnerViewPublishActivity extends AppCompatActivity implements
        PublishPropertyAdapter.OnPublishPropertyActionListener{

    private OwnerViewPublishActivityBinding binding;
    private List<Property> propertyList;
    private PublishPropertyAdapter publishPropertyAdapter;
    private DatabaseReference mDatabase;
    private String emailSession;
    private SharedPreferences misDatos;
    private Util utils = new Util();
    private PropertyService propertyService;
    private OfferService offerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.owner_view_publish_activity);
        binding.setLifecycleOwner(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        propertyList = new ArrayList<>();
        propertyService = new PropertyService();
        offerService = new OfferService();

        session();

        setupRecyclerView();

        getPropertiesList();

        binding.exitBtn.setOnClickListener(event -> {
            finish();
        });
    }

    /**
     * Función que recupera de SharePreferences el email del usuario
     */
    private void session() {
        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = misDatos.getString("email", null);

        if (email == null) {
            finish();
        }
        emailSession = email;
        Log.e("email session ", emailSession);
        Toast.makeText(getApplicationContext(), "email session " + emailSession, Toast.LENGTH_SHORT).show();
    }

    /**
     * Función que inicializa y configura el recycleview de la actividad
     */
    private void setupRecyclerView() {
        publishPropertyAdapter = new PublishPropertyAdapter(propertyList, this);
        binding.setMyAdapter(publishPropertyAdapter);
        binding.propertyRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this, R.drawable.divider));
    }


    /**
     * Función que recupera todas las propiedades de un usuario
     */
    private void getPropertiesList() {

        String ownerID = utils.generateID(emailSession);
        mDatabase.child("properties").child(ownerID).child("propertiesOwner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                propertyList.clear();

                for (DataSnapshot propertySnapshot : dataSnapshot.getChildren()) {
                    Property property = propertySnapshot.getValue(Property.class);
                    if (property != null) {
                        if(property.isValidated()) {
                            propertyList.add(property);
                        }
                    }
                }

                publishPropertyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseError", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    /**
     * Función que actualiza el atributo "published" en el recycleview de las propiedades
     *
     * @param propertyList
     * @param propertyToUpdate
     * @param status
     */
    public void updatePublishedStatus(List<Property> propertyList, Property propertyToUpdate, boolean status) {
        for (Property property : propertyList) {
            if (property.getPropertyId().equals(propertyToUpdate.getPropertyId())) {
                property.setPublished(status);
            }
        }
        publishPropertyAdapter.notifyDataSetChanged();
    }

    /**
     * Función que selecciona una propiedad de la lista en base a su id
     *
     * @param property
     */
    private void getPropertyById(Property property) {
        String ownerID = property.getOwnerId();
        String propertyID = property.getPropertyId();
        propertyService.getPropertyById(ownerID, propertyID, data -> {
            if (data != null) {
                saveFullAccommodationOffer(data);
            } else {
                utils.showAlert(this, "Error", "No se ha podido obtener la propiedad.");
            }
        });
    }

    /**
     * Función que inicializa un objeto Offer y lo guarda en base de datos
     *
     * @param property
     */
    private void saveFullAccommodationOffer(Property property) {
        FullAccommodationOffer offer = new FullAccommodationOffer(
                property.getOwnerId(), true, true, property.getName(),
                property.getAddress(), property.getTown(), property.getCountry(),
                "FullAccommodationOffer", property.getPrice(), property.getServices(),
                property.getProhibitions(), property.getCapacity(), property.getPropertyId(),
                property.getRooms()
        );

        Log.e("propiedad devuelta", property.toString());
        offerService.saveFullAccommodationOffer(offer, result -> {
            if (result) {
                updatePublishedProperty(property, true);
            } else {
                utils.showAlert(this, "Error", "No se ha podido publicar el alojamiento.");
            }
        });
    }

    /**
     * Función que actualiza la publicación de una propiedad
     *
     * @param property
     * @param status
     */
    private void updatePublishedProperty(Property property, boolean status) {
        String ownerID = property.getOwnerId();
        String propertyID = property.getPropertyId();
        propertyService.updatePublishedProperty(ownerID, propertyID, status, result -> {
            if (result) {
                updatePublishedStatus(propertyList, property, status);
                publishPropertyAdapter.notifyDataSetChanged();
                Log.e("exito", "el campo published ha sido actualizado a " + status);
            } else {
                Log.e("fallo", "el campo published no ha sido actualizado a " + status);
            }
        });
    }


    /**
     * Función que elimina una oferta de base de datos
     *
     * @param property
     */
    private void deleteFullAccommodationOffer(Property property) {
        String propertyID = property.getPropertyId();
        offerService.deleteFullAccommodationOffer(propertyID, result -> {
            Log.e("result", String.valueOf(result));
            if (result) {
                updatePublishedPropertyToFalse(property);
            } else {
                utils.showAlert(this, "Error", "No se ha podido retirar la publicación del alojamiento.");
            }
        });
    }

    /**
     * Función que despublica una oferta
     *
     * @param property
     */
    private void updatePublishedPropertyToFalse(Property property) {
        String ownerID = property.getOwnerId();
        String propertyID = property.getPropertyId();
        propertyService.updatePublishedPropertyToFalse(ownerID, propertyID, false, result -> {
            if (result) {
                updatePublishedStatus(propertyList, property, false);
                publishPropertyAdapter.notifyDataSetChanged();
                Log.e("exito", "el campo published ha sido actualizado a false");
            } else {
                Log.e("error", "el campo published no ha sido actualizado a false");
            }
        });
    }

    @Override
    public void onPublish(Property property) {
        updatePublishedStatus(propertyList, property, true);
        getPropertyById(property);

    }

    @Override
    public void onTakingOut(Property property) {
        updatePublishedStatus(propertyList, property, false);
        deleteFullAccommodationOffer(property);
    }
}
