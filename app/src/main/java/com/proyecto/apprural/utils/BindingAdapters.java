package com.proyecto.apprural.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.math.BigDecimal;

public class BindingAdapters {
    @BindingAdapter("priceWithCurrency")
    public static void setPriceWithCurrency(TextView textView, BigDecimal price) {
        textView.setText(String.format("%.2f €", price));
    }
}
