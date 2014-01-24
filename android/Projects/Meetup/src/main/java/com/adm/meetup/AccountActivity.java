package com.adm.meetup;

import android.app.DatePickerDialog;
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

                    if(id==0 || id==1 || id==5 || id==6 || id==3){
                        AlertDialog.Builder adb = new AlertDialog.Builder(AccountActivity.this);
                        adb.setTitle(map.get("title"));
                        final EditText input = new EditText(AccountActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        final AlertDialog.Builder adb2 = adb;
                        final HashMap<String, String> mapIp = map;
                        adb.setPositiveButton(getString(R.string.ok), new OnClickListener() {

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
                    else if(id==4){		//relationship status
                        AlertDialog.Builder adb = new AlertDialog.Builder(AccountActivity.this);
                        adb.setTitle(map.get("title"));
                        final String[] types = {getString(R.string.single), getString(R.string.inRelationship),
                                getString(R.string.engaged), getString(R.string.married), getString(R.string.complicated),
                                getString(R.string.openRelationship), getString(R.string.separated),
                                getString(R.string.divorced), getString(R.string.widowed)};
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
                        final String[] types = {getString(R.string.yes), getString(R.string.no)};
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
                    else if(id==2){ // date of birth
                        DatePickerDialog dialog = new DatePickerDialog(AccountActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));
                        dialog.show();

                        //if we click on back button or outside datepicker the date is set to default one (null)
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                Editor editor = pref.edit();

                                editor.putString(Util.PREFERENCES_DATEOFBIRTH, Util.PREFERENCES_DATEOFBIRTH_DEFAULT);

                                editor.commit();
                                majParametersListVAdapter();

                            }
                        });
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
        if (id==5) editor.putString(Util.PREFERENCES_HOMEUNIVERSITY, input.getText().toString());
        if (id==6) editor.putString(Util.PREFERENCES_ERASMUSUNIVERSITY, input.getText().toString());
        if (id==3) editor.putString(Util.PREFERENCES_NATIONALITY, input.getText().toString());

        //if (id==7) editor.putString(Util.PREFERENCES_STATUS, input.getText().toString());
        //if (id==7) editor.putString(Util.PREFERENCES_EMAIL, input.getText().toString());

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
            map.put("title", getString(R.string.firstName));
            map.put("description",preferences.getString(Util.PREFERENCES_FIRSTNAME,
                    Util.PREFERENCES_FIRSTNAME_DEFAULT));
            listItem.add(map);

            //1 last name
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.lastName));
            map.put("description",preferences.getString(Util.PREFERENCES_LASTNAME,
                    Util.PREFERENCES_LASTNAME_DEFAULT));
            listItem.add(map);

            // 2 date of birth
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.dateOfBirth));
            map.put("description",preferences.getString(Util.PREFERENCES_DATEOFBIRTH,
                    Util.PREFERENCES_DATEOFBIRTH_DEFAULT));
            listItem.add(map);

            //3 nationality
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.nationality));
            map.put("description",preferences.getString(Util.PREFERENCES_NATIONALITY,
                    Util.PREFERENCES_NATIONALITY_DEFAULT));
            listItem.add(map);

            //4 relationship
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.relationshipStatus));
            map.put("description",preferences.getString(Util.PREFERENCES_RELATIONSHIPSTATUS,
                    Util.PREFERENCES_RELATIONSHIPSTATUS_DEFAULT));
            listItem.add(map);

            //5 home university
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.homeUniversity));
            map.put("description",preferences.getString(Util.PREFERENCES_HOMEUNIVERSITY,
                    Util.PREFERENCES_HOMEUNIVERSITY_DEFAULT));
            listItem.add(map);

            //6 erasmus university
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.ErasmusUniversity));
            map.put("description",preferences.getString(Util.PREFERENCES_ERASMUSUNIVERSITY,
                    Util.PREFERENCES_ERASMUSUNIVERSITY_DEFAULT));
            listItem.add(map);

/*            map = new HashMap<String, String>();
            map.put("title", "Status");
            map.put("description",preferences.getString(Util.PREFERENCES_STATUS,
                    Util.PREFERENCES_STATUS_DEFAULT));
            listItem.add(map);*/

/*            //7 email
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.email));
            map.put("description",preferences.getString(Util.PREFERENCES_EMAIL,
                    Util.PREFERENCES_EMAIL_DEFAULT));
            listItem.add(map);*/

            //7 location services
            map = new HashMap<String, String>();
            map.put("title", getString(R.string.disableLocationServices));
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

    Calendar myCalendar = Calendar.getInstance();


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
            Editor editor = pref.edit();

            editor.putString(Util.PREFERENCES_DATEOFBIRTH, ""+dayOfMonth + "." + (monthOfYear+1) + "." + year );

            editor.commit();

            majParametersListVAdapter();
        }

    };

}
