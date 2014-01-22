package com.adm.meetup;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by florian on 22/01/2014.
 */
public class EventCreatorFragment extends Fragment {

    private Button timeBtnForDate;
    private Button dateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_creator, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
       timeBtnForDate = (Button) getView().findViewById(R.id.eventCreatorDateBtnTime);
       dateBtn = (Button) getView().findViewById(R.id.eventCreatorDateBtnDate);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
       timeBtnForDate.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {
              DialogFragment newFragment = new TimePickerFragment();
               newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");

           }
       });

    }

}
