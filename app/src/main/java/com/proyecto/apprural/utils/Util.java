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
    public void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Cerrar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String generateID(String email) {
        int code = email.hashCode();
        String id = String.valueOf(code);
        return id;
    }

    public LocalDateTime formatStringDateToLocalDateTime(String fecha) {
        LocalDateTime dateTime = null;
        String[] parts = fecha.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.of(year,month,day,0,0,0,0);
        }
        //Log.e("fecha", dateTime.toString());
        return dateTime;
    }

    public String formatLocalDateTimeToStringFecha(LocalDateTime fecha) {
        // Define el formato deseado
        DateTimeFormatter formatter;
        String fechaFormateada = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Formatea la fecha con el formato definido
            fechaFormateada = fecha.format(formatter);
        }


        // Devuelve la fecha formateada
        return fechaFormateada;
    }

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

                                    // Convertir LocalDateTime a Instant
                                    Instant checkinInstant = checkin.atZone(ZoneId.systemDefault()).toInstant();
                                    Instant checkoutInstant = checkout.atZone(ZoneId.systemDefault()).toInstant();

                                    // Obtener el tiempo en milisegundos
                                    long checkinMillis = checkinInstant.toEpochMilli();
                                    long checkoutMillis = checkoutInstant.toEpochMilli();

                                    if (selectedDate.getTimeInMillis() >= checkinMillis &&
                                            selectedDate.getTimeInMillis() <= checkoutMillis) {
                                        // La fecha seleccionada está dentro de un rango de reserva existente
                                        showAlert(context, "Aviso", "De la fecha "+ formatLocalDateTimeToStringFecha(checkin) +
                                                " a la fecha " + formatLocalDateTimeToStringFecha(checkout) + " no hay disponibilidad.");
                                        return;
                                    }
                                }
                            }
                        }

                        // Si la fecha no está dentro de ningún rango de reserva, establecerla en el TextView
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



    public static void showDatePickerDialog2(TextView textView, Context context, long fechaMin, long fechaMax) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    textView.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(fechaMin);
        datePickerDialog.getDatePicker().setMaxDate(fechaMax);
        datePickerDialog.show();
    }

    public Timestamp formatStringDateToTimestamp(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            Date date = formatter.parse(dateString);
            return new Timestamp(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
