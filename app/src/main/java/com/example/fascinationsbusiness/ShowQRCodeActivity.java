package com.example.fascinationsbusiness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class ShowQRCodeActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        imageView = findViewById(R.id.set_qr_code);
        progressBar = findViewById(R.id.qr_code_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String qrCodeUrl = bundle.getString("qr-code-url");

        Picasso.get().load(qrCodeUrl).into(imageView);
        progressBar.setVisibility(View.GONE);
    }
}
