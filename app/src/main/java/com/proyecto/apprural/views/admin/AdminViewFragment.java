package com.proyecto.apprural.views.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.AdminViewFragmentBinding;
import com.proyecto.apprural.views.logIn.LoginRouter;

public class AdminViewFragment extends Fragment {

    private AdminViewFragmentBinding binding;
    private LoginRouter loginFragment = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = AdminViewFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(loginFragment != null) {
            loginFragment.remove(getChildFragmentManager());
        }
        loginFragment = new LoginRouter();
        loginFragment.replace(getChildFragmentManager(), R.id.admin_logIn);
    }
}
