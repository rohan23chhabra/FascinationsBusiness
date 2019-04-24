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

class CustomAdapter extends ArrayAdapter<User> {

    private ImageView imageView;
    private TextView phoneNumberView;
    private TextView nameView;
    private TextView bagsView;
    private Activity context;
    private List<User> userList;

    public CustomAdapter(Activity context, List<User> userList) {
        super(context, R.layout.inventory_request_view);
        this.context = context;
        this.userList = userList;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.inventory_request_view, null, true);

        imageView = convertView.findViewById(R.id.inventory_user_photo);
        phoneNumberView = convertView.findViewById(R.id.show_user_phone);
        nameView = convertView.findViewById(R.id.show_user_name);
        bagsView = convertView.findViewById(R.id.show_user_bags);

        final User user = userList.get(position);
        Picasso.get().load(user.getImageURL())
                .into(imageView);

        nameView.setText("User Name: " + user.getName());
        phoneNumberView.setText("Phone: " + user.getPhoneNumber());
        bagsView.setText("Number of bags : " + user.getNumberOfBags());

        return convertView;
    }

    @Override public int getCount() {
        return userList.size();
    }
}
