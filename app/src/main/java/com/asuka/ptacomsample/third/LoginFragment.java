package com.asuka.ptacomsample.third;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.asuka.ptacomsample.R;
import com.asuka.ptacomsample.second.SettingListActivity;

public class LoginFragment extends DialogFragment implements View.OnClickListener {
    private LoginDialogListener dialogListener;
    private EditText loginEditText;
    private StringBuilder loginStringBuilder;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnClear, btnEnter;
    private boolean isLoginSuccessful = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (LoginDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement LoginDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_login, null);

        // Initialize views and set click listeners
        loginEditText = view.findViewById(R.id.loginEditText);
        loginStringBuilder = new StringBuilder();

        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);
        btn4 = view.findViewById(R.id.button4);
        btn5 = view.findViewById(R.id.button5);
        btn6 = view.findViewById(R.id.button6);
        btn7 = view.findViewById(R.id.button7);
        btn8 = view.findViewById(R.id.button8);
        btn9 = view.findViewById(R.id.button9);
        btn0 = view.findViewById(R.id.button0);
        btnClear = view.findViewById(R.id.buttonClear);
        btnEnter = view.findViewById(R.id.buttonEnter);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnEnter.setOnClickListener(this);

        builder.setView(view)
                .setTitle("Login");
//                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Handled in onClick method for btnEnter
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                loginStringBuilder.append("1");
                break;
            case R.id.button2:
                loginStringBuilder.append("2");
                break;
            case R.id.button3:
                loginStringBuilder.append("3");
                break;
            case R.id.button4:
                loginStringBuilder.append("4");
                break;
            case R.id.button5:
                loginStringBuilder.append("5");
                break;
            case R.id.button6:
                loginStringBuilder.append("6");
                break;
            case R.id.button7:
                loginStringBuilder.append("7");
                break;
            case R.id.button8:
                loginStringBuilder.append("8");
                break;
            case R.id.button9:
                loginStringBuilder.append("9");
                break;
            case R.id.button0:
                loginStringBuilder.append("0");
                break;
            case R.id.buttonClear:
                if (loginStringBuilder.length() > 0) {
                    loginStringBuilder.deleteCharAt(loginStringBuilder.length() - 1);
                }
                break;
            case R.id.buttonEnter:
                String loginInput = loginStringBuilder.toString();
                // Perform login validation
                if (validateLogin(loginInput)) {
                    // Login successful
                    dialogListener.onLoginResult(loginInput, true);
                    dismiss();
                } else {
                    // Login failed
                    dialogListener.onLoginResult(loginInput, false);
                }
                break;
        }
        loginEditText.setText(loginStringBuilder.toString());
    }

    private boolean validateLogin(String loginInput) {
        // TODO: Perform login validation logic here
        // Return true if login is valid, false otherwise
        return loginInput.equals("1234");
    }




}
