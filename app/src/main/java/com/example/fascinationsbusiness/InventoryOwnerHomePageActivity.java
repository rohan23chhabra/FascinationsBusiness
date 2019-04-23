package com.example.fascinationsbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fascinationsbusiness.core.InventoryOwner;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class InventoryOwnerHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InventoryOwner owner;
    String phoneNumber;
    private DatabaseReference databaseReference;
    TextView verifyStatusView;

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
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        phoneNumber = bundle.getString("phone");
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
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,SetOpeningTime.class);
            intent.putExtra("phonenumber",phoneNumber);
            startActivity(intent);
        } else if (id == R.id.nav_closing_time) {
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,SetClosingTime.class);
            intent.putExtra("phonenumber",phoneNumber);
            startActivity(intent);

        } else if (id == R.id.nav_close_business) {
            Intent intent = new Intent(InventoryOwnerHomePageActivity.this,TemporaryShutDownActivity.class);
            intent.putExtra("phonenumber",phoneNumber);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
