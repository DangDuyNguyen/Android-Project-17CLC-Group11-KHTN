package com.example.cardflipper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GameSystem sys;
    Button playButton;
    ImageButton soundButton;
    Boolean soundOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        soundOn = true;
        if (intent != null) {
           soundOn = intent.getBooleanExtra("sound",true);

        }
        sys = new GameSystem(MainActivity.this,new Audio(getApplicationContext(),R.raw.background),soundOn);
        setupComponent();
    }

    private void setupComponent()
    {
        playButton =  (Button) findViewById(R.id.PlayButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sys.showDifficultyDialog();
            }
        });


        soundButton = (ImageButton) findViewById(R.id.SoundButton);
        if (soundOn)
            soundButton.setImageResource(R.drawable.sound_on);
        else  soundButton.setImageResource(R.drawable.sound_off);
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sys.audio.isPlaying())
                {
                    soundButton.setImageResource(R.drawable.sound_off);
                    sys.audio.TurnOffSound();
                    sys.soundOn = false;
                }
                else
                {
                    soundButton.setImageResource(R.drawable.sound_on);
                    sys.audio.TurnOnSound();
                    sys.soundOn = true;
                }
            }
        });

    }
}