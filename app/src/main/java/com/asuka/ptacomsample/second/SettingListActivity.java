package com.asuka.ptacomsample.second;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.main.MainActivity;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;
import com.asuka.ptacomsample.third.SettingDetailsActivity;

import java.util.ArrayList;

public class SettingListActivity extends AppCompatActivity implements RecyclerViewInterface {
    ArrayList<SettingListModel> settingListModels = new ArrayList<>();
    RecyclerView recyclerView;
    Button homeBtn, upBtn, downBtn;
    private Handler handler;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private byte[] writeData;
    private String[] temp;
    private static final String TAG = "SettingListActivity";
    private int[] images = {R.drawable.baseline_flare_48, R.drawable.baseline_people_48, R.drawable.baseline_access_time_48, R.drawable.baseline_print_48, R.drawable.baseline_file_download_48, R.drawable.baseline_access_time_48, R.drawable.baseline_drive_eta_48, R.drawable.baseline_drive_eta_48, R.drawable.baseline_access_time_48, R.drawable.baseline_lightbulb_circle_48, R.drawable.baseline_info_48};

    public SettingListActivity(){
        super();
        mPort = new ComPort();
        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        recyclerView = findViewById(R.id.settingrecycle);
        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
//        writeData = "$LCD+PAGE=99".getBytes();
//        mPort.write(writeData, writeData.length);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                temp = msg.obj.toString().split(",");
            }
        };

        mRecvThread = new RecvThread(handler, mPort, this);
        mRecvThread.start();


        setSettingModels();

        SettingListAdapter settingListAdapter = new SettingListAdapter(this, settingListModels, this);
        recyclerView.setAdapter(settingListAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));


        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(SettingListActivity.this, MainActivity.class);
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
//            settingListModels.add(new SettingListModel(settingNames[i], settingDetails[i]));

            settingListModels.add(new SettingListModel(settingNames[i], images[i]));
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
        intent.putExtra("INDEX", position);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecvThread.interrupt();
        mPort.close();
    }
}
