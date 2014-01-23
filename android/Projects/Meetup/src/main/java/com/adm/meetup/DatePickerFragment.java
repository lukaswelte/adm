package com.adm.meetup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by florian on 22/01/2014.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Boolean isDueDate;
    public View view;

    public DatePickerFragment(View view){
        this.view=view;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Button dateBtnForDate;
        isDueDate();
        if(!isDueDate) dateBtnForDate=  (Button) getActivity().findViewById(R.id.eventCreatorDateBtnDate);
        else dateBtnForDate=  (Button) getActivity().findViewById(R.id.eventCreatorDueDateBtnDate);
        if(month<9 && day<10) dateBtnForDate.setText("0"+day+".0"+(month+1)+"."+year);
        else if(month<9 && day>9)  dateBtnForDate.setText(""+day+".0"+(month+1)+"."+year);
        else if(month>9 && day <10) dateBtnForDate.setText("0"+day+"."+(month+1)+"."+year);
        else dateBtnForDate.setText(""+day+"."+(month+1)+"."+year);
    }

    void isDueDate(){
        if(view.getId() == R.id.eventCreatorDateBtnDate) isDueDate=false;
        else isDueDate =true;
    }
}
