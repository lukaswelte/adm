package com.adm.meetup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int iDaysOfWeek = 8;
    public static final String sBackgrdColorItemClick = "#3A9CE9";
    public static final int iArrayListMonthMax = 37;
    public static final int iInMonth = 10;
    private GridView mGrid;
    private View mConvertView;
    private GregorianCalendar mCalendar;
    private ArrayList<Date> mMonthAL;
    private Context mContext;
    private TextView mMonthText;
    private SimpleDateFormat mFormatMonth;
    private SimpleDateFormat mFormatYear;
    private OnDispatchDateSelectListener mListenerDateSelect;
    private Button mArrowRight;
    private Button mArrowLeft;
    private CalendarAdapter mAdapter;

    public interface OnDispatchDateSelectListener {
        public void onDispatchDateSelect(Date date);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mFormatMonth = new SimpleDateFormat("MMMM");
        mFormatYear = new SimpleDateFormat("yyyy");

        mConvertView = LayoutInflater.from(context).inflate(R.layout.view_calendar, this);
        mGrid = (GridView) mConvertView.findViewById(R.id.calendar_days);

        mGrid.setOnItemClickListener(this);

        mMonthText = (TextView) mConvertView.findViewById(R.id.calendar_month);
        mArrowLeft = (Button) findViewById(R.id.calendar_arrow_left);
        mArrowLeft.setOnClickListener(this);

        mArrowRight = (Button) mConvertView.findViewById(R.id.calendar_arrow_right);
        mArrowRight.setOnClickListener(this);

        mCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_MONTH) - 1));

        mMonthAL = new ArrayList<Date>();

        mAdapter = new CalendarAdapter(mContext, mMonthAL);

        mGrid.setAdapter(mAdapter);
    }

    public GregorianCalendar getmCalendar() {
        //Calender is set to the next month, manipulation to set it
        GregorianCalendar returnCal = mCalendar;
        returnCal.add(Calendar.DAY_OF_YEAR, -1);
        returnCal.add(Calendar.DAY_OF_YEAR, -mCalendar.get(Calendar.DAY_OF_MONTH) + 1);
        return returnCal;
    }

    public void setmCalendar(GregorianCalendar mCalendar) {
        this.mCalendar = mCalendar;
    }

    private void implementMonth() {

        mMonthAL.clear();
        int dateMonth = mCalendar.get(Calendar.MONTH),
                currentMonth = mCalendar.get(Calendar.MONTH),
                i = 0;
        //Initializing to the previous sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_WEEK) - 1));
        while (dateMonth == currentMonth) {
            mMonthAL.add(mCalendar.getTime());
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);

            //Only doing that when for sure, next week (can be with previous month days) is over
            if (i > iDaysOfWeek) {
                currentMonth = mCalendar.get(Calendar.MONTH);
            }
            i++;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int arg2PositionItem, long arg3) {
        clearBackground();
        v.setBackgroundColor(Color.parseColor(sBackgrdColorItemClick));
        mListenerDateSelect.onDispatchDateSelect(mMonthAL.get(arg2PositionItem));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calendar_arrow_left:
                subMonth();
                break;
            case R.id.calendar_arrow_right:
                addMonth();
                break;
        }
    }

    private void addMonth() {
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_MONTH) - 1));
        refreshCalendar();
    }

    private void subMonth() {
        mCalendar.add(Calendar.DAY_OF_YEAR, -iArrayListMonthMax);
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_MONTH) - 1));
        refreshCalendar();
    }

    public void refreshCalendar() {
        implementMonth();
        mAdapter.notifyDataSetChanged();
        setSelectedMonthText();
        clearBackground();
    }

    private void clearBackground() {
        for (int i = 0; i < mGrid.getCount(); i++) {
            if (mGrid.getChildAt(i) != null) {
                mGrid.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private void setSelectedMonthText() {
        String monthText = mFormatMonth.format(mMonthAL.get(iInMonth));
        mMonthText.setText(monthText + " " + mFormatYear.format(mMonthAL.get(iInMonth)));

    }

    public void setOnDispatchDateSelectListener(OnDispatchDateSelectListener v) {
        mListenerDateSelect = v;
    }
}
