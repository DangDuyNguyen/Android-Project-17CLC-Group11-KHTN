package com.project.arcadedestroyer.CardFlipper;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.project.arcadedestroyer.R;

import java.util.ArrayList;

public class GameSystem  {
    private Context context;
    public Dialog diff;
    public Button sound;
    public Audio audio;
    Boolean soundOn;
    ArrayList<Integer>deck = new ArrayList<>();

    public GameSystem(Context context,Audio audio,Boolean soundOn) {
        this.context = context;
        this.audio = audio;
        this.soundOn = soundOn;
        if (soundOn)
            audio.TurnOnSound();
        deck.add(R.drawable.card_apple);
        deck.add(R.drawable.card_grape);
        deck.add(R.drawable.card_carrot);
        deck.add(R.drawable.card_banana);
    }

    public void TurnOnSound()
    {
        audio.TurnOnSound();
    }

    public void TurnOffSound()
    {
        audio.TurnOffSound();
    }
}
