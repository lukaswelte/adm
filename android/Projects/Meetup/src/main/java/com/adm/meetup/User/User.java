package com.adm.meetup.User;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Andreas on 1/23/14.
 */
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<String> friends = new ArrayList<String>();
    private String token;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String id;

    private LatLng latLng;

    public User(JsonObject jsonObject) {
        Log.d("requeestuserjson", jsonObject.toString());
        this.email = jsonObject.get("email").getAsString();
        JsonElement firstNameElement = jsonObject.get("firstName");
        if (firstNameElement != null) {
            firstName = firstNameElement.getAsString();
        }
        JsonElement lastNameElement = jsonObject.get("lastName");
        if (firstNameElement != null) {
            lastName = lastNameElement.getAsString();
        }
        JsonElement statusElement = jsonObject.get("status");
        if (statusElement != null) {
            if (statusElement instanceof JsonNull) {
                status = "";
            } else {
                status = statusElement.getAsString();
            }
        }
        this.token = jsonObject.get("token").getAsString();
        this.createdAt = jsonObject.get("createdAt").getAsString();
        this.updatedAt = jsonObject.get("updatedAt").getAsString();
        this.id = jsonObject.get("id").getAsString();
        JsonParser parser = new JsonParser();
        JsonElement locationElement = jsonObject.get("statuslocation");
        if (locationElement != null) {
            JsonObject latLngJsonObject = (JsonObject) parser.parse(locationElement.getAsString());
            this.latLng = new LatLng(latLngJsonObject.get("latitude").getAsDouble(),
                    latLngJsonObject.get("longitude").getAsDouble());
        }
        Iterator<JsonElement> it = jsonObject.get("friends").getAsJsonArray().iterator();
        while (it.hasNext()) {
            friends.add(it.next().getAsString());
        }
    }

    public JsonObject getJsonObject() {
        JsonObject object = new JsonObject();
        if (firstName != null) {
            object.addProperty("firstName", firstName);
        }
        if (lastName != null) {
            object.addProperty("lastName", lastName);
        }
        object.addProperty("email", email);
        object.addProperty("token", token);
        object.addProperty("status", status);
        object.addProperty("createdAt", createdAt);
        object.addProperty("updatedAt", updatedAt);
        object.addProperty("id", id);
        JsonArray friendsJsonArray = new JsonArray();
        for (String friendId : friends) {
            friendsJsonArray.add(new JsonPrimitive(friendId));
        }
        object.add("friends", friendsJsonArray);
        return object;
    }

    public Marker addToMap(GoogleMap map) {
        if (map == null) return null;
        return map.addMarker(new MarkerOptions().title(this.status).position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
    }

    public boolean isFriendWithUser(User user) {
        String userId = user.getId();
        for (String friendId : friends) {
            if (friendId.equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public void addFriend(User user) {
        this.friends.add(user.getId());
        user.getFriends().add(this.id);
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}