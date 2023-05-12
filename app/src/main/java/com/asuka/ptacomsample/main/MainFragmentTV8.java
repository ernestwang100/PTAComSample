package com.asuka.ptacomsample.main;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFragmentTV8 extends Fragment {
    private TextView tv8;
    private Handler handler;
    private String messageText = "NULL";
    private ExecutorService executorService;
    private int round = 0;

    public MainFragmentTV8(ExecutorService executorService) {
        super();
        this.executorService = executorService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv8, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv8 = (TextView) view.findViewById(R.id.mainTV_8);

        //executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (messageText.equals("5")) {
                    if(getActivity() == null)
                        return;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Alert")
                            .setIcon(R.drawable.baseline_info_24)
                            .setCancelable(false)
                            .setMessage("Number count is 5. Reset the number?")
                            .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    round = 0;
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
                tv8.setText("目前數值：" + messageText);
            }
        };


        executorService.execute(() -> {
            while (true) {
                if (round == 10)
                    round = 0;
                try {
                    sleep(1000);
                } catch (Exception e) {
                    Log.i("Thread: ", e.toString());
                }
                messageText = String.valueOf(++round);
                Message msg = Message.obtain();
                msg.what = 7;
                handler.sendMessage(msg);
            }
        });
    }
}
