package com.proyecto.apprural.views.owner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.OwnerViewFragmentBinding;
import com.proyecto.apprural.views.logIn.LoginRouter;

public class OwnerViewFragment extends Fragment {

    private OwnerViewFragmentBinding binding;
    private LoginRouter loginFragment = null;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = OwnerViewFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = new Bundle();
        arguments.putString("currentView", "owner");

        if(loginFragment != null) {
            loginFragment.remove(getChildFragmentManager());
        }
        loginFragment = new LoginRouter();
        loginFragment.setArguments(arguments);
        loginFragment.replace(getChildFragmentManager(), R.id.owner_logIn);
    }
}
