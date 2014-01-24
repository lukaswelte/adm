package com.adm.meetup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adm.meetup.User.User;
import com.adm.meetup.helpers.NetworkHelper;
import com.adm.meetup.helpers.SharedApplication;
import com.adm.meetup.util.Util;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;


public class LoginActivity extends ActionBarActivity {

    private EditText emailText, passwordText;
    ProgressDialog progressBar;


    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private static final int REAUTH_ACTIVITY_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        buttonLoginLogout = (Button) findViewById(R.id.buttonLoginLogout);

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

        emailText = (EditText) findViewById(R.id.login_email_field);
        passwordText = (EditText) findViewById(R.id.login_password_field);
        Button loginButton = (Button) findViewById(R.id.login_login_button);

        loginButton.setOnClickListener(loginListener);

        TextView registerScreen = (TextView) findViewById(R.id.login_signup_button);

        // Listening to register new account link
        registerScreen.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            //  textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
            buttonLoginLogout.setText(R.string.logout);
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    onClickLogout();
                }
            });
            // Get the user's data
            makeMeRequest(session);

        } else {
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    onClickLogin();
                }
            });
        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
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
                                if (user.asMap().get("email") !=null) editor.putString(Util.PREFERENCES_EMAIL, user.asMap().get("email").toString());
                                if (user.getFirstName() !=null) editor.putString(Util.PREFERENCES_FIRSTNAME, user.getFirstName());
                                if (user.getLastName() !=null) editor.putString(Util.PREFERENCES_LASTNAME, user.getLastName());
                                editor.commit();
                                final ProgressDialog progressBar = new ProgressDialog(LoginActivity.this);
                                progressBar.setCancelable(true);
                                progressBar.setMessage(getString(R.string.progressBar_message));
                                progressBar.setProgress(20000);
                                progressBar.show();
                                NetworkHelper.facebookAuthRequest(LoginActivity.this, session.getAccessToken(), new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject jsonObject) {
                                        progressBar.hide();
                                        JsonElement error = jsonObject.get("error");
                                        Log.d("facebook auth", jsonObject.toString());
                                        if (error != null) {
                                            Toast.makeText(getApplicationContext(), error.getAsString(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            String token = jsonObject.get("token").getAsString();
                                            if (token != null) {
                                                Log.d("facebook auth token", token);

                                                SharedApplication.getInstance().setUserToken(token);
                                                Intent mainview = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(mainview);
                                                finish();
                                            } else
                                                Toast.makeText(getApplicationContext(), getString(R.string.token_not_found_error), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        if (response.getError() != null) {
                            Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
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
            progressBar.setMessage(getString(R.string.progressBar_message));
            progressBar.setProgress(200);
            progressBar.show();
            FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject jsonObject) {
                    try {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                            throw e;
                        }

                        progressBar.hide();
                        JsonElement error = jsonObject.get("error");
                        Log.d("login", jsonObject.toString());
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), error.getAsString(), Toast.LENGTH_SHORT).show();
                        } else {
                            SharedApplication.getInstance().setUser(new User(jsonObject));
                            String token = jsonObject.get("token").getAsString();
                            if (token != null) {
                                SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Util.PREFERENCES_EMAIL, emailText.getText().toString());
                                editor.commit();
                                SharedApplication.getInstance().setUserToken(token);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(getApplicationContext(), getString(R.string.token_not_found_error), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };

            NetworkHelper.loginRequest(LoginActivity.this, emailText.getText().toString(), passwordText.getText().toString(), callback);

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
