package com.project.my.inz.app.fragment;


import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.activity.MainActivity;
import com.project.my.inz.adapter.AchiveArrayAdapter;
import com.project.my.inz.app.db.DatabaseHandler;
import com.project.my.inz.app.help.DataListLoaderAchive;

/**
 * Created by Luki on 28.06.14.
 */
public class AchiveFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<DataModel>> {

    AchiveArrayAdapter mAdapter;
    private DatabaseHandler db;


    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = ((MainActivity)getActivity()).getDataBaseHandler();

        System.out.println("DataListFragment.onActivityCreated");

        // Initially there is no data
        setEmptyText("Brak osiągnięć");

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new AchiveArrayAdapter(getActivity());
        setListAdapter(mAdapter);

        // Start out with a progress indicator.
        setListShown(false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

    }
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public Loader<List<DataModel>> onCreateLoader(int arg0, Bundle arg1) {
        System.out.println("DataListFragment.onCreateLoader1");
        return new DataListLoaderAchive(getActivity(), db);
    }

    @Override
    public void onLoadFinished(Loader<List<DataModel>> arg0, List<DataModel> data) {
        mAdapter.setData(data);
        System.out.println("DataListFragment.onLoadFinished");
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<DataModel>> arg0) {
        mAdapter.setData(null);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getLoaderManager().restartLoader(0, null, this);
    }


}



