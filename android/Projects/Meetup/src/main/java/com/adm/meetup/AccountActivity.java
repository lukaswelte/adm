package com.adm.meetup;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.adm.meetup.util.Util;





import java.util.*;

public class AccountActivity extends ActionBarActivity {

    private ListView parametersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        parametersList = (ListView)findViewById(R.id.list_account_data);

        majParametersListVAdapter();

        parametersList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, final long id) {
                ListView listV = (ListView)findViewById(R.id.list_account_data);

                Object itemAtPosition = listV.getItemAtPosition(position);
                if(itemAtPosition instanceof HashMap<?, ?>)
                {
                    HashMap<String, String> map = (HashMap<String, String>)itemAtPosition;

                    if(id==0 || id==1 || id==4 || id==5){
                        AlertDialog.Builder adb = new AlertDialog.Builder(AccountActivity.this);
                        adb.setTitle(map.get("title"));
                        final EditText input = new EditText(AccountActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        final AlertDialog.Builder adb2 = adb;
                        final HashMap<String, String> mapIp = map;
                        adb.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                changevariable(id,input);

                                majParametersListVAdapter();
                            }
                        });
                        adb.setNegativeButton(R.string.cancel, null);
                        adb.setView(input);
                        adb.show();
                    }
                    else if(id==3){		//relationship status
                        AlertDialog.Builder adb = new AlertDialog.Builder(AccountActivity.this);
                        adb.setTitle(map.get("title"));
                        final String[] types = {"Single", "In a relationship", "Engaged", "Married", "It's complicated", "In an open relationship", "Separated", "Divorced", "Widowed"};
                        adb.setItems(types, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                Editor editor = pref.edit();
                                editor.putString(Util.PREFERENCES_RELATIONSHIPSTATUS, types[which]);
                                editor.commit();

                                majParametersListVAdapter();
                            }
                        });

                        adb.show();
                    }
                    else if(id==7){		//location services
                        AlertDialog.Builder adb = new AlertDialog.Builder(AccountActivity.this);
                        adb.setTitle(map.get("title"));
                        final String[] types = {"Yes", "No"};
                        adb.setItems(types, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                Editor editor = pref.edit();
                                editor.putString(Util.PREFERENCES_LOCATIONSERVICES, types[which]);
                                editor.commit();

                                majParametersListVAdapter();
                            }
                        });

                        adb.show();
                    }
                    else if(id==2){		//date of birth
                        AlertDialog.Builder adb = new AlertDialog.Builder(AccountActivity.this);
                        adb.setTitle(map.get("title"));
                        final DatePicker input = new DatePicker(AccountActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        final AlertDialog.Builder adb2 = adb;
                        final HashMap<String, String> mapIp = map;
                        adb.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                Editor editor = pref.edit();
                                editor.putString(Util.PREFERENCES_DATEOFBIRTH, input.getDayOfMonth() + "." + (input.getMonth()+1) + "." + input.getYear() );
                                editor.commit();

                                majParametersListVAdapter();
                            }
                        });
                        adb.setNegativeButton(R.string.cancel, null);
                        adb.setView(input);
                        adb.show();
                    }
                }
            }
        });

    }

    private void changevariable(long id,EditText input){
        SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        if (id==0) editor.putString(Util.PREFERENCES_FIRSTNAME, input.getText().toString());
        if (id==1) editor.putString(Util.PREFERENCES_LASTNAME, input.getText().toString());
        if (id==6) editor.putString(Util.PREFERENCES_EMAIL, input.getText().toString());
        if (id==4) editor.putString(Util.PREFERENCES_HOMEUNIVERSITY, input.getText().toString());
        if (id==5) editor.putString(Util.PREFERENCES_ERASMUSUNIVERSITY, input.getText().toString());
        //if (id==7) editor.putString(Util.PREFERENCES_STATUS, input.getText().toString());

        editor.commit();
    }

    private void majParametersListVAdapter(){

        SimpleAdapter mSchedule=null;

        SharedPreferences preferences =
                getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        if(mSchedule==null){
            // 0 first name
            map.put("title", "First Name");
            map.put("description",preferences.getString(Util.PREFERENCES_FIRSTNAME,
                    Util.PREFERENCES_FIRSTNAME_DEFAULT));
            listItem.add(map);

            //1 last name
            map = new HashMap<String, String>();
            map.put("title", "Last Name");
            map.put("description",preferences.getString(Util.PREFERENCES_LASTNAME,
                    Util.PREFERENCES_LASTNAME_DEFAULT));
            listItem.add(map);

            // 2 date of birth
            map = new HashMap<String, String>();
            map.put("title", "Date of Birth");
            map.put("description",preferences.getString(Util.PREFERENCES_DATEOFBIRTH,
                    Util.PREFERENCES_DATEOFBIRTH_DEFAULT));
            listItem.add(map);

            //3 relationship
            map = new HashMap<String, String>();
            map.put("title", "Relationship status");
            map.put("description",preferences.getString(Util.PREFERENCES_RELATIONSHIPSTATUS,
                    Util.PREFERENCES_RELATIONSHIPSTATUS_DEFAULT));
            listItem.add(map);

            //4 home university
            map = new HashMap<String, String>();
            map.put("title", "Home University");
            map.put("description",preferences.getString(Util.PREFERENCES_HOMEUNIVERSITY,
                    Util.PREFERENCES_HOMEUNIVERSITY_DEFAULT));
            listItem.add(map);

            //5 erasmus university
            map = new HashMap<String, String>();
            map.put("title", "Erasmus University");
            map.put("description",preferences.getString(Util.PREFERENCES_ERASMUSUNIVERSITY,
                    Util.PREFERENCES_ERASMUSUNIVERSITY_DEFAULT));
            listItem.add(map);

/*            map = new HashMap<String, String>();
            map.put("title", "Status");
            map.put("description",preferences.getString(Util.PREFERENCES_STATUS,
                    Util.PREFERENCES_STATUS_DEFAULT));
            listItem.add(map);*/

            //6 email
            map = new HashMap<String, String>();
            map.put("title", "Email");
            map.put("description",preferences.getString(Util.PREFERENCES_EMAIL,
                    Util.PREFERENCES_EMAIL_DEFAULT));
            listItem.add(map);

            //7 location services
            map = new HashMap<String, String>();
            map.put("title", "Disable location services");
            map.put("description",preferences.getString(Util.PREFERENCES_LOCATIONSERVICES,
                    Util.PREFERENCES_LOCATIONSERVICES_DEFAULT));
            listItem.add(map);

            mSchedule = new SimpleAdapter(AccountActivity.this.getBaseContext(), listItem, R.layout.item_account,
                    new String[] {"title", "description"}, new int[] {R.id.item_title, R.id.item_description});

            parametersList.setAdapter(mSchedule);


        }

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
