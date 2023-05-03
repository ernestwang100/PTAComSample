package com.asuka.ptacomsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    ArrayList<SettingModel> settingModels = new ArrayList<>();
    RecyclerView recyclerView;
    Button homeBtn, upBtn, downBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        recyclerView = findViewById(R.id.settingrecycle);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);

        setSettingModels();

        SettingAdapter settingAdapter = new SettingAdapter(this, settingModels);
        recyclerView.setAdapter(settingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(SettingActivity.this, MainActivity.class);
            startActivity(intent);
        });
        upBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                recyclerView.scrollBy(0, -100);
            }
        });

        downBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                recyclerView.scrollBy(0, 100);
            }
        });
    }


    private void setSettingModels() {
        String[] settingNames = getResources().getStringArray(R.array.setting_full_txt);

        Log.d("settingNames", String.valueOf(settingNames.length));
        for (int i = 0; i < settingNames.length; i++) {
            settingModels.add(new SettingModel(settingNames[i]));
        }
    }

}
