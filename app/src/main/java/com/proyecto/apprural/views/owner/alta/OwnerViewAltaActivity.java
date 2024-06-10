package com.proyecto.apprural.views.owner.alta;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.OwnerViewAltaActivityBinding;
import com.proyecto.apprural.model.beans.Prohibition;
import com.proyecto.apprural.model.beans.Property;
import com.proyecto.apprural.model.beans.Room;
import com.proyecto.apprural.model.beans.Service;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.owner.alta.room.RoomAltaRouter;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
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

    private StorageReference storageReference;
    private Uri image;
    private ShapeableImageView foto_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OwnerViewAltaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serviceList = new ArrayList<>();
        prohibitionList = new ArrayList<>();
        roomList = new ArrayList<>();
        foto_usuario = binding.imagenUsuario;
        storageReference = FirebaseStorage.getInstance().getReference();

        setupRecyclerView();

        session();

        binding.addServiceBtn.setOnClickListener(v -> showAddServiceDialog());

        binding.addProhibitionBtn.setOnClickListener(event -> showAddProhibitionDialog());

        binding.addRoomBtn.setOnClickListener(event -> {
            Intent intent = new RoomAltaRouter().intent(this, null);
            addRoomLauncher.launch(intent);
        });

        binding.saveBtn.setOnClickListener(event -> {
            saveRoom();
        });

        binding.exitBtn.setOnClickListener( event -> {
            finish();
        });

        binding.fotoBtn.setOnClickListener(event -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });
    }

    /**
     * Función que recupera de SharePreferences el email del usuario
     */
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

    /**
     * Acción que añade una imagen al formulario de alta de la propiedad
     */
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                if(result.getData() != null) {
                    image = result.getData().getData();
                    ViewGroup.LayoutParams params = foto_usuario.getLayoutParams();
                    params.width = 800;
                    params.height = 800;
                    foto_usuario.setLayoutParams(params);
                    Glide.with(getApplicationContext()).load(image).into(foto_usuario);
                }
            } else {
                //utils.showAlert(getApplicationContext(), "Aviso", "Selecciona una imagen por favor.");
            }
        }
    });


    /**
     * Función que guarda la imagen en base de datos
     *
     * @param id
     * @param image
     */
    public void uploadImage(String id, Uri image) {
        String imageId = UUID.randomUUID().toString();
        StorageReference reference = storageReference.child("images").child(id).child(imageId);
        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("exito", "La foto ha sido guardada con éxito.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error", "Ha fallado el guardado de la foto.");
            }
        });
    }


    /**
     * Función que asigna los valores a un objeto propiedad y lo guarda en base de datos
     */
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

        uploadImage(propertyId, image);
    }

    /**
     * Acción que se ejecuta cuando se añade una nueva habitación a la propiedad
     */
    ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null) {
                            Room newRoom;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                newRoom = data.getSerializableExtra("room", Room.class);
                            } else {
                                newRoom = (Room) data.getSerializableExtra("room");
                            }

                            if (newRoom != null) {
                                Log.d("OwnerViewAltaActivity", "Received room: " + newRoom);
                                roomList.add(newRoom);
                                roomAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Habitación añadida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
    );

    /**
     * Función que inicializa y configura el recyclerview de la actividad
     */
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

    /**
     * Función que configura el popup dialog para añadir servicios
     */
    private void showAddServiceDialog() {
        AddServiceDialogFragment dialogFragment = AddServiceDialogFragment.newInstance();
        dialogFragment.setServiceAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddServiceDialogFragment");
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
     * Función que configura el popup dialog para añadir prohibiciones
     */
    private void showAddProhibitionDialog() {
        AddProhibitionDialogFragment dialogFragment = AddProhibitionDialogFragment.newInstance();
        dialogFragment.setProhibitionAddListener(this);
        dialogFragment.show(getSupportFragmentManager(), "AddProhibitionDialogFragment");
    }

    /**
     * Función que añade una prohibición al recycleview de prohibiciones
     *
     * @param prohibition
     */
    @Override
    public void onProhibitionAdded(Prohibition prohibition) {
        prohibitionList.add(prohibition);
        prohibitionAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Prohibición añadida", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onEditProhibition(Prohibition prohibition) {

    }

    /**
     * Función que elimina una prohibición del recycleview de prohibiciones
     *
     * @param prohibition
     */
    @Override
    public void onRemoveProhibition(Prohibition prohibition) {
        prohibitionList.remove(prohibition);
        prohibitionAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Prohibicion eliminada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditRoom(Room room) {

    }

    /**
     * Función que elimina una habitación del recycleview de habitaciones
     *
     * @param room
     */
    @Override
    public void onRemoveRoom(Room room) {
        roomList.remove(room);
        roomAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Habitación eliminada", Toast.LENGTH_SHORT).show();
    }
}