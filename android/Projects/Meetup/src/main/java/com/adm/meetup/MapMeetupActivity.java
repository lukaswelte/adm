package com.adm.meetup;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.code.gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapMeetupActivity extends ActionBarActivity implements LocationListener {

    public enum MarkerType {MARKER_TYPE_EVENTS, MARKER_TYPE_WHOS_PARTYING, MARKER_TYPE_FRIENDS};

    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Define the default zoom of the map, the one used first when the map is displayed
     */
    private final static int
            MAP_DEFAULT_CAMERA_ZOOM = 15;


    private GoogleMap mMap;
    private Marker markerUser;

    private ToggleButton mtoggleButtonPartyMode;
    private ToggleButton mtoggleButtonWhosPartying;
    private ToggleButton mtoggleButtonEvents;
    private ToggleButton mtoggleButtonFriends;

    private ArrayList<Marker> mwhosPartyingMarkers;
    private ArrayList<Marker> meventsMarkers;
    private ArrayList<Marker> mfriendsMarkers;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    protected boolean cameraMovedToCurrLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_meetup);

        if (savedInstanceState == null) {
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();*/

            cameraMovedToCurrLocation = false;

            if(servicesConnected())
            {
                mtoggleButtonEvents = (ToggleButton) findViewById(R.id.map_meetup_toggle_events);
                mtoggleButtonFriends = (ToggleButton) findViewById(R.id.map_meetup_toggle_friends);
                mtoggleButtonPartyMode = (ToggleButton) findViewById(R.id.map_meetup_toggle_party_mode);
                mtoggleButtonWhosPartying = (ToggleButton) findViewById(R.id.map_meetup_toggle_whos_partying);

                mtoggleButtonPartyMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(markerUser!=null)
                        {
                            if(b)
                            {
                                markerUser.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.google_maps_marker_party_mode));
                            }
                            else
                            {
                                markerUser.setIcon(BitmapDescriptorFactory.defaultMarker());
                            }
                        }
                    }
                });

                mtoggleButtonWhosPartying.setOnCheckedChangeListener(
                        new OnToggleWithMarkersCheckChangedListener(
                                this,
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                                mwhosPartyingMarkers,
                                MarkerType.MARKER_TYPE_WHOS_PARTYING));

                mtoggleButtonEvents.setOnCheckedChangeListener(
                        new OnToggleWithMarkersCheckChangedListener(
                                this,
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                                meventsMarkers,
                                MarkerType.MARKER_TYPE_EVENTS));

                mtoggleButtonFriends.setOnCheckedChangeListener(
                        new OnToggleWithMarkersCheckChangedListener(
                                this,
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                                mfriendsMarkers,
                                MarkerType.MARKER_TYPE_FRIENDS));

                mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if(marker.getId().equals(markerUser.getId())){
                            marker.setTitle(getString(R.string.map_meetup_youre_here));
                        }

                        return false;
                    }
                });

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    public ArrayList<LatLng> getLatLng(MarkerType atype) {
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        switch(atype){
            case MARKER_TYPE_WHOS_PARTYING:
                latLngs.add(new LatLng(39.483326,-0.344891));
                latLngs.add(new LatLng(39.48215,-0.346972));
                latLngs.add(new LatLng(39.482622,-0.348539));
                break;
            case MARKER_TYPE_EVENTS:
                latLngs.add(new LatLng(39.482282,-0.345781));
                latLngs.add(new LatLng(39.48239,-0.346683));
                break;
            case MARKER_TYPE_FRIENDS:
                latLngs.add(new LatLng(39.481794,-0.3465));
                latLngs.add(new LatLng(39.482746,-0.346575));
                break;
        }

        return latLngs;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.people_near_you, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_people_near_you, container, false);
            return rootView;
        }
    }



    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }


    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */

                        break;
                }
        }
    }

    private boolean servicesConnected() {

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        boolean connectionSuccess = (errorCode == ConnectionResult.SUCCESS);
        if (!connectionSuccess) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
        }
        return connectionSuccess;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("locaca", "changed");
        if(!cameraMovedToCurrLocation)
        {
            LatLng currLatLng = new LatLng(location.getLatitude(),location.getLongitude());

            // Move the camera instantly to the current location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, MAP_DEFAULT_CAMERA_ZOOM));
            markerUser = mMap.addMarker(new MarkerOptions()
                    .position(currLatLng)
                    .title("En tu mano izquierda, capullo !"));

            cameraMovedToCurrLocation = true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public GoogleMap getMap() {
        return mMap;
    }
}

class OnToggleWithMarkersCheckChangedListener implements CompoundButton.OnCheckedChangeListener {

private MapMeetupActivity msource;
private ArrayList<Marker> mmarkers;
private BitmapDescriptor mmarkersIcon;
private MapMeetupActivity.MarkerType mtype;

    public OnToggleWithMarkersCheckChangedListener(MapMeetupActivity asource,
                                                 BitmapDescriptor amarkersIcon,
                                                 ArrayList<Marker> amarkers,
                                                 MapMeetupActivity.MarkerType atype)
    {
        msource = asource;
        mmarkersIcon = amarkersIcon;
        mmarkers = amarkers;
        mtype = atype;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {
        if(b)
        {
            ArrayList<LatLng> whosPartyingLatLng = msource.getLatLng(mtype);
            {
                for(LatLng latLng : whosPartyingLatLng)
                {
                    if(mmarkers == null)
                    {
                        mmarkers = new ArrayList<Marker>();
                    }

                    mmarkers.add(msource.getMap().addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("I'm partying !")
                            .icon(mmarkersIcon)));
                }
            }
        }
        else
        {
            for(Marker marker : mmarkers)
            {
                marker.remove();
            }
            mmarkers.clear();
        }
    }

    public String readTwitterFeed()
    {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://twitter.com/statuses/user_timeline/vogella.json");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ParseJSON.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return builder.toString();
        }
}

class WaitForToastTask extends AsyncTask<Void, Integer, Boolean> {

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            Thread.sleep(3500);
            publishProgress();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        enableInteractions(true);
    }
}