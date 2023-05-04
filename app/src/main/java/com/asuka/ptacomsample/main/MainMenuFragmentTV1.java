package com.asuka.ptacomsample.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class MainMenuFragmentTV1 extends Fragment {
    private TextView[] textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_menu_tv1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = new TextView[4];
        textView[0] = (TextView) view.findViewById(R.id.mainMenuText1);
        textView[1] = (TextView) view.findViewById(R.id.mainMenuText2);
        textView[2] = (TextView) view.findViewById(R.id.mainMenuText3);
        textView[3] = (TextView) view.findViewById(R.id.mainMenuText4);
        textView[0].setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setTextViewBold(byte index) {
        if (index < 0)
            index = 3;
        else if (index > 3)
            index = 0;
        for (TextView tv : textView) {
            tv.setTypeface(Typeface.DEFAULT);
        }
        textView[index].setTypeface(Typeface.DEFAULT_BOLD);
    }
}
