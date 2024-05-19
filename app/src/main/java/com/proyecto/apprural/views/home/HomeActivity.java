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
import com.proyecto.apprural.views.logIn.LoginRouter;
import com.proyecto.apprural.views.owner.OwnerRouter;

public class HomeActivity extends AppCompatActivity {

    // Properties

    private HomeActivityBinding binding;
    private HomeViewModel viewModel;
    private ClientRouter clientViewFragment = null;
    private AdminRouter adminViewFragment = null;
    private OwnerRouter ownerViewFragment = null;

    private LoginRouter loginFragment = null;

    // Initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());

        // Content
        setContentView(binding.getRoot());

        // View Model
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Setup
        loadTabs();
        defaultTab();

    }


    // Private

    private void loadTabs() {

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
        if(loginFragment != null) {
            loginFragment.remove(getSupportFragmentManager());
        }

        adminViewFragment = new AdminRouter();
        clientViewFragment = new ClientRouter();
        ownerViewFragment = new OwnerRouter();
        loginFragment = new LoginRouter();

        binding.homeBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.e("id", String.valueOf(id));
                if(id != -1) {
                    switch (id) {
                        //2131231247 id opcion admin
                        case 2131231247:
                            clientViewFragment.hide(getSupportFragmentManager());
                            ownerViewFragment.hide(getSupportFragmentManager());
                            if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.admin_menu)) == null) {
                                adminViewFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.admin_menu));
                            }
                            adminViewFragment.show(getSupportFragmentManager());
                            return true;
                        //2131231248 id opcion cliente
                        case 2131231248:
                            adminViewFragment.hide(getSupportFragmentManager());
                            ownerViewFragment.hide(getSupportFragmentManager());
                            if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.client_menu)) == null) {
                                clientViewFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.client_menu));
                            }
                            clientViewFragment.show(getSupportFragmentManager());
                        return true;
                        //2131231249 id opcion owner
                        case 2131231249:
                            clientViewFragment.hide(getSupportFragmentManager());
                            adminViewFragment.hide(getSupportFragmentManager());
                            if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.owner_menu)) == null) {
                                ownerViewFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.owner_menu));
                            }
                            ownerViewFragment.show(getSupportFragmentManager());
                            return true;
                        default:
                        return true;
                    }
                }else {
                    return false;
                }
            }
        });



        /*binding.homeBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            if (selectedItem != menuItem.getItemId()) {

                selectedItem = menuItem.getItemId();

                switch (menuItem.getItemId()) {
                    case R.id.home_menu_countdown:
                        searchFragment.hide(getSupportFragmentManager());
                        accountFragment.hide(getSupportFragmentManager());
                        if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.home_menu_countdown)) == null) {
                            countdownFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.home_menu_countdown), this);
                        }
                        countdownFragment.show(getSupportFragmentManager());
                        return true;
                    case R.id.home_menu_search:
                        countdownFragment.hide(getSupportFragmentManager());
                        accountFragment.hide(getSupportFragmentManager());
                        if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.home_menu_search)) == null) {
                            searchFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.home_menu_search));
                        }
                        searchFragment.show(getSupportFragmentManager());
                        return true;
                    case R.id.home_menu_account:
                        countdownFragment.hide(getSupportFragmentManager());
                        searchFragment.hide(getSupportFragmentManager());
                        if (getSupportFragmentManager().findFragmentByTag(Integer.toString(R.id.home_menu_account)) == null) {
                            accountFragment.add(getSupportFragmentManager(), R.id.homeContainer, Integer.toString(R.id.home_menu_account), this);
                        }
                        accountFragment.show(getSupportFragmentManager());
                        return true;
                    default:
                        return false;
                }
            } else {
                return false;
            }
        });*/
    }

    private void defaultTab() {
        binding.homeBottomNavigationView.setSelectedItemId(R.id.client_menu);
    }

}
