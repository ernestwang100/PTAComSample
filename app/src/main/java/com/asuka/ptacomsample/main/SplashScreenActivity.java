package com.asuka.ptacomsample.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.asuka.ptacomsample.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        GifImageView gifImageView = findViewById(R.id.gifImageView);
        // Load and display the GIF file
        try {
            byte[] bytes = readGifBytes("jasslin_opener.gif");
            GifDrawable gifDrawable = new GifDrawable(bytes);
            gifImageView.setImageDrawable(gifDrawable);
            gifDrawable.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, 8500);
    }

    private byte[] readGifBytes(String fileName) throws IOException {
        InputStream inputStream = getAssets().open(fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }
}
