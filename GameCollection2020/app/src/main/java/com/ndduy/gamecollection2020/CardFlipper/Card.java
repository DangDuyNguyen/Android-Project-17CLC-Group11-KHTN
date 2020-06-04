package com.ndduy.gamecollection2020.CardFlipper;

import android.provider.ContactsContract;

import com.ndduy.gamecollection2020.R;

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
