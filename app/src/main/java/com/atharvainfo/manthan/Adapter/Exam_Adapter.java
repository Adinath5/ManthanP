package com.atharvainfo.manthan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.atharvainfo.manthan.Activity.view_exam;
import com.atharvainfo.manthan.Model.Exam_Model_List;
import com.atharvainfo.manthan.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Exam_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Exam_Model_List> items = new ArrayList<>();
    private ArrayList<Exam_Model_List> arraylist;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public interface OnItemClickListener {
        void onItemClick(View view, Exam_Model_List obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Exam_Adapter(Context context, List<Exam_Model_List> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<Exam_Model_List>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView test_name,test_type,txt_subject,txt_dept_class,text_exam_fee,text_deadline;
        Button btn_take_exam;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            test_name = (TextView) v.findViewById(R.id.test_name);
            test_type = (TextView) v.findViewById(R.id.test_type);
            txt_subject = (TextView) v.findViewById(R.id.txt_subject);
            txt_dept_class = (TextView) v.findViewById(R.id.txt_dept_class);
            text_exam_fee = (TextView) v.findViewById(R.id.text_exam_fee);
            text_deadline= (TextView) v.findViewById(R.id.text_deadline);
            btn_take_exam= (Button) v.findViewById(R.id.btn_take_exam);
            lyt_parent= (View) v.findViewById(R.id.lyt_parent);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exams, parent, false);
        vh = new OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Exam_Model_List p = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            view.test_name.setText(p.getClass_name());
            view.test_type.setText(p.getExam_type());
            view.txt_subject.setText(p.getExam_testname());
            view.txt_dept_class.setText(p.getExam_papername());
            view.text_exam_fee.setText(p.getExam_fee());
            view.text_deadline.setText(p.getExam_medium());


            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
            view.btn_take_exam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences =ctx.getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("exam_id", p.getExam_id());
                    editor.putString("exam_duration", p.getDuration());
                    editor.putString("exam_passmark", p.getPassmark());
                    editor.putString("exam_fee", p.getExam_fee());
                    editor.putString("exam_medium", p.getExam_medium());
                    editor.putString("exam_class", p.getClass_name());
                    editor.putString("exam_status", p.getExam_status());
                    editor.putString("quetions", p.getQuetions());
                    editor.putString("exam_testname", p.getExam_testname());
                    editor.putString("exam_papername", p.getExam_papername());
                    editor.putString("terms", p.getTerms());
                    editor.putString("exam_type", p.getExam_type());
                    editor.commit();

                    Intent i = new Intent(ctx, view_exam.class);
                    ctx.startActivity(i);
                    ((Activity)ctx).finish();
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arraylist);
        } else {
            for (Exam_Model_List wp : arraylist) {
                if (wp.getClass_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getExam_type().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getExam_testname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getExam_papername().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}



