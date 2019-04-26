package com.example.fascinationsbusiness;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fascinationsbusiness.core.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorListAdapter extends ArrayAdapter<User> {
    private ImageView imageView;
    private TextView phoneNumberView;
    private TextView nameView;
    private Activity context;
    private List<User> userList;

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

        final User user = userList.get(position);
        Picasso.get().load(user.getImageURL())
                .into(imageView);




        nameView.setText("User Name: " + user.getName());
        phoneNumberView.setText("Phone: " + user.getPhoneNumber());
        return convertView;
    }

    @Override public int getCount() {
        return userList.size();
    }
}
