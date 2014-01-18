package com.adm.meetup.test;

import com.adm.meetup.event.EventManager;
import com.adm.meetup.event.EventRestContentProvider;

import java.sql.SQLException;

/**
 * Created by jan on 18.1.14.
 */
public class TestEventManagerWithRest extends TestEventManager {
    public void setUp() throws SQLException {
        manager = new EventManager(new EventRestContentProvider(getContext()));
    }
}
