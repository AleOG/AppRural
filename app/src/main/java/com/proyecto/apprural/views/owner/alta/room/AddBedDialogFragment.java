package com.proyecto.apprural.views.owner.alta.room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.proyecto.apprural.databinding.DialogAddBedBinding;
import com.proyecto.apprural.model.beans.Bed;
import com.proyecto.apprural.utils.Util;

public class AddBedDialogFragment extends DialogFragment {
    DialogAddBedBinding binding;
    private Util utils = new Util();
    private BedAddListener listener;

    BedCategory[] bedCategories = BedCategory.values();

    public interface BedAddListener {
        void onBedAdded(Bed bed);
    }

    public static AddBedDialogFragment newInstance() {
        return new AddBedDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DialogAddBedBinding.inflate(inflater, container, false);
        setCategorySelect();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSave.setOnClickListener(v -> onSaveClicked());
        binding.buttonCancel.setOnClickListener(v -> dismiss());
    }

    private void setCategorySelect() {
        ArrayAdapter<BedCategory> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bedCategories);
        binding.spinnerBedCategory.setAdapter(adapter);
    }

    private void onSaveClicked() {

        String category = String.valueOf(binding.spinnerBedCategory.getSelectedItem()).trim();
        String quantityText = binding.bedQuantity.getText().toString().trim();

        if(category.isEmpty() || quantityText.isEmpty() ) {
            utils.showAlert(getActivity(), "Error", "Por favor, complete los campos Categoría y Cantidad");
            return;
        }

        int quantity = Integer.parseInt(quantityText);

        Bed bed = new Bed(category, quantity);

        if (listener != null) {
            Log.e("Entra", bed.toString());
            listener.onBedAdded(bed);
        }

        dismiss();
    }

    public void setBedAddListener(BedAddListener listener) {
        this.listener = listener;
    }

}
