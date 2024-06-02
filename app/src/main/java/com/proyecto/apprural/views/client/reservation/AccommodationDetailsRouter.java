package com.proyecto.apprural.views.client.reservation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.proyecto.apprural.views.client.SearchResultsActivity;
import com.proyecto.apprural.views.router.ActivityRouter;

public class AccommodationDetailsRouter implements ActivityRouter {
    @Override
    public Intent intent(Context activity, @Nullable Bundle extras) {
        Intent intent = new Intent(activity, AccommodationDetailsActivity.class);
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
