package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class DownloadFragment extends Fragment {
    private RadioGroup radioGroupDownload;
    private String cmd;
    private DatePickerFragment datePickerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        radioGroupDownload = view.findViewById(R.id.radioGroupDownload);
        radioGroupDownload.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.download1RB:
                    cmd = "$LCD+DOWNLOAD=24";
                    break;
                case R.id.download2RB:
                    showDatePickerDialog();
                    break;
                case R.id.download3RB:
                    cmd = "$LCD+DOWNLOAD=NOW";
                    break;

            }
        });
        return view;
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSelectedListener((year, month, day) -> {
            // Handle the selected date
            String selectedDate = String.format("%04d%02d%02d", year, month + 1, day);
            RadioButton download2RB = getView().findViewById(R.id.download2RB);
            download2RB.setText(selectedDate + " 下載");
            
            cmd = "$LCD+DOWNLOAD=" + selectedDate;
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    public String getCmd() {
        return cmd;
    }

    @Override
    public void onResume() {
        super.onResume();
//        TODO
//        tv1.setText("資料讀取中...");
    }
}

