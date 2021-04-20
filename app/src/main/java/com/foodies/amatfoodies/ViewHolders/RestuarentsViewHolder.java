package com.foodies.amatfoodies.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.foodies.amatfoodies.R;

public class RestuarentsViewHolder extends RecyclerView.ViewHolder{

    public TextView title_restaurants,distanse_restaurants,salogon_restaurants,item_price_tv,item_time_tv,baked_time_tv,
            item_delivery_time_tv;
    public SimpleDraweeView restaurant_img;
    public RelativeLayout restaurant_row_main;
    public RatingBar ratingBar;
    public ImageView favorite_icon,featured;

    public RestuarentsViewHolder(View itemView) {

        super(itemView);
        title_restaurants = (TextView)itemView.findViewById(R.id.title_restaurants);
        salogon_restaurants = (TextView)itemView.findViewById(R.id.salogon_restaurants);
        distanse_restaurants = (TextView) itemView.findViewById(R.id.distanse_restaurants) ;
        item_price_tv = itemView.findViewById(R.id.item_price_tv);

        restaurant_img =  itemView.findViewById(R.id.profile_image_restaurant) ;
        restaurant_row_main = itemView.findViewById(R.id.restaurant_row_main);
        ratingBar = itemView.findViewById(R.id.ruleRatingBar);
        favorite_icon = itemView.findViewById(R.id.favorite_icon);
        featured = itemView.findViewById(R.id.featured);
        item_time_tv = itemView.findViewById(R.id.item_time_tv);
        baked_time_tv = itemView.findViewById(R.id.baked_time_tv);
        item_delivery_time_tv = itemView.findViewById(R.id.item_delivery_time_tv);

    }
}

