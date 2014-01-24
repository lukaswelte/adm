package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.adm.meetup.User.User;
import com.adm.meetup.helpers.NetworkHelper;
import com.adm.meetup.helpers.SharedApplication;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    private ListView mList;
    private boolean lastFriendLoaded;
    private ArrayList<HashMap<String, String>> listItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        NetworkHelper.getUserProfile(getActivity().getApplicationContext(), new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                User user = new User(jsonObject);
                SharedApplication.getInstance().setUser(user);
                if (e == null) {

                    mList = (ListView) getView().findViewById(R.id.friends_listView);

                    listItem = new ArrayList<HashMap<String, String>>();

                    int i = 0;
                    ArrayList<String> friendsId = user.getFriends();
                    lastFriendLoaded = false;
                    for (String friendId : friendsId) {
                        if (i == friendsId.size() - 1) {
                            lastFriendLoaded = true;
                        }
                        NetworkHelper.getProfile(getActivity().getApplicationContext(), friendId, new FutureCallback<JsonElement>() {
                            @Override
                            public void onCompleted(Exception e, JsonElement jsonObject) {
                                JsonArray arrays = (JsonArray) jsonObject;
                                User friend = new User((JsonObject) arrays.iterator().next());
                                HashMap<String, String> map = new HashMap<String, String>();
                                String name = friend.getFirstName();
                                map.put("title", (name == null) ? getString(R.string.friends_no_name_friend) : name);
                                map.put("description", "\"" + friend.getStatus() + "\"");
                                listItem.add(map);

                                if (lastFriendLoaded) {
                                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItem, R.layout.item_account,
                                            new String[]{"title", "description"}, new int[]{R.id.item_title, R.id.item_description});
                                    mList.setAdapter(adapter);
                                }
                            }
                        });
                    }
                }
            }
        });

    }


}