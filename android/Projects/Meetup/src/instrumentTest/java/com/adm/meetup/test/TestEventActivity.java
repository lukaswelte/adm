package com.adm.meetup.test;

import android.test.ActivityInstrumentationTestCase2;

import com.adm.meetup.EventActivity;

/**
 * Created by Jan on 2.12.13.
 */
public class TestEventActivity extends ActivityInstrumentationTestCase2<EventActivity> {


    public TestEventActivity() {
        super(EventActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testPreconditions() throws Exception {

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
