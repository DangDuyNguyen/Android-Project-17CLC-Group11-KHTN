package com.ndduy.gamecollection2020;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

public class LobbyFragment extends Fragment {
    public LobbyFragment() {
        // Required empty public constructor
    }

    TextView hungry_text, flatter_text, mood_text, sleepy_text;
    Button hungriness, flattering, mood, sleepiness;
    Status hungry_stat = new Status("Hungriness", 100, 0.2,  R.drawable.green_hungry_status_button);
    Status flatter_stat = new Status("Flattering", 100, 0.7, R.drawable.green_bathroom_status_button);
    Status mood_stat= new Status("Sleepiness", 100, 1, R.drawable.green_mood_status_button);
    Status sleepiness_stat= new Status("Mood", 100, 0.8, R.drawable.green_sleepy_status_button);
    /*flatter_stat =
    mood_stat = new Status("Sleepiness", 100, 1, R.drawable.green_mood_status_button);
    sleepiness_stat = new Status("Mood", 100, 0.8, R.drawable.green_sleepy_status_button);*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lobby, container, false);

        hungriness = (Button) rootView.findViewById(R.id.hungry_status);
        flattering = (Button) rootView.findViewById(R.id.bathroom_status);
        sleepiness = (Button) rootView.findViewById(R.id.sleepy_status);
        mood = (Button) rootView.findViewById(R.id.mood_status);

        hungry_text = (TextView) rootView.findViewById(R.id.hungry_stat);
        flatter_text = (TextView) rootView.findViewById(R.id.flatter_stat);
        mood_text = (TextView) rootView.findViewById(R.id.mood_stat);
        sleepy_text = (TextView) rootView.findViewById(R.id.sleepy_stat);

        final Handler hungry_update = new Handler();
        hungry_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hungry_stat.getPercentage() > 0)
                    hungry_stat.setPercentage(hungry_stat.getPercentage() - 1);

                if (hungry_stat.getPercentage() > 70)
                    hungry_stat.setImage(R.drawable.green_hungry_status_button);
                else if (hungry_stat.getPercentage() > 40)
                    hungry_stat.setImage(R.drawable.yellow_hungry_status_button);
                else
                    hungry_stat.setImage(R.drawable.red_hungry_status_button);

                hungry_text.setText(Integer.toString(hungry_stat.getPercentage()) + '%');
                hungriness.setBackgroundResource(hungry_stat.getImage());

                hungry_update.postDelayed(this, 5000);
            }
        }, 5000); //after 5s

        final Handler flatter_update = new Handler();
        flatter_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flatter_stat.getPercentage() > 0)
                    flatter_stat.setPercentage(flatter_stat.getPercentage() - 1);

                if (flatter_stat.getPercentage() > 70)
                    flatter_stat.setImage(R.drawable.green_bathroom_status_button);
                else if (flatter_stat.getPercentage() > 40)
                    flatter_stat.setImage(R.drawable.yellow_bathroom_status_button);
                else
                    flatter_stat.setImage(R.drawable.red_bathroom_status_button);

                flatter_text.setText(Integer.toString(flatter_stat.getPercentage()) + '%');
                flattering.setBackgroundResource(flatter_stat.getImage());

                flatter_update.postDelayed(this, 12000);
            }
        }, 12000); //after 12s

        final Handler sleepy_update = new Handler();
        sleepy_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sleepiness_stat.getPercentage() > 0)
                    sleepiness_stat.setPercentage(sleepiness_stat.getPercentage() - 1);

                if (sleepiness_stat.getPercentage() > 70)
                    sleepiness_stat.setImage(R.drawable.green_sleepy_status_button);
                else if (sleepiness_stat.getPercentage() > 40)
                    sleepiness_stat.setImage(R.drawable.yellow_sleepy_status_button);
                else
                    sleepiness_stat.setImage(R.drawable.red_sleepy_status_button);

                sleepy_text.setText(Integer.toString(sleepiness_stat.getPercentage()) + '%');
                sleepiness.setBackgroundResource(sleepiness_stat.getImage());

                sleepy_update.postDelayed(this, 30000);
            }
        }, 30000); //after 30s

        final Handler mood_update = new Handler();
        mood_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mood_stat.getPercentage() > 0)
                    mood_stat.setPercentage(mood_stat.getPercentage() - 1);

                if (mood_stat.getPercentage() > 70)
                    mood_stat.setImage(R.drawable.green_mood_status_button);
                else if (mood_stat.getPercentage() > 40)
                    mood_stat.setImage(R.drawable.yellow_mood_status_button);
                else
                    mood_stat.setImage(R.drawable.red_mood_status_button);

                mood_text.setText(Integer.toString(mood_stat.getPercentage()) + '%');
                mood.setBackgroundResource(mood_stat.getImage());

                mood_update.postDelayed(this, 15000);
            }
        }, 15000); //after 15s

        return rootView;

    }

}


