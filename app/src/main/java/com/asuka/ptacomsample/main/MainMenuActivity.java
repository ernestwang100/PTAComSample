package com.asuka.ptacomsample.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.asuka.ptacomsample.R;

public class MainMenuActivity extends AppCompatActivity {
    private Button homeBtn;
    private Button upBtn;
    private Button downBtn;
    private Button confirmBtn;
    private Button printBtn;
    private MainMenuFragmentTV1 mainMenuFragmentTV1;
    private byte itemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        homeBtn = findViewById(R.id.homeBtn);
        upBtn = findViewById(R.id.upBtn);
        downBtn = findViewById(R.id.downBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        printBtn = findViewById(R.id.printBtn);

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainMenuActivity.this, MainActivity.class);
            startActivity(intent);
        });


        mainMenuFragmentTV1 = new MainMenuFragmentTV1();
        getSupportFragmentManager().beginTransaction().add(R.id.mainMenuFrameLayout, mainMenuFragmentTV1, "Main Menu List").commit();


        upBtn.setOnClickListener(v -> {
            if (itemIndex < 0)
                itemIndex = 4;
            else if (itemIndex > 3)
                itemIndex = 1;
            mainMenuFragmentTV1.setTextViewBold(--itemIndex);
        });
        downBtn.setOnClickListener(v -> {
            if (itemIndex < 0)
                itemIndex = 2;
            else if (itemIndex > 3)
                itemIndex = -1;
            mainMenuFragmentTV1.setTextViewBold(++itemIndex);
        });
    }
}
