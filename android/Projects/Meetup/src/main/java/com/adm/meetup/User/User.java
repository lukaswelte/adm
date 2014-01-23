package com.adm.meetup.User;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by Andreas on 1/23/14.
 */
public class User {
    private String email;
    private ArrayList<String> friends;
    private String token;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String id;

    private LatLng latLng;

    public User(JsonObject jsonObject) {
        this.email = jsonObject.get("email").getAsString();
        this.token = jsonObject.get("token").getAsString();
        this.status = jsonObject.get("status").getAsString();
        this.createdAt = jsonObject.get("createdAt").getAsString();
        this.updatedAt = jsonObject.get("updatedAt").getAsString();
        this.id = jsonObject.get("id").getAsString();
        JsonParser parser = new JsonParser();
        JsonObject latLngJsonObject = (JsonObject) parser.parse(jsonObject.get("statuslocation").getAsString());
        this.latLng = new LatLng(latLngJsonObject.get("latitude").getAsDouble(),
                latLngJsonObject.get("longitude").getAsDouble());
        for (JsonElement element : jsonObject.get("friends").getAsJsonArray()) {
            friends.add(element.getAsString());
        }
    }

    public Marker addToMap(GoogleMap map) {
        if (map == null) return null;
        return map.addMarker(new MarkerOptions().title(this.status).position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getId() {
        return id;
    }
}