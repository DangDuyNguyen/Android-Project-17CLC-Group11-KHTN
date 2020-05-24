package com.example.cardflipper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

public class GameSystem  {
    private Context context;
    public Dialog diff;
    public Button sound;
    public Audio audio;
    ArrayList<Integer>deck = new ArrayList<>();

    public GameSystem(Context context,Audio audio) {
        this.context = context;
        this.diff = new Dialog(context);
        this.audio = audio;
        audio.TurnOnSound();
        deck.add(R.drawable.card_apple);
        deck.add(R.drawable.card_grape);
        deck.add(R.drawable.card_carrot);
        deck.add(R.drawable.card_banana);
    }

    public void showDifficultyDialog()
    {
        this.diff.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.diff.setContentView(R.layout.custom_dialog);
        setUpdiffDialog();
        this.diff.show();
    }

    private void setUpdiffDialog()
    {
        Button easy,normal,hard;
        easy = diff.findViewById(R.id.easyButton);
        normal = diff.findViewById(R.id.normalButton);
        hard = diff.findViewById(R.id.hardButton);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GameActivity.class);
                intent.putExtra("difficult",4);
                intent.putIntegerArrayListExtra("deck",deck);
                intent.putExtra("new_game",true);
                audio.TurnOffSound();
                context.startActivity(intent);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GameActivity.class);
                intent.putExtra("difficult",6);
                intent.putIntegerArrayListExtra("deck",deck);
                intent.putExtra("new_game",true);
                audio.TurnOffSound();
                context.startActivity(intent);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GameActivity.class);
                intent.putExtra("difficult",8);
                intent.putIntegerArrayListExtra("deck",deck);
                intent.putExtra("new_game",true);
                audio.TurnOffSound();
                context.startActivity(intent);
            }
        });
    }
}
