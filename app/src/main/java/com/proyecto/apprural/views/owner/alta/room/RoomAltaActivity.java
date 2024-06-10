package com.proyecto.apprural.views.owner.alta.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.proyecto.apprural.databinding.RoomAltaActivityBinding;
import com.proyecto.apprural.model.beans.Bed;
import com.proyecto.apprural.model.beans.Room;
import com.proyecto.apprural.model.beans.Service;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.owner.alta.AddServiceDialogFragment;
import com.proyecto.apprural.views.owner.alta.ServiceAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomAltaActivity extends AppCompatActivity implements
        ServiceAdapter.OnServiceActionListener,
        AddServiceDialogFragment.ServiceAddListener,
        BedAdapter.OnBedActionListener,
        AddBedDialogFragment.BedAddListener
{

    private RoomAltaActivityBinding binding;
    private RoomCategory[] categories = RoomCategory.values();
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private BedAdapter bedAdapter;
    private List<Bed> bedList;
    public static final String EXTRA_ROOM = "com.example.EXTRA_ROOM";

    private Util utils = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RoomAltaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serviceList = new ArrayList<>();
        bedList = new ArrayList<>();
        setCategorySelect();

        setupRecyclerView();

        binding.addServiceBtn.setOnClickListener(event -> showAddServiceDialog());

        binding.addBedBtn.setOnClickListener(event -> showAddBedDialog());

        binding.saveBtn.setOnClickListener(event -> {

            String roomName = binding.roomNameInput.getText().toString().trim();
            String roomCategory = binding.roomCategorySpinner.getSelectedItem().toString().trim();
            String priceText = binding.roomPrice.getText().toString().trim();
            if(roomName.isEmpty() || roomCategory.isEmpty() || priceText.isEmpty() || bedList.size() < 1) {
                utils.showAlert(this, "Error", "La habitación debe tener nombre, categorías, precio y al menos una cama.");
            }else {
                UUID uuid = UUID.randomUUID();
                String id = uuid.toString();
                double price = Double.parseDouble(priceText);

                Room newRoom = new Room(id,roomName, roomCategory, false, price, "Limpia", serviceList, bedList);
                Bundle bundle = new Bundle();
                bundle.putSerializable("room", newRoom);
                Intent roomAltaRouter = new RoomAltaRouter().intent(this, bundle);
                setResult(RESULT_OK, roomAltaRouter);
                finish();
            }
        });

        binding.exitBtn.setOnClickListener(event -> finish());
    }

    /**
     * Función que configura el selector para elegir la categoría de la habitación
     */
    private void setCategorySelect() {
        ArrayAdapter<RoomCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        binding.roomCategorySpinner.setAdapter(adapter);
    }

    /**
     * Función que inicializa y configura el recyclerView
     */
    private void setupRecyclerView() {
        serviceAdapter = new ServiceAdapter(serviceList, this);
        binding.servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.servicesRecyclerView.setAdapter(serviceAdapter);

        bedAdapter = new BedAdapter(bedList, this);
        binding.bedsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.bedsRecyclerView.setAdapter(bedAdapter);
    }

    /**
     * Función que configura el popup dialog para añadir servicios
     */
    private void showAddServiceDialog() {
        AddServiceDialogFragment dialogFragment = AddServiceDialogFragment.newInstance();
        dialogFragment.setServiceAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddServiceDialogFragment");
    }

    /**
     * Función que configura el popup dialog para añadir camas
     */
    private void showAddBedDialog() {
        AddBedDialogFragment dialogFragment = AddBedDialogFragment.newInstance();
        dialogFragment.setBedAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddBedDialogFragment");
    }

    @Override
    public void onEditService(Service service) {

    }

    /**
     * Función que elimina un servicio del recycleview de servicios
     *
     * @param service
     */
    @Override
    public void onRemoveService(Service service) {
        serviceList.remove(service);
        serviceAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Servicio eliminado", Toast.LENGTH_SHORT).show();
    }

    /**
     * Función que añade un servicio al recycleview de servicios
     *
     * @param service
     */
    @Override
    public void onServiceAdded(Service service) {
        serviceList.add(service);
        serviceAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Servicio añadido", Toast.LENGTH_SHORT).show();
    }

    /**
     * Función que añade una cama al recycleview de camas
     *
     * @param bed
     */
    @Override
    public void onBedAdded(Bed bed) {
        Log.e("Cambia", bed.toString());
        bedList.add(bed);
        bedAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Cama añadida", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditBed(Bed bed) {

    }

    /**
     * Función que elimina una cama del recycleview de camas
     *
     * @param bed
     */
    @Override
    public void onRemoveBed(Bed bed) {
        bedList.remove(bed);
        bedAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Cama eliminada", Toast.LENGTH_SHORT).show();
    }
}