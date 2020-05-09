package com.example.cardflipper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GameSystem sys;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sys = new GameSystem(MainActivity.this,new Audio((ImageButton) findViewById(R.id.SoundButton),getApplicationContext()));
        button =  (Button) findViewById(R.id.PlayButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sys.showDifficultyDialog();
            }
        });
    }
}