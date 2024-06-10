package com.proyecto.apprural.views.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.apprural.databinding.PropertyValidItemBinding;
import com.proyecto.apprural.model.beans.Property;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>{

    private List<Property> propertyList;
    private LayoutInflater layoutInflater;
    private OnPropertyActionListener propertyActionListener;

    public PropertyAdapter(List<Property> propertyList, OnPropertyActionListener propertyActionListener) {
        this.propertyList = propertyList;
        this.propertyActionListener = propertyActionListener;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PropertyValidItemBinding binding = PropertyValidItemBinding.inflate(layoutInflater, parent, false);
        return new PropertyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.bind(property);
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder {

        private final PropertyValidItemBinding binding;

        public PropertyViewHolder(PropertyValidItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Property property) {
            binding.setProperty(property);
            binding.executePendingBindings();

            binding.btnView.setOnClickListener(event -> {
                if(propertyActionListener != null) {
                    propertyActionListener.onViewProperty(property);
                }
            });
            binding.btnAccept.setOnClickListener(event -> {
                if(propertyActionListener != null) {
                    propertyActionListener.onAcceptProperty(property);
                }
            });
            binding.btnCancel.setOnClickListener(event -> {
                if(propertyActionListener != null) {
                    propertyActionListener.onBlockProperty(property);
                }
            });
        }

    }

    public interface OnPropertyActionListener {

        void onViewProperty(Property property);
        void onAcceptProperty(Property property);
        void onBlockProperty(Property property);
    }
}
