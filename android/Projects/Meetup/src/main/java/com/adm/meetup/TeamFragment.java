package com.adm.meetup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TeamFragment extends Fragment {

    private ListView mList;
    private String[] mMember;
    private int[] mCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();

        mMember = getResources().getStringArray(R.array.members);
        mList = (ListView) getView().findViewById(R.id.team_list);
        mCountry = new int[]{
                R.drawable.country_fr,
                R.drawable.country_de,
                R.drawable.country_mr,
                R.drawable.country_fr,
                R.drawable.country_cz,
                R.drawable.country_de,
                R.drawable.country_fr
        };


        // Create and set MenuListAdapter
        TeamListAdapter mMenuAdapter = new TeamListAdapter(getActivity(), mMember, mCountry);
        mList.setAdapter(mMenuAdapter);

    }


}
