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
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    UserFunctions userFunctions;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = (Button) findViewById(R.id.dashboard_logout_button);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent login = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(login);
                // Closing dashboard screen
                finish();
            }
        });

/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/


        /**
         * MainActivity Screen for the application
         * */

/*         // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
            // user already logged in show mainActivity
            setContentView(R.layout.activity_main);
            btnLogout = (Button) findViewById(R.id.dashboard_logout_button);

            btnLogout.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), ProfileActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Closing dashboard screen
                    finish();
                }
            });
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), ProfileActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }
    }*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
