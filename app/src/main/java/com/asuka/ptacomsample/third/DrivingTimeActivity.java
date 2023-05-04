package com.asuka.ptacomsample.third;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingActivity;

public class DrivingTimeActivity extends AppCompatActivity {
    Button homeBtn, upBtn, downBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_time);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(DrivingTimeActivity.this, SettingActivity.class);
            startActivity(intent);
        });

    }
}