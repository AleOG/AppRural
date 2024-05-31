package com.proyecto.apprural.views.owner.alta;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.OwnerViewAltaActivityBinding;
import com.proyecto.apprural.model.beans.Prohibition;
import com.proyecto.apprural.model.beans.Property;
import com.proyecto.apprural.model.beans.Room;
import com.proyecto.apprural.model.beans.Service;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.owner.alta.room.RoomAltaRouter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwnerViewAltaActivity extends AppCompatActivity implements
        AddServiceDialogFragment.ServiceAddListener,
        AddProhibitionDialogFragment.ProhibitionAddListener,
        ServiceAdapter.OnServiceActionListener,
        ProhibitionAdapter.OnProhibitionActionListener,
        RoomAdapter.OnRoomActionListener
{

    private OwnerViewAltaActivityBinding binding;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    private ProhibitionAdapter prohibitionAdapter;
    private List<Prohibition> prohibitionList;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private Util utils = new Util();
    private String emailSession;
    private SharedPreferences misDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OwnerViewAltaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar la listas
        serviceList = new ArrayList<>();
        prohibitionList = new ArrayList<>();
        roomList = new ArrayList<>();

        // Configurar el RecyclerView
        setupRecyclerView();

        //obtener email en la sesion
        session();

        // Configurar el botón para añadir servicio
        binding.addServiceBtn.setOnClickListener(v -> showAddServiceDialog());

        // Configurar el botón para añadir prohibicion
        binding.addProhibitionBtn.setOnClickListener(event -> showAddProhibitionDialog());

        // Configurar el botón para añadir una habitación
        binding.addRoomBtn.setOnClickListener(event -> {
            //new RoomAltaRouter().launch(this);
            Intent intent = new RoomAltaRouter().intent(this, null);
            addRoomLauncher.launch(intent);
        });

        //Configurar el botón de guardar propiedad
        binding.saveBtn.setOnClickListener(event -> saveRoom());

        //Configurar el botón de salir
        binding.exitBtn.setOnClickListener( event -> {
            //getPropertiesForOwner("ownerId");
            finish();
        });
    }

    private void session() {
        misDatos = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = misDatos.getString("email", null);

        if(email == null) {
            finish();
        }
        emailSession = email;
        Log.e("email session ", emailSession);
        Toast.makeText(getApplicationContext(), "email session " + emailSession, Toast.LENGTH_SHORT).show();

    }

    private void getPropertiesForOwner(String ownerId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Obtener la referencia al nodo del propietario
        DatabaseReference ownerRef = database.child("properties").child(ownerId).child("propertiesOwner");

        // Agregar un ValueEventListener para escuchar los cambios en las propiedades
        ownerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpiar la lista de propiedades existentes
                List<Property> properties = new ArrayList<>();

                // Iterar a través de los hijos del nodo de propiedades
                for (DataSnapshot propertySnapshot : dataSnapshot.getChildren()) {
                    // Obtener la propiedad actual del DataSnapshot
                    Property property = propertySnapshot.getValue(Property.class);
                    // Agregar la propiedad a la lista
                    Log.e("property", property.toString());
                    properties.add(property);
                }

                // Aquí tienes la lista de propiedades del propietario
                // Puedes hacer lo que necesites con esta lista (por ejemplo, mostrarla en una lista RecyclerView)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar cualquier error que ocurra al recuperar los datos
                Log.e("eror", "Error al recuperar las propiedades del propietario: " + databaseError.getMessage());
            }
        });
    }


    private void saveRoom() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        String nameProperty = binding.nameInput.getText().toString().trim();
        String addressProperty = binding.addressInput.getText().toString().trim();
        String priceProperty = binding.priceProperty.getText().toString().trim();
        String townProperty = binding.townInput.getText().toString().trim();
        String countryProperty = binding.countryInput.getText().toString().trim();
        String capacityProperty = binding.capacityProperty.getText().toString().trim();

        if(nameProperty.isEmpty() || addressProperty.isEmpty() ||
                priceProperty.isEmpty() || roomList.size() < 1 ||
                townProperty.isEmpty() || countryProperty.isEmpty() ||
                capacityProperty.isEmpty()) {
            utils.showAlert(this, "Error", "La propiedad debe tener nombre, dirección, ciudad o pueblo, capacidad, precio y al menos una habitación.");
            return;
        }
        // Generar el ID del servicio
        UUID uuid = UUID.randomUUID();
        String propertyId = uuid.toString();

        String ownerId = utils.generateID(emailSession);

        double price = Double.parseDouble(priceProperty);
        int capacity = Integer.parseInt(capacityProperty);
        Property property = new Property(propertyId, nameProperty, addressProperty, townProperty, countryProperty, null, false, false, price, serviceList, prohibitionList, roomList, ownerId, capacity);

        DatabaseReference ownerRef = database.child("properties").child(ownerId);

        ownerRef.child("propertiesOwner").child(propertyId).setValue(property)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        utils.showAlert(OwnerViewAltaActivity.this, "Exito", "Propiedad guardada con éxito.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        utils.showAlert(OwnerViewAltaActivity.this, "Error", "Fallo al guardar la propiedad.");
                    }
                });

        DatabaseReference adminRef = database.child("propertiesValidation").child(ownerId);

        adminRef.child("propertiesOwner").child(propertyId).setValue(property)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //utils.showAlert(OwnerViewAltaActivity.this, "Exito", "Propiedad guardada con éxito.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //utils.showAlert(OwnerViewAltaActivity.this, "Error", "Fallo al guardar la propiedad.");
                    }
                });
    }

    ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null) {
                            Room newRoom = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                newRoom = data.getSerializableExtra("room", Room.class);
                            }
                            if (newRoom != null) {
                                // Procesar el nuevo objeto Room
                                Log.d("OwnerViewAltaActivity", "Received room: " + newRoom);
                                // Agregar la lógica necesaria para utilizar el objeto Room
                                roomList.add(newRoom);
                                roomAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Habitación añadida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
    );

    private void setupRecyclerView() {
        serviceAdapter = new ServiceAdapter(serviceList, this);
        binding.servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.servicesRecyclerView.setAdapter(serviceAdapter);

        prohibitionAdapter = new ProhibitionAdapter(prohibitionList, this);
        binding.prohibitionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.prohibitionsRecyclerView.setAdapter(prohibitionAdapter);

        roomAdapter = new RoomAdapter(roomList, this);
        binding.roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.roomsRecyclerView.setAdapter(roomAdapter);
    }

    private void showAddServiceDialog() {
        AddServiceDialogFragment dialogFragment = AddServiceDialogFragment.newInstance();
        dialogFragment.setServiceAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddServiceDialogFragment");
    }

    @Override
    public void onServiceAdded(Service service) {
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

    @Override
    public void onEditRoom(Room room) {

    }

    @Override
    public void onRemoveRoom(Room room) {
        roomList.remove(room);
        roomAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Habitación eliminada", Toast.LENGTH_SHORT).show();
    }
}