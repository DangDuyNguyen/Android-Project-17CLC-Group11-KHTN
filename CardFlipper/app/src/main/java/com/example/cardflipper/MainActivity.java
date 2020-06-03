package com.example.cardflipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    Dialog diff;
    Boolean soundOn;
    int REQUEST_CODE_SCORE = 555;
    Integer player_score;

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
               showDifficultyDialog();
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



    public void showDifficultyDialog()
    {
        diff = new Dialog(this);
        diff.requestWindowFeature(Window.FEATURE_NO_TITLE);
        diff.setContentView(R.layout.custom_dialog);
        setUpdiffDialog();
        diff.show();
    }

    private Intent SetIntent(int diff)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putIntegerArrayListExtra("deck",sys.deck);
        intent.putExtra("sound",sys.soundOn);
        intent.putExtra("difficult",diff);
        sys.TurnOffSound();
        return intent;
    }



    private void setUpdiffDialog()
    {
        Button easy,normal,hard;
        easy = diff.findViewById(R.id.easyButton);
        normal = diff.findViewById(R.id.normalButton);
        hard = diff.findViewById(R.id.hardButton);
        diff.setCanceledOnTouchOutside(true);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivityForResult(SetIntent(4),REQUEST_CODE_SCORE);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(SetIntent(6),REQUEST_CODE_SCORE);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(SetIntent(8),REQUEST_CODE_SCORE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SCORE && resultCode == RESULT_OK && data != null)
        {
           player_score = data.getIntExtra("scores",0);
           soundOn = data.getBooleanExtra("sound",true);
           if (soundOn)
               sys.TurnOnSound();
           else sys.TurnOffSound();
           diff.dismiss();
           Log.d("score",player_score.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}