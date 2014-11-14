package com.android.nazirshuqair.f1schedule;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by nazirshuqair on 11/11/14.
 */
public class CollectionWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {


    private static final int ID_CONSTANT = 0x0101010;

    private ArrayList<Race> mRacesList;
    private Context mContext;
    Race mRace;

    public CollectionWidgetViewFactory(Context context) {
        mContext = context;
        mRacesList = new ArrayList<Race>();
    }

    @Override
    public void onCreate() {
        try {
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() throws IOException, ClassNotFoundException {

        File dir = new File("/data/data/com.android.nazirshuqair.f1schedule/app_mydir");
        File[] filelist = dir.listFiles();
        mRacesList.clear();

        if (filelist != null) {
            for (File file : filelist) {
                if (file.isFile()) {
                    FileInputStream fin = new FileInputStream(file);
                    ObjectInputStream oin = new ObjectInputStream(fin);

                    mRace = (Race) oin.readObject();

                    oin.close();

                    mRacesList.add(mRace);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mRacesList.size();
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Race race = mRacesList.get(position);

        RemoteViews itemView = new RemoteViews(mContext.getPackageName(), R.layout.race_item);

        itemView.setTextViewText(R.id.circuit_name, race.getRaceCircuitName());
        itemView.setTextViewText(R.id.race_date, race.getRaceDate());
        itemView.setTextViewText(R.id.num_laps, race.getRaceLapNum());

        Intent intent = new Intent();
        intent.putExtra(CollectionWidgetProvider.EXTRA_ITEM, race);
        itemView.setOnClickFillInIntent(R.id.race_item, intent);

        return itemView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        // Heavy lifting code can go here without blocking the UI.
        // You would update the data in your collection here as well.
        mRacesList.clear();
        try {
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mRacesList.clear();
    }

}
