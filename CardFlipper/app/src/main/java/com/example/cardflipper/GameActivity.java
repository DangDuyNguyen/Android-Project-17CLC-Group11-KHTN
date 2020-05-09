package com.example.cardflipper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class GameActivity extends AppCompatActivity {
    int prevPos = -1;
    ImageView first,second;
    GridView table;
    ArrayList<Card>deck = new ArrayList<Card>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        prepareDeck();
        table = findViewById(R.id.card_table);
        table.setAdapter(new CardAdapter(GameActivity.this,deck));
        table.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (prevPos != position) {
                    if (first == null)
                    {
                        first = (ImageView) view.findViewById(R.id.card);
                        first.setImageResource(deck.get(position).FlipCard());
                        first.setTag(deck.get(position).FlipCard());
                        prevPos = position;
                    }
                    else if (second == null)
                    {
                        second = (ImageView) view.findViewById(R.id.card);
                        second.setImageResource(deck.get(position).FlipCard());
                        second.setTag(deck.get(position).FlipCard());
                    }

                    if (first != null && second != null)
                    {
                        Log.d("Choice",checkChoice().toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (checkChoice())
                                {
                                    first.setVisibility(View.INVISIBLE);
                                    second.setVisibility(View.INVISIBLE);
                                    first.setEnabled(false);
                                    second.setEnabled(false);
                                }
                                else
                                {
                                    first.setImageResource(R.drawable.card_down);
                                    second.setImageResource(R.drawable.card_down);
                                }
                                first= null;
                                second = null;
                                prevPos = -1;
                            }
                        }, 20000);
                    }
                }
            }
        });
    }

    private void prepareDeck()
    {
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot)); deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
        deck.add(new Card(R.drawable.card_down,R.drawable.card_carrot));
    }

    private Boolean checkChoice()
    {
        return first.getDrawable().getConstantState().equals(second.getDrawable().getConstantState());
    }
}
