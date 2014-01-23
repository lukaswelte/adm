package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.adm.meetup.event.Event;
import com.adm.meetup.event.EventManager;
import com.adm.meetup.event.EventType;
import com.adm.meetup.helpers.DateHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private EditText descriptionEditText;

    private Spinner typeSpinner;
    private EventType evType;

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
        descriptionEditText = (EditText) getView().findViewById(R.id.eventCreatorDescription);
        typeSpinner= (Spinner) getView().findViewById(R.id.eventCreatorType);


        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)  {
                int index = arg0.getSelectedItemPosition();
                switch (index){
                    case 1 :
                        evType = EventType.SPORT;
                        break;
                    case 2 :
                        evType = EventType.DANCE;
                        break;
                    case 3 :
                        evType = EventType.TRIP;
                        break;
                    default:
                        evType = EventType.DEFAULT;
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });


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

                Date date = null;
                Date dueDate = null;
                try {
                    date = DateHelper.parse(dateString);
                    dueDate = DateHelper.parse(dueDateString);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Event event = new Event();
                event.setId(Long.valueOf(id + 1));
                event.setName(nameEditText.getText().toString());
                event.setAttendee(Long.valueOf(232));
                event.setLocation(locationEditText.getText().toString());
                event.setDescription(descriptionEditText.getText().toString());
                event.addType(evType);
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
