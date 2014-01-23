package com.adm.meetup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.adm.meetup.util.Util;

public class MainActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mMenuTitles;
    private int[] mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mIcon = new int[]{
                R.drawable.ic_home_sidebar,
                R.drawable.ic_event_sidebar,
                R.drawable.ic_calendar_sidebar,
                R.drawable.ic_profile_sidebar,
                R.drawable.ic_about_sidebar,
                R.drawable.ic_settings_sidebar,
                R.drawable.ic_team_sidebar
        };

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Create and set MenuListAdapter
        MenuListAdapter mMenuAdapter = new MenuListAdapter(this, mMenuTitles, mIcon);
        mDrawerList.setAdapter(mMenuAdapter);

        // set up the drawer's list view with items and click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
                ; // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
                LoginActivity.onClickLogout();
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Util.PREFERENCES_EMAIL, Util.PREFERENCES_EMAIL_DEFAULT);
                editor.putString(Util.PREFERENCES_FIRSTNAME, Util.PREFERENCES_FIRSTNAME_DEFAULT);
                editor.putString(Util.PREFERENCES_LASTNAME, Util.PREFERENCES_LASTNAME_DEFAULT);
                editor.putString(Util.PREFERENCES_DATEOFBIRTH, Util.PREFERENCES_DATEOFBIRTH_DEFAULT);
                editor.putString(Util.PREFERENCES_STATUS, Util.PREFERENCES_STATUS_DEFAULT);
                editor.putString(Util.PREFERENCES_ERASMUSUNIVERSITY, Util.PREFERENCES_ERASMUSUNIVERSITY_DEFAULT);
                editor.putString(Util.PREFERENCES_HOMEUNIVERSITY, Util.PREFERENCES_HOMEUNIVERSITY_DEFAULT);
                editor.putString(Util.PREFERENCES_RELATIONSHIPSTATUS, Util.PREFERENCES_RELATIONSHIPSTATUS_DEFAULT);
                editor.putString(Util.PREFERENCES_LOCATIONSERVICES, Util.PREFERENCES_LOCATIONSERVICES_DEFAULT);
                editor.commit();
                // Closing dashboard screen
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

        }

    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new EventListFragment();
                break;
            case 2:
                fragment = new CalendarFragment();
                break;
            case 3:
                fragment = new ProfileFragment();
                break;
            case 5:
                fragment = new TeamFragment();
                break;
            case 6:
                fragment = new FriendsFragment();
                break;
            default:
                fragment = new Fragment();
                break;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
