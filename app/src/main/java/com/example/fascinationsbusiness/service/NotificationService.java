package com.example.fascinationsbusiness.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.fascinationsbusiness.InventoryOwnerHomePageActivity;
import com.example.fascinationsbusiness.R;
import com.example.fascinationsbusiness.core.InventoryRequest;
import com.example.fascinationsbusiness.db.DB;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.example.fascinationsbusiness.util.SessionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Iterator;

public class NotificationService extends Service {

    @Override public void onCreate() {
        super.onCreate();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {

        DB.getDatabaseReference().child("pending-inventory-requests").addValueEventListener(
                new ValueEventListener() {
                    @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Iterator<DataSnapshot> dataSnapshotIterator =
                                dataSnapshot.getChildren().iterator();
                        while (dataSnapshotIterator.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshotIterator.next();
                            Gson gson = MyGson.getGson();
                            Log.i("mera-json", gson.toJson(dataSnapshotChild.getValue()));
                            InventoryRequest request =
                                    gson.fromJson(gson.toJson(dataSnapshotChild.getValue()),
                                            InventoryRequest.class);
                            String user = gson.fromJson(gson.toJson(dataSnapshotChild.getKey()),
                                    String.class);
                            String loggedInPhoneNumber =
                                    new SessionDetails(getApplicationContext())
                                            .getSharedPreferences().getString("phone",
                                            "56");
                            if (loggedInPhoneNumber.equals(request.getOwnerPhoneNumber())) {
                                // iss owner k liye notification aaya hai
                                // notification fire.
                                fireNotification();
                            }
                        }
                    }

                    @Override public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    private void fireNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("PENDING_INVENTORY_REQUEST",
                    "PENDING_INVENTORY",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Chutiyapa kyon itna??");
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "PENDING_INVENTORY_REQUEST")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Inventory Request")
                .setAutoCancel(true)
                .setContentText("A User wants to connect with you.");
        Intent intent = new Intent(getApplicationContext(), InventoryOwnerHomePageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(1, builder.build());
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
