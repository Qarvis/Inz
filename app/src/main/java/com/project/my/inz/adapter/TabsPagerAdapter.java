package com.project.my.inz.adapter;

/* DOstarcza obsluge widoku fragmentow do tabow */



import com.project.my.inz.app.fragment.AchiveFragment;
import com.project.my.inz.app.fragment.QuestFragment;
import com.project.my.inz.app.fragment.StartFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Start fragment activity
                return new StartFragment();
            case 1:
                // Achive fragment activity
                return new AchiveFragment();
            case 2:
                // Quests fragment activity
                return new QuestFragment();
        }

        return null;
    }

    @Override
    public int getCount() { // liczba tabów
        return 3;
    }

}