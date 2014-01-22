package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;


/**
 * Created by florian on 22/01/2014.
 */
public class EventDescriptionFragment  extends Fragment  {

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
        Bundle extras = getArguments();
        EventManager manager = new EventManager(getActivity());
        Event event = manager.getEvents().get(extras.getInt("index"));
        nameTexView = (TextView) getView().findViewById(R.id.eventDescNameTextView);
        nameTexView.setText(event.getName());

        locationTexView = (TextView) getView().findViewById(R.id.eventDescLocationTextView);
        locationTexView.setText(event.getLocation());

        attenteeTextView = (TextView) getView().findViewById(R.id.eventDescAttendeeTextView);
        attenteeTextView.setText(" "+event.getAttendee());

        dateTexView = (TextView) getView().findViewById(R.id.eventDescDateTextView);
        dateTexView.setText(""+event.getDate().toString());

        dueDateTexView = (TextView) getView().findViewById(R.id.eventDescDueDateTextView);
        dueDateTexView.setText(""+event.getDueDate().toString());


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
