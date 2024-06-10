package com.proyecto.apprural.views.owner.alta;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proyecto.apprural.databinding.ServiceItemBinding;
import com.proyecto.apprural.model.beans.Service;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> services;
    private LayoutInflater layoutInflater;
    private OnServiceActionListener serviceActionListener;

    public ServiceAdapter(List<Service> services, OnServiceActionListener serviceActionListener) {
        this.services = services;
        this.serviceActionListener = serviceActionListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ServiceItemBinding binding = ServiceItemBinding.inflate(layoutInflater, parent, false);
        return new ServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        private final ServiceItemBinding binding;

        public ServiceViewHolder(ServiceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Service service) {
            binding.setService(service);
            binding.executePendingBindings();


            binding.removeButton.setOnClickListener(v -> {
                if (serviceActionListener != null) {
                    serviceActionListener.onRemoveService(service);
                }
            });
        }
    }

    public interface OnServiceActionListener {
        void onEditService(Service service);
        void onRemoveService(Service service);
    }
}
