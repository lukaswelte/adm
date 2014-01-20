package com.adm.meetup.helpers;

import android.app.Application;

/**
 * Created by lukas on 19.01.14.
 */
public class SharedApplication extends Application {

    public static final String testUserToken = "yv362uyjecplow29nuto16imuklkrzfr";

    private static SharedApplication sharedApplication;

    private String token = null;

    public static SharedApplication getInstance() {
        return sharedApplication;
    }

    public String userToken() {
        return token;
    }

    public void setUserToken(String token) {
        this.token = token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedApplication = this;
        sharedApplication.initializeInstance();
    }

    protected void initializeInstance() {

    }
}