package com.proyecto.apprural.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("priceWithCurrency")
    public static void setPriceWithCurrency(TextView textView, double price) {
        textView.setText(String.format("%.2f €", price));
    }

    @BindingAdapter("quantityBeds")
    public static void setQuantityBeds(TextView textView, int quantity) {
        if(quantity > 1) {
            textView.setText(String.valueOf(quantity)+ " unidades");
        }else {
            textView.setText(String.valueOf(quantity)+ " unidad");
        }
    }
}
