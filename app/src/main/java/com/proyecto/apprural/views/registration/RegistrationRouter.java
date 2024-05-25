package com.proyecto.apprural.views.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.proyecto.apprural.views.router.ActivityRouter;

public class RegistrationRouter implements ActivityRouter {
    @Override
    public Intent intent(Context activity, @Nullable Bundle extras) {
        Intent intent = new Intent(activity, RegistrationActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }

    @Override
    public void launch(Context activity, @Nullable Bundle extras) {
        ActivityRouter.super.launch(activity, extras);
    }

    @Override
    public void launch(Context activity) {
        ActivityRouter.super.launch(activity);
    }
}
