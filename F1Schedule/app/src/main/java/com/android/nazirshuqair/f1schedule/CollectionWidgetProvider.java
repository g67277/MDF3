package com.android.nazirshuqair.f1schedule;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by nazirshuqair on 11/11/14.
 */
public class CollectionWidgetProvider extends AppWidgetProvider {


    public static final String ACTION_VIEW_DETAILS = "com.android.ACTION_VIEW_DETAILS";
    public static final String EXTRA_ITEM = "com.android.CollectionWidgetProvider.EXTRA_ITEM";
    public static final String WIDGET_BUTTON = "com.android.WIDGET_BUTTON";

    int widgetId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; i++) {

            widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, CollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_ui);
            widgetView.setRemoteAdapter(R.id.race_widget_list, intent);
            widgetView.setEmptyView(R.id.race_widget_list, R.id.empty);

            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.race_widget_list, pIntent);

            /*Intent addIntent = new Intent(context, DetailActivity.class);
            addIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            addIntent.putExtra("edit", true);
            PendingIntent pIntent2 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setOnClickPendingIntent(R.id.add_race, pIntent2);*/

            Intent addIntent = new Intent(WIDGET_BUTTON);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, addIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setOnClickPendingIntent(R.id.add_race, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, widgetView);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            Race race = (Race)intent.getSerializableExtra(EXTRA_ITEM);
            if(race != null) {
                Intent details = new Intent(context, DetailActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(DetailActivity.EXTRA_ITEM, race);
                details.putExtra("edit", false);
                details.putExtra("raceLocation", race.getRaceLocation());
                details.putExtra("raceCircuit", race.getRaceCircuitName());
                details.putExtra("raceDate", race.getRaceDate());
                details.putExtra("raceLaps", race.getRaceLapNum());
                context.startActivity(details);
            }
        }else if (WIDGET_BUTTON.equals(intent.getAction())) {
            //your code here
           /*Intent add = new Intent(context, DetailActivity.class);
            add.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            add.putExtra("edit", true);
            context.startActivity(add);*/
        }

        super.onReceive(context, intent);
    }


}
