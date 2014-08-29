package com.project.my.inz.app.help;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.view.Menu;
import com.project.my.inz.Model.DataModel;
import com.project.my.inz.app.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;


public class DataListLoaderAchive extends AsyncTaskLoader<List<DataModel>> {

    List<DataModel> mModels;
    DatabaseHandler db;

    public DataListLoaderAchive(Context context, DatabaseHandler db) {
        super(context);
        this.db = db;
    }

    @Override
    public List<DataModel> loadInBackground() {
        System.out.println("DataListLoader.loadInBackground");
        List<DataModel> quests = db.getAllQuests();
            //region TESTOWANIE_LISTY




//        if (size != 0){
//            for(DataModel x : quests){
//                db.deletequest(x);
//            }}
//        db.addQuest(new QuestM("1","Książka","Przeczytaj sobie książkę","2", Long.toString(System.currentTimeMillis())));
//        db.addQuest(new QuestM("11","Książka","LOLEK","10", Long.toString(System.currentTimeMillis())));




//        for (DataModel cn : quests) {
//            String log = "Id: "+cn.getId()+" ,Name: " + cn.getName() + " ,DESC: " + cn.getDesc() + " ,Points: " + cn.getPoints()
//                    + " ,State: " + cn.getState()+ " ,Date: " + cn.getData();
//            // Writing Contacts to log
//            //Log.d("Kurczak",Long.toString(System.currentTimeMillis()));
//            Log.d("Kurczak: ", log);
//        }
        //endregion
        return getAchive(quests);
    }

    /**
     * Called when there is new data to deliver to the client.
     */
    @Override public void deliverResult(List<DataModel> listOfData) {
        if (isReset()) {
            // An async query came in while the loader is stopped.
            if (listOfData != null) {
                onReleaseResources(listOfData);
            }
        }
        List<DataModel> oldApps = listOfData;
        mModels = listOfData;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(listOfData);
        }
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (mModels != null) {
            // If we currently have a result available, deliver it
            deliverResult(mModels);
        }

        if (takeContentChanged() || mModels == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(List<DataModel> apps) {
        super.onCanceled(apps);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(apps);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mModels != null) {
            onReleaseResources(mModels);
            mModels = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<DataModel> apps) {}

    //function to ensure that object is achive
    public List<DataModel> getAchive(List<DataModel> list){
        List<DataModel> temp = new ArrayList<DataModel>(){};
        for(DataModel x : list){
            int check = Integer.parseInt(x.getState());
            if(check == 11 || check == 10){
                temp.add(x);
            }
        }
        return  temp;
    }



}