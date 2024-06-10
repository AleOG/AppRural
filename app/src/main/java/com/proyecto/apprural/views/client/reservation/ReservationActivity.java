package com.proyecto.apprural.views.client.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.ReservationActivityBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.Reservation;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.home.HomeActivityRouter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ReservationActivity extends AppCompatActivity {

    private ReservationActivityBinding binding;
    private SharedPreferences.Editor miEditor;
    private SharedPreferences misDatos;
    private String emailSession;
    private Util utils = new Util();
    private FullAccommodationOffer offer;
    private List<Reservation> reservationList;
    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReservationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        reservationList = new ArrayList<>();
        reservation = new Reservation();

        FullAccommodationOffer offerAux = (FullAccommodationOffer) intent.getSerializableExtra("offer");
        Bundle bundle = intent.getExtras();

        if (offerAux != null) {
            Log.e("offer en reservation", offerAux.toString());
            offer = offerAux;
            reservation.setOwnerId(offer.getIdOwner());

            reservation.setAccomodationId(offer.getIdProperty());
            reservation.setPrice(offer.getPrice());
            reservation.setRooms(offer.getRooms());
            binding.setReservation(reservation);
            recoverReservationByOfferId(offer.getIdProperty());
        }

        String email = null;
        if (bundle != null) {
            email = bundle.getString("email");
        }

        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        miEditor = misDatos.edit();
        miEditor.putString("email", email);
        miEditor.apply();

        session();
        setup();
    }

    /**
     * Función que recupera el email de la sesión del usuario
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
     * Función que inicializa y configura los elementos de esta actividad
     */
    private void setup() {

        TextView checkinDate = binding.checkinDate;
        TextView checkoutDate = binding.checkoutDate;

        binding.checkinBtn.setOnClickListener( event -> {
            Calendar calendar = Calendar.getInstance();
            long fechaMin = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR, 5);
            long fechaMax = calendar.getTimeInMillis();

            if (!checkoutDate.getText().toString().trim().isEmpty()) {
                LocalDateTime fechaDeparture = utils.formatStringDateToLocalDateTime(checkoutDate.getText().toString().trim());
                if (fechaDeparture != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fechaMax = fechaDeparture.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    }
                }
            }

            utils.showDatePickerDialogReservation(checkinDate, this, reservationList, fechaMin, fechaMax);
        });

        binding.checkoutBtn.setOnClickListener(event -> {
            Calendar calendar = Calendar.getInstance();
            long fechaMin = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR, 5);
            long fechaMax = calendar.getTimeInMillis();

            String fechaArrivalString = checkinDate.getText().toString().trim();

            if(!fechaArrivalString.isEmpty()) {
                LocalDateTime fechaArrival = utils.formatStringDateToLocalDateTime(fechaArrivalString);
                if(fechaArrival != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fechaMin = fechaArrival.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    }
                }
            }
            utils.showDatePickerDialogReservation(checkoutDate, this, reservationList, fechaMin, fechaMax);
        });

        binding.confirmReservationButton.setOnClickListener( event -> {
            if (areFieldsFilled()) {
                String numberGuests = binding.numberGuests.getText().toString();
                if(Integer.parseInt(numberGuests)<=offer.getCapacity()) {
                    createAndSaveReservation();
                }
                else {
                    utils.showAlert(this, "Aviso", "El número de huéspedes es superior al permitido "+ String.valueOf(offer.getCapacity()));
                }
            } else {
                Toast.makeText(getApplicationContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                utils.showAlert(this, "Aviso", "Debe completar todos los campos.");
            }
        });

        binding.exitButton.setOnClickListener( event -> {
            logoutAndFinish();
        });

        checkinDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newValue = s.toString();
                String checkoutString = checkoutDate.getText().toString();
                if(!checkoutString.isEmpty() && !newValue.isEmpty()) {
                    LocalDateTime checkin = utils.formatStringDateToLocalDateTime(newValue);
                    LocalDateTime checkout = utils.formatStringDateToLocalDateTime(checkoutString);
                    long daysBetween = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        daysBetween = ChronoUnit.DAYS.between(checkout, checkin);
                    }

                    Log.e("","Número de días entre check-in y check-out: " + daysBetween);
                    if(daysBetween<0) {
                        daysBetween = daysBetween*-1;
                    }
                    reservation.setPrice(offer.getPrice()*daysBetween);
                    binding.setReservation(reservation);
                    binding.executePendingBindings();
                }
            }
        });

        checkoutDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newValue = s.toString();
                String checkinString = checkinDate.getText().toString();
                if (!checkinString.isEmpty() && !newValue.isEmpty()) {
                    LocalDateTime checkin = utils.formatStringDateToLocalDateTime(newValue);
                    LocalDateTime checkout = utils.formatStringDateToLocalDateTime(checkinString);
                    long daysBetween = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        daysBetween = ChronoUnit.DAYS.between(checkout, checkin);
                    }

                    Log.e("", "Número de días entre check-in y check-out: " + daysBetween);
                    if(daysBetween<0) {
                        daysBetween = daysBetween*-1;
                    }
                    reservation.setPrice(offer.getPrice()*daysBetween);
                    binding.setReservation(reservation);
                    binding.executePendingBindings();
                }
            }
        });

        binding.resetReservationButton.setOnClickListener(event -> {
            resetFields();
        });
    }

    /**
     * Función que resetea los campos del formulario  de la reserva
     */
    private void resetFields() {
        binding.name.setText("");
        binding.firstlastname.setText("");
        binding.secondLastName.setText("");
        binding.numberGuests.setText("");

        binding.checkinDate.setText("");
        binding.checkoutDate.setText("");
        binding.price.setText("");

        reservation.setPrice(0.0);
    }

    /**
     * Función que elimina los datos de sesión de SharePreferences, finaliza sesión en firebase, inicia la actividad de Home y finaliza el resto de actividades en stack.
     */
    private void logoutAndFinish() {
        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        miEditor = misDatos.edit();
        miEditor.clear();
        miEditor.apply();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new HomeActivityRouter().intent(getApplicationContext(), null);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Función que comprueba que todos los campos del formulario tienen contenida
     *
     * @return
     */
    private boolean areFieldsFilled() {
        String name = binding.name.getText().toString();
        String firstLastName = binding.firstlastname.getText().toString();
        String secondLastName = binding.secondLastName.getText().toString();
        String checkinDate = binding.checkinDate.getText().toString();
        String checkoutDate = binding.checkoutDate.getText().toString();
        String numberGuests = binding.numberGuests.getText().toString();

        return !name.isEmpty() && !firstLastName.isEmpty() && !secondLastName.isEmpty() &&
                !checkinDate.isEmpty() && !checkoutDate.isEmpty() && !numberGuests.isEmpty();
    }

    /**
     * Función que asigna los datos de la reserva y la guarda en base de datos
     */
    private void createAndSaveReservation() {

        String name = binding.name.getText().toString();
        String firstLastName = binding.firstlastname.getText().toString();
        String secondLastName = binding.secondLastName.getText().toString();
        String checkinDate = utils.formatStringDateToLocalDateTime(binding.checkinDate.getText().toString()).toString();
        String checkoutDate = utils.formatStringDateToLocalDateTime(binding.checkoutDate.getText().toString()).toString();
        String status = "entrada";
        int numberGuests = Integer.parseInt(binding.numberGuests.getText().toString());

        String reservationCode = UUID.randomUUID().toString();
        String fullName = name + " "+ firstLastName +" " + secondLastName;

        reservation.setReservationCode(reservationCode);
        reservation.setCheckin(checkinDate);
        reservation.setCheckout(checkoutDate);
        reservation.setNumberOfPeople(numberGuests);
        reservation.setStatus(status);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime fechaActual = LocalDateTime.now();
            reservation.setCreationDate(fechaActual.toString());
        }
        reservation.setGuestId(utils.generateID(emailSession));
        reservation.setGuestName(fullName);
        reservation.setBookingType("Alojamiento completo");
        Log.e("reserva creada", reservation.toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reservations");
        databaseReference.push().setValue(reservation);

        utils.showAlert(this, "Éxito", "Reserva creada y guardada correctamente");
    }

    /**
     * Función que recupera una reserva en base a el id de la propiedad donde se ha hecho.
     *
     * @param propertyID
     */
    private void recoverReservationByOfferId(String propertyID) {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");
        databaseReference.orderByChild("accomodationId").equalTo(propertyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("entra", "entra");
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    reservationList.add(reservation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error al obtener las reservas", databaseError.toException());
            }
        });
    }
}