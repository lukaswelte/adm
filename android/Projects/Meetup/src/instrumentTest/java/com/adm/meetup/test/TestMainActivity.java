package com.adm.meetup.test;

import com.adm.meetup.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.adm.meetup.MainActivity;

/**
 * Created by lukas on 18.11.13.
 */
public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    public TestMainActivity() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testPreconditions() throws Exception {
        assertNotNull(getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
