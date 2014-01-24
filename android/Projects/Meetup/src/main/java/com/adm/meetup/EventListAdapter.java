package com.adm.meetup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adm.meetup.event.Event;
import com.adm.meetup.helpers.DateHelper;

import java.util.List;

/**
 * Created by lukas on 21.01.14.
 */
public class EventListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> eventList;

    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return (eventList == null) ? 0 : eventList.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.event_list_item, viewGroup, false);
        if (itemView != null) {
            Event event = eventList.get(i);

            ImageView EventImage = (ImageView) itemView.findViewById(R.id.EventImage);
            String uri = "drawable/ic_event_list";
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            Drawable image = context.getResources().getDrawable(imageResource);
            EventImage.setImageDrawable(image);

            TextView eventName = (TextView) itemView.findViewById(R.id.eventNameTextView);
            eventName.setText(event.getName());

            TextView eventLocation = (TextView) itemView.findViewById(R.id.eventLocationTextView);
            eventLocation.setText(event.getLocation() + ",");

            TextView eventDate = (TextView) itemView.findViewById(R.id.eventDescDueDateTextView);
            eventDate.setText("" + DateHelper.format(event.getDate()));
        }

        return itemView;
    }
}
