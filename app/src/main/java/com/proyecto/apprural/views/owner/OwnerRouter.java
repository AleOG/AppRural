package com.proyecto.apprural.views.owner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proyecto.apprural.views.router.ActivityRouter;
import com.proyecto.apprural.views.router.FragmentRouter;

public class OwnerRouter implements FragmentRouter, ActivityRouter {

    private OwnerViewFragment ownerViewFragment;

    @Override
    public Fragment fragment() {
        if (ownerViewFragment == null) {
            ownerViewFragment = new OwnerViewFragment();
        }
        return ownerViewFragment;
    }

    @Override
    public Intent intent(Context activity, @Nullable Bundle extras) {
        Intent intent = new Intent(activity, OwnerViewActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

}
