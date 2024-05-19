package com.proyecto.apprural.views.admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.proyecto.apprural.views.router.FragmentRouter;

public class AdminRouter implements FragmentRouter {

    private AdminViewFragment adminViewFragment;
    @Override
    public Fragment fragment() {
        if (adminViewFragment == null) {
            adminViewFragment = new AdminViewFragment();
        }
        return adminViewFragment;
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
}
