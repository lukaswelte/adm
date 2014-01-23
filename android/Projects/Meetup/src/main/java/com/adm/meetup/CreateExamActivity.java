package com.adm.meetup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adm.meetup.calendar.Exam;
import com.adm.meetup.helpers.NetworkHelper;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.util.Calendar;

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

        Button examDate;
        Button examTime;
        Button examNotifyDate;
        Button examNotifyTime;

        // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
        private int mYear, mMonth, mDay,mHour,mMinute;
        private int year;
        private String hour, minute, month, day;

        DatePickerDialog dpdFromDate, dpdFromDateNotify;
        TimePickerDialog dpdFromTime, dpdFromTimeNotify;

        // constructor

        public PlaceholderFragment()
        {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_create_exam, container, false);

            /**
             * Date and Time Picker
             */

            examDate = (Button) rootView.findViewById(R.id.examDate);
            examTime = (Button) rootView.findViewById(R.id.examTime);
            examNotifyDate = (Button) rootView.findViewById(R.id.examNotifyDate);
            examNotifyTime = (Button) rootView.findViewById(R.id.examNotifyTime);

            // Date Picker Exam

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            dpdFromDate = new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay);
            dpdFromDateNotify = new DatePickerDialog(getActivity(), mNotifyDateSetListener, mYear, mMonth, mDay);


            dpdFromDate.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        examDate.setText("");
                    }
                }
            });

            dpdFromDateNotify.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        examNotifyDate.setText("");
                    }
                }
            });

            // Time Picker Exam

            dpdFromTime = new TimePickerDialog(getActivity(), mTimeSetListener, mHour, mMinute, true);
            dpdFromTimeNotify = new TimePickerDialog(getActivity(), mNotifyTimeSetListener, mHour, mMinute, true);

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);


            dpdFromTime.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        examTime.setText("");
                    }
                }
            });

            dpdFromTimeNotify.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        examNotifyTime.setText("");
                    }
                }
            });

            // Set ClickListener on btnSelectDate
            examDate.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Show the DatePickerDialog
                    dpdFromDate.show();
                }
            });

            // Set ClickListener on btnSelectTime
            examTime.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Show the TimePickerDialog
                    dpdFromTime.show();
                }
            });

            // Date Picker ExamNotify

            // Set ClickListener on btnSelectDate
            examNotifyDate.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Show the DatePickerDialog
                    dpdFromDateNotify.show();
                }
            });

            // Time Picker ExamNotify

            examNotifyTime.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Show the TimePickerDialog
                    dpdFromTimeNotify.show();
                }
            });

            /*
             * end
             */


            final EditText nameTextView = (EditText) rootView.findViewById(R.id.examName);
            final Button dateTextView = (Button) rootView.findViewById(R.id.examDate);
            final Button timeTextView = (Button) rootView.findViewById(R.id.examTime);
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
        // Register  DatePickerDialog listener

        private DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // the callback received when the user "sets" the Date in the DatePickerDialog
                    public void onDateSet(DatePicker view, int yearSelected,
                                          int monthOfYear, int dayOfMonth) {
                        year = yearSelected;
                        month = checkValue(monthOfYear +1);
                        day = checkValue(dayOfMonth);
                        // Set the Selected Date in Select date Button
                        examDate.setText(day+"."+month+"."+year);
                    }
                };




        // Register  TimePickerDialog listener
        private TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    // the callback received when the user "sets" the TimePickerDialog in the dialog
                    public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                        hour = checkValue(hourOfDay);
                        minute = checkValue(min);
                        // Set the Selected Date in Select date Button
                        examTime.setText(hour+":"+minute);
                    }
                };

        // Listener for Notify

        private DatePickerDialog.OnDateSetListener mNotifyDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // the callback received when the user "sets" the Date in the DatePickerDialog
                    public void onDateSet(DatePicker view, int yearSelected,
                                          int monthOfYear, int dayOfMonth) {
                        year = yearSelected;
                        month = checkValue(monthOfYear + 1);
                        day = checkValue(dayOfMonth);
                        // Set the Selected Date in Select date Button
                        examNotifyDate.setText(day+"."+month+"."+year);
                    }
                };

        // Register  TimePickerDialog listener
        private TimePickerDialog.OnTimeSetListener mNotifyTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    // the callback received when the user "sets" the TimePickerDialog in the dialog
                    public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                        hour = checkValue(hourOfDay);
                        minute = checkValue(min);
                        // Set the Selected Date in Select date Button
                        examNotifyTime.setText(hour+":"+minute);
                    }
                };



        private static String checkValue(int input) {
            if (input >= 10)
                return Integer.toString(input);
            else
                return "0" + Integer.toString(input);
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
