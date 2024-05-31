package com.proyecto.apprural.views.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.proyecto.apprural.views.router.FragmentRouter;


public class ClientSearchViewRouter implements FragmentRouter {

    private ClientSearchViewFragment clientSearchViewFragment;
    private Bundle arguments;

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    @Override
    public Fragment fragment() {
        if (clientSearchViewFragment == null) {
            clientSearchViewFragment = new ClientSearchViewFragment();
            if (arguments != null) {
                clientSearchViewFragment.setArguments(arguments);
            }
        }
        return clientSearchViewFragment;
    }

    @Override
    public void add(FragmentManager manager, int containerId, String tag) {
        FragmentRouter.super.add(manager, containerId, tag);
    }

    @Override
    public void replace(FragmentManager manager, int containerId) {
        FragmentRouter.super.replace(manager, containerId);
    }

    @Override
    public int show(FragmentManager manager) {
        return FragmentRouter.super.show(manager);
    }

    @Override
    public void hide(FragmentManager manager) {
        FragmentRouter.super.hide(manager);
    }

    @Override
    public void remove(FragmentManager manager) {
        FragmentRouter.super.remove(manager);
    }
}
