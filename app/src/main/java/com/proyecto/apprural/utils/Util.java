package com.proyecto.apprural.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.proyecto.apprural.model.beans.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {

    /**
     * Función que muestra un mensaje por pantalla personalizado
     *
     * @param context
     * @param title
     * @param message
     */
    public void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Cerrar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Función que genera un id para el propietario en base a su email
     *
     * @param email
     * @return
     */
    public String generateID(String email) {
        int code = email.hashCode();
        String id = String.valueOf(code);
        return id;
    }

    /**
     * Función que transforma una fecha en string al formato LocalDateTime
     *
     * @param fecha
     * @return
     */
    public LocalDateTime formatStringDateToLocalDateTime(String fecha) {
        LocalDateTime dateTime = null;
        String[] parts = fecha.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.of(year,month,day,0,0,0,0);
        }
        return dateTime;
    }

    /**
     * Función que transforma una fecha de LocalDateTime a string
     *
     * @param fecha
     * @return
     */
    public String formatLocalDateTimeToStringFecha(LocalDateTime fecha) {
        DateTimeFormatter formatter;
        String fechaFormateada = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaFormateada = fecha.format(formatter);
        }

        return fechaFormateada;
    }

    /**
     * Función que construye un muestra un datePicker genérico con límites de fecha mínimo y máximo
     *
     * @param dateTextView
     * @param context
     * @param fechaMin
     * @param fechaMax
     */
    public void showDatePickerDialog(final TextView dateTextView, Context context, long fechaMin, long fechaMax) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateTextView.setText(selectedDate);
                    }
                },
                year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(fechaMin);
        datePicker.setMaxDate(fechaMax);
        datePickerDialog.show();
    }

    /**
     * Función que construye y muestra un datapicker específico para realizar reservas.
     * Cuenta con límites de fecha mínima y m´´axima y con los rangos de las fechas de los objetos de la lista de reservas.
     *
     * @param dateTextView
     * @param context
     * @param reservas
     * @param fechaMin
     * @param fechaMax
     */
    public void showDatePickerDialogReservation(TextView dateTextView, Context context, List<Reservation> reservas, long fechaMin, long fechaMax) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        // Verificar si la fecha seleccionada está dentro de algún rango de reserva existente
                        if (reservas != null) {
                            for (Reservation reserva : reservas) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    LocalDateTime checkin = LocalDateTime.parse(reserva.getCheckin());
                                    LocalDateTime checkout = LocalDateTime.parse(reserva.getCheckout());

                                    Instant checkinInstant = checkin.atZone(ZoneId.systemDefault()).toInstant();
                                    Instant checkoutInstant = checkout.atZone(ZoneId.systemDefault()).toInstant();

                                    long checkinMillis = checkinInstant.toEpochMilli();
                                    long checkoutMillis = checkoutInstant.toEpochMilli();

                                    if (selectedDate.getTimeInMillis() >= checkinMillis &&
                                            selectedDate.getTimeInMillis() <= checkoutMillis) {
                                        showAlert(context, "Aviso", "De la fecha "+ formatLocalDateTimeToStringFecha(checkin) +
                                                " a la fecha " + formatLocalDateTimeToStringFecha(checkout) + " no hay disponibilidad.");
                                        return;
                                    }
                                }
                            }
                        }

                        String selectedDateStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateTextView.setText(selectedDateStr);
                    }
                },
                year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(fechaMin);
        datePicker.setMaxDate(fechaMax);
        datePickerDialog.show();
    }

}
