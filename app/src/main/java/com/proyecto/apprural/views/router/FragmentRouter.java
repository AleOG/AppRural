package com.proyecto.apprural.views.router;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public interface FragmentRouter {
    Fragment fragment();

    default void add(FragmentManager manager, int containerId, String tag) {
        manager.beginTransaction().add(containerId, fragment(), tag).commitAllowingStateLoss();
    }

    default void replace(FragmentManager manager, int containerId) {
        manager.beginTransaction().replace(containerId, fragment()).commit();
    }

    default int show(FragmentManager manager) {
        return manager.beginTransaction().show(fragment()).commitAllowingStateLoss();
    }

    default void hide(FragmentManager manager) {
        manager.beginTransaction().hide(fragment()).commitAllowingStateLoss();
    }

    default void remove(FragmentManager manager) {
        manager.beginTransaction().remove(fragment()).commitAllowingStateLoss();
    }

}
