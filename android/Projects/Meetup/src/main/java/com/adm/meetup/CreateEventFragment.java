package com.adm.meetup;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.location.GpsStatus;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by florian on 01/12/2013.
 */
public class CreateEventFragment extends Fragment{

    private Button startTime;
    private Button startDate;
    private Button endTime;
    private Button endDate;
    private Calendar dateandtime;
    private Spinner sp;

    public CreateEventFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_event, container, false);
        startDate = (Button) rootView.findViewById(R.id.date_start_date_picker);
        startTime = (Button) rootView.findViewById(R.id.date_start_time_picker);
        endDate = (Button) rootView.findViewById(R.id.date_end_date_picker);
        endTime = (Button) rootView.findViewById(R.id.date_end_time_picker);
        sp = (Spinner) rootView.findViewById(R.id.spinner);
        dateandtime = Calendar.getInstance(Locale.FRANCE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.event_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
         View.OnClickListener dateListener = new View.OnClickListener() {

           @Override
           public void onClick(final View arg0) {

               DatePickerDailog dp = new DatePickerDailog(getActivity(),
                       dateandtime, new DatePickerDailog.DatePickerListner() {

                   @Override
                   public void OnDoneButton(Dialog datedialog, Calendar c) {
                       datedialog.dismiss();
                       dateandtime.set(Calendar.YEAR, c.get(Calendar.YEAR));
                       dateandtime.set(Calendar.MONTH,
                               c.get(Calendar.MONTH));
                       dateandtime.set(Calendar.DAY_OF_MONTH,
                               c.get(Calendar.DAY_OF_MONTH));
                       ((Button)arg0).setText(new SimpleDateFormat("MMMM dd yyyy")
                               .format(c.getTime()));
                   }

                   @Override
                   public void OnCancelButton(Dialog datedialog) {
                       // TODO Auto-generated method stub
                       datedialog.dismiss();
                   }
               });
               dp.show();




           }
       };

        startDate.setOnClickListener(dateListener);
        endDate.setOnClickListener(dateListener);

        View.OnClickListener timeListener = new View.OnClickListener() {

            @Override
            public void onClick(final View arg0) {

                TimePickerDialog dp = new TimePickerDialog(getActivity(),
                        dateandtime, new TimePickerDialog.DatePickerListner() {

                    @Override
                    public void OnDoneButton(Dialog datedialog, Calendar c) {
                        datedialog.dismiss();
                        dateandtime.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
                        dateandtime.set(Calendar.MINUTE,   c.get(Calendar.MINUTE));
                        ((Button)arg0).setText(new SimpleDateFormat("HH mm")
                                .format(c.getTime()));
                    }

                    @Override
                    public void OnCancelButton(Dialog datedialog) {
                        // TODO Auto-generated method stub
                        datedialog.dismiss();
                    }
                });
                dp.show();




            }
        };

        startTime.setOnClickListener(timeListener);
        endTime.setOnClickListener(timeListener);

        return rootView;
    }

}
