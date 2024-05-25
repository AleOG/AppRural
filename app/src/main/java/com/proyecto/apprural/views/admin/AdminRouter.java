package com.proyecto.apprural.views.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.proyecto.apprural.views.router.ActivityRouter;
import com.proyecto.apprural.views.router.FragmentRouter;

public class AdminRouter implements FragmentRouter, ActivityRouter {

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

    @Override
    public Intent intent(Context activity, @Nullable Bundle extras) {
        Intent intent = new Intent(activity, AdminViewActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

    @Override
    public void launch(Context activity, @Nullable Bundle extras) {
        ActivityRouter.super.launch(activity, extras);
    }

}
