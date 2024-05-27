package com.proyecto.apprural.views.owner.alta.room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.proyecto.apprural.views.router.ActivityRouter;

public class RoomAltaRouter implements ActivityRouter {
    @Override
    public Intent intent(Context activity, @Nullable Bundle extras) {
        Intent intent = new Intent(activity, RoomAltaActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }
}
