package com.proyecto.apprural.views.owner.alta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.proyecto.apprural.views.router.ActivityRouter;
public class OwnerAltaRouter implements ActivityRouter {
    @Override
    public Intent intent(Context activity, @Nullable Bundle extras) {
        Intent intent = new Intent(activity, OwnerViewAltaActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        return intent;
    }
}
