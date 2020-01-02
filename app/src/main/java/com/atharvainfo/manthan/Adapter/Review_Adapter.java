package com.atharvainfo.manthan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Model.Review_Model;
import com.atharvainfo.manthan.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Review_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Review_Model> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Review_Model obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Review_Adapter(Context context, List<Review_Model> items) {
        this.items = items;
        ctx = context;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_quetion,txt_selectanswer,txt_rightanswer, txt_quetion1,txt_quetion2, txt_quetion3,txt_quetion4;
        RadioGroup radiogrp;
        RadioButton radio1,radio2,radio3,radio4;
        public View lyt_parent;
        EditText edit_fill_answer;
        ImageView image_quetion,image_op1,image_op2,image_op3,image_op4, image_quetion1, image_quetion2, image_quetion3,image_quetion4;
        String student_id,exam_id,exam_title,exam_duration, noofqt;
        String exid;


        public OriginalViewHolder(View v) {
            super(v);

            txt_quetion = (TextView) v.findViewById(R.id.txt_quetion);
            txt_quetion1 = (TextView) v.findViewById(R.id.txt_quetion1);
            txt_quetion2 = (TextView) v.findViewById(R.id.txt_quetion2);
            txt_quetion3 = (TextView) v.findViewById(R.id.txt_quetion3);
            txt_quetion4 = (TextView) v.findViewById(R.id.txt_quetion4);
            //txt_quetion1 = (TextView) findViewById(R.id.txt_quetion4);
            radiogrp = (RadioGroup) v.findViewById(R.id.radiogrp);
            radio1 = (RadioButton) v.findViewById(R.id.radio1);
            radio2 = (RadioButton) v.findViewById(R.id.radio2);
            radio3 = (RadioButton) v.findViewById(R.id.radio3);
            radio4 = (RadioButton) v.findViewById(R.id.radio4);
            edit_fill_answer= (EditText) v.findViewById(R.id.edit_fill_answer);
            txt_selectanswer = (TextView) v.findViewById(R.id.txt_selectanswer);
            txt_rightanswer = (TextView) v.findViewById(R.id.txt_rightanswer);

            lyt_parent = (View) v.findViewById( R.id.lyt_parent);

            image_quetion = (ImageView) v.findViewById(R.id.image_quetion);
            image_op1 = (ImageView) v.findViewById(R.id.image_op1);
            image_op2 = (ImageView) v.findViewById(R.id.image_op2);
            image_op3 = (ImageView) v.findViewById(R.id.image_op3);
            image_op4 = (ImageView) v.findViewById(R.id.image_op4);
            image_quetion1 = (ImageView) v.findViewById(R.id.image_quetion1);
            image_quetion2 = (ImageView) v.findViewById(R.id.image_quetion2);
            image_quetion3 = (ImageView) v.findViewById(R.id.image_quetion3);

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        vh = new OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Review_Model p = items.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            view.radio1.setClickable(false);
            view.radio2.setClickable(false);
            view.radio3.setClickable(false);
            view.radio4.setClickable(false);

            int srno = position+1;
            view.txt_quetion.setText(srno+".  "+p.getQuestion());

            if (p.getQpart1() != null && !p.getQpart1().isEmpty() && !p.getQpart1().equals("null")){
                view.txt_quetion1.setVisibility(View.VISIBLE);
                String htmlText1 = p.getQpart1();
                view.txt_quetion1.setText(HtmlCompat.fromHtml(htmlText1, 0));

            } else {
                view.txt_quetion1.setVisibility(View.GONE);
            }

            if (p.getQpart2() != null && !p.getQpart2().isEmpty() && !p.getQpart2().equals("null")){
                view.txt_quetion2.setVisibility(View.VISIBLE);
                String htmlText2 = p.getQpart2();
                view.txt_quetion2.setText(HtmlCompat.fromHtml(htmlText2, 0));

            } else {
                view.txt_quetion2.setVisibility(View.GONE);
            }

            if (p.getQpart3() != null && !p.getQpart3().isEmpty() && !p.getQpart3().equals("null")){
                view.txt_quetion3.setVisibility(View.VISIBLE);
                String htmlText3 = p.getQpart3();
                view.txt_quetion3.setText(HtmlCompat.fromHtml(htmlText3, 0));

            } else {
                view.txt_quetion3.setVisibility(View.GONE);
            }

            if (p.getQpart4() != null && !p.getQpart4().isEmpty() && !p.getQpart4().equals("null")){
                view.txt_quetion4.setVisibility(View.VISIBLE);
                String htmlText4 = p.getQpart4();
                view.txt_quetion4.setText(HtmlCompat.fromHtml(htmlText4, 0));

            } else {
                view.txt_quetion4.setVisibility(View.GONE);
            }



            if(p.getOp1().equals("-")  && p.getOp2().equals("-")){
                view.edit_fill_answer.setVisibility(View.VISIBLE);
                view.radiogrp.setVisibility(View.GONE);
            }else{
                view.edit_fill_answer.setVisibility(View.GONE);
                view.radiogrp.setVisibility(View.VISIBLE);
                view.radio1.setText(p.getOp1());
                view.radio2.setText(p.getOp2());
                view.radio3.setText(p.getOp3());
                view.radio4.setText(p.getOp4());

                if (p.getQuestion_image() != null && !p.getQuestion_image().isEmpty() && !p.getQuestion_image().equals("null")){

                    view.image_quetion1.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(p.getQuestion_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    view.image_quetion1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    view.image_quetion1.setVisibility(View.GONE);
                }

                if (p.getQuestion_image1() != null && !p.getQuestion_image1().isEmpty() && !p.getQuestion_image1().equals("null")){

                    view.image_quetion2.setVisibility(View.VISIBLE);

                    byte[] decodedString = Base64.decode(p.getQuestion_image1(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    view.image_quetion2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                    //Glide.with(mCtx).load(imgBitMap).into(viewHolder.imageView);
                    //Log.e("quetion img", String.valueOf(img_path));

                }else{
                    view.image_quetion2.setVisibility(View.GONE);
                }

                if (p.getQuestion_image2() != null && !p.getQuestion_image2().isEmpty() && !p.getQuestion_image2().equals("null")){

                    view.image_quetion3.setVisibility(View.VISIBLE);

                    byte[] decodedString = Base64.decode(p.getQuestion_image2(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    view.image_quetion3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    view.image_quetion3.setVisibility(View.GONE);
                }



                if (p.getOp1_image() != null && !p.getOp1_image().isEmpty() && !p.getOp1_image().equals("null")){

                    view.image_op1.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(p.getOp1_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    view.image_op1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView


                }else{
                    view.image_op1.setVisibility(View.GONE);
                }

                if (p.getOp2_image() != null && !p.getOp2_image().isEmpty() && !p.getOp2_image().equals("null")){

                    view.image_op2.setVisibility(View.VISIBLE);
                    byte[] decodedString = Base64.decode(p.getOp2_image(), Base64.DEFAULT);
                    Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    view.image_op2.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    view.image_op2.setVisibility(View.GONE);
                }


                if (p.getOp3_image() != null && !p.getOp3_image().isEmpty() && !p.getOp3_image().equals("null")){

                    view.image_op3.setVisibility(View.VISIBLE);
                    //byte[] decodedString = Base64.decode(p.getOp3_image(), Base64.DEFAULT);
                    //Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //view.image_op3.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                }else{
                    view.image_op3.setVisibility(View.GONE);
                }


                if (p.getOp4_image() != null && !p.getOp4_image().isEmpty() && !p.getOp4_image().equals("null")){

                    view.image_op4.setVisibility(View.VISIBLE);
                    //byte[] decodedString = Base64.decode(p.getOp4_image(), Base64.DEFAULT);
                    //Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    //view.image_op4.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView

                   /* String img_path = MyConfig.Parent_Url+"assets/uploads/questions/"+p.getOp4_image();
                    InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
                    try {
                        in = (InputStream) new URL(img_path).getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
                    view.image_op4.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
                    try {
                        if(in != null)
                            in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("quetion img", String.valueOf(img_path));*/

                }else{
                    view.image_op4.setVisibility(View.GONE);
                }

            }
            view.radiogrp.setEnabled(false);
            String selct_ans = p.getSelected_answer();
            String right_ans = p.getAnswer();
           // Log.e("review ",selct_ans+"///"+right_ans);

            if(right_ans.equalsIgnoreCase(selct_ans)){

                view.txt_rightanswer.setText("Correct");
                view.txt_rightanswer.setTextColor(ctx.getResources().getColor(R.color.green_800));
            }
            else if(right_ans.equalsIgnoreCase("A"))
            {
                view.txt_rightanswer.setText("Right answer is : "+ "option1");

            }else if(right_ans.equalsIgnoreCase("B"))
            {

                view.txt_rightanswer.setText("Right answer is : "+ "option2");

            }else if(right_ans.equalsIgnoreCase("C")){

                view.txt_rightanswer.setText("select answer is : "+ "option3");

            }else if(right_ans.equalsIgnoreCase("D"))
            {

                view.txt_rightanswer.setText("Right answer is : "+ "option4");

            }else{
                view.txt_rightanswer.setText("Right answer is : "+ right_ans);
            }


            if(selct_ans.equalsIgnoreCase("A"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option1");
                view.radio1.setChecked(true);
            }
            else if(selct_ans.equalsIgnoreCase("B"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option2");
                view.radio2.setChecked(true);
            }
            else if(selct_ans.equalsIgnoreCase("C"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option3");
                view.radio3.setChecked(true);
            }
            else if(selct_ans.equalsIgnoreCase("D"))
            {
                view.txt_selectanswer.setText("Select answer is : "+ "option4");
                view.radio4.setChecked(true);
            }else{
                view.edit_fill_answer.setText(selct_ans);
                view.txt_selectanswer.setText("Select answer is : "+ selct_ans);
            }

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}



