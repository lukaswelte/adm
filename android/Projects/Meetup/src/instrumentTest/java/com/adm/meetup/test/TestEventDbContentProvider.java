package com.adm.meetup.test;

import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.adm.meetup.event.EventDbContentProvider;

public class TestEventDbContentProvider extends ProviderTestCase2<EventDbContentProvider> {

    private static final String TAG = TestEventDbContentProvider.class.getName();

    private static MockContentResolver resolve;

    public TestEventDbContentProvider(Class<EventDbContentProvider> providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public TestEventDbContentProvider() {
        super(EventDbContentProvider.class, "com.adm.meetup.event.EventManager");
    }


    @Override
    public void setUp() {
        try {
            super.setUp();
            resolve = this.getMockContentResolver();
        } catch (Exception e) {


        }
    }

    @Override
    public void tearDown() {
        try {
            super.tearDown();
        } catch (Exception e) {


        }
    }

    /*public void testPreconditions() {
        String[] projection = {"id" ,"name"};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        Cursor result = resolve.query(Uri.parse(EventDbContentProvider.EVENTS_URI), projection, selection, selectionArgs, sortOrder);

        //assertEquals(result.getCount(), 0);
        //assertEquals(result.getColumnCount(), 2);

        result.moveToNext();

        for(int i = 0; i < result.getCount(); i++) {
            String id = result.getString(0);
            String name = result.getString(1);
            result.moveToNext();
        }
    }*/

}
