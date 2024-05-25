package com.proyecto.apprural.utils;

import android.app.AlertDialog;
import android.content.Context;

public class Util {
    public void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Cerrar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
