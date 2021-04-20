package com.foodies.amatfoodies.Adapters;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.foodies.amatfoodies.Models.ImageSliderModel;
import com.foodies.amatfoodies.R;

import java.util.ArrayList;

/**
 * Created by qboxus on 10/18/2019.
 */

public class SlidingImageAdapterTrackingStatus extends PagerAdapter {

    private ArrayList<ImageSliderModel> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    ImageLoader imageLoader1;


    public SlidingImageAdapterTrackingStatus(Context context,ArrayList<ImageSliderModel> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_riding_status, view, false);

        final TextView textView = (TextView) imageLayout
                .findViewById(R.id.status_slider);
        ImageSliderModel imageSliderModel = IMAGES.get(position);

        textView.setText(imageSliderModel.getSliderImageUrl());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

