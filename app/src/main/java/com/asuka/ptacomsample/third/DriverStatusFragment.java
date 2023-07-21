package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Patterns;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.asuka.comm.ComPort;
import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.main.RecvThread;

public class DriverStatusFragment extends Fragment {
    private RadioGroup radioGroupDriverStatus, radioGroupCodriverStatus;
    private EditText driverCodeEdt, codriverCodeEdt;
    private Integer driverStatus, codriverStatus;

    private String cmd, temp[], cmdStart;
    public static final int DRIVER_STATUS = 0;
    public static final int CODRIVER_STATUS = 1;
    private boolean isDefaultsSet = false;
    private static final String TAG = "DriverStatusFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        cmdStart = "$LCD+DRIVER STATUS=";

        radioGroupDriverStatus = view.findViewById(R.id.DRBtnGroup);
        radioGroupCodriverStatus = view.findViewById(R.id.CRBtnGroup);
        driverCodeEdt = view.findViewById(R.id.driverCodeEdt);
        codriverCodeEdt = view.findViewById(R.id.codriverCodeEdt);

        radioGroupDriverStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                driverStatus = radioGroupDriverStatus.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: driverStatus: " + driverStatus);
            }
        });

        radioGroupCodriverStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                codriverStatus = radioGroupCodriverStatus.indexOfChild(view.findViewById(i));
                Log.d(TAG, "onCheckedChanged: codriverStatus: " + codriverStatus);
            }
        });

        // Create an input filter to restrict input to hexadecimal characters (0-9, A-F, a-f)
        InputFilter hexInputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Define the regular expression pattern for hexadecimal characters (0-9, A-F, a-f)
                String regexPattern = "[0-9A-Fa-f]+";

                // Combine the existing text with the new text being inserted
                String newText = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());

                // Check if the new text matches the pattern (i.e., only contains 0-9, A-F, or a-f)
                if (!newText.matches(regexPattern)) {
                    // Reject the new input by returning an empty string (no changes will be made to the EditText)
                    Toast.makeText(getContext(), "Only hexadecimal characters are allowed", Toast.LENGTH_SHORT).show();
                    return "";
                }

                // If the input is valid, allow it to be inserted into the EditText
                return null;
            }
        };

        // Set the input filter to the driverCodeEdt and codriverCodeEdt EditTexts
        driverCodeEdt.setFilters(new InputFilter[]{hexInputFilter});
        codriverCodeEdt.setFilters(new InputFilter[]{hexInputFilter});

        return view;
    }

    private void setRadioButtons(String driverStatus, String codriverStatus) {
        if (driverStatus.equals("0")) {
            radioGroupDriverStatus.check(R.id.DRBtn0);
        } else if (driverStatus.equals("1")) {
            radioGroupDriverStatus.check(R.id.DRBtn1);
        } else if (driverStatus.equals("2")) {
            radioGroupDriverStatus.check(R.id.DRBtn2);
        } else if (driverStatus.equals("3")) {
            radioGroupDriverStatus.check(R.id.DRBtn3);
        }

        if (codriverStatus.equals("0")) {
            radioGroupCodriverStatus.check(R.id.CRBtn0);
        } else if (codriverStatus.equals("1")) {
            radioGroupCodriverStatus.check(R.id.CRBtn1);
        } else if (codriverStatus.equals("2")) {
            radioGroupCodriverStatus.check(R.id.CRBtn2);
        } else if (codriverStatus.equals("3")) {
            radioGroupCodriverStatus.check(R.id.CRBtn3);
        }
    }

    public String getCmd() {
        if (driverStatus != null && codriverStatus != null) {
            cmd = cmdStart + driverStatus + "," + codriverStatus;
        }
        return cmd;
    }

    public void updateValues(String[] temp) {
        this.temp = temp;
        setRadioButtons(temp[DRIVER_STATUS], temp[CODRIVER_STATUS]);
    }
}
