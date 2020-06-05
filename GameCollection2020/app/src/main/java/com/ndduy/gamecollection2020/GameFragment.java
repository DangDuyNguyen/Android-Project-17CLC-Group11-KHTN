package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ndduy.gamecollection2020.CardFlipper.CardFlipperActivity;

public class GameFragment extends Fragment {

    private ImageButton AnimalRacing, Snake, CardFlipper, Sudoku;

    private int AR_REQUEST_CODE = 69;
    private int SNAKE_REQUEST_CODE = 12;
    private int CARDFLIPPER_REQUEST_CODE = 78;
    private int SUDOKU_REQUEST_CODE = 49;
    private String currentCoin = "0";

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        AnimalRacing = (ImageButton)  rootView.findViewById(R.id.animalcrossbtn);
        Snake = (ImageButton) rootView.findViewById(R.id.snakebtn);
        CardFlipper = (ImageButton) rootView.findViewById(R.id.cardflipbtn);

        AnimalRacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().setFragmentResult("pass_coin_please", null);
                getParentFragmentManager().setFragmentResultListener("pass_coin", getActivity(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        currentCoin= result.getString("currentCoin", "0");
                    }
                });
                Intent intent = new Intent(getActivity(), AnimalRacingActivity.class);
                intent.putExtra("currentCoin",currentCoin);
                startActivityForResult(intent, AR_REQUEST_CODE);
            }
        });

        Snake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SnakeActivity.class);
                startActivityForResult(intent, SNAKE_REQUEST_CODE);
            }
        });

        CardFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardFlipperActivity.class);
                startActivityForResult(intent, CARDFLIPPER_REQUEST_CODE);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AR_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                int result = data.getIntExtra("coin", 0);

                Bundle bundle = new Bundle();
                bundle.putInt("coin", result);
                getParentFragmentManager().setFragmentResult("coin", bundle);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if(requestCode == SNAKE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                int result = data.getIntExtra("Snake_coin", 0);

                Bundle bundle = new Bundle();
                bundle.putInt("coin", result);
                getParentFragmentManager().setFragmentResult("Game_coin", bundle);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == CARDFLIPPER_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                int result = data.getIntExtra("Card_Flipper_coin", 0);

                Bundle bundle = new Bundle();
                bundle.putInt("coin", result);
                getParentFragmentManager().setFragmentResult("Game_coin", bundle);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

}
