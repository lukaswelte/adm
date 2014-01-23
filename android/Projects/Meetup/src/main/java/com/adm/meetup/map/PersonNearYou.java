package com.adm.meetup.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

/**
 * Created by lukas on 22.01.14.
 */
public class PersonNearYou {

    private LatLng latLng;
    private Float distance;
    private String userID;

    public PersonNearYou(JsonObject jsonObject) {
        this.distance = jsonObject.get("distance").getAsFloat();
        this.userID = jsonObject.get("key").getAsString();
        Double latitude = jsonObject.get("latitude").getAsDouble();
        Double longitude = jsonObject.get("longitude").getAsDouble();
        this.latLng = new LatLng(latitude, longitude);
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public Float getDistance() {
        return distance;
    }

    public String getUserID() {
        return userID;
    }

    public Marker addToMap(GoogleMap map) {
        if (map == null) return null;
        return map.addMarker(new MarkerOptions().title(userID).position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
    }
}
