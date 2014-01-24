package com.adm.meetup;

/**
 * Created by timomuller on 21/01/14.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

import java.util.List;

public class HomeFragment extends Fragment {

    ListView events;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);


    }
    public void onStart() {
        super.onStart();

        events = (ListView) getView().findViewById(R.id.eventListViewHome);
        EventManager manager = new EventManager(getActivity());
        List<Event> eventList = manager.getEvents();
        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), eventList);
        events.setAdapter(eventListAdapter);

        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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


}

