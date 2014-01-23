package com.adm.meetup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.adm.meetup.calendar.Exam;
import com.adm.meetup.calendar.Holiday;
import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;
import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private ListView examListView;

    HashMap<String, ArrayList<Holiday>> holidaysCache = new HashMap<String, ArrayList<Holiday>>();
    HashMap<String, ArrayList<Event>> eventCache = new HashMap<String, ArrayList<Event>>();
    HashMap<String, ArrayList<Exam>> exams = new HashMap<String, ArrayList<Exam>>();
    ArrayList<String> details_name = new ArrayList<String>();

    private final String PREFERENCES_MONTH = "shown_month";
    private final String PREFERENCES_FILE = "calendar_preferences";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.event_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_create_event:
                Intent intent = new Intent(getActivity(), CreateExamActivity.class);
                startActivityForResult(intent, 20);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        DrawerLayout dr = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ListView dl = (ListView) getActivity().findViewById(R.id.left_drawer);
        if (dr.isDrawerOpen(dl)) {
            menu.findItem(R.id.action_create_event).setVisible(false);
        } else menu.findItem(R.id.action_create_event).setVisible(true);

        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 20: {
                if (resultCode == 200) { //successful

                    Exam exam = data.getParcelableExtra("exam");
                    addToExamList(exam);

                } else { //canceled

                }
                break;
            }
        }
    }

    private void addToExamList(Exam exam) {
        ArrayList<Exam> examsOnDate = exams.get(exam.getDateYearIdentifier());
        if (examsOnDate == null) {
            examsOnDate = new ArrayList<Exam>();
        }
        examsOnDate.add(exam);
        exams.put(exam.getDateYearIdentifier(), examsOnDate);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();

        mTextDate = (TextView) view.findViewById(R.id.display_date);
        mFormat = new SimpleDateFormat("EEEE d MMMM yyyy");
        details = (ListView) view.findViewById(R.id.display_details);
        examListView = (ListView) view.findViewById(R.id.examListView);

        fetchExamList();

        cal = (CalendarView) view.findViewById(R.id.calendar);
        cal.setOnDispatchDateSelectListener(this);



    }

    private void fetchExamList() {
        NetworkHelper.examsRequest(getActivity(), new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray jsonArray) {
                try {
                    if (e != null) throw e;
                    exams.clear();
                    Iterator<JsonElement> iterator = jsonArray.iterator();
                    JsonElement element;
                    Exam exam;
                    while (iterator.hasNext()) {
                        element = iterator.next();
                        if (element.isJsonObject()) {
                            exam = new Exam(element.getAsJsonObject());
                            addToExamList(exam);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Try fetching again in 1 second
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchExamList();
                        }
                    }, 1000);
                }
            }
        });
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
        cal.setmCalendar(tempCal);
        cal.refreshCalendar();

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

        SimpleDateFormat postFormatter = new SimpleDateFormat("yyyy");
        final String year = postFormatter.format(selectDate);

        //try retrieving from local cache
        ArrayList<Holiday> holidaysOfYear = holidaysCache.get(year);

        if (holidaysOfYear != null) {
            details_name.addAll(holidayNamesOnDay(selectDate, holidaysOfYear));
        } else {  //fetch new dates from the backend
            FutureCallback<JsonArray> callback = new FutureCallback<JsonArray>() {
                @Override
                public void onCompleted(Exception e, JsonArray jsonArray) {
                    try {
                        if (e != null) throw e;

                        ArrayList<Holiday> holidayList = new ArrayList<Holiday>();
                        Iterator iterator = jsonArray.iterator();
                        Holiday holidayObject;
                        while (iterator.hasNext()) {
                            JsonObject element = (JsonObject) iterator.next();
                            holidayObject = new Holiday(element);
                            holidayList.add(holidayObject);
                        }

                        holidaysCache.put(year, holidayList);
                        details_name.addAll(holidayNamesOnDay(selectDate, holidayList));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };

            NetworkHelper.holidaysRequest(getActivity(), year, "es", callback);
        }
    }

    private void getEvents(final Date selectDate) {
        SimpleDateFormat postFormatter = new SimpleDateFormat("yyyy");
        final String year = postFormatter.format(selectDate);

        //try retrieving from local cache
        ArrayList<Event> eventsOfYear = eventCache.get(year);

        if (eventsOfYear != null) {
            details_name.addAll(eventsNamesOnDay(selectDate, eventsOfYear));
        } else {  //fetch new dates from the backend
            EventManager eventManager = new EventManager(getActivity());
            ArrayList<Event> eventList = eventManager.getEvents();
            details_name.addAll(eventsNamesOnDay(selectDate, eventList));
        }
    }

    private ArrayList<String> holidayNamesOnDay(final Date date, ArrayList<Holiday> holidayDateList) {
        ArrayList<String> list_names = new ArrayList<String>();

        for (Holiday holiday : holidayDateList) {
            if (holiday.isOnTheSameDay(date)) {
                list_names.add(holiday.getName());
            }
        }
        return list_names;
    }

    private ArrayList<String> eventsNamesOnDay(final Date date, ArrayList<Event> eventDateList) {
        ArrayList<String> list_names = new ArrayList<String>();

        for (Event event : eventDateList) {
            if (event.isOnTheSameDay(date)) {
                list_names.add(event.getName());
            }
        }
        return list_names;
    }


    private void updateDetailsInCalendar(ArrayList<String> details_names) {
        SimpleAdapter mSchedule;
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;

        Iterator<String> iterator = details_names.iterator();
        while (iterator.hasNext()) {
            map = new HashMap<String, String>();
            map.put("detail", iterator.next());
            listItem.add(map);
        }

        mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.item_calendar_detail,
                new String[]{"detail"}, new int[]{R.id.item_detail});

        details.setAdapter(mSchedule);
    }

    @Override
    public void onDispatchDateSelect(Date date) {

        details_name = new ArrayList<String>();
        mTextDate.setText(mFormat.format(date));

        //Fetching details
        updateExamListForDate(date);
        getHolidays(date);
        getEvents(date);
        if (details_name.isEmpty()) details_name.add("");
        updateDetailsInCalendar(details_name);
    }

    private void updateExamListForDate(Date date) {
        ArrayList<Exam> examsOnDay = exams.get(Exam.getDateYearIdentifierOfDate(date));

        TextView tv = (TextView) getView().findViewById(R.id.exam_overview);
        if(examsOnDay!=null) tv.setVisibility(View.VISIBLE);
        else tv.setVisibility(View.GONE);

        examListView.setAdapter(new ExamListAdapter(getActivity(), examsOnDay));
    }
}
