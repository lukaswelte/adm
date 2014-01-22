package com.adm.meetup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adm.meetup.calendar.Exam;

import java.util.ArrayList;

/**
 * Created by lukas on 22.01.14.
 */
public class ExamListAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Exam> exams;

    public ExamListAdapter(Context context, ArrayList<Exam> exams) {
        this.context = context;
        this.exams = exams;
    }

    @Override
    public int getCount() {
        if (exams == null) return 0;
        return exams.size();
    }

    @Override
    public Object getItem(int i) {
        return exams.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.item_exam_list, viewGroup, false);

        TextView examName = (TextView) itemView.findViewById(R.id.examName);

        examName.setText(exams.get(i).getName());

        return itemView;
    }
}
