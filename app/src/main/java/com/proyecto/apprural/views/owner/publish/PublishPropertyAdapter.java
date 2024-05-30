package com.proyecto.apprural.views.owner.publish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.PropertyPublishItemBinding;
import com.proyecto.apprural.model.beans.Property;

import java.util.List;

public class PublishPropertyAdapter extends RecyclerView.Adapter<PublishPropertyAdapter.PublishPropertyViewHolder> {

    private List<Property> propertyList;
    private LayoutInflater layoutInflater;
    private OnPublishPropertyActionListener propertyActionListener;

    public PublishPropertyAdapter(List<Property> propertyList, OnPublishPropertyActionListener propertyActionListener) {
        this.propertyList = propertyList;
        this.propertyActionListener = propertyActionListener;
    }

    @NonNull
    @Override
    public PublishPropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PropertyPublishItemBinding propertyPublishItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.property_publish_item, parent, false);
        return new PublishPropertyViewHolder(propertyPublishItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishPropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.bind(property);
        holder.binding.setItemClickListener(propertyActionListener);
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class PublishPropertyViewHolder extends RecyclerView.ViewHolder {

        public final PropertyPublishItemBinding binding;

        public PublishPropertyViewHolder(PropertyPublishItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Property property) {
            binding.setProperty(property);
            binding.executePendingBindings();
        }

    }
    public interface OnPublishPropertyActionListener {
        void onPublishProperty(Property property);
        void onNoPublishProperty(Property property);
    }
}
