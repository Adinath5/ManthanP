package com.atharvainfo.manthan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.atharvainfo.manthan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> _imagePaths;
    private LayoutInflater inflater;


    public ViewPagerAdapter(List<String> imagePaths, Context context) {
        this._imagePaths = imagePaths;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imgDisplay;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_layout, null);


        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        //Picasso.with(context).load(arrayList.get(position))
        //        .placeholder(R.drawable.image_uploading)
        //        .error(R.drawable.image_not_found).into(imageView);

        //Glide.with(context).load(_imagePaths.get(position)).
        //        placeholder(R.drawable.images).error(R.drawable.images).into(imageView);
        String img = _imagePaths.get(position);
        Picasso.with(context).load(img).placeholder(R.drawable.logo).into(imageView);

        (container).addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);

    }
}
