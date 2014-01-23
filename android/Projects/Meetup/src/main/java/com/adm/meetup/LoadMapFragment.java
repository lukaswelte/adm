package com.adm.meetup;

/**
 * Created by timomuller on 23/01/14.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

public class LoadMapFragment extends Fragment {

    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progress = new ProgressDialog(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loadmap, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        int timeout = 1000; // make the activity visible for 500 miliseconds

        startProgress();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MapMeetupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);

            }
        }, timeout);

    }

    public void startProgress(){
        progress.setMessage(getString(R.string.map_load));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.show();

        final int totalProgressTime = 100;

        final Thread t = new Thread(){

            @Override
            public void run(){

                int jumpTime = 0;
                while(jumpTime < totalProgressTime){
                    try {
                        sleep(200);
                        jumpTime += 20;
                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        };
        t.start();

    }


}

