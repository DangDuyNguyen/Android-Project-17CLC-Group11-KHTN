package com.example.cardflipper;

import android.content.ComponentName;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

public class Audio {
    private Context context;
    private ImageButton button;
    private MediaPlayer media;

    public Audio(ImageButton button,  Context context) {
        this.button = button;
        this.context = context;
        media = MediaPlayer.create(context.getApplicationContext(), R.raw.background);
        media.setLooping(true);
        media.start();
        if (media.isPlaying())
            button.setImageResource(R.drawable.sound_on);
        else button.setImageResource(R.drawable.sound_off);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media.isPlaying()) TurnOffSound();
                else TurnOnSound();
            }
        });
    }

    public void TurnOffSound() {
            media.pause();
            button.setImageResource(R.drawable.sound_off);
    }

    public void TurnOnSound() {
            media.start();
            button.setImageResource(R.drawable.sound_on);
    }
}
