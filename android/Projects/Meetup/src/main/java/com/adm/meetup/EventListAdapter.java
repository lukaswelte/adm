package com.adm.meetup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adm.meetup.event.Event;

import java.util.List;

/**
 * Created by lukas on 21.01.14.
 */
public class EventListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Event> eventList;

    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return eventList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.event_list_item, viewGroup, false);

        Event event = eventList.get(i);
        TextView eventName = (TextView) itemView.findViewById(R.id.eventNameTextView);
        eventName.setText(event.getName());

        return itemView;
    }
}
