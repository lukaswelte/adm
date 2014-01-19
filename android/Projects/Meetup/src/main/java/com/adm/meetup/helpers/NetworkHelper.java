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
     * Method to trigger holidays on the backend
     *
     * @param context  The applications context interested in the request
     * @param year    The year we want the holidays of
     * @param country The country we want the holidays of
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void holidaysRequest(Context context, String year, String country,
                                       FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("year", year);
        postedJsonObject.addProperty("country", country);

        NetworkHelper.requestBackend(context, "/holidays", postedJsonObject, callback);
    }

    /**
     * Method to trigger list of exams on the backend
     * @param context The applications context interested in the request
     * @param token The token of the user we want the list of exams
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void examsRequest(Context context, String token, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("token", token);

        NetworkHelper.requestBackend(context, "/exam", postedJsonObject, callback);
    }

    /**
     * Method to add an exam on the backend
     * @param context The applications context interested in the request
     * @param token The token of the user we want the list of exams
     * @param date The date of the exam
     * @param notifyDate The date of notification of the exam
     * @param name The name of the exam
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void createExamRequest(Context context, String token, String date, String notifyDate,
                                         String name, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("token", token);
        postedJsonObject.addProperty("date", "14.12.2013 14:30");
        postedJsonObject.addProperty("notifydate", "14.12.2013 12:30");
        postedJsonObject.addProperty("name", "English Exam");

        NetworkHelper.requestBackend(context, "/exam/create", postedJsonObject, callback);
    }

    /**
     * Method to update an exam on the backend
     * @param context The applications context interested in the request
     * @param token The token of the user we want the list of exams
     * @param id The id of the exam to be updated
     * @param date The date of the exam
     * @param notifyDate The date of notification of the exam
     * @param name The name of the exam
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void updateExamRequest(Context context, String token, int id, String date,
                                         String notifyDate, String name, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("token", token);
        postedJsonObject.addProperty("id", 3123);
        postedJsonObject.addProperty("date", "14.12.2013 14:30");
        postedJsonObject.addProperty("notifydate", "14.12.2013 12:30");
        postedJsonObject.addProperty("name", "English Exam");

        NetworkHelper.requestBackend(context, "/exam/update", postedJsonObject, callback);
    }

    /**
     * Method to delete an exam on the backend
     * @param context The applications context interested in the request
     * @param token The token of the user we want the list of exams
     * @param id The id of the exam to be deleted
     * @param callback The Response JsonObject Handler dealing with the result
     */
    public static void deleteExamRequest(Context context, String token, int id, FutureCallback<JsonObject> callback) {
        JsonObject postedJsonObject = new JsonObject();
        postedJsonObject.addProperty("token", token);
        postedJsonObject.addProperty("id", 3123);

        NetworkHelper.requestBackend(context, "/exam/delete", postedJsonObject, callback);
    }

}
