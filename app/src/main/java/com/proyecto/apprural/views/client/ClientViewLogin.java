package com.proyecto.apprural.views.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.proyecto.apprural.R;
import com.proyecto.apprural.model.beans.FullAccommodationOffer;
import com.proyecto.apprural.views.logIn.LoginRouter;

public class ClientViewLogin extends AppCompatActivity {

    private LoginRouter loginRouter;

    private FullAccommodationOffer offer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_view_login);

        loginRouter = new LoginRouter();

        FullAccommodationOffer offerAux = (FullAccommodationOffer) getIntent().getSerializableExtra("offer");

        if (offerAux != null) {
            Log.e("offer en accomodation", offerAux.toString());
            offer = offerAux;
        }

        Bundle arguments = new Bundle();
        arguments.putString("currentView", "guest");
        arguments.putSerializable("offer", offer);
        loginRouter.setArguments(arguments);

        loadLoginFragment();
    }


    private void loadLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        loginRouter.replace(fragmentManager, R.id.guest_login);
    }
}