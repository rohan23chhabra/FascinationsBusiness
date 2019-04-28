package com.example.fascinationsbusiness;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fascinationsbusiness.core.Bill;
import com.example.fascinationsbusiness.core.VendorTransaction;
import com.example.fascinationsbusiness.db.DB;

import java.util.Map;

public class ViewOrderActivity extends AppCompatActivity {

    private VendorTransaction vendorTransaction;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        linearLayout = findViewById(R.id.menu_linear_layout);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        vendorTransaction =
                (VendorTransaction) bundle
                        .getSerializable("vendor-transaction");
        populateOrderDetails();
    }

    private void populateOrderDetails() {
        for (Map.Entry element : vendorTransaction.getOrderMap().entrySet()) {
            String item = (String) element.getKey();
            Integer price = (Integer) element.getValue();
            TextView textView = new TextView(this);
            textView.setText(item + " - Rs. " + price);
            linearLayout.addView(textView);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            Resources r = getResources();
            int pixels = (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f,
                            r.getDisplayMetrics());
            layoutParams.topMargin = pixels;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        }
    }

    public void generateBillOnClick(View view) {
        Bill bill = new Bill(vendorTransaction.getTransactionId(),
                vendorTransaction.getOrderMap(), vendorTransaction.getAmount());
        DB.getDatabaseReference().child("bills").child(String.valueOf(bill.getTransactionId()))
                .setValue(bill);
        Toast.makeText(this, "Bill generated.", Toast.LENGTH_SHORT).show();
    }
}
