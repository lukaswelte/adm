package com.adm.meetup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adm.meetup.calendar.Exam;
import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

public class CreateExamActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
            View rootView = inflater.inflate(R.layout.fragment_create_exam, container, false);

            final EditText nameTextView = (EditText) rootView.findViewById(R.id.examName);
            final EditText dateTextView = (EditText) rootView.findViewById(R.id.examDate);
            final EditText notifyDateTextView = (EditText) rootView.findViewById(R.id.examNotifyDate);

            Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onClick(View view) {
                    final Context context = getActivity();

                    String examName = nameTextView.getText().toString();
                    if (examName == null || examName.length() < 1) {
                        Toast.makeText(context, "You have to name the exam", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String dateString = dateTextView.getText().toString();
                    if (dateString == null || dateString.length() < 1) {
                        Toast.makeText(context, "You have to specify a date for the exam", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String notifyDateString = notifyDateTextView.getText().toString();

                    Exam exam = new Exam(null, dateString, notifyDateString, examName);
                    if (exam.getDate() == null) {
                        Toast.makeText(context, "Exam date in wrong format. Should be dd.MM.yyyy HH:mm", Toast.LENGTH_LONG).show();
                        return;
                    }
                    NetworkHelper.createExamRequest(context, exam, new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject jsonObject) {
                            try {
                                if (e != null) throw e;

                                Exam receivedExam = new Exam(jsonObject.getAsJsonObject());
                                ((CreateExamActivity) getActivity()).finishSuccessful(receivedExam);

                            } catch (Exception e1) {
                                e1.printStackTrace();
                                Toast.makeText(context, "Something went wrong. Try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CreateExamActivity) getActivity()).finishedCanceled();
                }
            });

            return rootView;
        }
    }

    protected void finishSuccessful(Exam createdExam) {
        Intent intent = new Intent();
        intent.putExtra("exam", createdExam);
        setResult(200, intent);
        finish();
    }

    protected void finishedCanceled() {
        Intent intent = new Intent();
        setResult(403, intent);
        finish();
    }

}
