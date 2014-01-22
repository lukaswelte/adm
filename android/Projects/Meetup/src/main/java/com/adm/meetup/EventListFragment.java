package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNotSame;

/**
 * Created by lukas on 21.01.14.
 */
public class EventListFragment extends Fragment {

    private ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventListView = (ListView) getView().findViewById(R.id.eventListView);
        EventManager manager = new EventManager(getActivity());
        List<Event> eventList = manager.getEvents();

        /* Test on event */
        manager.deleteEvents();
        Event event = new Event();
        event.setId(Long.valueOf(1));
        event.setName("Event Name");
        event.setLocation("Paris");
        event.setAttendee((long)231);
        manager.createEvent(event);
        eventList.add(event);

        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), eventList);
        eventListView.setAdapter(eventListAdapter);


        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "You Clicked at event " + position, Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.event_list, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_create_event:
                Toast.makeText(getActivity().getApplicationContext(), "Create new event ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
