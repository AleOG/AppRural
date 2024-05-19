package com.proyecto.apprural.views.home;

import android.content.Context;
import android.content.Intent;

import com.proyecto.apprural.views.router.ActivityRouter;

public class HomeActivityRouter implements ActivityRouter {

    @Override
    public Intent intent(Context activity) {
        return new Intent(activity, HomeActivity.class);
    }

}
