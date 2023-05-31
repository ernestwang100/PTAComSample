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

public class MainFragmentTV7 extends Fragment {
    private TextView tv1, tv2, tv3;
    private ImageButton ib1, ib2, ib3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tv7, container, false);
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

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setPackage("com.google.android.youtube");

                PackageManager packageManager = requireActivity().getPackageManager();
                ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo != null) {
                    startActivity(intent);
                } else {
                    // YouTube app is not installed
                    Toast.makeText(requireContext(), "YouTube app is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setPackage("com.android.phone");

                PackageManager packageManager = requireActivity().getPackageManager();
                ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo != null) {
                    startActivity(intent);
                } else {
                    // Phone app is not installed
                    Toast.makeText(requireContext(), "Phone app is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setPackage("com.android.launcher");

                PackageManager packageManager = requireActivity().getPackageManager();
                ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (resolveInfo != null) {
                    startActivity(intent);
                } else {
                    // Settings app is not installed
                    Toast.makeText(requireContext(), "Settings app is not installed", Toast.LENGTH_SHORT).show();
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
