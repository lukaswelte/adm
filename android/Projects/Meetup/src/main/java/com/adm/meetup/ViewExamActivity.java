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

        // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
        private int mYear, mMonth, mDay, mHour, mMinute;
        private int year;
        private String hour, minute, month, day;

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
                            //TODO check if it has been deleted
                        }
                    };
                    NetworkHelper.deleteExamRequest(getActivity(), exam, callback);
                    //TODO Go back
                }
            });

           /* // Date Picker Exam

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Time Picker Exam

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);


            final EditText nameTextView = (EditText) rootView.findViewById(R.id.examName);
            final Button dateTextView = (Button) rootView.findViewById(R.id.examDate);
            final Button timeTextView = (Button) rootView.findViewById(R.id.examNotifyDate);
            final Button notifyDateTextView = (Button) rootView.findViewById(R.id.examNotifyDate);
            final Button notifyTimeTextView = (Button) rootView.findViewById(R.id.examNotifyTime);

            Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onClick(View view) {
                    final Context context = getActivity();

                    String examName = nameTextView.getText().toString();
                    if (examName == null || examName.length() < 1) {
                        Toast.makeText(context, context.getString(R.string.advice_name_exam_missing), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String dateString = dateTextView.getText().toString();
                    if (dateString == null || dateString.length() < 1) {
                        Toast.makeText(context, context.getString(R.string.advice_date_exam_missing), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String timeString = timeTextView.getText().toString();
                    if (dateString == null || dateString.length() < 1) {
                        Toast.makeText(context, context.getString(R.string.advice_time_exam_missing), Toast.LENGTH_LONG).show();
                    }




                    String notifyDateString = notifyDateTextView.getText().toString() + " " + notifyTimeTextView.getText().toString();
                    String examDateString = dateString + " " + timeString;

                    Exam exam = new Exam(null, examDateString, notifyDateString, examName);
                    if (exam.getDate() == null) {
                        Toast.makeText(context, context.getString(R.string.advice_wrong_format), Toast.LENGTH_LONG).show();
                        return;
                    }
                    NetworkHelper.createExamRequest(context, exam, new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject jsonObject) {
                            try {
                                if (e != null) throw e;

                                Exam receivedExam = new Exam(jsonObject.getAsJsonObject());
                                ((ViewExamActivity) getActivity()).finishSuccessful(receivedExam);

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
                    ((ViewExamActivity) getActivity()).finishedCanceled();
                }
            });
*/
            return rootView;
        }


        private static String checkValue(int input) {
            if (input >= 10)
                return Integer.toString(input);
            else
                return "0" + Integer.toString(input);
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
