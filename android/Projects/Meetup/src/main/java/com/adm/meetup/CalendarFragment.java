package com.adm.meetup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

public class CalendarFragment extends Fragment implements CalendarView.OnDispatchDateSelectListener {
    public static final int iMoreThanAMonth = 32;
    private TextView mTextDate;
    private SimpleDateFormat mFormat;
    private CalendarView cal;
    private ListView details;

    private final String PREFERENCES_MONTH = "shown_month";
    private final String PREFERENCES_FILE = "calendar_preferences";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mTextDate = (TextView) getView().findViewById(R.id.display_date);
        mFormat = new SimpleDateFormat("EEEE d MMMM yyyy");

        cal = (CalendarView) getView().findViewById(R.id.calendar);
        cal.setOnDispatchDateSelectListener(this);


    }

    @Override
    public void onPause() {
        super.onPause();

        //Saving current month to show it later
        GregorianCalendar actualCal = cal.getmCalendar();
        Long timeToSave = actualCal.getTimeInMillis();
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREFERENCES_MONTH, timeToSave);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES_FILE,
                Context.MODE_PRIVATE);

        //Getting default value : today's date
        GregorianCalendar tempCal = new GregorianCalendar();
        tempCal.setTime(new Date());
        tempCal.add(Calendar.DAY_OF_YEAR, -(tempCal.get(Calendar.DAY_OF_MONTH) - 1));
        long iDefaultTime = tempCal.getTimeInMillis();
        //Setting month
        tempCal.setTimeInMillis(preferences.getLong(PREFERENCES_MONTH, iDefaultTime));
        cal = (CalendarView) getView().findViewById(R.id.calendar);
        cal.setmCalendar(tempCal);
        cal.refreshCalendar();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        //super.onBackPressed();
        resettingCalendar();

    }

    private void resettingCalendar() {
        //Getting today's date
        GregorianCalendar tempCal = new GregorianCalendar();
        tempCal.setTime(new Date());
        //Setting to the next month for future getmMonth method's call
        tempCal.add(Calendar.DAY_OF_YEAR, iMoreThanAMonth);
        tempCal.add(Calendar.DAY_OF_YEAR, -(tempCal.get(Calendar.DAY_OF_MONTH) - 1));
        cal.setmCalendar(tempCal);
    }

    private void getHolidays(final Date selectDate) {
        FutureCallback<JsonArray> callback = new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray jsonArray) {
                try {
                    if (e != null) throw e;
                    ArrayList<String> list_names = new ArrayList<String>();
                    Iterator iterator = jsonArray.iterator();
                    while (iterator.hasNext()) {
                        JsonObject element = (JsonObject) iterator.next();
                        String name = element.get("englishName").getAsString();
                        JsonObject date = element.get("date").getAsJsonObject();

                        //Comparing dates
                        Date dateObj = new Date(date.get("year").getAsInt() - 1900, date.get("month").getAsInt() - 1, date.get("day").getAsInt());
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(selectDate);
                        cal2.setTime(dateObj);
                        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

                        if (sameDay) {
                            list_names.add(name);
                        }
                    }
                    if (list_names.isEmpty())
                        list_names.add(getString(R.string.calendar_no_detail));
                    
                    majHolidays(list_names);
                } catch (Exception ex) {

                }
            }
        };

        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy");
        String year = postFormater.format(selectDate);
        NetworkHelper.holidaysRequest(getActivity(), year, "es", callback);
    }

    private void majHolidays(ArrayList<String> details_names) {
        SimpleAdapter mSchedule;
        details = (ListView) getView().findViewById(R.id.display_details);
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;
        if (details_names.isEmpty()) {
            map = new HashMap<String, String>();
            map.put("detail", getString(R.string.calendar_loading));
            listItem.add(map);
        } else {

            Iterator<String> iterator = details_names.iterator();
            while (iterator.hasNext()) {
                map = new HashMap<String, String>();
                map.put("detail", iterator.next());
                listItem.add(map);
            }
        }

        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.item_calendar_detail,
                new String[]{"detail"}, new int[]{R.id.item_detail});

        details.setAdapter(mSchedule);
    }

    public void onAttachedToWindow() {
        //super.onAttachedToWindow();
        getActivity().getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            resettingCalendar();
            //Normal home button action
            Intent showOptions = new Intent(Intent.ACTION_MAIN);
            showOptions.addCategory(Intent.CATEGORY_HOME);
            startActivity(showOptions);
        }
        return true; //super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDispatchDateSelect(Date date) {
        ArrayList<String> list_names = new ArrayList<String>();
        majHolidays(list_names);
        mTextDate.setText(mFormat.format(date));
        getHolidays(date);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

            return rootView;
        }
    }

}
