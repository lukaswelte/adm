package com.adm.meetup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adm.meetup.helpers.NetworkHelper;
import com.adm.meetup.helpers.SharedApplication;
import com.adm.meetup.util.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

public class RegisterActivity extends ActionBarActivity {

    Button registerButton;
    Button loginButton;
    EditText emailText;
    EditText passwordText;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        emailText = (EditText) findViewById(R.id.register_email_field);
        passwordText = (EditText) findViewById(R.id.register_password_field);
        registerButton = (Button) findViewById(R.id.register_registerNewAccount_button);
        loginButton = (Button) findViewById(R.id.register_linkToLogin_button);

        // Register Button Click event
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                progressBar = new ProgressDialog(view.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage(getString(R.string.progressBar_message));
                progressBar.setProgress(20000);
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
                            Log.d("register", jsonObject.toString());
                            if (error != null) {
                                Toast.makeText(getApplicationContext(), error.getAsString(), Toast.LENGTH_SHORT).show();
                            } else {
                                String token = jsonObject.get("token").getAsString();
                                if (token != null) {
                                    SharedPreferences pref = getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(Util.PREFERENCES_EMAIL, emailText.getText().toString());
                                    editor.commit();
                                    SharedApplication.getInstance().setUserToken(token);
                                    Intent intent = new Intent(RegisterActivity.this, RegisterConfirmationActivity.class);
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
                NetworkHelper.registerRequest(RegisterActivity.this, email, password, callback);
            }
        });

        // Link to Login Screen
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);
            return rootView;
        }
    }

}
