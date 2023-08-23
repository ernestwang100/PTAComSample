package com.asuka.ptacomsample.third;


import android.app.Dialog;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class WaitingFragment extends DialogFragment {
    private TextView tv;
    private static final String TAG = "WaitingFragment";
    private String mStr;

        @NonNull
        @Override
        public Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.fragment_waiting, null);

            tv = view.findViewById(R.id.waiting_tv);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                tv.setText(mStr);
            }, 1000);

            // Disable dialog cancellation
//            setCancelable(false);

            builder.setView(view);
            return builder.create();

        }

    public void setmStr(String mStr) {
        this.mStr = mStr;
    }
}
