package com.adm.meetup.test;

import com.adm.meetup.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

/**
 * Created by lukas on 18.11.13.
 */
public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    private TextView helloWorldTextView = null;

    public TestMainActivity() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        helloWorldTextView = (TextView)getActivity().findViewById(R.id.helloWorldTextView);
    }

    public void testPreconditions() throws Exception {
        assertNotNull(getActivity());
    }

    public void testHelloWorldTextViewNotNull() throws Exception {
        assertNotNull(helloWorldTextView);
    }

    public void testHelloWorldTextViewDisplaysHelloWorld() throws Exception {
        assertEquals(getActivity().getString(R.string.hello_world),helloWorldTextView.getText());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        helloWorldTextView = null;
    }
}
