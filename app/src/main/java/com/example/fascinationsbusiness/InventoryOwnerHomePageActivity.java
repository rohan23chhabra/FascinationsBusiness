package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.core.InventoryRequest;
import com.example.fascinationsbusiness.core.User;
import com.example.fascinationsbusiness.db.DB;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.example.fascinationsbusiness.service.ListViewItemDeleteService;
import com.example.fascinationsbusiness.util.SessionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InventoryOwnerHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InventoryOwner owner;
    String phoneNumber;
    private DatabaseReference databaseReference;
    TextView verifyStatusView;
    ListView listView;
    List<User> userList = new ArrayList<>();
    List<String> userPhoneList = new ArrayList<>();
    List<Integer> userBagsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_owner_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(
                R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(
                R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verifyStatusView = findViewById(R.id.verification_status);
        listView = findViewById(R.id.listview);

        DB.getDatabaseReference().child("pending-inventory-requests")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {


                                Iterator<DataSnapshot> dataSnapshotIterator =
                                        dataSnapshot.getChildren().iterator();
                                userPhoneList.clear();
                                userBagsList.clear();
                                while (dataSnapshotIterator.hasNext()) {
                                    DataSnapshot dataSnapshotChild =
                                            dataSnapshotIterator.next();
                                    final Gson gson = MyGson.getGson();
                                    final InventoryRequest request =
                                            gson.fromJson(gson.toJson(
                                                    dataSnapshotChild
                                                            .getValue()),
                                                    InventoryRequest.class);

                                    final String user = gson
                                            .fromJson(gson.toJson(
                                                    dataSnapshotChild.getKey()),
                                                    String.class);
                                    String loggedInPhoneNumber =
                                            new SessionDetails(
                                                    getApplicationContext())
                                                    .getSharedPreferences()
                                                    .getString("phone",
                                                            "56");
                                    if (loggedInPhoneNumber.equals(request
                                            .getOwnerPhoneNumber())) {
                                        final int[] prevCapacity = {0};
                                        DB.getDatabaseReference()
                                                .child("inventory-owner")
                                                .child(request
                                                        .getOwnerPhoneNumber())
                                                .child("capacity")
                                                .addValueEventListener(
                                                        new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(
                                                                    @NonNull DataSnapshot dataSnapshot) {
                                                                prevCapacity[0] =
                                                                        gson.fromJson(
                                                                                gson.toJson(
                                                                                        dataSnapshot
                                                                                                .getValue()),
                                                                                int.class);
                                                                Log.i("mc-bc-aids",
                                                                        String.valueOf(
                                                                                prevCapacity[0]));
                                                            }

                                                            @Override
                                                            public void onCancelled(
                                                                    @NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                        userPhoneList.add(user);
                                        userBagsList
                                                .add(request.getNumberOfBags());
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Do something after 100ms
                                                final Intent intent =
                                                        new Intent(
                                                                InventoryOwnerHomePageActivity.this,
                                                                ListViewItemDeleteService.class);
                                                intent.putExtra(
                                                        "user-phone-number",
                                                        user);
                                                intent.putExtra("add-capacity",
                                                        request.getNumberOfBags());
                                                intent.putExtra(
                                                        "current-time-millis",
                                                        request.getCurrentTimeMillis());
                                                intent.putExtra(
                                                        "owner-phone-number",
                                                        request.getOwnerPhoneNumber());
                                                intent.putExtra("prev-capacity",
                                                        prevCapacity[0]);
                                                startService(intent);
                                            }
                                        }, 2000);
                                    }
                                }
                                //final Semaphore userListSemaphore = new Semaphore(0);
                                for (int i = 0;
                                        i < userBagsList.size();
                                        i++) {
                                    String userPhone = userPhoneList.get(i);
                                    final int userBag = userBagsList.get(i);
                                    DB.getDatabaseReference().child("users")
                                            .child(userPhone)
                                            .addValueEventListener(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(
                                                                @NonNull DataSnapshot dataSnapshot) {
                                                            Gson gson =
                                                                    MyGson.getGson();
                                                            User user =
                                                                    gson.fromJson(
                                                                            gson.toJson(
                                                                                    dataSnapshot
                                                                                            .getValue()),
                                                                            User.class);
                                                            user.setNumberOfBags(
                                                                    userBag);
                                                            userList.add(user);
                                                        }

                                                        @Override
                                                        public void onCancelled(
                                                                @NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                    //userListSemaphore.release();
                                }
//                        try {
//                            userListSemaphore.acquire();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        CustomAdapter customAdapter =
                                                new CustomAdapter(
                                                        InventoryOwnerHomePageActivity.this,
                                                        userList);
                                        listView.setAdapter(customAdapter);
                                    }
                                }, 1000);

                            }

                            @Override public void onCancelled(
                                    @NonNull DatabaseError databaseError) {


                            }
                        });


        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            phoneNumber = new SessionDetails(this).getSharedPreferences()
                    .getString("phone", "89");
        } else {
            phoneNumber = bundle.getString("phone");
        }
        databaseReference.child("inventory-owner").child(phoneNumber)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                Gson gson = MyGson.getGson();
                                owner =
                                        gson.fromJson(gson.toJson(
                                                dataSnapshot.getValue()),
                                                InventoryOwner.class);
                                Log.i("owner", owner.toString());
                                Log.i("owner", "got owner");
                                verifyStatusView.setVisibility(View.VISIBLE);
                                if (owner.getVerified().equals("true")) {
                                    verifyStatusView.setText("Verified Owner");
                                } else {
                                    verifyStatusView
                                            .setText("Not yet verified");
                                }
                            }

                            @Override public void onCancelled(
                                    @NonNull DatabaseError databaseError) {

                            }
                        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inventory_owner_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(
                    "Session",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_opening_time) {
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,
                    SetOpeningTime.class);
            intent.putExtra("phonenumber", phoneNumber);
            startActivity(intent);
        } else if (id == R.id.nav_closing_time) {
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,
                    SetClosingTime.class);
            intent.putExtra("phonenumber", phoneNumber);
            startActivity(intent);

        } else if (id == R.id.nav_close_business) {
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,
                    TemporaryShutDownActivity.class);
            intent.putExtra("phonenumber", phoneNumber);
            startActivity(intent);
        } else if (id == R.id.nav_set_price) {
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,
                    SetPriceActivity.class);
            intent.putExtra("phone", phoneNumber);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
