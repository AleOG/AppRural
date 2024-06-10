package com.proyecto.apprural.views.owner.alta;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proyecto.apprural.databinding.ProhibitionItemBinding;
import com.proyecto.apprural.model.beans.Prohibition;
import java.util.List;

public class ProhibitionAdapter extends RecyclerView.Adapter<ProhibitionAdapter.ProhibitionViewHolder> {

    private List<Prohibition> prohibitions;
    private LayoutInflater layoutInflater;

    private OnProhibitionActionListener prohibitionActionListener;

    public ProhibitionAdapter(List<Prohibition> prohibitions, OnProhibitionActionListener prohibitionActionListener) {
        this.prohibitions = prohibitions;
        this.prohibitionActionListener = prohibitionActionListener;
    }

    @NonNull
    @Override
    public ProhibitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ProhibitionItemBinding binding = ProhibitionItemBinding.inflate(layoutInflater, parent, false);
        return new ProhibitionAdapter.ProhibitionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProhibitionViewHolder holder, int position) {
        Prohibition prohibition = prohibitions.get(position);
        holder.bind(prohibition);
    }

    @Override
    public int getItemCount() {
        return prohibitions.size();
    }

    public class ProhibitionViewHolder extends RecyclerView.ViewHolder {

        private final ProhibitionItemBinding binding;

        public ProhibitionViewHolder(ProhibitionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Prohibition prohibition) {
            binding.setProhibition(prohibition);
            binding.executePendingBindings();


            binding.removeButton.setOnClickListener(v -> {
                if(prohibitionActionListener != null) {
                    prohibitionActionListener.onRemoveProhibition(prohibition);
                }
            });
        }
    }

    public interface OnProhibitionActionListener {
        void onEditProhibition(Prohibition prohibition);
        void onRemoveProhibition(Prohibition prohibition);
    }
}
