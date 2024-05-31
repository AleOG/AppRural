package com.proyecto.apprural.views.home;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationBarView;
import com.proyecto.apprural.databinding.HomeActivityBinding;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.proyecto.apprural.R;
import com.proyecto.apprural.views.admin.AdminRouter;
import com.proyecto.apprural.views.client.ClientRouter;
import com.proyecto.apprural.views.owner.OwnerRouter;

public class HomeActivity extends AppCompatActivity {

    // Properties

    private HomeActivityBinding binding;
    private ClientRouter clientViewFragment = null;
    private AdminRouter adminViewFragment = null;
    private OwnerRouter ownerViewFragment = null;

    // Initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());

        // Content
        setContentView(binding.getRoot());

        // Setup
        loadTabsMenu();
        defaultTabMenu();

    }


    private void loadTabsMenu() {

        // Fragments
        if (clientViewFragment != null) {
            clientViewFragment.remove(getSupportFragmentManager());
        }
        if (adminViewFragment != null) {
            adminViewFragment.remove(getSupportFragmentManager());
        }
        if (ownerViewFragment != null) {
            ownerViewFragment.remove(getSupportFragmentManager());
        }

        adminViewFragment = new AdminRouter();
        clientViewFragment = new ClientRouter();
        ownerViewFragment = new OwnerRouter();

        binding.homeBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                Log.e("id", String.valueOf(id));
                if(id != -1) {
                    if(id == R.id.client_menu) {
                        adminViewFragment.hide(getSupportFragmentManager());
                        ownerViewFragment.hide(getSupportFragmentManager());
                        if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.client_menu)) == null) {
                            clientViewFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.client_menu));
                        }
                        clientViewFragment.show(getSupportFragmentManager());
                        return true;
                    } else if (id == R.id.admin_menu) {
                        clientViewFragment.hide(getSupportFragmentManager());
                        ownerViewFragment.hide(getSupportFragmentManager());
                        if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.admin_menu)) == null) {
                            adminViewFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.admin_menu));
                        }
                        adminViewFragment.show(getSupportFragmentManager());
                        return true;
                    } else if (id == R.id.owner_menu) {
                        clientViewFragment.hide(getSupportFragmentManager());
                        adminViewFragment.hide(getSupportFragmentManager());
                        if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.owner_menu)) == null) {
                            ownerViewFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.owner_menu));
                        }
                        ownerViewFragment.show(getSupportFragmentManager());
                        return true;
                    }else {
                        return true;
                    }
                }else {
                    return false;
                }
            }
        });
    }

    private void defaultTabMenu() {
        binding.homeBottomNavigationView.setSelectedItemId(R.id.client_menu);
    }

}
