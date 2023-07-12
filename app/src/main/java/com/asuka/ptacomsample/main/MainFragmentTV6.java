package com.asuka.ptacomsample.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;

public class MainFragmentTV6 extends Fragment {
    private TextView tv1, tv2, tv3;
    private ImageButton ib1, ib2, ib3;
    private  String pk_fail, pk1, pk2, pk3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv6, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        ib1 = view.findViewById(R.id.ib1);
        ib2 = view.findViewById(R.id.ib2);
        ib3 = view.findViewById(R.id.ib3);

        tv1.setText("Youtube");
        tv2.setText("Phone Call");
        tv3.setText("Settings");
        ib1.setImageResource(R.drawable.youtube);
        ib2.setImageResource(R.drawable.phone);
        ib3.setImageResource(R.drawable.gear);

        pk_fail = " cannot be opened";
        pk1 = "com.google.android.youtube";
        pk2 = "com.asuka.radio";
        pk3 = "com.android.launcher";

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setPackage(pk1);

                PackageManager packageManager = requireActivity().getPackageManager();
                ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo != null) {
                    startActivity(intent);
                } else {
                    // YouTube app is not installed
                    Toast.makeText(requireContext(), pk1 + pk_fail , Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(pk2);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    // Sound Recorder app is not installed or cannot be opened
                    // Handle this case accordingly
                    Toast.makeText(requireContext(), pk2 + pk_fail, Toast.LENGTH_SHORT).show();
                }

//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setPackage(pk2);
//
//                PackageManager packageManager = requireActivity().getPackageManager();
//                ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                if (resolveInfo != null) {
//                    startActivity(intent);
//                } else {
//                    // Phone app is not installed
//                    Toast.makeText(requireContext(), pk2 + pk_fail, Toast.LENGTH_SHORT).show();
//                }
            }
        });

        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setPackage(pk3);

                PackageManager packageManager = requireActivity().getPackageManager();
                ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo != null) {
                    startActivity(intent);
                } else {
                    // Settings app is not installed
                    Toast.makeText(requireContext(), pk3 + pk_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });


//        Intent intent=null;
//        final PackageManager packageManager=requireActivity().getPackageManager();
//        for(final ResolveInfo resolveInfo:packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY))
//        {
//            if(!requireActivity().getPackageName().equals(resolveInfo.activityInfo.packageName))  //if this activity is not in our activity (in other words, it's another default home screen)
//            {
//                intent=packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName));
//                break;
//            }
//        }



    }



    private boolean appInstalled(String uri){
        PackageManager pm = requireActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
