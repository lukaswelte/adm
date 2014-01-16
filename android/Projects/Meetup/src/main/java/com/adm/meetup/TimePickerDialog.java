package com.adm.meetup;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import com.adm.wheel.NumericWheelAdapter;
import com.adm.wheel.WheelView;

public class TimePickerDialog extends Dialog {

    private Context Mcontex;

    public TimePickerDialog(Context context, Calendar calendar,
                            final DatePickerListner dtp) {

        super(context);
        Mcontex = context;
        LinearLayout lytmain = new LinearLayout(Mcontex);
        lytmain.setOrientation(LinearLayout.VERTICAL);
        LinearLayout lytdate = new LinearLayout(Mcontex);
        LinearLayout lytbutton = new LinearLayout(Mcontex);

        Button btnset = new Button(Mcontex);
        Button btncancel = new Button(Mcontex);

        btnset.setText("Set");
        btncancel.setText("Cancel");

        final WheelView hour = new WheelView(Mcontex);
        final WheelView min = new WheelView(Mcontex);

        lytdate.addView(hour, new LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.2f));
        lytdate.addView(min, new LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0.8f));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        lytbutton.addView(btnset, new LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        lytbutton.addView(btncancel, new LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        lytbutton.setPadding(5, 5, 5, 5);
        lytmain.addView(lytdate);
        lytmain.addView(lytbutton);

        setContentView(lytmain);

        getWindow().setLayout(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);

        // hour
        updateDays(hour,min);
        hour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY) );

        // min
        updateDays(hour,min);
        hour.setCurrentItem(calendar.get(Calendar.MINUTE));

        btnset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar c = updateDays(hour,min);
                dtp.OnDoneButton(TimePickerDialog.this, c);
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dtp.OnCancelButton(TimePickerDialog.this);

            }
        });

    }

    Calendar updateDays(WheelView hour, WheelView min) {
        Calendar calendar = Calendar.getInstance();

        /*Hours*/
        int maxHours = 23;
        hour.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, maxHours,"%02d"));
        int curHour = Math.min(maxHours, hour.getCurrentItem());
        hour.setCurrentItem(curHour, true);
        calendar.set(Calendar.HOUR_OF_DAY, curHour);

        /*Min*/
        int maxMinutes = calendar.getActualMaximum(Calendar.MINUTE);
        min.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, maxMinutes, "%02d"));
        int curMin = Math.min(maxMinutes, min.getCurrentItem());
        min.setCurrentItem(curMin, true);
        calendar.set(Calendar.MINUTE, curMin);

        return calendar;

    }


    public interface DatePickerListner {
        public void OnDoneButton(Dialog datedialog, Calendar c);

        public void OnCancelButton(Dialog datedialog);
    }
}
