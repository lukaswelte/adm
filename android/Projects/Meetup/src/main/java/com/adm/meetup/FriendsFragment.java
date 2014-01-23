package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    private ListView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        NetworkHelper.getUserProfile(getActivity().getApplicationContext(), new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {

                mList = (ListView) getView().findViewById(R.id.friends_listView);

                ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map = new HashMap<String, String>();
                JsonElement usernameElement = jsonObject.get("username");
                String username = "";
                if (usernameElement != null) {
                    username = usernameElement.getAsString();
                }
                map.put("title", username);
                map.put("description", jsonObject.get("status").getAsString());
                listItem.add(map);

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItem, R.layout.item_account,
                        new String[]{"title", "description"}, new int[]{R.id.item_title, R.id.item_description});
                mList.setAdapter(adapter);
            }
        });

    }


}