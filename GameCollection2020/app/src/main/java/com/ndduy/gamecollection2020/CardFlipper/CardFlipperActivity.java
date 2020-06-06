package com.ndduy.gamecollection2020.CardFlipper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ndduy.gamecollection2020.R;

public class CardFlipperActivity extends Activity {
    GameSystem sys;
    Button playButton, exitButton;
    ImageButton soundButton;
    Dialog diff;
    Boolean soundOn;
    int REQUEST_CODE_SCORE = 555;
    int player_score;

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
        sys = new GameSystem(CardFlipperActivity.this,new Audio(getApplicationContext(),R.raw.background),soundOn);
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
            soundButton.setImageResource(R.drawable.volume_unmute);
        else  soundButton.setImageResource(R.drawable.volume_mute);
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

        exitButton = (Button) findViewById(R.id.ExitButton) ;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sys.audio.TurnOffSound();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Card_Flipper_coin", player_score);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}