package com.example.fascinationsbusiness;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fascinationsbusiness.core.User;
import com.example.fascinationsbusiness.core.VendorTransaction;
import com.example.fascinationsbusiness.db.DB;
import com.example.fascinationsbusiness.serialize.MyGson;
import com.example.fascinationsbusiness.util.SessionDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorListAdapter extends ArrayAdapter<User> {
    private ImageView imageView;
    private TextView phoneNumberView;
    private TextView nameView;
    private Activity context;
    private List<User> userList;
    private Button viewOrderButton;
    private VendorTransaction vendorTransaction;

    public VendorListAdapter(Activity context, List<User> userList) {
        super(context, R.layout.vendor_transaction_view);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.vendor_transaction_view, null
                , true);

        imageView = convertView.findViewById(R.id.vendor_user_photo);
        phoneNumberView = convertView.findViewById(R.id.show_user_phone_vendor);
        nameView = convertView.findViewById(R.id.show_user_name_vendor);
        viewOrderButton = convertView.findViewById(R.id.view_order_by_user);


        final User user = userList.get(position);
        Picasso.get().load(user.getImageURL())
                .into(imageView);

        final String ownerPhoneNumber =
                new SessionDetails(context).getSharedPreferences().getString(
                        "phone", "");

        DB.getDatabaseReference().child("vendor-transaction").addValueEventListener(
                new ValueEventListener() {
                    @Override public void onDataChange(
                            @NonNull DataSnapshot dataSnapshot) {
                        Gson gson = MyGson.getGson();
                        for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                            VendorTransaction vendorTransaction = gson.fromJson(
                                    gson.toJson(dataSnapshotChild.getValue()),
                                    VendorTransaction.class);
                            if (vendorTransaction.getVendorPhoneNumber()
                                    .equalsIgnoreCase(ownerPhoneNumber)) {
                                VendorListAdapter.this.vendorTransaction = vendorTransaction;
                            }
                        }
                    }

                    @Override public void onCancelled(
                            @NonNull DatabaseError databaseError) {

                    }
                });

        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context, ViewOrderActivity.class);
                intent.putExtra("vendor-transaction", vendorTransaction);
                context.startActivity(intent);
            }
        });


        nameView.setText("User Name: " + user.getName());
        phoneNumberView.setText("Phone: " + user.getPhoneNumber());
        return convertView;
    }

    @Override public int getCount() {
        return userList.size();
    }
}
