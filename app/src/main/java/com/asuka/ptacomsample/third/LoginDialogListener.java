package com.asuka.ptacomsample.third;

public interface LoginDialogListener {
    void showisLoginSucess(String username, boolean isLoginSuccessful);
    void onCancel();
    boolean isLoginSuccessful();

}
