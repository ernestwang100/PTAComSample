package com.asuka.ptacomsample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    ArrayList<SettingModel> settingModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.settingrecycle);

        setSettingModels();

        SettingAdapter settingAdapter = new SettingAdapter(this, settingModels);
        recyclerView.setAdapter(settingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setSettingModels() {
        String[] settingNames = getResources().getStringArray(R.array.setting_full_txt);

        for (int i = 0; i < settingNames.length; i++) {
            settingModels.add(new SettingModel(settingNames[i]));
        }
    }

}
