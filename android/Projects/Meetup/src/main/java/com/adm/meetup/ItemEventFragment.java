package com.adm.meetup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.model.EventItem;

/**
 * Created by florian on 04/12/2013.
 */
public class ItemEventFragment extends Fragment {

    private EventItem item;
    private TextView location;
    private TextView description;
    private TextView title;
    private ImageView icon;

    public ItemEventFragment(){}
    public ItemEventFragment(EventItem item ){
        this.item = item;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_item, container, false);
        title = (TextView) rootView.findViewById(R.id.titleEvent);
        location = (TextView) rootView.findViewById(R.id.location);
        description = (TextView) rootView.findViewById(R.id.description);
        icon = (ImageView) rootView.findViewById(R.id.imgCategorie);

        title.setText(item.getTitle());
        location.setText(item.getLocation());
        description.setText(item.getDescription());
        icon.setImageResource(item.getIcon());


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.actions_item_event, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_edit_event:
                return true;
            case R.id.action_delete_event:
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
            menu.findItem(R.id.action_edit_event).setVisible(false);
            menu.findItem(R.id.action_delete_event).setVisible(false);

        }
        else  {
            menu.findItem(R.id.action_edit_event).setVisible(true);
            menu.findItem(R.id.action_delete_event).setVisible(true);
        }

        super.onPrepareOptionsMenu(menu);
    }
}