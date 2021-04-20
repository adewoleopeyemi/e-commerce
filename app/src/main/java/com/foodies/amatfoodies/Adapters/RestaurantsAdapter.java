package com.foodies.amatfoodies.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.toolbox.ImageLoader;
import com.foodies.amatfoodies.ActivitiesAndFragments.ShowFavoriteRestFragment;
import com.foodies.amatfoodies.Constants.ApiRequest;
import com.foodies.amatfoodies.Constants.Callback;
import com.foodies.amatfoodies.Constants.PreferenceClass;
import com.foodies.amatfoodies.Models.RestaurantsModel;
import com.foodies.amatfoodies.ViewHolders.RestuarentsViewHolder;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.foodies.amatfoodies.Constants.Config;
import com.foodies.amatfoodies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qboxus on 10/18/2019.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestuarentsViewHolder> implements Filterable {

    ArrayList<RestaurantsModel> getDataAdapter;
    private ArrayList<RestaurantsModel> mFilteredList;
    Context context;
    ImageLoader imageLoader1;
    OnItemClickListner onItemClickListner;
    SharedPreferences sharedPreferences;
    CamomileSpinner progressBar;
    ShowFavoriteRestFragment fragment;

    public RestaurantsAdapter(ArrayList<RestaurantsModel> getDataAdapter, Context context,ShowFavoriteRestFragment fragment,CamomileSpinner progressBar){
        super();
        this.getDataAdapter = getDataAdapter;
        mFilteredList = getDataAdapter;
        this.context = context;
        this.progressBar = progressBar;
        this.fragment = fragment;
    }

    @Override
    public RestuarentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items_restaurants, parent, false);

        RestuarentsViewHolder viewHolder = new RestuarentsViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RestuarentsViewHolder holder, final int position) {

        final RestaurantsModel getDataAdapter1 =  mFilteredList.get(position);

        sharedPreferences = context.getSharedPreferences(PreferenceClass.user,Context.MODE_PRIVATE);
        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        holder.favorite_icon.setTag(getDataAdapter1);
        RestaurantsModel checkWetherToShow=(RestaurantsModel)holder.favorite_icon.getTag();


        Uri uri = Uri.parse(Config.imgBaseURL+getDataAdapter1.restaurant_image);
        holder.restaurant_img.setImageURI(uri);

        holder.title_restaurants.setText(getDataAdapter1.restaurant_name.trim());



        String symbol = getDataAdapter1.restaurant_currency;
        holder.salogon_restaurants.setText(getDataAdapter1.restaurant_salgon.trim());
        holder.baked_time_tv.setText(getDataAdapter1.preparation_time+ " min");
        holder.item_delivery_time_tv.setText(getDataAdapter1.deliveryTime+" min");

        holder.ratingBar.setRating(Float.parseFloat(getDataAdapter1.restaurant_avgRating));

        if(getDataAdapter1.min_order_price.equalsIgnoreCase("0.00")){
            holder.item_time_tv.setText(symbol + " " + getDataAdapter1.delivery_fee_per_km + " /km");
        }
        else {
            holder.item_time_tv.setText(symbol + " " + getDataAdapter1.delivery_fee_per_km+ " /km- Free over" + " " + symbol + " " + getDataAdapter1.min_order_price);

        }

        if (checkWetherToShow.restaurant_isFav.equalsIgnoreCase("1")){
            holder.favorite_icon.setImageResource(R.drawable.ic_heart_filled);
        }
        else {
            holder.favorite_icon.setImageResource(R.drawable.ic_heart_not_filled);
        }

        String getPromotedString = getDataAdapter1.promoted;

        if (getPromotedString.equalsIgnoreCase("1"))
        {
            holder.featured.setVisibility(View.VISIBLE);
        }
        else {
            holder.featured.setVisibility(View.GONE);
        }

        holder.distanse_restaurants.setText(getDataAdapter1.restaurant_distance);

        holder.restaurant_row_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onItemClickListner !=null){
                    int position = holder.getAdapterPosition();
                    String name = mFilteredList.get(position).restaurant_id;
                    for (int i=0 ; i <getDataAdapter.size() ; i++ ){
                        if(name.equals(getDataAdapter.get(i).restaurant_id)){
                            position = i;
                            break;
                        }
                    }
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListner.OnItemClicked(view,position);
                    }
                }
            }
        });


        holder.favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean getLoINSession = sharedPreferences.getBoolean(PreferenceClass.IS_LOGIN,false);
                if(!getLoINSession){}
                else {
                    addFavoriteRestaurant(getDataAdapter1.restaurant_id);

                }

            }
        });







    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size() ;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = getDataAdapter;
                } else {
                    ArrayList<RestaurantsModel> filteredList = new ArrayList<>();
                    for (RestaurantsModel row : getDataAdapter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.restaurant_name.toLowerCase().contains(charString.toLowerCase()) || row.restaurant_name.contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<RestaurantsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };


    }



    public interface OnItemClickListner {
        void OnItemClicked(View view, int position);
    }

    public void setOnItemClickListner(OnItemClickListner onCardClickListner) {
        this.onItemClickListner = onCardClickListner;
    }


    public void addFavoriteRestaurant(String res_id){
        progressBar.setVisibility(View.VISIBLE);
        final String user_id = sharedPreferences.getString(PreferenceClass.pre_user_id,"");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",user_id);
            jsonObject.put("restaurant_id",res_id);
            jsonObject.put("favourite","1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Config.ADD_FAV_RESTAURANT, jsonObject, new Callback() {
            @Override
            public void Responce(String resp) {


                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject  converResponseToJson = new JSONObject(resp);

                    int code_id  = Integer.parseInt(converResponseToJson.optString("code"));
                    if(code_id == 200) {
                        fragment.getRestaurantList(user_id);
                        ShowFavoriteRestFragment.FROM_FAVORITE = true;
                        notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


}
