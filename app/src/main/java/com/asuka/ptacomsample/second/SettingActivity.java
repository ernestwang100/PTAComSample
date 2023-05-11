package com.asuka.ptacomsample.second;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asuka.ptacomsample.main.MainActivity;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.third.DetailsActivity;

import java.util.ArrayList;
import java.util.Set;

public class SettingActivity extends AppCompatActivity implements RecyclerViewInterface {
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

        SettingAdapter settingAdapter = new SettingAdapter(this, settingModels, this);
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
        String[] settingDetails = getResources().getStringArray(R.array.setting_details_txt);

        Log.d("settingNames", String.valueOf(settingNames.length));
        for (int i = 0; i < settingNames.length; i++) {
            settingModels.add(new SettingModel(settingNames[i], settingDetails[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SettingActivity.this, DetailsActivity.class);
//        intent.putExtra("TITLE", settingModels.get(position).getSettingName());
//        intent.putExtra("DETAILS", settingModels.get(position).getSettingDetails());

        intent.putExtra("TITLES", getResources().getStringArray(R.array.setting_full_txt));
        intent.putExtra("DETAILS", getResources().getStringArray(R.array.setting_details_txt));
        intent.putExtra("INDEX", position);
        startActivity(intent);
    }
}
