package com.asuka.ptacomsample.second;

public class SettingModel {
    String settingName;
    int image;

    public SettingModel(String settingName) {
        this.settingName = settingName;
    }

    public SettingModel(String settingName, int image) {
        this.settingName = settingName;
        this.image = image;
    }

    public String getSettingName() {
        return settingName;
    }

    public int getImage() {
        return image;
    }
}

