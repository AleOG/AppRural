package com.proyecto.apprural.views.owner;

import androidx.fragment.app.Fragment;
import com.proyecto.apprural.views.router.FragmentRouter;

public class OwnerRouter implements FragmentRouter {

    private OwnerViewFragment ownerViewFragment;
    @Override
    public Fragment fragment() {
        if (ownerViewFragment == null) {
            ownerViewFragment = new OwnerViewFragment();
        }
        return ownerViewFragment;
    }
}
