package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by florian on 22/01/2014.
 */
public class EventCreatorFragment extends Fragment {

    private Button timeBtnForDate;
    private Button dateBtn;
    private Button timeBtnForDueDate;
    private Button dueDateBtn;
    private Button saveBtn;

    private EditText nameEditText;
    private EditText locationEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_creator, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        timeBtnForDate = (Button) getView().findViewById(R.id.eventCreatorDateBtnTime);
        dateBtn = (Button) getView().findViewById(R.id.eventCreatorDateBtnDate);
        timeBtnForDueDate = (Button) getView().findViewById(R.id.eventCreatorDueDateBtnTime);
        dueDateBtn = (Button) getView().findViewById(R.id.eventCreatorDueDateBtnDate);
        saveBtn = (Button) getView().findViewById(R.id.eventCreatorSaveBtn);
        nameEditText = (EditText) getView().findViewById(R.id.eventCreatorNameTextField);
        locationEditText = (EditText) getView().findViewById(R.id.eventCreatorLocationTextField);

        View.OnClickListener dateListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(view);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        };

        View.OnClickListener timeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(view);
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        };

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                EventManager manager = new EventManager(getActivity());

                int id = manager.getEvents().size();

                String dateString = dateBtn.getText().toString() + " " + timeBtnForDate.getText().toString();
                String dueDateString = dueDateBtn.getText().toString() + " " + timeBtnForDueDate.getText().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

                Date date = null;
                Date dueDate = null;
                try {
                    date = dateFormat.parse(dateString);
                    dueDate = dateFormat.parse(dueDateString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Event event = new Event();
                event.setId(Long.valueOf(id + 1));
                event.setName(nameEditText.getText().toString());
                event.setAttendee(Long.valueOf(232));
                event.setLocation(locationEditText.getText().toString());

                event.setDate(date);
                event.setDueDate(dueDate);
                manager.createEvent(event);

                Fragment fragmentListEvent = new EventListFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentListEvent).commit();
                fragmentManager.beginTransaction().addToBackStack(null);
            }
        });


        timeBtnForDate.setOnClickListener(timeListener);
        timeBtnForDueDate.setOnClickListener(timeListener);
        dateBtn.setOnClickListener(dateListener);
        dueDateBtn.setOnClickListener(dateListener);


    }

}
