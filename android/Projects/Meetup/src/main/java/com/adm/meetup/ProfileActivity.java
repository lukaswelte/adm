package com.adm.meetup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.adm.meetup.helpers.NetworkHelper;
import com.adm.meetup.helpers.SharedApplication;
import com.adm.meetup.util.Util;
import com.facebook.*;
import com.facebook.model.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;


public class ProfileActivity extends ActionBarActivity {

    private EditText emailText,passwordText;
    private Button loginButton;
    private TextView registerScreen;
    ProgressDialog progressBar;


    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private static final int REAUTH_ACTIVITY_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
//END OF FACEBOOK INTEGRATION (INSIDE ON CREATE)

        emailText=(EditText)findViewById(R.id.profile_email_field);
        passwordText=(EditText)findViewById(R.id.profile_password_field);
        loginButton=(Button)findViewById(R.id.profile_login_button);

        loginButton.setOnClickListener(loginListener);

        registerScreen = (TextView) findViewById(R.id.profile_signup_button);

        // Listening to register new account link
        registerScreen.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        uiHelper = new UiLifecycleHelper(this,callback);
        uiHelper.onCreate(savedInstanceState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            //  textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
            buttonLoginLogout.setText(R.string.logout);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });
            // Get the user's data
            makeMeRequest(session);

        } else {
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin();
                }
            });
        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true,statusCallback);
        }
    }

    public static void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Session session = Session.getActiveSession();
        Session.saveSession(session, bundle);
        uiHelper.onSaveInstanceState(bundle);

    }

    private void makeMeRequest(final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Util.PREFERENCES_EMAIL,user.asMap().get("email").toString());
                                editor.putString(Util.PREFERENCES_FIRSTNAME,user.getFirstName());
                                editor.putString(Util.PREFERENCES_LASTNAME, user.getLastName());
                                editor.commit();
                                NetworkHelper.facebookAuthRequest(ProfileActivity.this, session.getAccessToken(), new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject jsonObject) {
                                        JsonElement error = jsonObject.get("error");
                                        Log.d("facebook auth", jsonObject.toString());
                                        if(error != null)
                                        {
                                            Toast.makeText(getApplicationContext(),error.getAsString(), Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            String token = jsonObject.get("token").getAsString();
                                            if (token != null) {
                                                Log.d("facebook auth token", token);

                                                SharedApplication.getInstance().setUserToken(token);
                                                Intent mainview = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(mainview);
                                            }
                                            else Toast.makeText(getApplicationContext(),getString(R.string.token_not_found_error), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }

    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            // Get the user's data.
            makeMeRequest(session);
        }
    }
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
    private OnClickListener loginListener = new OnClickListener() {
        public void onClick(View v) {
            progressBar = new ProgressDialog(v.getContext());
            progressBar.setCancelable(true);
            progressBar.setMessage("Please wait");
            progressBar.setProgress(20000);
            progressBar.show();
                FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject jsonObject) {
                        progressBar.hide();
                        JsonElement error = jsonObject.get("error");
                        Log.d("login", jsonObject.toString());
                        if (error !=null)
                        {
                            Toast.makeText(getApplicationContext(),error.getAsString(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String token = jsonObject.get("token").getAsString();
                            if (token != null) {
                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Util.PREFERENCES_EMAIL,emailText.getText().toString());
                                editor.commit();
                                SharedApplication.getInstance().setUserToken(token);
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else Toast.makeText(getApplicationContext(),getString(R.string.token_not_found_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                NetworkHelper.loginRequest(ProfileActivity.this, emailText.getText().toString(), passwordText.getText().toString(), callback);

        }
    };

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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


}
