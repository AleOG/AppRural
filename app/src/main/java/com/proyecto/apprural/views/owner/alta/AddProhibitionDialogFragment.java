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

        //return inflater.inflate(R.layout.dialog_add_service, container, false);
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
        // Verificar si se ha introducido un valor en editTextName y editTextPrice
        if (name.isEmpty()) {
            // Mostrar un mensaje de error o indicar al usuario que complete los campos
            utils.showAlert(getActivity(), "Error", "Por favor, complete el campo Nombre");
            return; // Salir del método sin guardar el servicio
        }

        // Generar el ID del servicio
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        // Crear el objeto Prohibicion
        Prohibition prohibition = new Prohibition(id, name);


        // Notificar al listener
        if (listener != null) {
            listener.onProhibitionAdded(prohibition);
        }

        // Cerrar el diálogo
        dismiss();
    }


    public void setProhibitionAddListener(AddProhibitionDialogFragment.ProhibitionAddListener listener) {
        this.listener = listener;
    }
}
