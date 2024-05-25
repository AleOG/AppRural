package com.proyecto.apprural.views.owner.alta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.proyecto.apprural.databinding.DialogAddServiceBinding;
import com.proyecto.apprural.model.beans.Service;
import com.proyecto.apprural.utils.Util;
import java.math.BigDecimal;
import java.util.UUID;

public class AddServiceDialogFragment extends DialogFragment {

    DialogAddServiceBinding binding;

    private Util utils = new Util();

    private ServiceAddListener listener;

    public interface ServiceAddListener {
        void onServiceAdded(Service service);
    }


    public static AddServiceDialogFragment newInstance() {
        return new AddServiceDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.dialog_add_service, container, false);
        binding = DialogAddServiceBinding.inflate(inflater, container, false);
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
        String priceText = binding.editTextPrice.getText().toString().trim();
        Boolean included = binding.checkboxIncluded.isChecked();
        // Verificar si se ha introducido un valor en editTextName y editTextPrice
        if (name.isEmpty() || priceText.isEmpty()) {
            // Mostrar un mensaje de error o indicar al usuario que complete los campos
            utils.showAlert(getActivity(), "Error", "Por favor, complete los campos Nombre y Precio");
            return; // Salir del método sin guardar el servicio
        }

        // Convertir el precio a BigDecimal
        BigDecimal price = new BigDecimal(priceText);

        // Generar el ID del servicio
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        // Crear el objeto Service
        Service service = new Service(id, name, included, price);

        //Si el servicio está incluido, el precio es 0
        if(included) {
            service.setPrice(new BigDecimal(0));
        }

        // Notificar al listener
        if (listener != null) {
            listener.onServiceAdded(service);
        }

        // Cerrar el diálogo
        dismiss();
    }


    public void setServiceAddListener(ServiceAddListener listener) {
        this.listener = listener;
    }
}
