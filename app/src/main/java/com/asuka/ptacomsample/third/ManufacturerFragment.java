package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class ManufacturerFragment extends Fragment {
    private TextView tv;
    private String cmd, cmdEnd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manufacturer, container, false);

        tv = view.findViewById(R.id.tv);

        cmdEnd = "\r\n";


        return view;
    }

    public String getCmd() {
        cmd = "$LCD+MANUFACTURER=" + tv.getText().toString() + cmdEnd;
        return cmd;
    }

    public void updateValues(String[] values) {
//        tv.setText(values[0]);
    }
}
