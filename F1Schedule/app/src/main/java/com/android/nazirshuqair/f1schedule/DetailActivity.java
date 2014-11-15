package com.android.nazirshuqair.f1schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by nazirshuqair on 10/13/14.
 */
public class DetailActivity extends Activity implements EditFragment.MasterClickListener{

    public static final String EXTRA_ITEM = "com.android.DetailActivity.EXTRA_ITEM";

    Race mRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //enables the back button in the action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //changes the action bar color
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff113cd8")));

        Intent intent = getIntent();

        if (intent.getBooleanExtra("edit", true)){
            if(savedInstanceState == null) {
                EditFragment frag = EditFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.detail_activity, frag, EditFragment.TAG).commit();
            }
        }else {

            if(savedInstanceState == null) {
                ViewFragment frag = ViewFragment.newInstance(intent.getStringExtra("raceLocation"),
                        intent.getStringExtra("raceCircuit"),
                        intent.getStringExtra("raceDate"),
                        intent.getStringExtra("raceLaps"),
                        intent.getIntExtra("position", 0));
                getFragmentManager().beginTransaction().replace(R.id.detail_activity, frag, ViewFragment.TAG).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();

        if (intent.getBooleanExtra("edit", true)) {
            MenuItem saveItem = menu.add("Save");
            saveItem.setShowAsAction(1);

            saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    EditFragment frag = (EditFragment) getFragmentManager().findFragmentByTag(EditFragment.TAG);

                    frag.saveItem();

                    return false;
                }
            });
            MenuItem resetForm = menu.add("Reset");
            resetForm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    EditFragment frag = (EditFragment) getFragmentManager().findFragmentByTag(EditFragment.TAG);

                    frag.clearForm();

                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    //Write the object to storage
    private void writeToFile(Race _data, String _filename) throws IOException {

        File mydir = this.getDir("mydir", Context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, _filename); //Getting a file within the dir.
        FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
        ObjectOutputStream oos = new ObjectOutputStream(out);

        if (mRace == null){
            mRace = new Race();
        }
        mRace.setData(_data);
        oos.writeObject(mRace);
        oos.close();

    }

    @Override
    public void pushData(String _inputLocation, String _inputCircuit, String _inputDate, String _inputLaps) {

        Race race = new Race(_inputLocation, _inputCircuit, _inputDate, _inputLaps);
        try {
            writeToFile(race, _inputCircuit);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setResult(RESULT_OK);
        finish();
    }
}
