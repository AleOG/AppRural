package com.proyecto.apprural.views.owner.publish;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.apprural.databinding.PropertyPublishItemBinding;
import com.proyecto.apprural.model.beans.Property;

import java.util.List;

public class PublishPropertyAdapter extends RecyclerView.Adapter<PublishPropertyAdapter.PublishPropertyViewHolder> {

    private List<Property> propertyList;
    private LayoutInflater layoutInflater;
    private OnPublishPropertyActionListener onPublishPropertyActionListener;

    public PublishPropertyAdapter(List<Property> propertyList, OnPublishPropertyActionListener onPublishPropertyActionListener) {
        this.propertyList = propertyList;
        this.onPublishPropertyActionListener = onPublishPropertyActionListener;
    }

    @NonNull
    @Override
    public PublishPropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PropertyPublishItemBinding propertyPublishItemBinding = PropertyPublishItemBinding.inflate(layoutInflater, parent, false);
        return new PublishPropertyViewHolder(propertyPublishItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishPropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.bind(property);
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

            binding.btnAccept.setOnClickListener(event -> {
                onPublishPropertyActionListener.onPublish(property);
            });

            binding.btnCancel.setOnClickListener(event -> {
                onPublishPropertyActionListener.onTakingOut(property);
            });
        }

    }
    public interface OnPublishPropertyActionListener {
        void onPublish(Property property);
        void onTakingOut(Property property);
    }

}
