package com.adm.meetup.test;

import com.adm.meetup.event.EventManager;
import com.adm.meetup.event.EventRestContentProvider;
import com.adm.meetup.helpers.SharedApplication;

import junit.framework.TestCase;

import java.sql.SQLException;

/**
 * Created by jan on 18.1.14.
 */
public class TestEventManagerWithRest extends TestCase {
    //Don't call it every time.. but works
    /*public void setUp() throws SQLException {
        SharedApplication.getInstance().setUserToken(SharedApplication.testUserToken);
        manager = new EventManager(new EventRestContentProvider(getContext()));
    }*/
}
