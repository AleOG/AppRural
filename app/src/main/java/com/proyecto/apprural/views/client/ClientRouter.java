package com.proyecto.apprural.views.client;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.proyecto.apprural.views.router.FragmentRouter;

public class ClientRouter implements FragmentRouter {

    private ClientViewFragment clientViewFragment;
    @Override
    public Fragment fragment() {
        if (clientViewFragment == null) {
            clientViewFragment = new ClientViewFragment();
        }
        return clientViewFragment;
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
        Fragment fragment = fragment();
        /*if (fragment instanceof ClienteViewFragment) {
            ((ClienteViewFragment) fragment).load();
        }*/
        return manager.beginTransaction().show(fragment).commitAllowingStateLoss();
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
