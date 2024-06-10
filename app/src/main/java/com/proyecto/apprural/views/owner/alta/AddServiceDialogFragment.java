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
            utils.showAlert(getActivity(), "Error", "Por favor, complete los campos Nombre y Precio");
            return;
        }

        double price = Double.parseDouble(priceText);

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        Service service = new Service(id, name, included, price);

        if(included) {
            service.setPrice(0);
        }

        if (listener != null) {
            listener.onServiceAdded(service);
        }

        dismiss();
    }


    public void setServiceAddListener(ServiceAddListener listener) {
        this.listener = listener;
    }
}
