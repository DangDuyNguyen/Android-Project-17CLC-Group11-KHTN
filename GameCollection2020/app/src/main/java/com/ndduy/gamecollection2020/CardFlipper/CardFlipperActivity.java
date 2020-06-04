package com.ndduy.gamecollection2020.CardFlipper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.ndduy.gamecollection2020.R;

import java.util.ArrayList;

public class CardFlipperActivity extends Activity {
    GameSystem sys;
    Button playButton, exitButton;
    ImageButton soundButton;
    Boolean soundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        //getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flipper);
        Intent intent = getIntent();
        soundOn = true;
        if (intent != null) {
           soundOn = intent.getBooleanExtra("sound",true);

        }
        sys = new GameSystem(CardFlipperActivity.this,new Audio(getApplicationContext(), R.raw.background),soundOn);
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
            soundButton.setImageResource(R.drawable.volume_unmute);
        else
            soundButton.setImageResource(R.drawable.volume_mute);
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sys.audio.isPlaying())
                {
                    soundButton.setImageResource(R.drawable.volume_mute);
                    sys.audio.TurnOffSound();
                    sys.soundOn = false;
                }
                else
                {
                    soundButton.setImageResource(R.drawable.volume_unmute);
                    sys.audio.TurnOnSound();
                    sys.soundOn = true;
                }
            }
        });

        exitButton = (Button) findViewById(R.id.ExitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent returnIntent = new Intent();
                returnIntent.putExtra("Card_Flipper_coin", 20);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });
    }

    private int convertToCoin(int score){
        return score*20/100;
    }

}