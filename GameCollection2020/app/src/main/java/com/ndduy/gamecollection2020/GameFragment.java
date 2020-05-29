package com.ndduy.gamecollection2020;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class GameFragment extends Fragment {

    //LinearLayout AnimalRacing, Snake, CardFlipper, Sudoku;
    ImageButton AnimalRacing;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        AnimalRacing = (ImageButton)  rootView.findViewById(R.id.animalcrossbtn);

        AnimalRacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnimalRacingActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
