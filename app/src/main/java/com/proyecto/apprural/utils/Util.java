package com.proyecto.apprural.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.Calendar;

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
        Log.e("fecha", dateTime.toString());
        return dateTime;
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
}
