package com.atharvainfo.manthan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.atharvainfo.manthan.Model.Assessment_Model_List;
import com.atharvainfo.manthan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Assessment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Assessment_Model_List> items = new ArrayList<>();
    private ArrayList<Assessment_Model_List> arraylist;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Assessment_Model_List obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Assessment_Adapter(Context context, List<Assessment_Model_List> items) {
        this.items = items;
        ctx = context;
        this.arraylist = new ArrayList<Assessment_Model_List>();
        this.arraylist.addAll(items);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView text_exam,text_exdate,text_noofqt,text_rans,text_wans,text_solvqt,text_status;
        //public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);

            text_exam = (TextView) v.findViewById(R.id.test_name);
            text_exdate = (TextView) v.findViewById(R.id.test_date);
            text_noofqt = (TextView) v.findViewById(R.id.txt_noofqt);
            text_rans = (TextView) v.findViewById(R.id.txt_wrightans);
            text_wans = (TextView) v.findViewById(R.id.text_wrongans);
            text_solvqt = (TextView)v.findViewById(R.id.text_solvedqt);
            text_status = (TextView) v.findViewById(R.id.text_status);
        }
    }

  /*  public static class SectionViewHolder extends RecyclerView.ViewHolder {
       // public TextView title_section;

        public SectionViewHolder(View v) {
            super(v);
           // title_section = (TextView) v.findViewById(R.id.title_section);
        }
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment_record, parent, false);
        vh = new OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Assessment_Model_List p = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            view.text_exam.setText(p.getExam_id());
            view.text_exdate.setText(p.getExamdate());
            view.text_noofqt.setText("No.Of Quetions : "+p.getNoofqt());
            view.text_rans.setText("Right Ans : "+ p.getRightans());
            view.text_wans.setText("Wrong Ans : "+p.getWrongans());
            view.text_solvqt.setText("Solved Quetion : "+p.getSolveqt());
            view.text_status.setText(p.getExcompleted());

            String status = p.getExcompleted();
            if(status.equalsIgnoreCase("Completed")) {
                view.text_status.setTextColor(ctx.getResources().getColor(R.color.green_900));
                view.text_status.setText(p.getExcompleted());
            }else{
               view.text_status.setTextColor(ctx.getResources().getColor(R.color.amber_800));
                view.text_status.setText(p.getExcompleted());
            }
            //view.description.setTextColor( ctx.getResources().getColor( R.color.grey_80 ) );

          /*  view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });*/
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
            for (Assessment_Model_List wp : arraylist) {
                if (wp.getExam_id().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }
                if (wp.getExamdate().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    items.add(wp);
                }


            }
        }
        notifyDataSetChanged();
    }
}


