package com.proyecto.apprural.views.client.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.AccommodationDetailsActivityBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.Reservation;
import com.proyecto.apprural.model.beans.Room;
import com.proyecto.apprural.model.beans.Service;
import com.proyecto.apprural.views.client.ClientViewLoginRouter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccommodationDetailsActivity extends AppCompatActivity {

    private AccommodationDetailsActivityBinding binding;
    private FullAccommodationOffer offer =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.accommodation_details_activity);
        binding.getLifecycleOwner();

        // Retrieve FullAccommodationOffer from Intent
        FullAccommodationOffer offerAux = (FullAccommodationOffer) getIntent().getSerializableExtra("offer");

        if (offerAux != null) {
            // Set offer to binding
            Log.e("offer en accomodation", offerAux.toString());
            binding.setOffer(offerAux);
            offer = offerAux;
        }

        setup();
    }
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
