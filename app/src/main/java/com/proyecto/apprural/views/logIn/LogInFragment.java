package com.proyecto.apprural.views.logIn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.LoginFragmentBinding;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
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

        binding = LoginFragmentBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Recuperar los argumentos
        Bundle args = getArguments();
        if (args != null) {
            currentView = args.getString("currentView");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                offer = args.getSerializable("offer", FullAccommodationOffer.class);
            }
            // Mostrar el rol en un Toast
            Toast.makeText(getActivity(), "Emisor: " + currentView, Toast.LENGTH_SHORT).show();
        }
        /*misDatos = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = misDatos.getString("email", null);

        if (email == null) {
            Log.e("Entra aquí", "Entra aquí");
            // Usuario no está logueado, mostrar fragmento de login
            return binding.getRoot();
        } else {
            // Usuario está logueado, iniciar otra actividad
            Bundle extras = new Bundle();
            extras.putString("email", email);
            extras.putString("proveedor", "email/contraseña");
            if(currentView.equals("admin")) {
                new AdminRouter().launch(getActivity(), extras);
            }
            if(currentView.equals("owner")) {
                new OwnerRouter().launch(getActivity(),extras);
            }
            return null; // No devolver una vista ya que se inicia otra actividad
        }*/
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }


    private void setup() {
    Bundle extras = new Bundle();

        binding.loginBtn.setOnClickListener(event -> {
            String email = binding.emailEdtText.getText().toString().trim();
            String password = binding.passEdtText.getText().toString().trim();

            // Validar las credenciales de inicio de sesión
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                String userId = utils.generateID(email);
                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Maneja los datos recuperados
                            String roleUser = snapshot.child("role").getValue().toString();

                            //Log.d(TAG, "firebaseAuthWithGoogleRole: " + roleUser);
                            loginWithEmailAndPassword(email, roleUser, password);
                            //loginWithGoogleGmail(account.getIdToken(), roleUser, account.getEmail());
                            //Log.d("firebase", String.valueOf(snapshot.getValue()));
                        } else {
                            // Maneja el caso cuando no existen datos
                            //Log.d("firebase", "No data found for userId: " + userId);
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
                //getActivity().finish();
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

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);

                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.e("Account", account.getEmail());
                            String userId = utils.generateID(account.getEmail());
                            if(account != null) {
                                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Maneja los datos recuperados
                                            String roleUser = dataSnapshot.child("role").getValue().toString();

                                            Log.d(TAG, "firebaseAuthWithGoogleRole: " + roleUser);
                                            loginWithGoogleGmail(account.getIdToken(), roleUser, account.getEmail());
                                            Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                                        } else {
                                            // Maneja el caso cuando no existen datos
                                            Log.d("firebase", "No data found for userId: " + userId);
                                            utils.showAlert(getActivity(), "Error", "Cuenta y email no registrados.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Maneja los errores
                                        Log.e("firebase", "Error getting data", databaseError.toException());
                                    }
                                });
                            }

                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e);
                            utils.showAlert(getActivity(), "Error", "Fallo en login con Gmail");
                        }
                    }
                }
            }
    );

    private void loginWithGoogleGmail(String idToken, String roleUser, String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //updateUI(user);
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
                                    //Iniciar la actividad de realizar reserva
                                    new ReservationActivityRouter().launch(getActivity(), extras);
                                }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            utils.showAlert(getActivity(), "Error", "Fallo en login con Gmail");
                        }
                    }
                });
    }


    private void loginWithEmailAndPassword(String email, String roleUser, String password) {
        // Implementar la lógica de autenticación con Firebase

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
                            //iniciar la actividad de realizar reserva
                            new ReservationActivityRouter().launch(getActivity(), extras);
                        }
                    }
                    //getActivity().finish(); // Close the login fragment/activity
                } else {
                    // If sign in fails, display a message to the user.
                    utils.showAlert(getActivity(), "Error", "Email o contraseña erroneos");
                    //Toast.makeText(getActivity(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
