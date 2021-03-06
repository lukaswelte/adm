package com.adm.meetup;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by florian on 22/01/2014.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Boolean isDueDate;
    public View view;

    public TimePickerFragment(View view) {
        this.view = view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        Button timeBtnForDate;
        isDueDate();
        if (!isDueDate)
            timeBtnForDate = (Button) getActivity().findViewById(R.id.eventCreatorDateBtnTime);
        else timeBtnForDate = (Button) getActivity().findViewById(R.id.eventCreatorDueDateBtnTime);
        if (i < 10 && i2 > 9) timeBtnForDate.setText("0" + i + ":" + i2);
        else if (i < 10 && i2 < 10) timeBtnForDate.setText("0" + i + ":0" + i2);
        else if (i > 9 && i2 < 10) timeBtnForDate.setText("" + i + ":0" + i2);
        else timeBtnForDate.setText("" + i + ":" + i2);

    }

    void isDueDate() {
        isDueDate = view.getId() != R.id.eventCreatorDateBtnTime;
    }


}
