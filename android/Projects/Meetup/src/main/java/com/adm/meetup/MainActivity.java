package com.adm.meetup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.content.Intent;
import android.widget.Button;

import com.adm.meetup.util.Util;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Settings;
import com.facebook.model.GraphUser;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_account:
                Intent intent1 = new Intent(this, AccountActivity.class);
                startActivity(intent1);
                return true;

            case R.id.action_friends:
                Intent intent2 = new Intent(this, FriendsActivity.class);
                startActivity(intent2);
                return true;

            case R.id.action_logout:
                ProfileActivity.onClickLogout();
                Intent login = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(login);
                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Util.PREFERENCES_EMAIL,Util.PREFERENCES_EMAIL_DEFAULT);
                editor.putString(Util.PREFERENCES_FIRSTNAME,Util.PREFERENCES_FIRSTNAME_DEFAULT);
                editor.putString(Util.PREFERENCES_LASTNAME,Util.PREFERENCES_LASTNAME_DEFAULT);
                editor.putString(Util.PREFERENCES_DATEOFBIRTH,Util.PREFERENCES_DATEOFBIRTH_DEFAULT);
                editor.putString(Util.PREFERENCES_STATUS,Util.PREFERENCES_STATUS_DEFAULT);
                editor.putString(Util.PREFERENCES_ERASMUSUNIVERSITY,Util.PREFERENCES_ERASMUSUNIVERSITY_DEFAULT);
                editor.putString(Util.PREFERENCES_HOMEUNIVERSITY,Util.PREFERENCES_HOMEUNIVERSITY_DEFAULT);
                editor.putString(Util.PREFERENCES_RELATIONSHIPSTATUS,Util.PREFERENCES_RELATIONSHIPSTATUS_DEFAULT);
                editor.putString(Util.PREFERENCES_LOCATIONSERVICES,Util.PREFERENCES_LOCATIONSERVICES_DEFAULT);
                editor.commit();
                // Closing dashboard screen
                finish();
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
