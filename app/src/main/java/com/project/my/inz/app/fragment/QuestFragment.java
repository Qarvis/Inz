package com.project.my.inz.app.fragment;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.activity.MainActivity;
import com.project.my.inz.adapter.QuestArrayAdapter;
import com.project.my.inz.app.R;
import com.project.my.inz.app.db.DatabaseHandler;
import com.project.my.inz.app.help.DataListLoaderQuest;


import java.util.List;

/**
 * Created by Luki on 28.06.14.
 */
public class QuestFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<DataModel>> {

    QuestArrayAdapter mAdapter;
    FragmentManager fm;
    DatabaseHandler db;

    SharedPreferences settings;
    SharedPreferences.Editor editor;


    // constant for identifying the dialog
    private static final int DIALOG_ALERT = 10;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("DataListFragment.onActivityCreated");
        fm = getActivity().getFragmentManager();
        db = ((MainActivity)getActivity()).getDataBaseHandler();

        settings = getActivity().getSharedPreferences(MainActivity.PREFS, getActivity().MODE_PRIVATE);
        editor = settings.edit();

        // Initially there is no data
        setEmptyText("Misję można pobrać tylko raz dziennie");

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new QuestArrayAdapter(getActivity());
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {

                DataModel item = (DataModel) arg0.getItemAtPosition(position);

                showDialog(Integer.parseInt(item.getId()));


                return true;
            }
        });
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public Loader<List<DataModel>> onCreateLoader(int arg0, Bundle arg1) {
        System.out.println("DataListFragment.onCreateLoader2");
            return  new DataListLoaderQuest(getActivity());

    }

    @Override
    public void onLoadFinished(Loader<List<DataModel>> arg0, List<DataModel> data) {
        mAdapter.setData(data);
        System.out.println("DataListFragment.onLoadFinished");
        // The list should now be shown.
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.new_quest:
                initMyLoader(); // refresh loader
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    void showDialog(int id) {
        DialogFragment newFragment = newInstance(id);
        newFragment.show(fm, "dialog");
    }
    public static AlertDFragment newInstance(int id) {
        AlertDFragment frag = new AlertDFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        frag.setArguments(args);
        return frag;
    }
    public void initMyLoader(){
        getLoaderManager().restartLoader(0, null, this);

    }






}






