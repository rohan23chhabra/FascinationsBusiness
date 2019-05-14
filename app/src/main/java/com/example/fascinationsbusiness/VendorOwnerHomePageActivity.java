package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fascinationsbusiness.core.User;
import com.example.fascinationsbusiness.core.VendorOwner;
import com.example.fascinationsbusiness.core.VendorTransaction;
import com.example.fascinationsbusiness.db.DB;
import com.example.fascinationsbusiness.net.QRCodeFetchCallback;
import com.example.fascinationsbusiness.net.UserFetchCallback;
import com.example.fascinationsbusiness.net.UserPhoneFetchCallback;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.example.fascinationsbusiness.util.SessionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class VendorOwnerHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<User> userList = new ArrayList<>();
    List<String> userPhoneList = new ArrayList<>();
    TextView verifyStatusView;
    ListView listView;
    ProgressBar progressBar;
    final Gson gson = MyGson.getGson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_owner_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);
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

        NavigationView navigationView =
                (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //verifyStatusView = findViewById(R.id.vendor_verification_status);
        listView = findViewById(R.id.user_list);
        progressBar = findViewById(R.id.vendor_on_users_list_load);

        progressBar.setVisibility(View.VISIBLE);
        populateUserPhoneNumberList(new UserPhoneFetchCallback() {
            @Override public void onUsersPhoneFetched(List<String> userPhoneList) {
                populateUserList(userPhoneList.size(), new UserFetchCallback() {
                    @Override public void onUsersFetched(List<User> userList) {
                        VendorListAdapter vendorListAdapter =
                                new VendorListAdapter(VendorOwnerHomePageActivity.this,
                                        userList);
                        listView.setAdapter(vendorListAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void populateUserPhoneNumberList(final UserPhoneFetchCallback userPhoneFetchCallback) {
        DB.getDatabaseReference().child("vendor-transaction")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshotChild : dataSnapshot
                                        .getChildren()) {
                                    final VendorTransaction vendorTransaction =
                                            gson.fromJson(gson.toJson(
                                                    dataSnapshotChild
                                                            .getValue()),
                                                    VendorTransaction.class);
                                    String userPhoneNumber = gson
                                            .fromJson(gson.toJson(
                                                    dataSnapshotChild.getKey()),
                                                    String.class);
                                    String loggedInPhoneNumber =
                                            new SessionDetails(
                                                    VendorOwnerHomePageActivity.this)
                                                    .getSharedPreferences()
                                                    .getString("phone",
                                                            "56");
                                    if (loggedInPhoneNumber.equalsIgnoreCase(
                                            vendorTransaction
                                                    .getVendorPhoneNumber())) {
                                        userPhoneList.add(userPhoneNumber);
                                    }
                                }
                                userPhoneFetchCallback.onUsersPhoneFetched(userPhoneList);
                            }

                            @Override public void onCancelled(
                                    @NonNull DatabaseError databaseError) {
                            }
                        });
    }

    private void populateUserList(final int numberOfUsers,
                                  final UserFetchCallback userFetchCallback) {
        final int[] count = {0};
        for (String userPhone : userPhoneList) {
            DB.getDatabaseReference()
                    .child("users")
                    .child(userPhone)
                    .addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(
                                        @NonNull DataSnapshot dataSnapshot) {
                                    User user =
                                            gson.fromJson(
                                                    gson.toJson(
                                                            dataSnapshot
                                                                    .getValue()),
                                                    User.class);
                                    userList.add(
                                            user);
                                    count[0]++;
                                    if (count[0] == numberOfUsers) {
                                        userFetchCallback.onUsersFetched(userList);
                                    }
                                }

                                @Override
                                public void onCancelled(
                                        @NonNull DatabaseError databaseError) {
                                }
                            });
        }
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
        getMenuInflater().inflate(R.menu.vendor_owner_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences sharedPreferences = getSharedPreferences(
                    "Session",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("phone");
            editor.remove("password");
            editor.remove("selectedOwner");
            editor.apply();
            Intent intent = new Intent(VendorOwnerHomePageActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.show_qr_code) {
            String ownerPhoneNumber = new SessionDetails(this).getSharedPreferences().getString(
                    "phone", "");
            fetchQRCode(ownerPhoneNumber, new QRCodeFetchCallback() {
                @Override public void onQRCodeFetched(String qrCodeUrl) {
                    Intent intent =
                            new Intent(VendorOwnerHomePageActivity.this, ShowQRCodeActivity.class);
                    intent.putExtra("qr-code-url", qrCodeUrl);
                    VendorOwnerHomePageActivity.this.startActivity(intent);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchQRCode(String ownerPhoneNumber, final QRCodeFetchCallback callback) {
        DB.getDatabaseReference().child("vendor-owner").child(ownerPhoneNumber)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Gson gson = MyGson.getGson();
                                VendorOwner vendorOwner =
                                        gson.fromJson(gson.toJson(dataSnapshot.getValue()),
                                                VendorOwner.class);
                                String qrCodeUrl = vendorOwner.getQrCodeURL();
                                callback.onQRCodeFetched(qrCodeUrl);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
