package com.adm.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adm.meetup.calendar.Exam;
import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

/**
 * Created by Maela on 23/01/14.
 */
public class ViewExamActivity extends ActionBarActivity {

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exam = bundle.getParcelable("exam");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(exam))
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Exam exam;
        TextView examName;
        TextView examDate;
        TextView examNotifyDate;
        Button bDelete;

        // constructor

        public PlaceholderFragment(Exam exam1) {
            exam = exam1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_exam_view, container, false);
            examDate = (TextView) rootView.findViewById(R.id.examDate);
            examNotifyDate = (TextView) rootView.findViewById(R.id.examNotifyDate);
            examName = (TextView) rootView.findViewById(R.id.examName);

            examDate.setText(getString(R.string.exam_date) + " : " + Exam.stringFromDate(exam.getDate()));
            examNotifyDate.setText(getString(R.string.exam_notify_date) + " : " + Exam.stringFromDate(exam.getNotifyDate()));
            examName.setText(getString(R.string.exam_name) + exam.getName());

            bDelete = (Button) rootView.findViewById(R.id.deleteButton);
            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FutureCallback<JsonObject> callback = new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject jsonObject) {
                            try {
                                if (e != null) throw e;
                                if (jsonObject.get("status").getAsInt() == 404) {
                                    Toast.makeText(getActivity(), getString(R.string.exam_delete_error), Toast.LENGTH_SHORT).show();
                                } else {
                                    Exam deletedExam = new Exam(jsonObject);
                                    ((ViewExamActivity) getActivity()).finishSuccessful(deletedExam);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                    NetworkHelper.deleteExamRequest(getActivity(), exam, callback);
                }
            });
            return rootView;
        }




    }

    protected void finishSuccessful(Exam deletedExam) {
        Intent intent = new Intent();
        intent.putExtra("exam", deletedExam);
        setResult(200, intent);
        finish();
    }

    protected void finishedCanceled() {
        Intent intent = new Intent();
        setResult(403, intent);
        finish();
    }


}
