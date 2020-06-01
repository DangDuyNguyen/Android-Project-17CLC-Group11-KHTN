package com.example.cardflipper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class GameActivity extends AppCompatActivity {
    int prevPos = -1,curPos = -1;
    int player_score = 0;
    Boolean sound = true;
    ImageView first, second, settingButton;
    GridView table;
    Animation anim;
    TextView scores;
    ArrayList<Integer> Deck;
    Audio wrong, correct;
    ArrayList<Card> deck;
    ArrayList<Integer> diff;
    CardAdapter adapter;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        intent = getIntent();
        diff = new ArrayList<>();
        diff.add(intent.getIntExtra("difficult", 8)); //lấy độ khó của game
        Deck = intent.getIntegerArrayListExtra("deck");             // lấy set hình của game
        setUpComponent();
        setUpComponentListener();
        new BackgroundJob().execute(diff, Deck);
    }


    // hàm trở về menu chính
    private void ReturntoMainMenu() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //hàm chuẩn bị bộ bài
    class BackgroundJob extends AsyncTask<ArrayList<Integer>, ArrayList<Card>, ArrayList<Card>> {
        @Override
        protected ArrayList<Card> doInBackground(ArrayList<Integer>... arrayLists) {

            Random generator = new Random();
            ArrayList<Card> deck = new ArrayList<>();
            for (int i = 0; i < arrayLists[0].get(0); i++) {
                int value = generator.nextInt(arrayLists[1].size());
                deck.add(new Card(R.drawable.card_down, arrayLists[1].get(value)));
                deck.add(new Card(R.drawable.card_down, arrayLists[1].get(value)));
            }
            Collections.shuffle(deck);
            return deck;
        }

        @Override
        protected void onPostExecute(ArrayList<Card> cards) {
            deck = cards;
            int column_width = table.getMeasuredWidth() / 4;
            int column_height = table.getMeasuredHeight() / (diff.get(0) / 2);
            adapter = new CardAdapter(GameActivity.this, deck, column_width, column_height);
            table.setAdapter(adapter);
            super.onPostExecute(cards);
        }
    }

    private Boolean checkChoice(Card item1, Card item2) {
        return item1.ImgUp == item2.ImgUp;
    }


    //Hàm set up component
    private void setUpComponent() {
        scores = (TextView) findViewById(R.id.scores);
        table = findViewById(R.id.card_table);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_disappear);
        settingButton = (ImageView) findViewById(R.id.settingButton);
        correct = new Audio(GameActivity.this, R.raw.correct);
        wrong = new Audio(GameActivity.this, R.raw.wrong);
    }

    //Hàm kiểm tra hết bài
    private void checkState() {
        if (adapter.areAllItemsEnabled()) {

            final Dialog win = new Dialog(GameActivity.this);
            win.requestWindowFeature(Window.FEATURE_NO_TITLE);
            win.setContentView(R.layout.custom_endgame_dialog);

            TextView result = win.findViewById(R.id.result);
            Button retunMenu = win.findViewById(R.id.returnToMenuButton);
            Button playAgain = win.findViewById(R.id.PlayagainButton);
            retunMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(inten);
                }
            });

            playAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BackgroundJob().execute(diff,Deck);
                    scores.setText("Scores: "+ player_score);
                    win.dismiss();
                }
            });
            result.setText("Your score: " + player_score);
            win.setCanceledOnTouchOutside(false);
            win.show();
        }
    }


    //Hàm setUpListener cho các component
    private void setUpComponentListener() {
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog sett = new Dialog(GameActivity.this);
                sett.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sett.setContentView(R.layout.custom_setting_dialog);

                final Button soundButt = sett.findViewById(R.id.settingSoundButton);
                if (sound == true) {
                    soundButt.setText("Sound: ON");

                } else {
                    soundButt.setText("Sound: OFF");
                }
                Button returntoMenu = sett.findViewById(R.id.settingreturnToMenuButton);
                soundButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sound == true) {
                            soundButt.setText("Sound: OFF");
                            sound = false;
                        } else {

                            soundButt.setText("Sound: ON");
                            sound = true;
                        }
                    }
                });
                returntoMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReturntoMainMenu();
                    }
                });
                sett.show();
            }
        });
        table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (first == null) {
                    first = (ImageView) view.findViewById(R.id.card);
                    first.setImageResource(deck.get(position).FlipCard());
                    first.setEnabled(false);
                    prevPos = position;
                } else if (second == null) {
                    second = (ImageView) view.findViewById(R.id.card);
                    second.setImageResource(deck.get(position).FlipCard());
                    second.setEnabled(false);
                    curPos = position;
                }
                if (first != null && second != null) {
                    BackGroundCheckingState bg = new BackGroundCheckingState();
                    bg.execute(deck.get(prevPos), deck.get(position));
                }
            }
        });
    }



    private class BackGroundCheckingState extends AsyncTask<Card, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Card... imageViews) {
            if (imageViews[0] != null && imageViews[1] != null) {
                if (checkChoice(imageViews[0],imageViews[1])) {
                    if (sound)
                        correct.TurnOnSound();
                    return true;
                } else {
                    if (sound)
                        wrong.TurnOnSound();
                    return false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (aBoolean == null) return;
            else if (aBoolean == true) {

                first.startAnimation(anim);
                second.startAnimation(anim);
                adapter.setItemClickable(prevPos, false);
                adapter.setItemClickable(curPos, false);
                player_score += 10;
                scores.setText("Scores: " + player_score);
                checkState();
            }
            else if (aBoolean == false) {
                first.setImageResource(R.drawable.card_down);
                second.setImageResource(R.drawable.card_down);
            }

            first = null;
            second = null;
            prevPos = -1;
            curPos = -1;
        }
    }
}



