package com.asuka.ptacomsample.third;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.asuka.ptacomsample.R;

public class DriverStatusFragment extends Fragment {
    private RadioGroup radioGroupDriverStatus, radioGroupCodriverStatus;
    private EditText driverCodeEdt, codriverCodeEdt;
    private Integer driverStatus, codriverStatus;

    private String cmd, temp[], cmdStart;
    public static final int DRIVER_STATUS = 0, DRIVER_ID = 1, CODRIVER_STATUS = 2, CODRIVER_ID = 3;
    private String driverCode, codriverCode;
    private static final String TAG = "DriverStatusFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        cmdStart = "$LCD+DRIVER STATUS=";

        radioGroupDriverStatus = view.findViewById(R.id.DRBtnGroup);
        radioGroupCodriverStatus = view.findViewById(R.id.CRBtnGroup);
        driverCodeEdt = view.findViewById(R.id.driverCodeEdt);
        codriverCodeEdt = view.findViewById(R.id.codriverCodeEdt);

        driverCodeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (input.isEmpty()) {
                    driverCodeEdt.setError("Please enter a driver code");
                } else if (input.length() != 8) {
                    driverCodeEdt.setError("Driver code must be 8 characters long");
                } else if (!isValidHexadecimal(input)) {
                    driverCodeEdt.setError("Driver code must be a valid hexadecimal value");
                }
            }
        });

        codriverCodeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (input.isEmpty()) {
                    codriverCodeEdt.setError("Please enter a codriver code");
                } else if (input.length() != 8) {
                    codriverCodeEdt.setError("Codriver code must be 8 characters long");
                } else if (!isValidHexadecimal(input)) {
                    codriverCodeEdt.setError("Codriver code must be a valid hexadecimal value");
                }
            }
        });




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
                // Define the regular expression pattern for hexadecimal characters (0-9, A-F)
                String regexPattern = "[0-9A-F]+";

                // Combine the existing text with the new text being inserted
                String newText = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());

                // Check if the new text matches the pattern (i.e., only contains 0-9, A-F)
                if (!newText.matches(regexPattern)) {
                    // Reject the new input by returning an empty string (no changes will be made to the EditText)
                    Toast.makeText(getContext(), "Only hexadecimal characters are allowed", Toast.LENGTH_SHORT).show();
                    return "";
                }

                // If the input is valid, allow it to be inserted into the EditText
                return null;
            }
        };



        InputFilter eightDigitInputFilter = new InputFilter.LengthFilter(8);

        // Set the input filter to the driverCodeEdt and codriverCodeEdt EditTexts
        driverCodeEdt.setFilters(new InputFilter[]{hexInputFilter, eightDigitInputFilter});
        codriverCodeEdt.setFilters(new InputFilter[]{hexInputFilter, eightDigitInputFilter});

        return view;
    }

    private boolean isValidHexadecimal(String input) {
        return input.matches("[0-9A-Fa-f]+");
    }

    private void setDefaultID(String driverCode, String codriverCode) {
        if (driverCode != null && codriverCode != null) {
            this.driverCode = driverCode;
            this.codriverCode = codriverCode;
            driverCodeEdt.setText(driverCode);
            codriverCodeEdt.setText(codriverCode);
        }

    }

    private void setDefaultRadioButtons(String driverStatus, String codriverStatus) {
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
        driverCode = driverCodeEdt.getText().toString();
        codriverCode = codriverCodeEdt.getText().toString();

        if (driverStatus != null && codriverStatus != null) {
            cmd = cmdStart + driverStatus + "," + driverCode + "," + codriverStatus + "," + codriverCode;
        }
        return cmd;
    }

    public void updateValues(String[] temp) {
        this.temp = temp;
        setDefaultRadioButtons(temp[DRIVER_STATUS], temp[CODRIVER_STATUS]);
        setDefaultID(temp[DRIVER_ID], temp[CODRIVER_ID]);
    }
}
