package com.android.nazirshuqair.f1schedule;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by nazirshuqair on 11/11/14.
 */
public class CollectionWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CollectionWidgetViewFactory(getApplicationContext());
    }
}
