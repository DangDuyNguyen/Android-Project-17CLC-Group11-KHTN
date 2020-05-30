package com.ndduy.gamecollection2020;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

public class updateStatus extends Service {
    ArrayList<Status> statuses = new ArrayList<>();
    public updateStatus(ArrayList<Status> statuses) {
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
    public int onStartCommand(Intent intent, int flags, final int startId) {
        new Thread(new Runnable(){
            public void run() {
                while (true) {
                //    statuses.get(0).decrease();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
