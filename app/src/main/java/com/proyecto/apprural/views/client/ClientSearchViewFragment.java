package com.proyecto.apprural.views.client;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.proyecto.apprural.databinding.ClientSearchViewFragmentBinding;
import com.proyecto.apprural.utils.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;


public class ClientSearchViewFragment extends Fragment {

    private ClientSearchViewFragmentBinding binding;
    private Util utils = new Util();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ClientSearchViewFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText minPrecio = binding.minPrice;
        EditText maxPrecio = binding.maxPrice;
        TextView arrivalDate = binding.arrivalDate;
        TextView departureDate = binding.departureDate;
        EditText destination = binding.destinationInput;
        EditText travelers = binding.travelersInput;

        binding.arrivalBtn.setOnClickListener(event -> {
            Calendar calendar = Calendar.getInstance();
            long fechaMin = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR, 5);
            long fechaMax = calendar.getTimeInMillis();

            if (!departureDate.getText().toString().trim().isEmpty()) {
                LocalDateTime fechaDeparture = utils.formatStringDateToLocalDateTime(departureDate.getText().toString().trim());
                if (fechaDeparture != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fechaMax = fechaDeparture.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    }
                }
            }

            utils.showDatePickerDialog(arrivalDate, getContext(), fechaMin, fechaMax);
        });

        binding.departureBtn.setOnClickListener(event -> {
            Calendar calendar = Calendar.getInstance();
            long fechaMin = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR, 5);
            long fechaMax = calendar.getTimeInMillis();

            String fechaArrivalString = arrivalDate.getText().toString().trim();

            if(!fechaArrivalString.isEmpty()) {
                LocalDateTime fechaArrival = utils.formatStringDateToLocalDateTime(fechaArrivalString);
                if(fechaArrival != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fechaMin = fechaArrival.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    }
                }
            }

            utils.showDatePickerDialog(departureDate, getContext(), fechaMin, fechaMax);
        });

        binding.resetButton.setOnClickListener(event -> {
            resetFields(minPrecio, maxPrecio, arrivalDate, departureDate, destination, travelers);
        });

        binding.searchButton.setOnClickListener(event ->{
            Bundle bundle = new Bundle();
            String minPrecioString = minPrecio.getText().toString().trim();
            String maxPrecioString = maxPrecio.getText().toString().trim();
            String arrivalDateString = arrivalDate.getText().toString().trim();
            String departureDateString = departureDate.getText().toString().trim();
            String destinationString = destination.getText().toString().trim();
            String travelersString = travelers.getText().toString().trim();

            double precioMinAunx = minPrecioString.isEmpty() ? 0 : Double.parseDouble(minPrecioString);
            double precioMaxAux = maxPrecioString.isEmpty() ? Double.POSITIVE_INFINITY : Double.parseDouble(maxPrecioString);

            if(arrivalDateString.isEmpty() && !departureDateString.isEmpty()) {
                utils.showAlert(getContext(), "Aviso", "Selecciona fecha de llegada.");
                return;
            }
            if(departureDateString.isEmpty() && !arrivalDateString.isEmpty()) {
                utils.showAlert(getContext(), "Aviso", "Selecciona fecha de salida.");
                return;
            }

            if(precioMinAunx>precioMaxAux) {
                utils.showAlert(getContext(), "Aviso", "El precio máximo debe ser superior al mínimo.");
                return;
            }

            bundle.putString("minPrecio", minPrecioString);
            bundle.putString("maxPrecio", maxPrecioString);
            bundle.putString("arrivalDate", arrivalDateString);
            bundle.putString("departureDate", departureDateString);
            bundle.putString("destination", destinationString);
            bundle.putString("travelers", travelersString);

            new ClientRouter().launch(getActivity(), bundle);
        });
    }


    /**
     * Función que resetea los campos de búsqueda de alojamientos
     *
     * @param minPrice
     * @param maxPrice
     * @param arrivalDate
     * @param departureDate
     * @param destinationInput
     * @param travelersInput
     */
    private void resetFields(EditText minPrice, EditText maxPrice, TextView arrivalDate, TextView departureDate, EditText destinationInput, EditText travelersInput) {
        minPrice.setText("");
        maxPrice.setText("");
        arrivalDate.setText("");
        departureDate.setText("");
        destinationInput.setText("");
        travelersInput.setText("");
    }
}