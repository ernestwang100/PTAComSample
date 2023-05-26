package com.asuka.ptacomsample.third;


import android.app.Dialog;
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
    private ComPort mPort;
    private byte[] readBuf = new byte[200];
    private int mCount;
    private TextView tv;
    private static final String TAG = "WaitingFragment";

        public WaitingFragment() {
            mPort = new ComPort();
            mPort.open(5, ComPort.BAUD_115200, 8, 'N', 1);
        }


        @NonNull
        @Override
        public Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.fragment_waiting, null);

            tv = view.findViewById(R.id.waiting_tv);


            mCount = mPort.read(readBuf, readBuf.length);
            Log.d(TAG, "onViewCreated: mCount = " + mCount);

            // Disable dialog cancellation
//            setCancelable(false);

            thread.start();
            builder.setView(view);
            return builder.create();

        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    mCount = mPort.read(readBuf, readBuf.length);
                    if (mCount > 0) {
                        String messageText = new String(readBuf, 0, mCount);
                        Log.d(TAG, "run: messageText = " + messageText);
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(messageText);
                            }
                        });

                        if (messageText.contains("OK")) {
                            break;
                        }
                    }
                }
            }
        });

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Received = run onPause()");
        Log.d(TAG, "Received = current thread: " + Thread.currentThread().toString());
        thread.interrupt();
    }









}
