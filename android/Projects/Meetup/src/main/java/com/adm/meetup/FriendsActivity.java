package com.adm.meetup;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.*;

public class FriendsActivity extends ActionBarActivity {

    private TabHost tabHost;
    private ListView parametersListFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        tabHost.setCurrentTabByTag("tab_local");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {
                SimpleAdapter mSchedule=null;

                if (tabHost.getCurrentTabTag()=="tab_local"){

                    parametersListFriends = (ListView)findViewById(R.id.listView_friends);

                    ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

                    HashMap<String, String> map = new HashMap<String, String>();

                    if(mSchedule==null){
                        map.put("friendname", "Friend Name 1");
                        listItem.add(map);

                        map = new HashMap<String, String>();
                        map.put("friendname", "Friend Name 2");
                        listItem.add(map);

                        map = new HashMap<String, String>();
                        map.put("friendname", "Friend Name 3");
                        listItem.add(map);

                        mSchedule = new SimpleAdapter (FriendsActivity.this.getBaseContext(), listItem, R.layout.item_friends,
                                new String[] {"friendname"}, new int[] {R.id.item_friendname});

                    }
                    parametersListFriends.setAdapter(mSchedule);
                }
            }
        });

        TabHost.TabSpec spec = tabHost.newTabSpec("tab_local");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.tab_friends));
        tabHost.addTab(spec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            return rootView;
        }
    }

}
