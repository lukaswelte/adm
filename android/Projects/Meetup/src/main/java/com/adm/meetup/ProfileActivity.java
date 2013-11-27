package com.adm.meetup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.adm.meetup.MainActivity;
import com.adm.meetup.UserFunctions;

public class ProfileActivity extends ActionBarActivity {
    EditText emailText,passwordText;
    Button loginButton,registerButton;
    TextView loginErrorMsg;

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
        setContentView(R.layout.activity_profile);
/*
        // Importing all assets like buttons, text fields
        emailText = (EditText) findViewById(R.id.profile_email_field);
        passwordText = (EditText) findViewById(R.id.profile_password_field);
        loginButton = (Button) findViewById(R.id.profile_login_button);
        registerButton = (Button) findViewById(R.id.profile_signup_button);
        loginErrorMsg = (TextView) findViewById(R.id.profile_error_field);

        // Login button Click Event
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUser(email, password);

                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
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

                            // Close Login Screen
                            finish();
                        }else{
                            // Error in login
                            loginErrorMsg.setText("Incorrect username/password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Link to Register Screen
        registerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });*/


        TextView registerScreen = (TextView) findViewById(R.id.profile_signup_button);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        emailText=(EditText)findViewById(R.id.profile_email_field);
        passwordText=(EditText)findViewById(R.id.profile_password_field);
       loginButton=(Button)findViewById(R.id.profile_login_button);

        loginButton.setOnClickListener(loginListener);

/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }*/
    }
    private OnClickListener loginListener = new OnClickListener() {
        public void onClick(View v) {

            //getting inputs from user and performing data operations
            if(emailText.getText().toString().equals("swineas") &&
                    passwordText.getText().toString().equals("abc")){
                //responding to the User inputs
                Toast.makeText(getApplicationContext(), "Connexion réussie !!!", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainIntent);
            }else
                Toast.makeText(getApplicationContext(), "Connexion échouée !!!", Toast.LENGTH_LONG).show();
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

/*    *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            return rootView;
        }
    }*/

}
