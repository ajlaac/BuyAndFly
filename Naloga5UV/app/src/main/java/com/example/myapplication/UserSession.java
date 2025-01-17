package com.example.myapplication;

public class UserSession {
    private static UserSession instance;
    private String loggedInEmail;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getLoggedInEmail() {
        return loggedInEmail;
    }

    public void setLoggedInEmail(String loggedInEmail) {
        this.loggedInEmail = loggedInEmail;
    }
}
