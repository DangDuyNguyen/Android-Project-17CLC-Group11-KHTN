package com.project.arcadedestroyer.CardFlipper;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.project.arcadedestroyer.R;

public class Audio extends AsyncTask<Void,Void,Void> {
    private Context context;
    private MediaPlayer media;

    public Audio(Context context, int music) {
        this.context = context;
        media = MediaPlayer.create(context.getApplicationContext(), music);
        if (music == R.raw.background)
            media.setLooping(true);
        else media.setLooping(false);
    }

    public void TurnOffSound() {
            if (media.isPlaying())
                media.pause();
    }

    public void TurnOnSound() {
            media.start();
    }

    public boolean isPlaying()
    {
        return media.isPlaying();
    }


    @Override
    protected Void doInBackground(Void... voids) {
        TurnOnSound();
        return null;
    }
}
