package com.example.donpoly.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String userId;
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    String getUserId() {
        return userId;
    }
}