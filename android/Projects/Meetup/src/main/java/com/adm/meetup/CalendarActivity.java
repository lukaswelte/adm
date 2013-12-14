package com.adm.meetup;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Date;
import java.util.List;

public class CalendarActivity extends ActionBarActivity implements CalendarView.OnDispatchDateSelectListener{
    private TextView mTextDate;
    private SimpleDateFormat mFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        new GetHolidaysTask().execute();


        mTextDate=(TextView)findViewById(R.id.display_date);

        mFormat = new SimpleDateFormat("EEEE d MMMM yyyy");

        CalendarView cal = (CalendarView) findViewById(R.id.calendar);
        cal.setOnDispatchDateSelectListener(this);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
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
                        sb.append(line + "\n");
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
