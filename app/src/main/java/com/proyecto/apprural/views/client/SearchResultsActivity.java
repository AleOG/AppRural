package com.proyecto.apprural.views.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.proyecto.apprural.model.beans.Reservation;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.client.reservation.AccommodationDetailsRouter;
import com.proyecto.apprural.views.common.RecyclerViewItemDecoration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.OnClickListener {

    private SearchResultsActivityBinding binding;
    private List<FullAccommodationOffer> resultsList;
    private SearchResultsAdapter adapter;
    private Util utils = new Util();

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
        LocalDateTime arrivalDate = null;
        LocalDateTime departureDate = null;
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
                arrivalDate = utils.formatStringDateToLocalDateTime(arrivalDateString);
            }
            if (departureDateString != null && !departureDateString.isEmpty()) {
                departureDate = utils.formatStringDateToLocalDateTime(departureDateString);
            }
            if (destinationString != null && !destinationString.isEmpty()) {
                destination = destinationString;
            }
            if (travelersString != null && !travelersString.isEmpty()) {
                travelers = Integer.parseInt(travelersString);
            }

        }
        setRecyclerView();
        searchOffers(minPrecio, maxPrecio, arrivalDate, departureDate, destination, travelers);

        binding.exitButton.setOnClickListener(event -> {
            finish();
        });
    }


    /**
     * Función que inicializa y configura el recycleviw de la actividad
     */
    private void setRecyclerView() {
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
    private void searchOffers(double minPrecio, double maxPrecio, LocalDateTime arrivalDate, LocalDateTime departureDate, String destination, int travelers) {
        DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference("offers");

        offersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultsList.clear();
                for (DataSnapshot offerSnapshot : dataSnapshot.getChildren()) {
                    FullAccommodationOffer offer = offerSnapshot.getValue(FullAccommodationOffer.class);
                    if (offer != null && offer.isPublished() && offer.getPrice() >= minPrecio && offer.getPrice() <= maxPrecio
                            && matchesDestination(offer, destination) && offer.getCapacity() >= travelers) {
                        String propertyID = offer.getIdProperty();
                        DatabaseReference databaseReference;
                        List<Reservation> reservationList = new ArrayList<>();
                        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");

                        if(arrivalDate != null && departureDate != null) {
                            databaseReference.orderByChild("accomodationId").equalTo(propertyID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    reservationList.clear();
                                    boolean validated = true;
                                    if (dataSnapshot.hasChildren()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Log.e("entra", "entra");
                                            Reservation reservation = snapshot.getValue(Reservation.class);
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                LocalDateTime reservationCheckIn = LocalDateTime.parse(reservation.getCheckin());
                                                LocalDateTime reservationCheckOut = LocalDateTime.parse(reservation.getCheckout());
                                                boolean isArrivalOutside = arrivalDate.isBefore(reservationCheckIn) && departureDate.isBefore(reservationCheckIn);
                                                boolean isDepartureOutside = arrivalDate.isAfter(reservationCheckOut) && departureDate.isAfter(reservationCheckOut);

                                                if (!isArrivalOutside && !isDepartureOutside) {
                                                    //Log.e("ReservationCheck", "Valid reservation with dates: " + reservation);
                                                    Log.e("ReservationCheckOut", "Eliminada: " + offer);
                                                    Log.e("Checkin", reservationCheckIn.toString());
                                                    Log.e("arrival", arrivalDate.toString());
                                                    Log.e("chechout", reservationCheckOut.toString());
                                                    Log.e("departure", departureDate.toString());
                                                    //resultsList.add(offer);
                                                    validated = false;
                                                    break;
                                                }
                                            }
                                        }
                                        if(validated) {
                                            resultsList.add(offer);
                                        }
                                    } else {
                                        Log.e("ReservationCheck", "No reservations found for offer: " + offer);
                                        resultsList.add(offer);
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("Firebase", "Error al obtener las reservas", databaseError.toException());
                                }
                            });

                        }else {
                            resultsList.add(offer);
                        }

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchResultsActivity", "Error al buscar ofertas: " + error.getMessage());
            }
        });
    }

    /**
     * Función que filtra por destino
     *
     * @param offer
     * @param destination
     * @return
     */
    private boolean matchesDestination(Offer offer, String destination) {
        if(destination.isEmpty()) {
            return true;
        }
        return offer.getTown().equalsIgnoreCase(destination) || offer.getCountry().equalsIgnoreCase(destination);
    }


    @Override
    public void onClick(FullAccommodationOffer offer) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("offer", offer);

        new AccommodationDetailsRouter().launch(this, bundle);
    }
}