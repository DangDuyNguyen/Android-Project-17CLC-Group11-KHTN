package com.ndduy.gamecollection2020;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

import java.util.ArrayList;

class UpdateStatus extends Service {

    boolean isRunning;

    public UpdateStatus() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId){
        int hungry_stat = intent.getIntExtra("hungryStat", 0);
        int flatter_stat = intent.getIntExtra("flatterStat", 0);
        int sleepy_stat = intent.getIntExtra("sleepStat", 0);
        int mood_stat = intent.getIntExtra("moodStat", 0);

        updateStatus(hungry_stat, flatter_stat, sleepy_stat, mood_stat);

        return START_STICKY;
    }

    public void updateStatus(int hungry_stat, final int flatter_stat, final int sleepy_stat, final int mood_stat)
    {
        int new_hungry_stat = hungry_stat;
        int new_flatter_stat = flatter_stat;
        int new_sleepy_stat = sleepy_stat;
        int new_mood_stat = mood_stat;

        while (new_hungry_stat > 0 || new_flatter_stat > 0 || new_sleepy_stat > 0 || new_mood_stat > 0) {

            if (new_hungry_stat > 0){
                new_hungry_stat--;
            }
            if (new_flatter_stat > 0){
                new_flatter_stat--;
            }
            if (new_sleepy_stat > 0){
                new_sleepy_stat--;
            }
            if (new_mood_stat > 0)
                new_mood_stat--;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
