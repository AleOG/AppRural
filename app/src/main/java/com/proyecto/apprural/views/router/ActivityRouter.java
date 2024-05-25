package com.proyecto.apprural.views.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public interface ActivityRouter {

    Intent intent(Context activity, @Nullable Bundle extras);

    default void launch(Context activity,  @Nullable Bundle extras) {
        activity.startActivity(intent(activity, extras));
    }
    default void launch(Context activity) {
        launch(activity, null);
    }
}
