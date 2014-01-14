package com.adm.meetup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class CalendarAdapter extends BaseAdapter {

    public static final int iDaysOfWeek = 7;
    private Context 			mContext;
    private ArrayList<Date>     mMonth;
    private SimpleDateFormat 	mFormatNumber;
    private SimpleDateFormat 	mFormatDay;
    private LayoutInflater		mInflater;

    public CalendarAdapter(Context _context, ArrayList<Date> _month) {
        mContext 		= _context;
        mMonth          = _month;
        mFormatNumber	= new SimpleDateFormat("d");
        mFormatDay  	= new SimpleDateFormat("EEE");
        mInflater 		= LayoutInflater.from(mContext);
    }

    private static class ViewHolder {
        public TextView		tvDay;
        public TextView		tvNumber;
        public LinearLayout lLayout;
        public Layout layout;
    }

    @Override
    public int getCount() {
        return mMonth.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView		= mInflater.inflate(R.layout.view_calendar_day, null);
            holder			= new ViewHolder();
            if(convertView.findViewById(R.id.tv_day) != null) {
            holder.tvDay 	= (TextView)  convertView.findViewById(R.id.tv_day);}
            if(convertView.findViewById(R.id.tv_number) != null){
            holder.tvNumber	= (TextView)  convertView.findViewById(R.id.tv_number);}
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mMonth.get(position) != null)
        {
            String sNumberDay = mFormatNumber.format(mMonth.get(position));
            if(!(Integer.parseInt(sNumberDay) > 7 && position < 7))
            {

                {
                    holder.tvNumber.setText(sNumberDay);
                    holder.tvDay.setText(mFormatDay.format(mMonth.get(position)));
                }
            }
            else {
                holder.tvNumber.setText("");
                holder.tvDay.setText("");
            }
        }

        return convertView;
    }

}
