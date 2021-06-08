package com.example.gesturepractice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayVideo extends AppCompatActivity {
    VideoView gesture_video;
    ProgressDialog progress;
    private static String gesture_url;
    private static String gesture_name;
    Button play_button;
    Button practice_button;
    private static String user_name;
    TextView gesture;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_video);

        play_button=findViewById(R.id.btn_play);
        practice_button=findViewById(R.id.btn_practice);

        gesture_name=getIntent().getStringExtra("Gesture");
        user_name=getIntent().getStringExtra("UserName");

        gesture=findViewById(R.id.gesture_name);
        gesture.setText("Gesture "+gesture_name);

        ShowVideo();

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowVideo();
            }
        });

        practice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayVideo.this,PracticeVideo.class);
                intent.putExtra("Gesture",gesture_name);
                intent.putExtra("UserName",user_name);
                intent.putExtra("Url",gesture_url.toString());
                startActivityForResult(intent,2);
            }
        });
    }

    public void ShowVideo()
    {
        gesture_video=(VideoView)findViewById(R.id.gesture_video);
        progress=new ProgressDialog(DisplayVideo.this);
        progress.show();
        gesture_url=getIntent().getStringExtra("Url");
        Uri url= Uri.parse(gesture_url);
        gesture_video.setVideoURI(url);
        gesture_video.start();
        gesture_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progress.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        user_name=getIntent().getStringExtra("UserName");
        Intent intent=new Intent(DisplayVideo.this,SelectDropDown.class);
        intent.putExtra("UserName",user_name);
        startActivityForResult(intent,2);
    }
}
