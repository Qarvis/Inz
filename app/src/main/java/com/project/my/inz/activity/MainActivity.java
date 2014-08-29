package com.project.my.inz.activity;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.my.inz.adapter.TabsPagerAdapter;
import com.project.my.inz.app.R;
import com.project.my.inz.app.db.DatabaseHandler;
import com.project.my.inz.app.fragment.StartFragment;
import com.project.my.inz.app.help.AlarmReceiver;

import java.util.Calendar;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, SensorEventListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    // Tab titles
    private String[] tabs = { "Praca", "Osiagniecia", "Misje" };

    Button startbutton;

    DatabaseHandler db;
    //sensors
    private SensorManager sManager;
    private Sensor sAccelerometer;
    private boolean isSensorRun = false;
    private long lastSensorUpdate;
    private static final int SHAKE_THRESHOLD = 50;
    private float last_x, last_y, last_z;

    //sheredpreferences
    public final static String PREFS = "PrefsFile";
    public final static String PREFS_work = "pref_work_list";
    public final static String PREFS_break = "pref_break_list";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sharedpreferences
        // Save time of run:

        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = settings.edit();

        // First time running app?
        if (!settings.contains("lastTimeActionDone"))
            editor.putLong("lastTimeActionDone", 0).commit();
        
        //Sensors
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        db = new DatabaseHandler(this);


        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        startbutton = (Button) findViewById(R.id.start_button);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));

        }

        Log.d("SEKUNDY",System.currentTimeMillis()/1000/60/60 + "");

        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    public DatabaseHandler getDataBaseHandler() {
        return db;
    } // return database

    public void registerSensor(){
        sManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unRegisterSensor(){
        sManager.unregisterListener(this);
    }
    public boolean getIsSensorRun(){
        return isSensorRun;
    }
    public void setIsSensorRun(boolean isSensorRun){
        this.isSensorRun = isSensorRun;
    }


    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_bookmark:
                return true;

            case R.id.menu_preferences:
                Intent intent = new Intent(getApplicationContext(), PreferenceActivity.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        String temp = "";
        int points=0;
        try {
            points  = settings.getInt("points", 0);

        }catch (Exception e){}
        menu.findItem(R.id.user_points).setTitle(points +" pkt");

        menu.findItem(R.id.new_quest).setVisible(false);

        return true;
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastSensorUpdate) > 100) {
                long diffTime = (curTime - lastSensorUpdate);
                lastSensorUpdate = curTime;

                float speed = Math.abs(x+y+z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    this.isSensorRun = true;
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    protected void onResume() {
        super.onResume();
        if(isSensorRun)
            sManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sManager.unregisterListener(this);
    }



}
