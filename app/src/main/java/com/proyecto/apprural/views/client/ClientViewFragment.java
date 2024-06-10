package com.proyecto.apprural.views.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proyecto.apprural.R;
import com.proyecto.apprural.databinding.ClientViewFragmentBinding;

public class ClientViewFragment extends Fragment {

    private ClientViewFragmentBinding binding;

    private ClientSearchViewRouter clientSearchViewRouter = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ClientViewFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = new Bundle();
        arguments.putString("currentView", "guest");

        if(clientSearchViewRouter != null) {
            clientSearchViewRouter.remove(getChildFragmentManager());
        }
        clientSearchViewRouter = new ClientSearchViewRouter();
        clientSearchViewRouter.setArguments(arguments);
        clientSearchViewRouter.replace(getChildFragmentManager(), R.id.guest_search);
    }
}
