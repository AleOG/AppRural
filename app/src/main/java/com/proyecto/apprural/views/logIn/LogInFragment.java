package com.proyecto.apprural.views.logIn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.LoginFragmentBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.model.beans.User;
import com.proyecto.apprural.utils.Util;
import com.proyecto.apprural.views.admin.AdminRouter;
import com.proyecto.apprural.views.client.reservation.ReservationActivityRouter;
import com.proyecto.apprural.views.owner.OwnerRouter;
import com.proyecto.apprural.views.registration.RegistrationRouter;

public class LogInFragment extends Fragment {

    private LoginFragmentBinding binding;
    private static final String TAG = "GoogleActivity";
    private SharedPreferences.Editor miEditor;
    private SharedPreferences misDatos;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    private String currentView;

    private FullAccommodationOffer offer;
    private Util utils = new Util();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle args = getArguments();
        if (args != null) {
            currentView = args.getString("currentView");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                offer = args.getSerializable("offer", FullAccommodationOffer.class);
            } else {
                offer = (FullAccommodationOffer) args.getSerializable("offer");
            }
            User user = new User();
            //Se recupera el tipo de rol del usuario
            if(currentView.equals("admin")) {
                user.setRole("admin");
            }else if(currentView.equals("owner")) {
                user.setRole("owner");
            }else {
                user.setRole("guest");
            }
            binding.setUser(user);
            binding.executePendingBindings();
            Toast.makeText(getActivity(), "Emisor: " + currentView, Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }


    /**
     * Función que inicializa y configura los elementos de la actividad
     */
    private void setup() {
    Bundle extras = new Bundle();

        binding.loginBtn.setOnClickListener(event -> {
            String email = binding.emailEdtText.getText().toString().trim();
            String password = binding.passEdtText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                String userId = utils.generateID(email);
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String roleUser = snapshot.child("role").getValue().toString();

                            loginWithEmailAndPassword(email, roleUser, password);

                        } else {
                            utils.showAlert(getActivity(), "Error", "Cuenta y email no registrados.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.signupBtn.setOnClickListener(event -> {
            if(currentView.equals("owner") || currentView.equals("guest")) {
                extras.putString("role", currentView);
                new RegistrationRouter().launch(getActivity(), extras);
            }
            if(currentView.equals("admin")) {

            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GOOGLE
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("411006371232-knal604jou5eksg6621thf0i9jnk4e46.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                mGoogleSignInClient.signOut();
                resultLauncher.launch(new Intent((mGoogleSignInClient.getSignInIntent())));
            }
        });

    }

    /**
     * Acción que se ejecuta cuando se clica en el botón de sigin con google
     */
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);

                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.e("Account", account.getEmail());
                            String userId = utils.generateID(account.getEmail());
                            if(account != null) {

                                //se comprueba si el cliente existe registrado en base de datos
                                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String roleUser = dataSnapshot.child("role").getValue().toString();

                                            Log.d(TAG, "firebaseAuthWithGoogleRole: " + roleUser);
                                            loginWithGoogleGmail(account.getIdToken(), roleUser, account.getEmail());
                                            Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                                        } else {
                                            Log.d("firebase", "No data found for userId: " + userId);
                                            utils.showAlert(getActivity(), "Error", "Cuenta y email no registrados.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("firebase", "Error getting data", databaseError.toException());
                                    }
                                });
                            }

                        } catch (ApiException e) {
                            Log.w(TAG, "Google sign in failed", e);
                            utils.showAlert(getActivity(), "Error", "Fallo en login con Gmail");
                        }
                    }
                }
            }
    );

    /**
     * Función que realiza el logueo con el servicio de SignIn with Google de Firebase. Si es exitoso inicializa la actividad correspondiente.
     *
     * @param idToken
     * @param roleUser
     * @param email
     */
    private void loginWithGoogleGmail(String idToken, String roleUser, String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Bundle extras = new Bundle();
                            extras.putString("email",email);
                            extras.putString("proveedor", "google");
                            if(roleUser.equals("admin") && currentView.equals("admin")) {
                                Log.e("params", roleUser + " " +currentView);
                                new AdminRouter().launch(getActivity(), extras);
                            }
                            else if(roleUser.equals("client")) {
                                if(currentView.equals("owner")) {
                                    new OwnerRouter().launch(getActivity(),extras);
                                }
                                if(currentView.equals("guest")) {
                                    extras.putSerializable("offer", offer);
                                    new ReservationActivityRouter().launch(getActivity(), extras);
                                }
                            }

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            utils.showAlert(getActivity(), "Error", "Fallo en login con Gmail");
                        }
                    }
                });
    }


    /**
     * Función que realiza el logueo con el servicio de email/password de Firebase. Si es exitoso inicializa la actividad correspondiente.
     *
     * @param email
     * @param roleUser
     * @param password
     */
    private void loginWithEmailAndPassword(String email, String roleUser, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Bundle extras = new Bundle();
                    extras.putString("email", email);
                    extras.putString("proveedor", "email/contraseña");
                    if(roleUser.equals("admin") && currentView.equals("admin")) {
                        Log.e("params", roleUser + " " +currentView);
                        new AdminRouter().launch(getActivity(), extras);
                    }
                    else if(roleUser.equals("client")) {
                        if(currentView.equals("owner")) {
                            new OwnerRouter().launch(getActivity(),extras);
                        }
                        if(currentView.equals("guest")) {
                            extras.putSerializable("offer", offer);
                            new ReservationActivityRouter().launch(getActivity(), extras);
                        }
                    }
                } else {
                    utils.showAlert(getActivity(), "Error", "Email o contraseña erroneos");
                }
            }
        });
    }

}
