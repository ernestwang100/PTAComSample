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
import com.asuka.ptacomsample.third.SettingDetailsActivity;

import java.util.ArrayList;

public class SettingListActivity extends AppCompatActivity implements RecyclerViewInterface {
    private ArrayList<SettingListModel> settingListModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button homeBtn, upBtn, downBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        recyclerView = findViewById(R.id.settingrecycle);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);

        setSettingModels();

        SettingListAdapter settingListAdapter = new SettingListAdapter(this, settingListModels, this);
        recyclerView.setAdapter(settingListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingListActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("FragmentIndex", 1);
            startActivity(intent);
//            finish();
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
            settingListModels.add(new SettingListModel(settingNames[i], settingDetails[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SettingListActivity.this, SettingDetailsActivity.class);
//        intent.putExtra("TITLE", settingModels.get(position).getSettingName());
//        intent.putExtra("DETAILS", settingModels.get(position).getSettingDetails());

//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("TITLES", getResources().getStringArray(R.array.setting_full_txt));

        intent.putExtra("TITLES", getResources().getStringArray(R.array.setting_full_txt));
        intent.putExtra("DETAILS", getResources().getStringArray(R.array.setting_details_txt));
        intent.putExtra("INDEX", position);
        startActivity(intent);
    }
}
