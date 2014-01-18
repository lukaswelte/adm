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

import java.util.*;

public class AccountActivity extends ActionBarActivity {

    private ListView parametersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        SimpleAdapter mSchedule=null;

        // ListView
        parametersList = (ListView)findViewById(R.id.list_account_data);

        //Creation de la ArrayList qui nous permettra de remplire la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        //On declare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map = new HashMap<String, String>();

        if(mSchedule==null){
            map.put("title", "Fisrt Name");
            map.put("description",ProfileActivity.getFIRST_NAME());
            listItem.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Last Name");
            map.put("description",ProfileActivity.getLAST_NAME());
            listItem.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Email");
            map.put("description",ProfileActivity.getEmail());
            listItem.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Age");
            map.put("description","" + 123456789);
            listItem.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Home University");
            map.put("description","abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
            listItem.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Erasmus University");
            map.put("description","");
            listItem.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Status");
            map.put("description","abcdefghijklmnopqrstuvwxyz");
            listItem.add(map);

            mSchedule = new SimpleAdapter(AccountActivity.this.getBaseContext(), listItem, R.layout.item_account,
                    new String[] {"title", "description"}, new int[] {R.id.item_title, R.id.item_description});

        }
        parametersList.setAdapter(mSchedule);


/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);
            return rootView;
        }
    }

}
