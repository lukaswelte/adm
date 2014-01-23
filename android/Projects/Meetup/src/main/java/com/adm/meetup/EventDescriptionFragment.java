package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

import java.text.SimpleDateFormat;


/**
 * Created by florian on 22/01/2014.
 */
public class EventDescriptionFragment extends Fragment {

    private TextView dateTexView;
    private TextView dueDateTexView;
    private TextView nameTexView;
    private TextView attenteeTextView;
    private TextView locationTexView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_description, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        SimpleDateFormat sd = new SimpleDateFormat("dd:MM:yyyy HH:mm");

        Bundle extras = getArguments();
        EventManager manager = new EventManager(getActivity());
        Event event = manager.getEvents().get(extras.getInt("index"));

        nameTexView = (TextView) getView().findViewById(R.id.eventDescNameTextView);
        nameTexView.setText(event.getName());

        locationTexView = (TextView) getView().findViewById(R.id.eventDescLocationTextView);
        locationTexView.setText(event.getLocation());

        attenteeTextView = (TextView) getView().findViewById(R.id.eventDescAttendeeTextView);
        attenteeTextView.setText(" " + event.getAttendee());

        dateTexView = (TextView) getView().findViewById(R.id.eventDescDateTextView);
        dateTexView.setText("" + sd.format(event.getDate()));

        dueDateTexView = (TextView) getView().findViewById(R.id.eventDescDueDateTextView);
        dueDateTexView.setText("" + sd.format(event.getDueDate()));

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
