package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingActivity;

public class DetailsActivity extends AppCompatActivity {
    private Button homeBtn, upBtn, downBtn;
    private TextView titleTV, detailsTV;
    private String[] titles;
    private String[] details;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent it = getIntent();
        titles = it.getStringArrayExtra("TITLES");
        details = it.getStringArrayExtra("DETAILS");
        currentIndex = it.getIntExtra("INDEX", 0);
        Log.d("titles", String.valueOf(titles));
        Log.d("details", String.valueOf(details));
        Log.d("currentIndex", String.valueOf(currentIndex));


        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
        titleTV = findViewById(R.id.titleTV);
        detailsTV = findViewById(R.id.detailsTV);

        updateViews();

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(DetailsActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        upBtn.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateViews();
            }
        });

        downBtn.setOnClickListener(v -> {
            if (currentIndex < titles.length - 1) {
                currentIndex++;
                updateViews();
            }
        });
    }

    private void updateViews() {
        titleTV.setText(titles[currentIndex]);
        detailsTV.setText(details[currentIndex]);
    }
}