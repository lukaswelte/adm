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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener{

    private GridView                        mGrid;
    private View 							mConvertView;
    private GregorianCalendar               mCalendar;
    private ArrayList<Date>                 mMonthAL;
    private Context                         mContext;
    private TextView                        mMonthText;
    private SimpleDateFormat                mFormatMonth;
    private SimpleDateFormat 				mFormatYear;
    private OnDispatchDateSelectListener 	mListenerDateSelect;
    private Button                          mArrowRight;
    private Button							mArrowLeft;
    private CalendarAdapter					mAdapter;

    public interface OnDispatchDateSelectListener {
        public void onDispatchDateSelect(Date date);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext			= context;
        mFormatMonth		= new SimpleDateFormat("MMMM");
        mFormatYear			= new SimpleDateFormat("yyyy");

        mConvertView = LayoutInflater.from(context).inflate(R.layout.view_calendar, this);
        //May produce NullPointerExc !
        mGrid=(GridView)mConvertView.findViewById(R.id.calendar_days);

        mGrid.setOnItemClickListener(this);

        mMonthText=(TextView)mConvertView.findViewById(R.id.calendar_month);
        mArrowLeft=(Button)findViewById(R.id.calendar_arrow_left);
        mArrowLeft.setOnClickListener(this);

        mArrowRight=(Button)mConvertView.findViewById(R.id.calendar_arrow_right);
        mArrowRight.setOnClickListener(this);

        mCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_MONTH) -1));

        mMonthAL = new ArrayList<Date>();
        implementMonth();
        setSelectedMonthText();

        mAdapter=new CalendarAdapter(mContext, mMonthAL);

        mGrid.setAdapter(mAdapter);
    }

    private void implementMonth() {

        mMonthAL.clear();
        int dateMonth = mCalendar.get(Calendar.MONTH),
                currentMonth = mCalendar.get(Calendar.MONTH),
                i=0;
        mCalendar.add(Calendar.DAY_OF_YEAR, - (mCalendar.get(Calendar.DAY_OF_WEEK) -1));
        while (dateMonth == currentMonth) {
            mMonthAL.add(mCalendar.getTime());
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);

            if(i>8){ //do not use magic numbers uncommented
            currentMonth = mCalendar.get(Calendar.MONTH);}
            i++;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        clearBackground();
        v.setBackgroundColor(Color.parseColor("#3A9CE9")); //constant
        mListenerDateSelect.onDispatchDateSelect(mMonthAL.get(arg2)); //what is arg2?
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.calendar_arrow_left:
                subMonth();
                break;
            case R.id.calendar_arrow_right:
                addMonth();
                break;
        }
    }

    private void addMonth()
    {
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_MONTH) -1));
        refreshCalendar();
    }

    private void refreshCalendar() {
        implementMonth();
        mAdapter.notifyDataSetChanged();
        setSelectedMonthText();
        clearBackground();
    }

    private void clearBackground()
    {
        for(int i=0;i<mGrid.getCount();i++)
        {
            if(mGrid.getChildAt(i) != null) {
                mGrid.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private void subMonth()
    {
        mCalendar.add(Calendar.DAY_OF_YEAR, -37); //magic number
        mCalendar.add(Calendar.DAY_OF_YEAR, -(mCalendar.get(Calendar.DAY_OF_MONTH) - 1));
        refreshCalendar();
    }

    private void setSelectedMonthText()
    {
        //TODO change constants
        String monthText=mFormatMonth.format(mMonthAL.get(10)); //magic numb)er
        mMonthText.setText(monthText + " " + mFormatYear.format(mMonthAL.get(10))); //magic number

    }

    public void setOnDispatchDateSelectListener(OnDispatchDateSelectListener v) {
        mListenerDateSelect = v;
    }
}
