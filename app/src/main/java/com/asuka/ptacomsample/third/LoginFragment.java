package com.asuka.ptacomsample.third;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.asuka.ptacomsample.R;

public class LoginFragment extends DialogFragment {
    private LoginDialogListener dialogListener;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView hintTV;
    private Button loginButton, cancelButton;
    private static final String TAG = "LoginFragment";

    @Override
    public void onAttach(@NonNull android.content.Context context) {
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

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        hintTV = view.findViewById(R.id.hintTV);

        loginButton = view.findViewById(R.id.confirmButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        loginButton.setOnClickListener(confirmButtonListener);
        cancelButton.setOnClickListener(cancelButtonListener);


        return builder.setView(view).create();

    }

    private View.OnClickListener confirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Handle login confirmation
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (validateCredentials(username, password)) {
                // Login successful
                dialogListener.showisLoginSucess(username, true);
                dismiss();
            } else {
                // Login failed
                dialogListener.showisLoginSucess(username, false);
            };
        }
    };

    private View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Handle login cancellation
            dialogListener.onCancel();
            dismiss();
        }
    };


    private boolean validateCredentials(String username, String password) {
        // Implement your own logic for validating credentials
        // Return true if credentials are valid, false otherwise
        return username.equals("a") && password.equals("a");
    }



}

