package com.proyecto.apprural.views.client;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.apprural.databinding.SearchResultItemBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

    private List<FullAccommodationOffer> resultsList;
    private LayoutInflater layoutInflater;

    private OnClickListener onClickListener;

    public SearchResultsAdapter(List<FullAccommodationOffer> resultsList, OnClickListener onClickListener) {
        this.resultsList = resultsList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        SearchResultItemBinding binding = SearchResultItemBinding.inflate(layoutInflater, parent, false);
        return new SearchResultsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {
        FullAccommodationOffer fullAccommodationOffer = resultsList.get(position);
        holder.bind(fullAccommodationOffer);
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder {

        public final SearchResultItemBinding binding;

        public SearchResultsViewHolder(SearchResultItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FullAccommodationOffer offer) {
            binding.setOffer(offer);
            binding.executePendingBindings();

            binding.getRoot().setOnClickListener(event -> {
                onClickListener.onClick(offer);
            });
        }
    }

    public interface OnClickListener {
        void onClick(FullAccommodationOffer offer);
    }
}
