package com.proyecto.apprural.views.logIn;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.proyecto.apprural.views.owner.OwnerViewFragment;
import com.proyecto.apprural.views.router.FragmentRouter;

public class LoginRouter implements FragmentRouter {

    private LogInFragment logInFragment;

    @Override
    public Fragment fragment() {
        if (logInFragment == null) {
            logInFragment = new LogInFragment();
        }
        return logInFragment;
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
