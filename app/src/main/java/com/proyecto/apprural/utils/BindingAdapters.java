package com.proyecto.apprural.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.proyecto.apprural.model.beans.Property;

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

    @BindingAdapter("location")
    public static void setLocation(TextView textView, Property property) {
        textView.setText(property.getAddress() +", "+property.getTown() + ", "+property.getCountry());
    }

    @BindingAdapter("capacity")
    public static void setCapacity(TextView textView, int capacity) {
        if(capacity > 1) {
            textView.setText(String.valueOf(capacity)+ " personas");
        }else {
            textView.setText(String.valueOf(capacity)+ " persona");
        }
    }
}
