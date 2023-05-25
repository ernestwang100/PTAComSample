package com.asuka.ptacomsample.third;

public interface LoginDialogListener {
    void onLoginResult(String username, boolean isLoginSuccessful);
    void onLoginCancelled();
}