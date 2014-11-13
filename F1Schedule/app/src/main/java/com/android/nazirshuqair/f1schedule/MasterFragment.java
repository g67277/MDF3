package com.android.nazirshuqair.f1schedule;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nazirshuqair on 10/5/14.
 */
public class MasterFragment extends Fragment {

        //Tag to identify the fragment
    public static final String TAG = "MasterFragment.TAG";

    private static final String RLOCATION = "location";
    private static final String RCIRCUIT = "circuit";
    private static final String RDATE = "date";
    private static final String RLAP = "lap";

    //ListView for the games
    ListView racesListView;

    EditText et;

    private ActionMode mActionMode;
    private int mRaceSelected = -1;
    SimpleAdapter adapter;

    //This is a test, probably need to change
    private boolean longClick = false;

    public ArrayList<HashMap<String, Object>> mRaceList = new ArrayList<HashMap<String, Object>>();


    //This is to create a new instance of the fragment
    public static MasterFragment newInstance() {
        MasterFragment frag = new MasterFragment();
        return frag;
    }

    public void updateDisplay (ArrayList<Race> _object, boolean _refresh){

        mRaceList.clear();

        for (Race race: _object){
            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put(RLOCATION, race.getRaceLocation());
            map.put(RCIRCUIT, race.getRaceCircuitName());
            map.put(RDATE, race.getRaceDate());
            map.put(RLAP, race.getRaceLapNum());

            mRaceList.add(map);
        }

        if (_refresh) {
            racesListView.invalidateViews();
        }

    }

    public interface MasterClickListener {
        public void retriveData(HashMap<String, Object> item, int position);
        public void deleteContactFromFragment(String name) throws IOException, ClassNotFoundException;
    }

    private MasterClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof MasterClickListener) {
            mListener = (MasterClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.list_fragment, container, false);

        //Connecting the ListView
        racesListView = (ListView) myFragmentView.findViewById(R.id.race_list_view);


        return myFragmentView;
    }




    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Creating an array of our keys
        String[] keys = new String[] {
                RCIRCUIT, RDATE
        };

        // Creating an array of our list item components.
        // Indices must match the keys array.
        int[] views = new int[] {
                R.id.circuit_name_item,
                R.id.circuit_date_item
        };

        //creating an adapter to populate the listview
        adapter = new SimpleAdapter(getActivity(), mRaceList,  R.layout.list_item, keys, views);
        racesListView.setAdapter(adapter);

        racesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mActionMode != null){
                    return false;
                }
                mRaceSelected = i;
                mActionMode = getActivity().startActionMode(mActionModeCallBack);

                longClick = true;

                return false;
            }
        });

        racesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Call the displayDetails method and pass the adapter view and position
                //to populate details elements
                if (!longClick) {
                    mListener.retriveData(mRaceList.get(position), position);
                }
                longClick = false;
            }
        });
    }

    //Contextual Actionbar Listener

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback(){

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.delete_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {

            HashMap<String, Object> incomingObject = (HashMap<String, Object>) adapter.getItem(mRaceSelected);

            switch (item.getItemId()){

                case R.id.contactDelete:
                    try {
                        mListener.deleteContactFromFragment(String.valueOf(incomingObject.get(RCIRCUIT)));
                        actionMode.finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

            mActionMode = null;
        }
    };


}
