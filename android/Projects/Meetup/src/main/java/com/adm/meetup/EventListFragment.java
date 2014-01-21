package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

import java.util.Date;
import java.util.List;

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
    }
}
