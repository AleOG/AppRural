package com.proyecto.apprural.views.owner.alta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.proyecto.apprural.databinding.DialogAddProhibitionBinding;
import com.proyecto.apprural.model.beans.Prohibition;
import com.proyecto.apprural.utils.Util;
import java.util.UUID;

public class AddProhibitionDialogFragment extends DialogFragment {
    DialogAddProhibitionBinding binding;

    private Util utils = new Util();

    private ProhibitionAddListener listener;

    public interface ProhibitionAddListener {
        void onProhibitionAdded(Prohibition prohibition);
    }

    public static AddProhibitionDialogFragment newInstance() {
        return new AddProhibitionDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DialogAddProhibitionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSave.setOnClickListener(v -> onSaveClicked());
        binding.buttonCancel.setOnClickListener(v -> dismiss());
    }

    private void onSaveClicked() {
        String name = binding.editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            utils.showAlert(getActivity(), "Error", "Por favor, complete el campo Nombre");
            return;
        }

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        Prohibition prohibition = new Prohibition(id, name);

        if (listener != null) {
            listener.onProhibitionAdded(prohibition);
        }

        dismiss();
    }


    public void setProhibitionAddListener(AddProhibitionDialogFragment.ProhibitionAddListener listener) {
        this.listener = listener;
    }
}
