package com.example.cardflipper;

import android.provider.ContactsContract;

import java.util.ArrayList;

public class Card {
    int ImgDown;
    int ImgUp;



    public Card(int imgDown,int imgUp) {
        ImgDown = R.drawable.card_down;
        ImgUp = imgUp;
    }


    public int FlipCard()
    {
        return ImgUp;
    }

    public int DownCard(){
        return ImgDown;
    }
}
