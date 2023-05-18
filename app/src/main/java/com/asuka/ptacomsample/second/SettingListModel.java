package com.asuka.ptacomsample.second;

public class SettingListModel {
    String settingName, settingDetails;
    int image;

    public SettingListModel(String settingName, String details) {
        this.settingName = settingName;
        this.settingDetails = details;
    }

    public SettingListModel(String settingName, int image) {
        this.settingName = settingName;
        this.image = image;
    }

    public String getSettingName() {
        return settingName;
    }

    public String getSettingDetails() {
        return settingDetails;
    }

    public int getImage() {
        return image;
    }
}

