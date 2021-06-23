package com.foodies.amatfoodies.activitiesAndFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.foodies.amatfoodies.adapters.AddressListAdapter;
import com.foodies.amatfoodies.constants.ApiRequest;
import com.foodies.amatfoodies.constants.Callback;
import com.foodies.amatfoodies.constants.Config;
import com.foodies.amatfoodies.constants.FragmentCallback;
import com.foodies.amatfoodies.constants.Functions;
import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.models.AddressListModel;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;
import com.foodies.amatfoodies.utils.TabLayoutUtils;
import com.foodies.amatfoodies.R;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by foodies on 10/18/2019.
 */

public class AddressListFragment extends RootFragment {

    Button cancel;
    ImageView backIcon;
    RelativeLayout addAddressDiv;
    public static boolean FLAG_ADDRESS_LIST,CART_NOT_LOAD;
    SharedPreferences sharedPreferences;

    ArrayList<AddressListModel> arrayListAddress;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    AddressListAdapter recyclerViewadapter;
    RecyclerView recyclerView;
    CamomileSpinner addresListProgress;
    FrameLayout addressListContainer;

    RelativeLayout transparentLayer,progressDialog;
    String address_id;

    View view;
    Context context;

    Bundle bundle;
    String forDelivery = "false";



    public AddressListFragment(){

    }

    FragmentCallback fragment_callback;
    public AddressListFragment(FragmentCallback fragment_callback){
        this.fragment_callback=fragment_callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.activity_add_address, container, false);
         context=getContext();

         bundle=getArguments();

         forDelivery = bundle.getString("forDelivery");
         sharedPreferences = getContext().getSharedPreferences(PreferenceClass.user, Context.MODE_PRIVATE);
        addressListContainer = view.findViewById(R.id.address_list_container);
        addressListContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        initUI(view);

        getAddressList();

        return view;
    }

    public void initUI(View v){

        progressDialog = v.findViewById(R.id.progressDialog_address);
        transparentLayer = v.findViewById(R.id.transparent_layer_address);
        addresListProgress = v.findViewById(R.id.addresListProgress);
        addresListProgress.start();
        recyclerView = v.findViewById(R.id.list_address);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        cancel = v.findViewById(R.id.cancle_address_btn);
        backIcon = v.findViewById(R.id.back_icon);
        addAddressDiv = v.findViewById(R.id.add_address_div);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });

        addAddressDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment restaurantMenuItemsFragment = new AddressDetailFragment(new FragmentCallback() {
                    @Override
                    public void onResponce(Bundle bundle) {
                        getAddressList();
                    }
                });
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.address_list_container, restaurantMenuItemsFragment,"parent").commit();
                FLAG_ADDRESS_LIST = true;

            }
        });

        if(UserAccountFragment.FLAG_DELIVER_ADDRESS || DealOrderFragment.DEAL_ADDRESS){
            backIcon.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
            UserAccountFragment.FLAG_DELIVER_ADDRESS = false;
            backIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                       Functions.Hide_keyboard(getActivity());

                       getActivity().onBackPressed();

                }
            });

        }

    }
    String res_id="",sub_total="";
    public void getAddressList(){
        TabLayoutUtils.enableTabs(PagerMainActivity.tabLayout,false);
        transparentLayer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);
        arrayListAddress = new ArrayList<>();
        String user_id = sharedPreferences.getString(PreferenceClass.pre_user_id,"");


        JSONObject addressJsonObject = new JSONObject();
        try {

            addressJsonObject.put("user_id", user_id);

            if(bundle!=null) {
                res_id = bundle.getString("rest_id");
                sub_total = bundle.getString("grand_total");
                addressJsonObject.put("restaurant_id", res_id);
                addressJsonObject.put("sub_total", sub_total);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.callApi(context, Config.GET_DELIVERY_ADDRESES, addressJsonObject, new Callback() {
            @Override
            public void onResponce(String resp) {

                try {
                    JSONObject jsonResponse = new JSONObject(resp);

                    int code_id  = Integer.parseInt(jsonResponse.optString("code"));

                    if(code_id == 200) {

                        JSONObject json = new JSONObject(jsonResponse.toString());
                        JSONArray jsonarray = json.getJSONArray("msg");

                        for (int i=0;i<jsonarray.length();i++){

                            JSONObject addressJson = jsonarray.getJSONObject(i);
                            JSONObject jsonObjAdd = addressJson.getJSONObject("Address");

                            AddressListModel addressListModel = new AddressListModel();
                            addressListModel.setApartment(jsonObjAdd.optString("apartment"));
                            addressListModel.setCity(jsonObjAdd.optString("city"));
                            addressListModel.setState(jsonObjAdd.optString("state"));
                            addressListModel.setStreet(jsonObjAdd.optString("street"));
                            addressListModel.setAddress_id(jsonObjAdd.optString("id"));
                            addressListModel.setDelivery_fee(jsonObjAdd.optString("delivery_fee"));
                            addressListModel.setInstruction(jsonObjAdd.optString("instructions"));
                            arrayListAddress.add(addressListModel);

                        }

                        if(arrayListAddress!=null) {

                            recyclerViewadapter = new AddressListAdapter(arrayListAddress, getActivity());
                            recyclerView.setAdapter(recyclerViewadapter);
                            recyclerViewadapter.notifyDataSetChanged();
                            recyclerViewadapter.setOnItemClickListner(new AddressListAdapter.OnItemClickListner() {
                                @Override
                                public void OnItemClicked(View view, int position) {

                                    AddressListModel addressListModel=arrayListAddress.get(position);

                                    if(fragment_callback!=null){
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("data",addressListModel);
                                        fragment_callback.onResponce(bundle);
                                        if (!forDelivery.equals("true")){
                                            getActivity().onBackPressed();
                                        }
                                    }


                                }
                            });
                        }

                    }



                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                TabLayoutUtils.enableTabs(PagerMainActivity.tabLayout,true);
                transparentLayer.setVisibility(View.GONE);
                progressDialog.setVisibility(View.GONE);

            }
        });
    }
}
