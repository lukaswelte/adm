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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener{

    private GridView mGrid;
    private View 							mConvertView;
    private GregorianCalendar mCalendar;
    private Date[] 							mWeek;
    private Context mContext;
    private TextView mMonthText;
    private SimpleDateFormat mFormatMonth;
    private SimpleDateFormat 				mFormatDay;
    private SimpleDateFormat 				mFormatYear;
    private OnDispatchDateSelectListener 	mListenerDateSelect;
    private Button mArrowRight;
    private Button							mArrowLeft;
    private CalendarAdapter					mAdapter;
    public interface OnDispatchDateSelectListener {
        public void onDispatchDateSelect(Date date);
    }
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext			= context;
        mFormatMonth		= new SimpleDateFormat("MMMM");
        mFormatDay			= new SimpleDateFormat("d");
        mFormatYear			= new SimpleDateFormat("yyyy");

        mConvertView = LayoutInflater.from(context).inflate(R.layout.fragment_calendar_view, this);
        mGrid=(GridView)mConvertView.findViewById(R.id.calendar_days);

        mGrid.setOnItemClickListener(this);

        mMonthText=(TextView)mConvertView.findViewById(R.id.calendar_month);
        mArrowLeft=(Button)findViewById(R.id.calendar_arrow_left);
        mArrowLeft.setOnClickListener(this);

        mArrowRight=(Button)mConvertView.findViewById(R.id.calendar_arrow_right);
        mArrowRight.setOnClickListener(this);

        mCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DAY_OF_YEAR, -7);

        mWeek=new Date[7];
        for(int i=0;i<7;i++)
        {
            mWeek[i]=mCalendar.getTime();

            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        setSelectedMonthText();

        mAdapter=new CalendarAdapter(mContext, mWeek);

        mGrid.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        clearBackground();
        v.setBackgroundColor(Color.parseColor("#3A9CE9"));
        mListenerDateSelect.onDispatchDateSelect(mWeek[arg2]);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.calendar_arrow_left:
                subWeek();
                break;
            case R.id.calendar_arrow_right:
                addWeek();
                break;
        }
    }

    private void addWeek()
    {
        for(int i=0;i<7;i++)
        {
            mWeek[i]=mCalendar.getTime();
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        mAdapter.notifyDataSetChanged();

        setSelectedMonthText();
        clearBackground();
    }

    private void clearBackground()
    {
        for(int i=0;i<mGrid.getCount();i++)
        {
            mGrid.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void subWeek()
    {
        mCalendar.add(Calendar.DAY_OF_YEAR, -14);
        for(int i=0;i<7;i++)
        {
            mWeek[i]=mCalendar.getTime();
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        setSelectedMonthText();
        mAdapter.notifyDataSetChanged();

        clearBackground();
    }

    private void setSelectedMonthText()
    {
        String monthText;
        if(Integer.parseInt(mFormatDay.format(mWeek[0]))>Integer.parseInt(mFormatDay.format(mWeek[6])))
            monthText=mFormatMonth.format(mWeek[0])+" / "+mFormatMonth.format(mWeek[6]);
        else
            monthText=mFormatMonth.format(mWeek[0]);

        mMonthText.setText(monthText+" "+mFormatYear.format(mWeek[6]));
    }

    public void setOnDispatchDateSelectListener(OnDispatchDateSelectListener v) {
        mListenerDateSelect = v;
    }


}
