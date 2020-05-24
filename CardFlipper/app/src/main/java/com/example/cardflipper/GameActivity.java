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
    Boolean result;
    ImageView first, second, settingButton;
    GridView table;
    Animation anim;
    TextView scores;
    Audio wrong, correct;
    ArrayList<Card> deck;
    ArrayList<Integer> diff;
    CardAdapter adapter;
    Intent intent;

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        intent = getIntent();
        diff = new ArrayList<>();
        diff.add(intent.getIntExtra("difficult", 8));
        ArrayList<Integer> Deck = intent.getIntegerArrayListExtra("deck");
        setUpComponent();
        setUpComponentListener();
        new BackgroundJob().execute(diff, Deck);
    }


    private void ReturntoMainMenu() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }

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

    private Boolean checkChoice(ImageView first, ImageView second) {
        return first.getDrawable().getConstantState().equals(second.getDrawable().getConstantState());
    }

    private void setUpComponent() {
        scores = (TextView) findViewById(R.id.scores);
        table = findViewById(R.id.card_table);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_disappear);
        settingButton = (ImageView) findViewById(R.id.settingButton);
        correct = new Audio(GameActivity.this, R.raw.correct);
        wrong = new Audio(GameActivity.this, R.raw.wrong);
    }


    private void checkState() {
        if (adapter.areAllItemsEnabled()) {

            Dialog win = new Dialog(GameActivity.this);
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
                    Intent inten = new Intent(GameActivity.this, GameActivity.class);
                    startActivity(inten);
                }
            });
            result.setText("Your score: " + player_score);
            win.setCanceledOnTouchOutside(false);
            win.show();
        }
    }

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
                if (prevPos != position) {
                    if (first == null) {
                        first = (ImageView) view.findViewById(R.id.card);
                        first.setImageResource(deck.get(position).FlipCard());
                        first.setTag(deck.get(position).FlipCard());
                        first.setEnabled(false);
                        prevPos = position;
                    } else if (second == null) {
                        second = (ImageView) view.findViewById(R.id.card);
                        second.setImageResource(deck.get(position).FlipCard());
                        second.setTag(deck.get(position).FlipCard());
                        curPos = position;
                    }
                    BackGroundCheckingState bg = new BackGroundCheckingState();
                    bg.execute(first, second);
                    try {
                        if (bg.get() == null) return;
                        if (bg.get() == true)
                        {
                            Thread.sleep(5000);
                            first.startAnimation(anim);
                            second.startAnimation(anim);
                            adapter.setItemClickable(prevPos,false);
                            adapter.setItemClickable(curPos,false);
                            player_score+=10;
                            scores.setText("Scores: "+player_score);
                            checkState();
                        }
                        if (bg.get() == false)
                        {
                            Thread.sleep(5000);
                            first.setImageResource(R.drawable.card_down);
                            second.setImageResource(R.drawable.card_down);
                        }
                        first = null;
                        second = null;
                        prevPos= -1;
                        curPos = -1;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class BackGroundCheckingState extends AsyncTask<ImageView, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ImageView... imageViews) {
            if (imageViews[0] != null && imageViews[1] != null) {
                Log.d("Choice", checkChoice(imageViews[0],imageViews[1]).toString());

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
    }
}



