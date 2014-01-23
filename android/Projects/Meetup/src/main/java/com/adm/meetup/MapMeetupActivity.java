package com.adm.meetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.adm.meetup.helpers.NetworkHelper;
import com.adm.meetup.map.PersonNearYou;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MapMeetupActivity extends ActionBarActivity {

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

    /*
     * Define the factor between the zoom value and the distance in which the user will see
     * markers. DISTANCE = FACTOR * ZOOM
     */
    private final static int
            FACTOR_BETWEEN_ZOOM_AND_DISTANCE_TO_MARKERS = 10;


    private GoogleMap map;

    private ArrayList<Marker> whoPartiesMarkers = new ArrayList<Marker>();
    private ArrayList<Marker> eventsMarkers = new ArrayList<Marker>();
    private ArrayList<Marker> friendsMarkers = new ArrayList<Marker>();

    private HashMap<Marker, PersonNearYou> personNearYouHashMap = new HashMap<Marker, PersonNearYou>();

    protected LocationManager locationManager;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_map_meetup);

        if (savedInstanceState == null) {

            if (servicesConnected()) {

                EditText statusEditText = (EditText) findViewById(R.id.map_status_edit_text);

                final MapMeetupActivity currContext = this;

                statusEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                            InputMethodManager inputManager = (InputMethodManager)
                                    currContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.toggleSoftInput(0, 0);

                            return true;
                        }
                        return false;
                    }
                });

                ToggleButton toggleButtonEvents = (ToggleButton) findViewById(R.id.map_meetup_toggle_events);
                ToggleButton toggleButtonFriends = (ToggleButton) findViewById(R.id.map_meetup_toggle_friends);
                //unused -- ToggleButton toggleButtonPartyMode = (ToggleButton) findViewById(R.id.map_meetup_toggle_party_mode);
                ToggleButton toggleButtonWhosPartying = (ToggleButton) findViewById(R.id.map_meetup_toggle_whos_partying);

                toggleButtonWhosPartying.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {

                        } else {
                            removeMarkers(whoPartiesMarkers);
                        }
                    }
                });

                toggleButtonEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    }
                });

                toggleButtonFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    }
                });

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0,0);
        setupMap();
    }

    private void removeMarkers(ArrayList<Marker> markers) {
        for (Marker marker : markers) {
            marker.remove();
        }
    }

    private void setupMap() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        zoomToUserLocationAndFetchMarkers();

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.d("lol", "pas lol");
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                return false;
            }
        });
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
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
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

    private void zoomToUserLocationAndFetchMarkers() {
        Location location = map.getMyLocation();

        if (location != null) {
            // Move the camera instantly to the current location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_DEFAULT_CAMERA_ZOOM));
            fetchMarkersWithLocation(location);

        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    zoomToUserLocationAndFetchMarkers();
                }
            }, 500);
        }
    }

    private void fetchMarkersWithLocation(Location location) {
        if (location == null) {
            location = map.getMyLocation();
            if (location == null) return;
        }

        VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
        LatLng latLngLeft = visibleRegion.farLeft;
        LatLng latLngRight = visibleRegion.farRight;
        Location farLeft = new Location("farLeft");
        farLeft.setLatitude(latLngLeft.latitude);
        farLeft.setLongitude(latLngLeft.longitude);

        Location farRight = new Location("farRight");
        farRight.setLatitude(latLngRight.latitude);
        farRight.setLongitude(latLngRight.longitude);

        float distanceInMeters = farLeft.distanceTo(farRight);

        NetworkHelper.peopleNearYouRequest(this, location.getLongitude(), location.getLatitude(), distanceInMeters, new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray jsonElements) {
                try {
                    if (e != null) throw e;

                    Iterator<JsonElement> iterator = jsonElements.iterator();
                    while (iterator.hasNext()) {
                        JsonElement element = iterator.next();
                        if (element.isJsonObject()) {
                            PersonNearYou personNearYou = new PersonNearYou(element.getAsJsonObject());
                            Marker marker = personNearYou.addToMap(map);
                            if (marker != null) {
                                personNearYouHashMap.put(marker, personNearYou);
                            }
                        }
                    }

                } catch (Exception ex) {
                    final Handler handler = new Handler(); //try again
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchMarkersWithLocation(null);
                        }
                    }, 1000);
                }
            }
        });
    }
}