package com.proyecto.apprural.views.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.SearchResultsActivityBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.Offer;
import com.proyecto.apprural.views.common.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.OnClickListener {

    private SearchResultsActivityBinding binding;
    private List<FullAccommodationOffer> resultsList;
    private SearchResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchResultsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /**
         * Se muestran todos los alojamientos cuando los precios y los viajeros son 0 y las fechas y el destino son Empty().
         */
        double minPrecio = 0;
        double maxPrecio = Double.POSITIVE_INFINITY;
        String arrivalDate = "";
        String departureDate = "";
        String destination = "";
        int travelers = 0;
        resultsList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String minPrecioString = bundle.getString("minPrecio");
            String maxPrecioString = bundle.getString("maxPrecio");
            String arrivalDateString = bundle.getString("arrivalDate");
            String departureDateString = bundle.getString("departureDate");
            String destinationString = bundle.getString("destination");
            String travelersString = bundle.getString("travelers");

            if (minPrecioString != null && !minPrecioString.isEmpty()) {
                minPrecio = Double.parseDouble(minPrecioString);
            }
            if (maxPrecioString != null && !maxPrecioString.isEmpty()) {
                maxPrecio = Double.parseDouble(maxPrecioString);
            }
            if (arrivalDateString != null && !arrivalDateString.isEmpty()) {
                arrivalDate = arrivalDateString;
            }
            if (departureDateString != null && !departureDateString.isEmpty()) {
                departureDate = departureDateString;
            }
            if (destinationString != null && !destinationString.isEmpty()) {
                destination = destinationString;
            }
            if (travelersString != null && !travelersString.isEmpty()) {
                travelers = Integer.parseInt(travelersString);
            }

        }
        seupAdapter();
        searchOffers(minPrecio, maxPrecio, arrivalDate, departureDate, destination, travelers);

        binding.exitButton.setOnClickListener(event -> {
            finish();
        });
    }

    private void seupAdapter() {
        adapter = new SearchResultsAdapter(resultsList, this);
        binding.resultsRecyclerView.setAdapter(adapter);
        binding.resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.resultsRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(getApplicationContext(), R.drawable.divider));
    }
    /**
     * 1º Buscamos por capacidad. capacity >= travelers
     * 2º Buscamos por destination. Comparar con address, town, country
     * 3º Buscamos por precioMin y maxPrecio. minPrecio <= precio <= maxPrecio
     * 4º Se obtienen los ids de las propiedades de las offers
     * 5º Se busca en las colección de reservas, las reservas activas que hay en esas propiedades.
     * 6º Se comparan las fechas elegidas por el usuario para saber si hay disponibilidad.
     * 7º Se devuelve el id de la propiedad.
     * 8º Solo se añaden a la lista de resultados las offer con esos ids.
     */
    private void searchOffers(double minPrecio, double maxPrecio, String arrivalDate, String departureDate, String destination, int travelers) {
        DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference("offers");

        offersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultsList.clear();
                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    FullAccommodationOffer offer = offerSnapshot.getValue(FullAccommodationOffer.class);
                    Log.e("null", String.valueOf(offer != null));
                    Log.e("publish", String.valueOf(offer.isPublished()));
                    Log.e("min", String.valueOf(offer.getPrice() >= minPrecio));
                    Log.e("precio", String.valueOf(maxPrecio));
                    Log.e("max", String.valueOf(offer.getPrice() <= maxPrecio));
                    Log.e("dest", String.valueOf(matchesDestination(offer, destination)));
                    Log.e("capac", String.valueOf(offer.getCapacity()>=travelers));
                    if (offer != null && offer.isPublished() && offer.getPrice() >= minPrecio && offer.getPrice() <= maxPrecio
                            && matchesDestination(offer, destination) && offer.getCapacity()>=travelers) {
                        Log.e("offer out", offer.toString());
                        // Agregar la oferta a la lista de ofertas coincidentes
                        resultsList.add(offer);
                    }
                }
                adapter.notifyDataSetChanged();
                // Ahora puedes hacer lo que necesites con la lista de ofertas coincidentes

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchResultsActivity", "Error al buscar ofertas: " + error.getMessage());

            }

        });
    }
    // Método para verificar si una oferta coincide con el destino
    private boolean matchesDestination(Offer offer, String destination) {
        if(destination.isEmpty()) {
            return true;
        }
        return offer.getTown().equalsIgnoreCase(destination) || offer.getCountry().equalsIgnoreCase(destination);
    }


    @Override
    public void onClick(FullAccommodationOffer offer) {
        Log.e("offer recuperada", offer.toString());
    }
}