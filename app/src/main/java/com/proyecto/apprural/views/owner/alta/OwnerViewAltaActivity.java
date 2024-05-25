package com.proyecto.apprural.views.owner.alta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.proyecto.apprural.databinding.OwnerViewAltaActivityBinding;
import com.proyecto.apprural.model.beans.Prohibition;
import com.proyecto.apprural.model.beans.Service;

import java.util.ArrayList;
import java.util.List;

public class OwnerViewAltaActivity extends AppCompatActivity implements
        AddServiceDialogFragment.ServiceAddListener,
        AddProhibitionDialogFragment.ProhibitionAddListener,
        ServiceAdapter.OnServiceActionListener,
        ProhibitionAdapter.OnProhibitionActionListener
{

    private OwnerViewAltaActivityBinding binding;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private ProhibitionAdapter prohibitionAdapter;
    private List<Prohibition> prohibitionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OwnerViewAltaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar la lista de servicios
        serviceList = new ArrayList<>();
        prohibitionList = new ArrayList<>();
        // Configurar el RecyclerView
        setupRecyclerView();

        // Configurar el botón para añadir servicio
        binding.addServiceBtn.setOnClickListener(v -> showAddServiceDialog());

        // Configurar el botón para añadir prohibicion
        binding.addProhibitionBtn.setOnClickListener(event -> showAddProhibitionDialog());

        // Configurar el botón para añadir una habitación

    }

    private void setupRecyclerView() {
        serviceAdapter = new ServiceAdapter(serviceList, this);
        binding.servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.servicesRecyclerView.setAdapter(serviceAdapter);

        prohibitionAdapter = new ProhibitionAdapter(prohibitionList, this);
        binding.prohibitionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.prohibitionsRecyclerView.setAdapter(prohibitionAdapter);
    }

    private void showAddServiceDialog() {
        AddServiceDialogFragment dialogFragment = AddServiceDialogFragment.newInstance();
        dialogFragment.setServiceAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddServiceDialogFragment");
    }

    @Override
    public void onServiceAdded(Service service) {
        Log.e("Servicio", service.toString());
        serviceList.add(service);
        serviceAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Servicio añadido", Toast.LENGTH_SHORT).show();
    }

    private void showAddProhibitionDialog() {
        AddProhibitionDialogFragment dialogFragment = AddProhibitionDialogFragment.newInstance();
        dialogFragment.setProhibitionAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddProhibitionDialogFragment");
    }

    @Override
    public void onProhibitionAdded(Prohibition prohibition) {
        prohibitionList.add(prohibition);
        prohibitionAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Prohibición añadida", Toast.LENGTH_SHORT).show();
    }

    //Aplicar la lógica para editar un servicio
    @Override
    public void onEditService(Service service) {

    }

    @Override
    public void onRemoveService(Service service) {
        serviceList.remove(service);
        serviceAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Servicio eliminado", Toast.LENGTH_SHORT).show();
    }

    //Aplicar la lógica para editar una prohibicion
    @Override
    public void onEditProhibition(Prohibition prohibition) {

    }

    @Override
    public void onRemoveProhibition(Prohibition prohibition) {
        prohibitionList.remove(prohibition);
        prohibitionAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Prohibicion eliminada", Toast.LENGTH_SHORT).show();
    }
}