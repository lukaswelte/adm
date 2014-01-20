package com.adm.meetup.test;

import com.adm.meetup.event.EventManager;

import java.sql.SQLException;

/**
 * Created by jan on 18.1.14.
 */
public class TestEventManagerWithDb extends TestEventManager {
    public void setUp() throws SQLException {
        manager = new EventManager(getContext());
    }
}
