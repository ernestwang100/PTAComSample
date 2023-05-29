package com.asuka.ptacomsample.third;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.WindowManager.LayoutParams;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;


//Reference https://www.c-sharpcorner.com/UploadFile/1e5156/change-brightness-of-a-screen-in-using-seekbar-in-android-st/#:~:text=To%20get%20the%20brightness%20of,the%20brightness%20of%20a%20screen.
public class BrightnessFragment extends Fragment {
    private SeekBar seekBar;
    private int brightness;
    private ContentResolver cResolver;
    private Window window;
    private TextView txtPerc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brightness, container, false);

        seekBar = view.findViewById(R.id.seekBar);
        txtPerc = view.findViewById(R.id.txtPercentage);

        cResolver = getActivity().getContentResolver();
        window = getActivity().getWindow();

        seekBar.setMax(255);
        seekBar.setKeyProgressIncrement(1);

        try {
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        seekBar.setProgress(brightness);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                LayoutParams layoutpars = window.getAttributes();  // Update this line
                layoutpars.screenBrightness = brightness / (float) 255;
                window.setAttributes(layoutpars);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing handled here
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 20) {
                    brightness = 20;
                } else {
                    brightness = progress;
                }
                float perc = (brightness / (float) 255) * 100;
                txtPerc.setText((int) perc + " %");
            }
        });

        return view;
    }
}
