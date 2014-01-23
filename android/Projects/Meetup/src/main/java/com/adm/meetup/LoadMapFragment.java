package com.adm.meetup;

/**
 * Created by timomuller on 23/01/14.
 */


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class LoadMapFragment extends Fragment {

    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loadmap, container, false);

    }

    @Override
    public void onStart() {

        super.onStart();

        int timeout = 500; // make the activity visible for 500 miliseconds

        progress = (ProgressBar) getView().findViewById(R.id.progress);

        new AsyncTask<Void, Integer, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                int progressStatus = 0;
                while (progressStatus < 500) {
                    progressStatus++;
                    publishProgress(progressStatus);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setProgress(values[0]);

            }
        }.execute();


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

    


}

