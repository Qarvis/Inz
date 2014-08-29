package com.project.my.inz.app.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.my.inz.Model.QuestM;
import com.project.my.inz.activity.MainActivity;
import com.project.my.inz.app.R;
import com.project.my.inz.app.db.DatabaseHandler;
import com.project.my.inz.app.help.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by Luki on 28.06.14.
 */
public class StartFragment extends Fragment {

    Button startbutton;
    TextView day_workTV ,jobTV, pauseTV;         // czas pracy , odpoczynek, przerwa

    CountDownTimer countDownTimer;          // count time
    CountDownTimer countDownTimer_pause;

    long day_work = 0;
    long job=0;                           // total count down time (job) in milliseconds
    String s_job="";
    long pause = 0;
    String s_pause="";
    long timeBlinkInMilliseconds;           // blinking
    boolean blink;
    boolean test;                           //test

    View globalView;

    Boolean timer_running=false;
    DatabaseHandler db;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    private boolean isSensorRun;

    static PendingIntent pendingIntent;
    static AlarmManager alarmManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setActionListeners();
//        setRetainInstance(true); // ZAPISUJE informacje pomiędzy widokami
        changeImage();
    }
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intentsOpen = new Intent(getActivity(), AlarmReceiver.class);
        intentsOpen.setAction("com.project.my.inz.alarm.ACTION");
        pendingIntent = PendingIntent.getBroadcast(getActivity(),111, intentsOpen, 0);
        alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        db = ((MainActivity)getActivity()).getDataBaseHandler();
        settings = getActivity().getSharedPreferences(MainActivity.PREFS, getActivity().MODE_PRIVATE);

        editor = settings.edit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        globalView = rootView;

        startbutton = (Button) rootView.findViewById(R.id.start_button);
        day_workTV = (TextView) rootView.findViewById(R.id.day_workTV);
        jobTV = (TextView) rootView.findViewById(R.id.jobTV);
        pauseTV = (TextView) rootView.findViewById(R.id.pauseTV);

        setTime();

        return rootView;

    }
    private void setActionListeners() {

            startbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTime();
                    if(!timer_running) {
                        pauseTV.setText(formatTime(pause/1000));
                        timer_running = true;
                        //fireAlarm();
                        jobTV.setTextAppearance(getActivity(), R.style.normalText);
                        countDownTimer = new CountDownTimer(job, 1000) {
                            @Override
                            public void onTick(long leftTimeInMilliseconds) {

                                long seconds = leftTimeInMilliseconds / 1000; //sekundy
                                day_work += 1;

                                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                                    jobTV.setTextAppearance(getActivity(), R.style.blinkText);

                                    if (blink) {
                                        jobTV.setVisibility(View.VISIBLE);
                                        // if blink is true, textview will be visible
                                    } else {
                                        jobTV.setVisibility(View.INVISIBLE);
                                    }

                                    blink = !blink;         // toggle the value of blink
                                }

                                jobTV.setText(formatTime(seconds));
                                day_workTV.setText(formatTime(day_work));
                                // format the textview to show the easily readable format
                            }

                            @Override
                            public void onFinish() {
                                day_work ++;
                                jobTV.setText("Czas na przerwę!");
                                jobTV.setVisibility(View.VISIBLE);
                                ((MainActivity)getActivity()).registerSensor();
                                ((MainActivity)getActivity()).setIsSensorRun(false);

                                countDownTimer_pause = new CountDownTimer(pause, 1000) {

                                    long reactionTime = 0;
                                    int minus_point=10;
                                    int points = settings.getInt("points", 0);


                                    @Override
                                    public void onTick(long leftTimeInMilliseconds) {
                                        long seconds1 = leftTimeInMilliseconds / 1000; //second
                                        pauseTV.setText(formatTime(seconds1));

                                        reactionTime ++;
                                        isSensorRun = ((MainActivity)getActivity()).getIsSensorRun();
                                        if(isSensorRun)
                                            stopAlarm();

                                        Log.d("SENSOR", "RT: " + reactionTime + "; isSensor: " + String.valueOf(isSensorRun));

                                        if(reactionTime%10==0 && reactionTime>=10){ // 10 sec check if phone move
                                            if(!isSensorRun){
                                                minus_point --;
                                                fireAlarm();
                                                Toast.makeText(getActivity(), "Nie używasz telefonu, przestań kodować!", Toast.LENGTH_SHORT).show();}
                                            ((MainActivity)getActivity()).setIsSensorRun(false);
                                        }

                                    }

                                    @Override
                                    public void onFinish() {
                                        if(minus_point>0)
                                            db.addQuest(new QuestM("Dobry pracownik","Popracowane i odpoczętę, brawo!",minus_point+"",""+System.currentTimeMillis(),"10"));
                                        else
                                            db.addQuest(new QuestM("Nie tak miało być","Pasji Ci nie brakuję, ale odpoczynek też jest ważny!",minus_point+"",""+System.currentTimeMillis(),"11"));
                                        points = points + minus_point;
                                        editor.putInt("points", points).commit();
                                        pauseTV.setText("Zacznij pracę!");
                                        jobTV.setTextAppearance(getActivity(), R.style.normalText);
                                        jobTV.setText(formatTime(job/1000));
                                        timer_running = false;
                                        ((MainActivity)getActivity()).unRegisterSensor();
                                        stopAlarm();
                                    }
                                }.start();

                            }
                        }.start();

                    }
                }
            });

    }

    private String formatTime(long seconds){
        return String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60) + " min";
    }
    public void setTime(){//set time from preference
        s_pause = sharedPreferences.getString("pref_break_list","1");
        s_job = sharedPreferences.getString("pref_work_list","1");
        job = Integer.parseInt(s_job) * 1000;
        pause = Integer.parseInt(s_pause) * 1000;
        timeBlinkInMilliseconds = 20 * 1000;

        test = sharedPreferences.getBoolean("PREF_CHECKBOX",false);
        if(test){
            job = 1000 * 5;
            pause = 1000 * 50;
        }

    }
    void cancelTimer() {
        if(countDownTimer != null)
            countDownTimer.cancel();
        if(countDownTimer_pause != null)
            countDownTimer_pause.cancel();
        //countDownTimer_pause.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelTimer();
        stopAlarm();
    }

    public void fireAlarm() {
        /**
         * call broadcost reciver
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pendingIntent);


    }
    public void stopAlarm(){
        alarmManager.cancel(pendingIntent);

    }
    public void changeImage(){
        int points = settings.getInt("points", 0);
        ImageView iv= (ImageView)getActivity().findViewById(R.id.imageView2);
        if(points<10){
            getActivity().setTitle("Przeciętniak");
        }if(points>10){
            iv.setImageResource(R.drawable.lvl1);
            getActivity().setTitle("Myśliciel");
        }if(points>20) {
            iv.setImageResource(R.drawable.lvl2);
            getActivity().setTitle("Zawodowiec");
        }if(points > 40) {
            iv.setImageResource(R.drawable.lvl3);
            getActivity().setTitle("Pan umysłu");
        }if(points > 60) {
            iv.setImageResource(R.drawable.lvl4);
            getActivity().setTitle("Wszechwiedzący");
        }if(points > 80) {
            iv.setImageResource(R.drawable.lvl5);
            getActivity().setTitle("Mistrz");
        }
    }

}
