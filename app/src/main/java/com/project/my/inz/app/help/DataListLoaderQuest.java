package com.project.my.inz.app.help;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.Model.QuestM;
import com.project.my.inz.activity.MainActivity;
import com.project.my.inz.app.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luki on 2014-07-02.
 */
public class DataListLoaderQuest extends AsyncTaskLoader<List<DataModel>> {

    List<DataModel> mModels;

    ConnectionDetector cd;
    Boolean isInternetPresent; // true or false
    Context context;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    private static Long MILLISECS_PER_DAY = 86400000L;
//    private static long delay = 60000;                 // 1 minute (for testing)
    private static long delay = MILLISECS_PER_DAY;   // 1 day

    DatabaseHandler db;


    public DataListLoaderQuest(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public List<DataModel> loadInBackground() {
        System.out.println("DataListLoader.loadInBackground");

        cd = new ConnectionDetector(context);
        isInternetPresent = cd.isConnectingToInternet();

        settings = context.getSharedPreferences(MainActivity.PREFS, context.MODE_PRIVATE);
        editor = settings.edit();

        List<DataModel> entries = new ArrayList<DataModel>();

        db = ((MainActivity)context).getDataBaseHandler();

        Long lastTimeDone = settings.getLong("lastTimeActionDone", 0); // read
        editor.putLong("lastTimeActionDone", System.currentTimeMillis()).commit(); //save

        if ((System.currentTimeMillis() - lastTimeDone) >= delay) {
            entries = getQuest(db.getAllQuests());
            checkQuest(entries);
            if (isInternetPresent) {//if online
                try {//load from web
                    editor.putLong("lastTimeActionDone", System.currentTimeMillis()).commit();
                    XmlLoader pom = new XmlLoader();
                    entries = pom.LoadXML();
                    db.addQuestList(entries);
                } catch (Exception e) {
                }
            }
        }else{
            entries = db.getAllQuests();
        }

        return getQuest(entries);
    }

    /**
     * Called when there is new data to deliver to the client.
     */
    @Override
    public void deliverResult(List<DataModel> listOfData) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
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
    @Override
    protected void onStartLoading() {
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
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(List<DataModel> apps) {
        super.onCanceled(apps);
        // At this point we can release the resources associated with 'apps'
        onReleaseResources(apps);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        if (mModels != null) {
            onReleaseResources(mModels);
            mModels = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<DataModel> apps) {
    }
    //function to ensure that object is quest
    public List<DataModel> getQuest(List<DataModel> list){
        List<DataModel> temp = new ArrayList<DataModel>(){};
        for(DataModel x : list){
            int check = Integer.parseInt(x.getState());
            if(check == 21 || check == 20){
                temp.add(x);
            }
        }
        return  temp;
    }
    //function to update old quest
    public void checkQuest(List<DataModel> list){
        int id;
        if ( list.size() > 0){
            for( DataModel item : list){
                id = Integer.parseInt(item.getId());
                DataModel get = db.getQuest(id);
                get.setState("11");
                db.updateQuest(get);
            }
        }

    }

}