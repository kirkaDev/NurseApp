package com.desiredsoftware.nurseapp;

public class RecyclerViewNotificationItem {

    private String notificationTime;
    private boolean isEnabled;
    private String[] actionsList;

    public RecyclerViewNotificationItem(String notificationTime, boolean isEnabled, String[] actionsList) {
        this.notificationTime = notificationTime;
        this.isEnabled = isEnabled;
        this.actionsList = actionsList;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public String[] getActionsList() {
        return actionsList;
    }
}
