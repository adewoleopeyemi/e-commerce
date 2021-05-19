package com.foodies.amatfoodies.activitiesAndFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodies.amatfoodies.adapters.AdapterAllRestaurantData;
import com.foodies.amatfoodies.adapters.AdapterPromoted;
import com.foodies.amatfoodies.constants.ApiRequest;
import com.foodies.amatfoodies.constants.Callback;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.constants.DataParser;
import com.foodies.amatfoodies.constants.FragmentCallback;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.googleMapWork.MapsActivity;
import com.foodies.amatfoodies.models.RestaurantsModel;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;
import com.foodies.amatfoodies.utils.TabLayoutUtils;
import com.foodies.amatfoodies.databinding.LayoutRestaurantFragmentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class NewRestuarantsFragment extends RootFragment {
    String currentLoc;
    SharedPreferences sharedPreferences;
    String lat, lon, userId;
    private ArrayList<RestaurantsModel> datalist;
    private ArrayList<RestaurantsModel> promotedDataList;
    LayoutRestaurantFragmentBinding binding;
    ArrayList<String> specialties;
    ArrayList<RestaurantsModel> africanCuisine;
    ArrayList<RestaurantsModel> africanWares;
    ArrayList<RestaurantsModel> beautyAndPersonalItems;
    ArrayList<RestaurantsModel> foodAndGroceries;
    ArrayList<ArrayList<RestaurantsModel>> allData = new ArrayList<>();
    public static String specialtyFIlter;
    String search;
    AdapterAllRestaurantData adapterAllRestaurantData;
    boolean onLocation = false;

    public NewRestuarantsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.layout_restaurant_fragment, container, false);

        sharedPreferences = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        currentLoc = sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, "");
        lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");
        userId = sharedPreferences.getString(PreferenceClass.pre_user_id, "");

        if (lat.isEmpty() || lon.isEmpty()) {
            lat = "31.4904023";
            lon = "74.2906989";
        }
        saveAddressRequest();
        datalist = new ArrayList<>();
        specialties = new ArrayList<>();
        specialties.add("African Cuisine");
        specialties.add("African Wares");
        specialties.add("Beauty and Personal Items");
        specialties.add("Food and Groceries");
        promotedDataList = new ArrayList<>();
        africanCuisine = new ArrayList<>();
        africanWares = new ArrayList<>();
        beautyAndPersonalItems = new ArrayList<>();
        foodAndGroceries = new ArrayList<>();
        binding.collapsingToolbar.setVisibility(GONE);
        binding.nestedScroolView.setVisibility(GONE);
        binding.loadingRelLayout.setVisibility(VISIBLE);
        binding.resFilter.setOnClickListener(view -> {
            Fragment restaurantMenuItemsFragment = new RestaurantSpecialityFrag(new FragmentCallback() {
                @Override
                public void onResponce(Bundle bundle) {
                    if (bundle != null) {
                        specialtyFIlter = bundle.getString("speciality");
                        if (specialtyFIlter.equals("African Cuisine")){
                            binding.collapsingToolbar.setVisibility(GONE);
                            binding.nestedScroolView.setVisibility(GONE);
                            binding.loadingRelLayout.setVisibility(VISIBLE);
                            getRestaurantListAgainstSpeciality(specialtyFIlter, true);
                            africanWares.clear();
                            beautyAndPersonalItems.clear();
                            foodAndGroceries.clear();
                            adapterAllRestaurantData.notifyDataSetChanged();
                        }
                        else if (specialtyFIlter.equals("African Wares")){
                            binding.collapsingToolbar.setVisibility(GONE);
                            binding.nestedScroolView.setVisibility(GONE);
                            binding.loadingRelLayout.setVisibility(VISIBLE);
                            getRestaurantListAgainstSpeciality(specialtyFIlter, true);
                            africanCuisine.clear();
                            beautyAndPersonalItems.clear();
                            foodAndGroceries.clear();
                            adapterAllRestaurantData.notifyDataSetChanged();
                        }
                        else if (specialtyFIlter.equals("Beauty and Personal Items")){
                            binding.collapsingToolbar.setVisibility(GONE);
                            binding.nestedScroolView.setVisibility(GONE);
                            binding.loadingRelLayout.setVisibility(VISIBLE);
                            getRestaurantListAgainstSpeciality(specialtyFIlter, true);
                            africanWares.clear();
                            africanCuisine.clear();
                            foodAndGroceries.clear();
                            adapterAllRestaurantData.notifyDataSetChanged();
                        }
                        else if (specialtyFIlter.equals("Food and Groceries")){
                            binding.collapsingToolbar.setVisibility(GONE);
                            binding.nestedScroolView.setVisibility(GONE);
                            binding.loadingRelLayout.setVisibility(VISIBLE);
                            getRestaurantListAgainstSpeciality(specialtyFIlter, true);
                            africanWares.clear();
                            africanCuisine.clear();
                            beautyAndPersonalItems.clear();
                            adapterAllRestaurantData.notifyDataSetChanged();
                        }
                    }
                }
            });
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.newsFragment, restaurantMenuItemsFragment, "ParentFragment").commit();
        });
        try{
            search = getArguments().getString("search");
        }
        catch (Exception e){

        }
        if (currentLoc.isEmpty()) {
            binding.toolbarDashboard.setTitle("Kalma Chowk Lahore");
        } else {
            binding.toolbarDashboard.setTitle(currentLoc);
        }
        getrestaurantlist();
        if (specialtyFIlter != null){
            getRestaurantListAgainstSpeciality(specialtyFIlter, true);
        }
        else if (search != null){
            getRestaurantListAgainstSpeciality(search, true);
        }
        else{
            getRestaurantListAgainstSpeciality(specialties.get(0), false);
            getRestaurantListAgainstSpeciality(specialties.get(1), false);
            getRestaurantListAgainstSpeciality(specialties.get(2), false);
            getRestaurantListAgainstSpeciality(specialties.get(3), false);
        }

        binding.map.setOnClickListener(view -> {
            openRestaurentOnMap();
        });
        binding.toolbarDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
            }
        });
        binding.toolbarDashboard.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HomeScreen.class);
                startActivity(i);
            }
        });

        /*binding.refreshLayout.setOnRefreshListener(() -> {
            allData.clear();
            for (int i = 0; i<specialties.size(); i++){
                getRestaurantListAgainstSpeciality(specialties.get(i));
            }
            adapterAllRestaurantData.notifyDataSetChanged();
            binding.restaurantRv.setAdapter(adapterAllRestaurantData);
            binding.refreshLayout.setRefreshing(false);
        });*/
        binding.edtSearch.setFocusable(false);
        binding.edtSearch.setOnClickListener(view -> {
            Fragment restaurantMenuItemsFragment = new RestaurantSpecialityFrag(bundle -> {
                if (bundle != null) {
                    specialtyFIlter = bundle.getString("speciality");
                    allData.clear();
                    if (specialtyFIlter.equals("African Cuisine")){
                        allData.add(africanCuisine);
                    }
                    else if (specialtyFIlter.equals("African Wares")){
                        allData.add(africanWares);
                    }
                    else if (specialtyFIlter.equals("Beauty and Personal Items")){
                        allData.add(beautyAndPersonalItems);
                    }
                    else if (specialtyFIlter.equals("Food and Groceries")){
                        allData.add(foodAndGroceries);
                    }
                    adapterAllRestaurantData.notifyDataSetChanged();
                }
            });
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.newsFragment, restaurantMenuItemsFragment, "ParentFragment").commit();
        });

        return binding.getRoot();
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    public void getRestaurantListAgainstSpeciality(String speciality, boolean forSearch) {
        String lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        String lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("long", lon);
            jsonObject.put("user_id", userId);
            jsonObject.put("speciality", speciality);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiRequest.callApi(getContext(), Config.SHOW_REST_AGAINST_SPECIALITY, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {
                if (forSearch){
                    allData.clear();
                }
                try {
                    JSONObject jsonResponse = new JSONObject(resp);
                    int code_id = Integer.parseInt(jsonResponse.optString("code"));
                    if (code_id == 200) {
                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject json1 = jsonarray.getJSONObject(i);

                            if (speciality.equals("African Cuisine")){
                                africanCuisine.add(DataParser.Pasrse_Restaurent(json1));
                            }
                            else if (speciality.equals("African Wares")){
                                africanWares.add(DataParser.Pasrse_Restaurent(json1));
                            }
                            else if (speciality.equals("Beauty and Personal Items")){
                                beautyAndPersonalItems.add(DataParser.Pasrse_Restaurent(json1));
                            }
                            else if (speciality.equals("Food and Groceries")){
                                foodAndGroceries.add(DataParser.Pasrse_Restaurent(json1));
                            }
                        }
                        binding.collapsingToolbar.setVisibility(VISIBLE);
                        binding.nestedScroolView.setVisibility(VISIBLE);
                        binding.loadingRelLayout.setVisibility(GONE);
                        if (forSearch){
                            if (speciality.equals("African Cuisine")){
                                adapterAllRestaurantData = new AdapterAllRestaurantData(NewRestuarantsFragment.this, africanCuisine, new ArrayList<RestaurantsModel>(), new ArrayList<RestaurantsModel>(), new ArrayList<RestaurantsModel>(), getContext());
                            }
                            else if (speciality.equals("African Wares")){
                                adapterAllRestaurantData = new AdapterAllRestaurantData(NewRestuarantsFragment.this, new ArrayList<RestaurantsModel>(), africanWares, new ArrayList<RestaurantsModel>(), new ArrayList<RestaurantsModel>(), getContext());
                            }
                            else if (speciality.equals("Beauty and Personal Items")){
                                adapterAllRestaurantData = new AdapterAllRestaurantData(NewRestuarantsFragment.this, new ArrayList<RestaurantsModel>(), new ArrayList<RestaurantsModel>(), beautyAndPersonalItems, new ArrayList<RestaurantsModel>(), getContext());
                            }
                            else if (speciality.equals("Food and Groceries")){
                                adapterAllRestaurantData = new AdapterAllRestaurantData(NewRestuarantsFragment.this, new ArrayList<RestaurantsModel>(), new ArrayList<RestaurantsModel>(), new ArrayList<RestaurantsModel>(), foodAndGroceries, getContext());
                            }
                        }
                        else{
                            adapterAllRestaurantData = new AdapterAllRestaurantData(NewRestuarantsFragment.this, africanCuisine, africanWares, beautyAndPersonalItems, foodAndGroceries, getContext());
                        }
                        binding.restaurantRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.restaurantRv.setAdapter(adapterAllRestaurantData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getActivity(), HomeScreen.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void search() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                   /* if (adapter != null)
                        adapter.getFilter().filter(s);
                    RelativeLayout.LayoutParams parms =
                            new RelativeLayout.LayoutParams(0, 0);
                    recyclerHeader.setLayoutParams(parms);

                } else {

                    RelativeLayout.LayoutParams parms =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, AllConstants.height / 3);
                    recyclerHeader.setLayoutParams(parms);

                    adapter.notifyDataSetChanged();
                }*/

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void openRestaurentOnMap(){
        Fragment restaurentsOnMap_a = new RestaurentsOnMap_F();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.newsFragment, restaurentsOnMap_a, "parent").commit();
    }

    public void getrestaurantlist() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("long", lon);
            jsonObject.put("current_time", formattedDate);
            jsonObject.put("user_id", userId);


        } catch (JSONException e) {
            Toast.makeText(getContext(), "Error occured "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        TabLayoutUtils.enableTabs(PagerMainActivity.tabLayout, false);

        ApiRequest.callApi(getContext(), Config.SHOW_RESTAURANTS, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                try {
                    JSONObject jsonResponse = new JSONObject(resp);
                    int code_id = Integer.parseInt(jsonResponse.optString("code"));
                    if (code_id == 200) {

                        ArrayList<RestaurantsModel> tem_list = new ArrayList<>();

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        JSONArray promoted = json.optJSONArray("promoted");
                        if (promoted != null) {
                            for (int i = 0; i < promoted.length(); i++) {

                                JSONObject json1 = promoted.getJSONObject(i);

                                promotedDataList.add(DataParser.Pasrse_Restaurent(json1));
                            }
                        }

                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject json1 = jsonarray.getJSONObject(i);

                            tem_list.add(DataParser.Pasrse_Restaurent(json1));
                        }

                        AdapterPromoted adapterPromoted = new AdapterPromoted(NewRestuarantsFragment.this, getContext(), promotedDataList);
                        binding.promotedRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        binding.promotedRv.setAdapter(adapterPromoted);
                    } else {
                        JSONObject json = new JSONObject(jsonResponse.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class SwipeAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }
    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (MapsActivity.SAVE_LOCATION) {

            lat = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
            lon = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");

            Address locationAddress;

            locationAddress = getAddress(Double.parseDouble(lat), Double.parseDouble(lon));
            if (locationAddress != null) {

                String city = "";
                if (locationAddress.getLocality() != null && !locationAddress.getLocality().equals("null"))
                    city = "" + locationAddress.getLocality();

                String country = "";
                if (locationAddress.getCountryName() != null && !locationAddress.getCountryName().equals("null"))
                    country = "" + locationAddress.getCountryName();


                String address = city + " " + country;

                binding.toolbarDashboard.setTitle(address);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PreferenceClass.CURRENT_LOCATION_ADDRESS, address).commit();
                getrestaurantlist();
                MapsActivity.SAVE_LOCATION = false;
            }
        }
    }

    public void saveAddressRequest(){
        String latitude = sharedPreferences.getString(PreferenceClass.LATITUDE, "");
        String longitude = sharedPreferences.getString(PreferenceClass.LONGITUDE, "");

        String user_id = sharedPreferences.getString(PreferenceClass.pre_user_id,"");


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("default","1");
            jsonObject.put("street",sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, ""));
            jsonObject.put("apartment","0");
            jsonObject.put("city", sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, ""));
            jsonObject.put("state","state");
            jsonObject.put("country","0");
            jsonObject.put("zip","0");
            jsonObject.put("instruction", "");
            jsonObject.put("lat",""+latitude);
            jsonObject.put("long",""+longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TabLayoutUtils.enableTabs(PagerMainActivity.tabLayout,false);

        ApiRequest.callApi(getApplicationContext(), Config.ADD_DELIVERY_ADDRESS, jsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                TabLayoutUtils.enableTabs(PagerMainActivity.tabLayout,true);

                try {
                    JSONObject jsonResponse = new JSONObject(resp);

                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {

                    }

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}