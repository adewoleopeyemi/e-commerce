package com.foodies.amatfoodies.activitiesAndFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodies.amatfoodies.constants.PreferenceClass;
import com.foodies.amatfoodies.googleMapWork.MapsActivity;
import com.foodies.amatfoodies.R;

import org.firezenk.bubbleemitter.BubbleEmitterView;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceClass.user, MODE_PRIVATE);
        String getCurrentLocationAddress = sharedPreferences.getString(PreferenceClass.CURRENT_LOCATION_ADDRESS, "");
        TextView location = findViewById(R.id.current_location);
        TextView locationTxt = findViewById(R.id.welcome_location_txt);
        View beauty_screen = findViewById(R.id.beauty);
        View anything_screen = findViewById(R.id.anything);
        View african_market_screen = findViewById(R.id.african_market);
        View resturant_view_screen = findViewById(R.id.resturant_view);
        View delivery_screen = findViewById(R.id.delivery);
        BubbleEmitterView bubbleEmitterView = findViewById(R.id.bubbemitterview);
        Button sell_or_buy = findViewById(R.id.sellORbuy);
        ImageView view6 = findViewById(R.id.imageView6);
        ImageView view7 = findViewById(R.id.imageView7);
        ImageView view8 = findViewById(R.id.imageView8);
        RelativeLayout searchRl = findViewById(R.id.search_div);

        searchRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });


        delivery_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(HomeScreen.this, DeliveryActivity.class);
                startActivity(nextScreen);
            }
        });
        sell_or_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, WebViewSellActivity.class);
                startActivity(i);
            }
        });
        location.setText(getCurrentLocationAddress.toString());
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });

        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });

        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, MapsActivity.class));
            }
        });

        //if the text on the search bar is clicked
        locationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(HomeScreen.this, MapsActivity.class);
                nextScreen.putExtra("search", "search");
                startActivity(nextScreen);

            }
        });

        //clicks for the individual views
        beauty_screen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent nextScreen = new Intent(HomeScreen.this, MainActivity.class);
                nextScreen.putExtra("search", "Beauty and Personal Items");
                startActivity(nextScreen);

            }
        });

        african_market_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextScreen = new Intent(HomeScreen.this, MainActivity.class);
                nextScreen.putExtra("search", "Food and Groceries");
                startActivity(nextScreen);
            }
        });


        resturant_view_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(HomeScreen.this, MainActivity.class);
                nextScreen.putExtra("search", "African Cuisine");
                startActivity(nextScreen);
            }
        });



        anything_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceClass.getSharedPreference(getApplicationContext()).edit().putString(PreferenceClass.SEARCH,null ).commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });




        emitbubble(bubbleEmitterView);


    }

    public void emitbubble(BubbleEmitterView bubbleEmitterView){
        new Handler().postDelayed(new Runnable()
        {
            public void run() {


                int size = (int) ((Math.random() * (200 - 100)) + 100);
                long time = (long) ((Math.random() * (700 - 1200)) + 100);

                bubbleEmitterView.setColorResources(R.color.badge_color,R.color.accent,R.color.ampm_text_color);
                bubbleEmitterView.setColors(R.color.default_circle_indicator_stroke_color,
                        R.color.badge_color,R.color.bpBlue);
                bubbleEmitterView.emitBubble(size);
                emitbubble(bubbleEmitterView);
            }
        },  700);

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

