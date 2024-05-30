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
import com.proyecto.apprural.databinding.PropertyOfferItemFailBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.Offer;
import com.proyecto.apprural.model.beans.Prohibition;
import com.proyecto.apprural.model.beans.Property;
import com.proyecto.apprural.model.beans.Room;
import com.proyecto.apprural.model.beans.Service;
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
    PropertyService propertyService;
    OfferService offerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.owner_view_publish_activity);
        binding.setLifecycleOwner(this);
        propertyService = new PropertyService();
        // Inicializa la referencia de la base de datos
        mDatabase = FirebaseDatabase.getInstance().getReference();
        propertyList = new ArrayList<>();
        offerService = new OfferService();
        // Configura la sesión
        session();

        // Configura RecyclerView
        setupRecyclerView();

        // Obtén la lista de propiedades
        getPropertiesList();
    }

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

    private void setupRecyclerView() {
        publishPropertyAdapter = new PublishPropertyAdapter(propertyList, this);
        binding.setMyAdapter(publishPropertyAdapter);
        binding.propertyRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this, R.drawable.divider));
    }


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

                publishPropertyAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja el error
                Log.w("DatabaseError", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onPublishProperty(Property property) {
        String ownerID = property.getOwnerId();
        String propertyID = property.getPropertyId();
        propertyService.getPropertyById(ownerID, propertyID, data -> {
            if (data != null) {
                FullAccommodationOffer offer = new FullAccommodationOffer(data.getOwnerId(), true, true, data.getName(),
                        data.getAddress(), "FullAccommodationOffer", data.getPrice(), data.getServices(),
                        data.getProhibitions(), data.getPropertyId(), data.getRooms());

                Log.e("propiedad devuelta", data.toString());
                offerService.saveFullAccommodationOffer(offer, result -> {
                    if(result) {
                        //se actualiza el atributo published de la propiedad
                        propertyService.updatePublishedProperty(ownerID, propertyID, true, result1 -> {
                            if(result) {
                                updatePublishedStatus(propertyList, data, true);
                                publishPropertyAdapter.notifyDataSetChanged();
                                Log.e("success", "el campo published ha sido actualizado a true");
                            }else {
                                Log.e("fail", "el campo published no ha sido actualizado a true");
                            }
                        });
                    }else {
                        utils.showAlert(this, "Error", "No se ha podido publicar el alojamiento.");
                    }
                });
            } else {
                utils.showAlert(this, "Error", "No se ha podido publicar el alojamiento.");
            }
        });


    }

    @Override
    public void onNoPublishProperty(Property property) {
        String propertyID = property.getPropertyId();
        String ownerID = property.getOwnerId();
        offerService.deleteFullAccommodationOffer(propertyID, result -> {
            if (result){
                //se actualiza el atributo published de la propiedad
                propertyService.updatePublishedProperty(ownerID, propertyID, false, result1 -> {
                    if(result1) {
                        updatePublishedStatus(propertyList, property, false);
                        publishPropertyAdapter.notifyDataSetChanged();
                        Log.e("success", "el campo published ha sido actualizado a false");
                    }else {
                        Log.e("fail", "el campo published no ha sido actualizado a false");
                    }
                });
            }else {
                utils.showAlert(this, "Error", "No se ha podido retirar la publicación del alojamiento.");
            }
        });
    }

    public void updatePublishedStatus(List<Property> propertyList, Property propertyToUpdate, boolean status) {
        for (Property property : propertyList) {
            if (property.getPropertyId().equals(propertyToUpdate.getPropertyId())) {
                property.setPublished(status);
            }
        }
    }

}
