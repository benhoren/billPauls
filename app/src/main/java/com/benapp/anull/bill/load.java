package com.benapp.anull.bill;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class load extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        play();
    }

    void play(){

        VideoView view = (VideoView)findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.loadvid;
        view.setVideoURI(Uri.parse(path));
        view.start();

        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                startActivity(new Intent(load.this, MainActivity.class));

            }
        });

//        try {
//            Thread.sleep(6000);
//        }catch(Exception e){}
//        startActivity(new Intent(this, MainActivity.class));

//        while(view.isPlaying()){}


    }
}
