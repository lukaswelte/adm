package com.adm.meetup.helpers;

import android.content.Context;

import com.adm.meetup.User.User;
import com.adm.meetup.calendar.Exam;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        requestBackend(context, apiURL, postedJsonObject, jsonObjectHandler, true);
    }

    /**
     * Request Something from the backend
     *
     * @param context                The applications context interested in the request. Can be null
     * @param apiURL                 The Path to the API Method /auth/login for example
     * @param postedJsonObject       A json object if desired. Pass in null for get requests
     * @param jsonObjectHandler      The Response JsonObject Handler dealing with the result
     * @param addAuthenticationToken Should add the current logged in user token to the request or not
     */
    public static void requestBackend(Context context, String apiURL, JsonObject postedJsonObject, FutureCallback jsonObjectHandler, boolean addAuthenticationToken) {
        if (context == null) {
            context = SharedApplication.getInstance().getApplicationContext();
        }

        if (addAuthenticationToken) {
            String userToken = SharedApplication.getInstance().userToken();
            if (userToken != null && userToken.length() > 0) {
                apiURL = apiURL + "?token=" + userToken;
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
            if (userToken != null && userToken.length() > 0) {
                apiURL = apiURL + "?token=" + userToken;
            }
        }

        Builders.Any.B requestBuilder = Ion.with(context, PRODUCTION_URL + apiURL);
        if (postedJsonObject != null) {
            requestBuilder.setJsonObjectBody(postedJsonObject);
        }

        return requestBuilder.asJsonArray().get();
    }

    /**
     * Method to trigger holidays on the backend
     *
     * @param context  The applications context interested in the request
     * @param year     The year we want the holidays of
     * @param country  The country we want the holidays of
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void holidaysRequest(Context context, String year, String country,
                                       FutureCallback<JsonArray> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("year", year);
        postedJsonObject.addProperty("country", country);

        NetworkHelper.requestBackend(context, "/holidays", postedJsonObject, callback, true);
    }

    /**
     * Method to trigger list of exams on the backend
     *
     * @param context  The applications context interested in the request
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void examsRequest(Context context, FutureCallback<JsonArray> callback) {
        NetworkHelper.requestBackend(context, "/exam", null, callback, true);
    }

    /**
     * Method to add an exam on the backend
     *
     * @param context  The applications context interested in the request
     * @param exam     The exam to create
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void createExamRequest(Context context,
                                         Exam exam, FutureCallback<JsonObject> callback) {

        NetworkHelper.requestBackend(context, "/exam/create", exam.asJsonObject(), callback, true);
    }

    /**
     * Method to update an exam on the backend
     *
     * @param context  The applications context interested in the request
     * @param id       The id of the exam to be updated
     * @param exam     The exam to update
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void updateExamRequest(Context context, int id,
                                         Exam exam, FutureCallback<JsonObject> callback) {
        NetworkHelper.requestBackend(context, "/exam/update", exam.asJsonObject(), callback, true);
    }

    /**
     * Method to delete an exam on the backend
     *
     * @param context  The applications context interested in the request
     * @param exam     The exam to be deleted
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void deleteExamRequest(Context context, Exam exam, FutureCallback<JsonObject> callback) {
        NetworkHelper.requestBackend(context, "/exam/delete", exam.asJsonObject(), callback, true);
    }

    /**
     * Method to find the people around you
     *
     * @param context   The applications context interested in the request
     * @param longitude The users longitude
     * @param latitude  The users latitude
     * @param distance  The max distance between the user and the people he will see
     * @param callback  The Response JsonArray Handler dealing with the result
     */
    public static void peopleNearYouRequest(Context context, double longitude, double latitude, float distance, FutureCallback<JsonArray> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("latitude", latitude);
        postedJsonObject.addProperty("longitude", longitude);
        postedJsonObject.addProperty("distance", distance);

        NetworkHelper.requestBackend(context, "/profiles/nearyou/", postedJsonObject, callback, true);
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
        postedJsonObject.addProperty("email", email);
        postedJsonObject.addProperty("password", password);

        NetworkHelper.requestBackend(context, "/auth/login", postedJsonObject, callback, false);
    }

    /**
     * Method to Register new user via email
     *
     * @param context  The applications context interested in the request
     * @param email    The users email address
     * @param password The users password
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void registerRequest(Context context, String email, String password, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("email", email);
        postedJsonObject.addProperty("password", password);

        NetworkHelper.requestBackend(context, "/auth/signup", postedJsonObject, callback, false);
    }

    /**
     * Method to Authenticate the user from facebook
     *
     * @param context           The applications context interested in the request
     * @param facebookAuthToken facebook auth token
     * @param callback          The Response JsonObject Handler dealing with the result
     */
    public static void facebookAuthRequest(Context context, String facebookAuthToken, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("facebookToken", facebookAuthToken);

        NetworkHelper.requestBackend(context, "/auth/facebook", postedJsonObject, callback, false);
    }

    /**
     * Method to get your profile
     *
     * @param context  The applications context interested in the request
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void getUserProfile(Context context, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        NetworkHelper.requestBackend(context, "/profiles/me", postedJsonObject, callback, true);
    }

    /**
     * Method to update your profile
     *
     * @param context  The applications context interested in the request
     * @param user     the user to update
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void updateProfile(Context context, User user, FutureCallback<JsonElement> callback) {
        JsonObject postedJsonObject = user.getJsonObject();
        NetworkHelper.requestBackend(context, "/profiles/updateme", postedJsonObject, callback, true);
    }

    /**
     * Method to get someone s profile
     *
     * @param context  The applications context interested in the request
     * @param id       the id og the user to get
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void getProfile(Context context, String id, FutureCallback<JsonElement> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("id", id);
        NetworkHelper.requestBackend(context, "/profiles", postedJsonObject, callback, true);
    }
}
