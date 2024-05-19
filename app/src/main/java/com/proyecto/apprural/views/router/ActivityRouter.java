package com.proyecto.apprural.views.router;

import android.content.Context;
import android.content.Intent;
public interface ActivityRouter {

    Intent intent(Context activity);

    default void launch(Context activity) {
        activity.startActivity(intent(activity));
    }
}
