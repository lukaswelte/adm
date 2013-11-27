package com.adm.meetup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.adm.meetup.DatabaseHandler;
import com.adm.meetup.UserFunctions;

public class RegisterActivity extends ActionBarActivity {

    Button registerButton;
    Button loginButton;
    EditText fullNameText;
    EditText emailText;
    EditText passwordText;
    TextView registerErrorMsg;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
/*
        TextView loginScreen = (TextView) findViewById(R.id.register_linkToLogin_button);
*/

        // Importing all assets like buttons, text fields
        fullNameText = (EditText) findViewById(R.id.register_fullName_field);
        emailText = (EditText) findViewById(R.id.register_email_field);
        passwordText = (EditText) findViewById(R.id.register_password_field);
        registerButton = (Button) findViewById(R.id.register_registerNewAccount_button);
        loginButton = (Button) findViewById(R.id.register_linkToLogin_button);
        registerErrorMsg = (TextView) findViewById(R.id.register_error_field);

        // Register Button Click event
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = fullNameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.registerUser(name, email, password);

                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully registred
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");

                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                            // Close Registration Screen
                            finish();
                        }else{
                            // Error in registration
                            registerErrorMsg.setText("Error occured in registration");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Link to Login Screen
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ProfileActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });

/*        // Listening to login screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
*//*                // Switching to Login screen
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);*//*
                finish();
            }
        });*/

/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
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
