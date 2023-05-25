package com.asuka.ptacomsample.third;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);

        builder.setView(view)
                .setTitle("Login")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        // Perform login validation
                        boolean isLoginSuccessful = validateCredentials(username, password);
                        dialogListener.onLoginResult(username, isLoginSuccessful);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onLoginCancelled();
                    }
                });

        return builder.create();
    }

    private boolean validateCredentials(String username, String password) {
        // Implement your own logic for validating credentials
        // Return true if credentials are valid, false otherwise
        return username.equals("a") && password.equals("a");
    }


}


