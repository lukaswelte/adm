package com.adm.meetup;

/**
 * Created by florian on 01/12/2013.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adm.adapter.EventAdapter;
import com.adm.model.EventItem;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    // slide menu items
    private TypedArray eventListIcons;
    private ListView eventsList;
    private ArrayList<EventItem> events;
    private EventAdapter adapter;
    final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed pretium mi. " +
            "In hac habitasse platea dictumst. Vivamus aliquam at neque non ultrices. Ut sed convallis nulla." +
            " Nulla tempor eros sit amet scelerisque viverra. Fusce gravida justo odio, at scelerisque augue molestie luctus." +
            " Vestibulum in tempus augue, non auctor odio. Nulla vulputate nibh id consequat hendrerit. " +
            "Vestibulum et turpis ac metus fermentum tristique vel nec turpis. Donec mattis fringilla convallis." +
            " Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
            "Morbi ac tortor euismod, sagittis nulla scelerisque, placerat est. Aliquam ac hendrerit magna, in interdum mauris. " +
            "Etiam tempus sem eu sem congue convallis eu sit amet purus.";

    public EventFragment(){}

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        // nav drawer icons from resources
        eventListIcons = getResources()
                .obtainTypedArray(R.array.list_event_icons);

      //  mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        eventsList = (ListView) rootView.findViewById(R.id.listEvent);

        events = new ArrayList<EventItem>();

        // adding nav drawer items to array
        // Home

        events.add(new EventItem("Football game : Valencia vs Real Madrid", eventListIcons.getResourceId(0, -1),
                "Valencia,Spain, " , lorem));
        // Find People
        events.add(new EventItem("Botellon + Umbracle&Mya", eventListIcons.getResourceId(1, -1),"Valencia,Spain",lorem));
        // Photos
        events.add(new EventItem("Trip to Barcelona : 3 days, 99 euros", eventListIcons.getResourceId(2, -1),"Barcelona,Spain",lorem));
        // Communities, Will add a counter here
        events.add(new EventItem("Live concert David Guetta", eventListIcons.getResourceId(3, -1),"Madrid,Spain",lorem));
        // Pages
        events.add(new EventItem( "Tennis : Nadal vs Ferrer", eventListIcons.getResourceId(4, -1),"Valencia,Spain",lorem));
        // What's hot, We  will add a counter here
        events.add(new EventItem("Trip to Portugal : 5 days, 130 euros", eventListIcons.getResourceId(5, -1),"Lisbon, Portugal",lorem));

        events.add(new EventItem("Botellon + Las Animas", eventListIcons.getResourceId(6, -1),"Valencia,Spain",lorem));

        final  Context c= rootView.getContext();

        // Recycle the typed array
        eventListIcons.recycle();

        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Fragment f = new ItemEventFragment(events.get(position));
                makeTransaction(f);
                //Toast.makeText(c, "You Clicked at event " + events.get(position).getDescription(), Toast.LENGTH_SHORT).show();

            }
        });

        // setting the nav drawer list adapter
        adapter = new EventAdapter(rootView.getContext(),
                events);
        eventsList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.actions_events, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_new_event:
                Fragment fragment = new CreateEventFragment();
                makeTransaction(fragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        DrawerLayout dr =(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ListView dl= (ListView) getActivity().findViewById(R.id.list_slidermenu);
        if(dr.isDrawerOpen(dl)){
            menu.findItem(R.id.action_new_event).setVisible(false);
        }
        else  menu.findItem(R.id.action_new_event).setVisible(true);

        super.onPrepareOptionsMenu(menu);
    }

    public void makeTransaction(Fragment fragment){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}