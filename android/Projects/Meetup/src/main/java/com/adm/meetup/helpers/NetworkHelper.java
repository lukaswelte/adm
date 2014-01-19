package com.adm.meetup.helpers;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

/**
 * Created by lukas on 18.01.14.
 */
public class NetworkHelper {

    public static final String PRODUCTION_URL = "http://erasmus-meetup.herokuapp.com";

    /**
     * Request Something from the backend
     *
     * @param context           The applications context interested in the request
     * @param apiURL            The Path to the API Method /auth/login for example
     * @param postedJsonObject  A json object if desired. Pass in null for get requests
     * @param jsonObjectHandler The Response JsonObject Handler dealing with the result
     */
    public static void requestBackend(Context context, String apiURL, JsonObject postedJsonObject, FutureCallback<JsonObject> jsonObjectHandler) {
        Builders.Any.B requestBuilder = Ion.with(context, PRODUCTION_URL + apiURL);
        if (postedJsonObject != null) {
            requestBuilder.setJsonObjectBody(postedJsonObject);
        }

        requestBuilder.asJsonObject().setCallback(jsonObjectHandler);
    }


    /**
     * Method to trigger login via email and password on the backend
     *
     * @param context  The applications context interested in the request
     * @param email    The users email address
     * @param password The users password
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void loginRequest(Context context, String email, String password, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("email", "test@test.com");
        postedJsonObject.addProperty("password", "pass");

        NetworkHelper.requestBackend(context, "/auth/login", postedJsonObject, callback);
    }


    /**
     * Method to find the people around you
     *
     * @param context  The applications context interested in the request
     * @param longitude    The users longitude
     * @param latitude The users latitude
     * @param distance The max distance between the user and the people he will see
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void peopleNearYouRequest(Context context, double longitude, double latitude, int distance, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("latitude", latitude);
        postedJsonObject.addProperty("longitude", longitude);
        postedJsonObject.addProperty("distance", distance);

        NetworkHelper.requestBackend(context, "/profiles/nearyou/", postedJsonObject, callback);
    }

}
