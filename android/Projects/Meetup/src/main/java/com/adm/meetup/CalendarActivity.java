package com.adm.meetup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class CalendarActivity extends ActionBarActivity implements CalendarView.OnDispatchDateSelectListener {
    public static final int iMoreThanAMonth = 32;
    private TextView mTextDate;
    private SimpleDateFormat mFormat;
    private CalendarView cal;
    private Context context;

    private final String PREFERENCES_MONTH = "shown_month";
    private final String PREFERENCES_FILE = "calendar_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mTextDate=(TextView)findViewById(R.id.display_date);
        mFormat = new SimpleDateFormat("EEEE d MMMM yyyy");
        context = this;

        cal = (CalendarView) findViewById(R.id.calendar);
        cal.setOnDispatchDateSelectListener(this);
        getHolidays(cal.getmCalendar().getTime());
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Saving current month to show it later
        GregorianCalendar actualCal = cal.getmCalendar();
        Long timeToSave = actualCal.getTimeInMillis();
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREFERENCES_MONTH, timeToSave);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_FILE,
                Context.MODE_PRIVATE);

        //Getting default value : today's date
        GregorianCalendar tempCal = new GregorianCalendar();
        tempCal.setTime(new Date());
        tempCal.add(Calendar.DAY_OF_YEAR, -(tempCal.get(Calendar.DAY_OF_MONTH) -1));
        long iDefaultTime = tempCal.getTimeInMillis();
        //Setting month
        tempCal.setTimeInMillis(preferences.getLong(PREFERENCES_MONTH, iDefaultTime));
        cal = (CalendarView) findViewById(R.id.calendar);
        cal.setmCalendar(tempCal);
        cal.refreshCalendar();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resettingCalendar();

    }

    private void resettingCalendar() {
        //Getting today's date
        GregorianCalendar tempCal = new GregorianCalendar();
        tempCal.setTime(new Date());
        //Setting to the next month for future getmMonth method's call
        tempCal.add(Calendar.DAY_OF_YEAR, iMoreThanAMonth);
        tempCal.add(Calendar.DAY_OF_YEAR, -(tempCal.get(Calendar.DAY_OF_MONTH) -1));
        cal.setmCalendar(tempCal);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            resettingCalendar();
            //Normal home button action
            Intent showOptions = new Intent(Intent.ACTION_MAIN);
            showOptions.addCategory(Intent.CATEGORY_HOME);
            startActivity(showOptions);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDispatchDateSelect(Date date) {
        mTextDate.setText(mFormat.format(date));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Date> getHolidays(final Date currentDate) {
        ArrayList<Date> holidays = new ArrayList<Date>();
        FutureCallback<JsonArray> callback = new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray jsonArray) {
                try{
                    if (e != null) {
                        throw e;
                    }
                    Iterator<JsonElement> iterator = jsonArray.iterator();
                    while (iterator.hasNext()) {
                        JsonObject jsonObject = iterator.next().getAsJsonObject();

                        //Get date
                        String dateStr = jsonObject.get("date").getAsString();
                        dateStr = dateStr.replace(".", "/");
                        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateObj = curFormater.parse(dateStr);

                        //Comparing dates
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(currentDate);
                        cal2.setTime(dateObj);
                        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

                        //If sameDay, we need details
                        if (sameDay) {
                            String name = jsonObject.get("name").getAsString();
                        }
                    }
                }
                catch (Exception ex)
                {
                    Log.e("ERROR", "Error in CalendarActivity: " + ex.toString());
                    Toast.makeText(context, getString(R.string.calendar_servor_error), Toast.LENGTH_SHORT).show();
                }

            }
        };
        NetworkHelper.holidaysRequest(this, "2013", "es", callback);

        return holidays;
    }

    private ArrayList<Date> getExams()
    {
        ArrayList<Date> exams = new ArrayList<Date>();
        FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                try{
                    Log.d("TAG", jsonObject.toString());
                    Log.d("Wait", "1");
                }
                catch (Exception ex)
                {
                    Log.e("ERROR", "Error in CalendarActivity: " + e.toString());
                    Toast.makeText(context, getString(R.string.calendar_servor_error), Toast.LENGTH_SHORT).show();
                }

            }
        };
        NetworkHelper.examsRequest(context, "token", callback);

        return exams;
    }

    private void createExam(String date, String notifyDate, String name)
    {
        FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                try{
                    Log.d("TAG", jsonObject.toString());
                    Log.d("Wait", "1");
                }
                catch (Exception ex)
                {
                    Log.e("ERROR", "Error in CalendarActivity: " + e.toString());
                    Toast.makeText(context, getString(R.string.calendar_servor_error), Toast.LENGTH_SHORT).show();
                }

            }
        };
        NetworkHelper.createExamRequest(context, "token", date, notifyDate, name, callback);
    }

    private void updateExam(int examId, String date, String notifyDate, String name)
    {
        FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                try{
                    Log.d("TAG", jsonObject.toString());
                    Log.d("Wait", "1");
                }
                catch (Exception ex)
                {
                    Log.e("ERROR", "Error in CalendarActivity: " + e.toString());
                    Toast.makeText(context, getString(R.string.calendar_servor_error), Toast.LENGTH_SHORT).show();
                }

            }
        };
        NetworkHelper.updateExamRequest(context, "token", examId, date, notifyDate, name, callback);
    }

    private void deleteExam(int examId)
    {
        FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                try{
                    Log.d("TAG", jsonObject.toString());
                    Log.d("Wait", "1");
                }
                catch (Exception ex)
                {
                    Log.e("ERROR", "Error in CalendarActivity: " + e.toString());
                    Toast.makeText(context, getString(R.string.calendar_servor_error), Toast.LENGTH_SHORT).show();
                }

            }
        };
        NetworkHelper.deleteExamRequest(context, "token", examId, callback);
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
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

            return rootView;
        }
    }

    private class GetHolidaysTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                // Values to add

                /*SharedPreferences preferences = getSharedPreferences(
                        PREFERENCES_FILE, Context.MODE_PRIVATE);

                name = preferences.getString(PREFERENCES_USER_NAME, null);*/

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();

                pairs.add(new BasicNameValuePair("year", "2013"));
                pairs.add(new BasicNameValuePair("country", "es"));

                HttpClient client = new DefaultHttpClient();

                HttpGet request = new HttpGet(
                        "http://meetup.apiary.io/holidays?"
                                + URLEncodedUtils.format(pairs, "utf-8"));

                HttpResponse response = client.execute(request);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream stream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(stream));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    stream.close();
                    String responseString = sb.toString();
                    System.out.println(responseString);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
        }

    }

}
