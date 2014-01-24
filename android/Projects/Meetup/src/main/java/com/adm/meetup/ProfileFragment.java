package com.adm.meetup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.adm.meetup.User.User;
import com.adm.meetup.helpers.NetworkHelper;
import com.adm.meetup.helpers.SharedApplication;
import com.adm.meetup.util.Util;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private ListView profileList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        profileList = (ListView) getView().findViewById(R.id.profile_list);

        majParametersListVAdapter();

        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, final long id) {

                Object itemAtPosition = profileList.getItemAtPosition(position);
                if (itemAtPosition instanceof HashMap<?, ?>) {
                    HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;

                    if (id == 0) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                        adb.setTitle(map.get("title"));
                        final EditText input = new EditText(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        adb.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText() != null) {
                                    String status = input.getText().toString();
                                    if (status != null) {
                                        if (!status.equals("")) {
                                            User user = SharedApplication.getInstance().getUser();
                                            user.setStatus(status);
                                            NetworkHelper.updateProfile(ProfileFragment.this.getActivity().getApplicationContext(),
                                                    user, new FutureCallback<JsonElement>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonElement jsonElement) {

                                                }
                                            });

                                            majParametersListVAdapter();
                                        }
                                    }
                                }
                            }
                        });
                        adb.setNegativeButton(R.string.cancel, null);
                        adb.setView(input);
                        adb.show();
                    }
                }
            }
        });
        Button profileButton = (Button) getView().findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        Button profileButtonLogout = (Button) getView().findViewById(R.id.profile_button_logout);
        profileButtonLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginActivity.onClickLogout();
                Intent login = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(login);
                SharedPreferences pref = getActivity().getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Util.PREFERENCES_EMAIL, Util.PREFERENCES_EMAIL_DEFAULT);
                editor.putString(Util.PREFERENCES_FIRSTNAME, Util.PREFERENCES_FIRSTNAME_DEFAULT);
                editor.putString(Util.PREFERENCES_LASTNAME, Util.PREFERENCES_LASTNAME_DEFAULT);
                editor.putString(Util.PREFERENCES_DATEOFBIRTH, Util.PREFERENCES_DATEOFBIRTH_DEFAULT);
                editor.putString(Util.PREFERENCES_STATUS, Util.PREFERENCES_STATUS_DEFAULT);
                editor.putString(Util.PREFERENCES_ERASMUSUNIVERSITY, Util.PREFERENCES_ERASMUSUNIVERSITY_DEFAULT);
                editor.putString(Util.PREFERENCES_HOMEUNIVERSITY, Util.PREFERENCES_HOMEUNIVERSITY_DEFAULT);
                editor.putString(Util.PREFERENCES_RELATIONSHIPSTATUS, Util.PREFERENCES_RELATIONSHIPSTATUS_DEFAULT);
                editor.putString(Util.PREFERENCES_LOCATIONSERVICES, Util.PREFERENCES_LOCATIONSERVICES_DEFAULT);
                editor.putString(Util.PREFERENCES_NATIONALITY, Util.PREFERENCES_NATIONALITY_DEFAULT);
                editor.commit();
                // Closing dashboard screen
                getActivity().finish();
            }
        });


    }

    private void majParametersListVAdapter() {

        User user = SharedApplication.getInstance().getUser();

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", getString(R.string.status));
        map.put("description", user.getStatus());
        listItem.add(map);

        SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.item_account,
                new String[]{"title", "description"}, new int[]{R.id.item_title, R.id.item_description});

        profileList.setAdapter(mSchedule);
    }
}
