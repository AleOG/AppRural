package com.proyecto.apprural.views.owner.alta;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.apprural.databinding.RoomItemBinding;
import com.proyecto.apprural.model.beans.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{

    private List<Room> rooms;
    private LayoutInflater layoutInflater;
    private OnRoomActionListener roomActionListener;

    public RoomAdapter(List<Room> rooms, OnRoomActionListener roomActionListener) {
        this.rooms = rooms;
        this.roomActionListener = roomActionListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RoomItemBinding binding = RoomItemBinding.inflate(layoutInflater, parent, false);
        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {

        private final RoomItemBinding binding;

        public RoomViewHolder(RoomItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Room room) {
            binding.setRoom(room);
            binding.executePendingBindings();

            binding.removeButton.setOnClickListener(event -> {
                if (roomActionListener != null) {
                    roomActionListener.onRemoveRoom(room);
                }
            });
        }
    }

    public interface OnRoomActionListener {
        void onEditRoom(Room room);
        void onRemoveRoom(Room room);
    }
}
