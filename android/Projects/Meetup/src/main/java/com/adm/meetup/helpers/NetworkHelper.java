package com.adm.meetup.helpers;

import android.app.Activity;
import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.util.concurrent.ExecutionException;

/**
 * Created by lukas on 18.01.14.
 */
public class NetworkHelper {

    public static final String PRODUCTION_URL = "http://erasmus-meetup.herokuapp.com";

    /**
     * Request Something from the backend
     *
     * @param context           The applications context interested in the request. Can be null
     * @param apiURL            The Path to the API Method /auth/login for example
     * @param postedJsonObject  A json object if desired. Pass in null for get requests
     * @param jsonObjectHandler The Response JsonObject Handler dealing with the result
     */
    public static void requestBackend(Context context, String apiURL, JsonObject postedJsonObject, FutureCallback jsonObjectHandler) {
        requestBackend(context, apiURL, postedJsonObject, jsonObjectHandler, false);
    }

    /**
     * Request Something from the backend
     *
     * @param context           The applications context interested in the request. Can be null
     * @param apiURL            The Path to the API Method /auth/login for example
     * @param postedJsonObject  A json object if desired. Pass in null for get requests
     * @param jsonObjectHandler The Response JsonObject Handler dealing with the result
     * @param addAuthenticationToken Should add the current logged in user token to the request or not
     */
    public static void requestBackend(Context context, String apiURL, JsonObject postedJsonObject, FutureCallback jsonObjectHandler, boolean addAuthenticationToken) {
        if (context == null) {
            context = SharedApplication.getInstance().getApplicationContext();
        }

        if (addAuthenticationToken) {
            String userToken = SharedApplication.getInstance().userToken();
            if (userToken!=null && userToken.length() > 0) {
                apiURL = apiURL + "?token="+userToken;
            }
        }

        Builders.Any.B requestBuilder = Ion.with(context, PRODUCTION_URL + apiURL);
        if (postedJsonObject != null) {
            requestBuilder.setJsonObjectBody(postedJsonObject);
        }

        requestBuilder.asJsonObject().setCallback(jsonObjectHandler);
    }

    public static JsonElement requestBackendSynchronously(Context context, String apiURL, JsonObject postedJsonObject, boolean addAuthenticationToken) throws ExecutionException, InterruptedException {
        if (context == null) {
            context = SharedApplication.getInstance().getApplicationContext();
        }

        if (addAuthenticationToken) {
            String userToken = SharedApplication.getInstance().userToken();
            if (userToken!=null && userToken.length() > 0) {
                apiURL = apiURL + "?token="+userToken;
            }
        }

        Builders.Any.B requestBuilder = Ion.with(context, PRODUCTION_URL + apiURL);
        if (postedJsonObject != null) {
            requestBuilder.setJsonObjectBody(postedJsonObject);
        }

        return requestBuilder.asJsonArray().get();
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

}
