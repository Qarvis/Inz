package com.project.my.inz.app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.project.my.inz.Model.DataModel;
import com.project.my.inz.activity.MainActivity;
import com.project.my.inz.app.db.DatabaseHandler;

public class AlertDFragment extends DialogFragment {

    DatabaseHandler db;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int points = 0;


    public static AlertDFragment newInstance(int id) {
        AlertDFragment frag = new AlertDFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int id = getArguments().getInt("id");
        db = ((MainActivity)getActivity()).getDataBaseHandler();

        settings = getActivity().getSharedPreferences(MainActivity.PREFS, getActivity().MODE_PRIVATE);
        editor = settings.edit();

        return new AlertDialog.Builder(getActivity())
                        // Set Dialog Title
                .setTitle("Misja!")
                        // Set Dialog Message
                .setMessage("Czy aby na pewno ukończyłeś tą misję")

                        // Positive button
                .setPositiveButton("TAK!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        points = settings.getInt("points", 0);
                        DataModel dm;
                        dm = db.getQuest(id);
                        dm.setState("10"); // quest done
                        points += Integer.parseInt(dm.getPoints());
                        editor.putInt("points", points).commit();
                        db.updateQuest(dm); // quest update
                    }
                })

                 // Negative Button
                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
    }
}