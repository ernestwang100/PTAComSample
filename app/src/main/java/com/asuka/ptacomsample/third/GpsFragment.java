package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class GpsFragment extends Fragment {
    private Handler handler;
    private ComPort mPort;
    private RecvThread mRecvThread;
    private byte[] writeData;
    private TextView tv;


//    public GpsFragment() {
//        super();
//        mPort = new ComPort();
//        mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gps, container, false);


        return view;
    }


    public void updateValues(String[] temp) {
        tv = getView().findViewById(R.id.tv);
        tv.setText(temp[0]);
    }

}
