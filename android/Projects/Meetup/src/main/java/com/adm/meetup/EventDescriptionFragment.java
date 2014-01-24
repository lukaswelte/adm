package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;
import com.adm.meetup.helpers.DateHelper;


/**
 * Created by florian on 22/01/2014.
 */
public class EventDescriptionFragment extends Fragment {

    Button cancelBtn;

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

        cancelBtn = (Button) getView().findViewById(R.id.cancelEventDesc);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Fragment eventFragment = new EventListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.content_frame, eventFragment);
                transaction.commit();
            }
        });

        TextView nameTexView = (TextView) getView().findViewById(R.id.eventDescNameTextView);
        nameTexView.setText(event.getName());

        TextView locationTexView = (TextView) getView().findViewById(R.id.eventDescLocationTextView);
        locationTexView.setText(event.getLocation());

        TextView attenteeTextView = (TextView) getView().findViewById(R.id.eventDescAttendeeTextView);
        attenteeTextView.setText(" " + event.getAttendee());

        TextView dateTexView = (TextView) getView().findViewById(R.id.eventDescDateTextView);
        dateTexView.setText("" + DateHelper.format(event.getDate()));

        TextView dueDateTexView = (TextView) getView().findViewById(R.id.eventDescDueDateTextView);
        dueDateTexView.setText("" + DateHelper.format(event.getDueDate()));

        TextView descriptionTextView = (TextView) getView().findViewById(R.id.eventDescriptionTextView);
        descriptionTextView.setText(event.getDescription());

        TextView typeTextView = (TextView) getView().findViewById(R.id.eventTypeTextView);
        try {
            typeTextView.setText(event.getTypes().get(0).getName());
        } catch (ArrayIndexOutOfBoundsException e) {

        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
