package com.foodies.amatfoodies.ActivitiesAndFragments;

import androidx.appcompat.app.AppCompatActivity;
import com.foodies.amatfoodies.R;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class GetStartedActivity extends AppCompatActivity {

    //Video View for Splash Screen
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Video View for Splash Screen
        videoView = (VideoView) findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logomotionsmall);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
//                startNextActivity(); //Here you have to define what happens when the video finish playing. (You should put it on replay if the activity is still active)
            }
        });

        videoView.start();

        // End Video View for Splash Screen
    }
}