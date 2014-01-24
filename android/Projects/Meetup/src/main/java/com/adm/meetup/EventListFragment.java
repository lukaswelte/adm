package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

import java.util.List;


/**
 * Created by lukas on 21.01.14.
 */
public class EventListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ListView eventListView = (ListView) getView().findViewById(R.id.eventListView);

        EventManager manager = new EventManager(getActivity());

        /*
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Football");
        event.setAttendee(Long.valueOf(232));
        event.setLocation("Valencia");
        event.setDescription("Lets play football at 12:30");
        event.addType(EventType.SPORT);

        try {
            event.setDate(DateHelper.parse("11/03/2013 12:33"));
            event.setDueDate(DateHelper.parse("11/03/2014 13:24"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        manager.createEvent(event);

        */

        List<Event> eventList = manager.getEvents();
        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListView.setAdapter(eventListAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Bundle data = new Bundle();
                data.putInt("index", position);
                Fragment fragmentDescEvent = new EventDescriptionFragment();
                fragmentDescEvent.setArguments(data);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentDescEvent).commit();
                fragmentManager.beginTransaction().addToBackStack(null);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragmentCreatorEvent = new EventCreatorFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentCreatorEvent).commit();
                fragmentManager.beginTransaction().addToBackStack(null);
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
}
