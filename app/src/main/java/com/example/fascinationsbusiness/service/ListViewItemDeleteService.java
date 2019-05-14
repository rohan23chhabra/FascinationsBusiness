package com.example.fascinationsbusiness.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.fascinationsbusiness.concurrent.DeleteInventoryRequestTask;

import java.util.Date;
import java.util.Timer;

public class ListViewItemDeleteService extends Service {
    public ListViewItemDeleteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String userPhoneNumber = intent.getStringExtra("user-phone-number");
        int capacityToBeAdded = intent.getIntExtra("add-capacity", 0);
        long currentTimeMillis = intent.getLongExtra("current-time-millis", 0);
        String ownerPhoneNumber = intent.getStringExtra("owner-phone-number");
        int prevCapacity = intent.getIntExtra("prev-capacity", 0);
        int requestId = intent.getIntExtra("request-id", 0);
        Log.i("mc-add", String.valueOf(capacityToBeAdded));
        Log.i("mc-prev", String.valueOf(prevCapacity));
        Date date = new Date(currentTimeMillis + 24 * 60 * 60 * 1000);
        Timer timer = new Timer();
        timer.schedule(
                new DeleteInventoryRequestTask(userPhoneNumber, capacityToBeAdded,
                        ownerPhoneNumber, prevCapacity, requestId),
                date);
        return super.onStartCommand(intent, flags, startId);
    }
}
