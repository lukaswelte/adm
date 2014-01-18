package com.adm.meetup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends ActionBarActivity {

    Button registerButton;
    Button loginButton;
    EditText fullNameText;
    EditText emailText;
    EditText passwordText;
    TextView registerErrorMsg;

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
