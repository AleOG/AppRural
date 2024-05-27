package com.proyecto.apprural.views.owner.alta.room;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proyecto.apprural.databinding.BedItemBinding;
import com.proyecto.apprural.model.beans.Bed;

import java.util.List;

public class BedAdapter extends RecyclerView.Adapter<BedAdapter.BedViewHoler> {

    private List<Bed> beds;
    private LayoutInflater layoutInflater;
    private OnBedActionListener bedActionListener;

    public BedAdapter(List<Bed> beds, OnBedActionListener bedActionListener) {
        this.beds = beds;
        this.bedActionListener = bedActionListener;
    }

    @NonNull
    @Override
    public BedViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        BedItemBinding binding = BedItemBinding.inflate(layoutInflater, parent, false);
        return new BedViewHoler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BedViewHoler holder, int position) {
        Bed bed = beds.get(position);
        holder.bind(bed);
    }

    @Override
    public int getItemCount() {
        return beds.size();
    }

    public class BedViewHoler extends RecyclerView.ViewHolder {

        private final BedItemBinding binding;

        public BedViewHoler(BedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Bed bed) {
            binding.setBed(bed);
            binding.executePendingBindings();

            binding.removeButton.setOnClickListener(event -> {
                if(bedActionListener != null) {
                    bedActionListener.onRemoveBed(bed);
                }
            });
        }
    }

    public interface OnBedActionListener {
        void onEditBed(Bed bed);
        void onRemoveBed(Bed bed);
    }
}
