package com.ndduy.gamecollection2020;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
//import com.google.android.gms.games.snapshot;


public class MainActivity extends FragmentActivity {

    //in-code attributes
    TabLayout tabs;
    MyViewPager viewPager;

    //Main Game Fragment
    GameFragment gameFrag = new GameFragment();
    LobbyFragment lobbyFrag = new LobbyFragment();
    StoreFragment storeFrag = new StoreFragment();

    MediaPlayer backgroundMusic;
    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundMusic.seekTo(0);
        backgroundMusic.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        EditText coin = (EditText) findViewById(R.id.coin);

        final Button setting_btn = (Button) findViewById(R.id.setting_btn);
        final EditText nameChange = (EditText) findViewById(R.id.name);

        /*set background music for the game*/
        backgroundMusic = MediaPlayer.create(this, R.raw.maintheme);
        backgroundMusic.setLooping(true); // Set looping
        backgroundMusic.setVolume(100, 100);

        /*get adapter for viewpager for the tablayout*/
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), 1);

        adapter.addFragment(gameFrag, "Game");
        adapter.addFragment(lobbyFrag, "Character");
        adapter.addFragment(storeFrag, "Store");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            FragmentTransaction fragmentTransaction;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().equals("Character"))
                    /*setting_btn.setClickable(true);*/
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                /*if(tab.getText().toString().equals("Character"))
                    setting_btn.setClickable(false);
                    nameChange.setFocusableInTouchMode(false);
                    nameChange.setEnabled(false);*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabs.getTabAt(0).setIcon(R.drawable.game_icon);
        tabs.getTabAt(1).setIcon(R.drawable.character_icon);
        tabs.getTabAt(2).setIcon(R.drawable.cart_icon);

        Intent receiver = getIntent();
        int extraCoin = receiver.getIntExtra("coin", 0);


        //receive data with key "volume"
        getSupportFragmentManager().setFragmentResultListener("volume", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int vol_request = result.getInt("volume");
                if (vol_request == 0)
                    backgroundMusic.pause();
                else if (vol_request == 1) {
                    backgroundMusic.seekTo(0);
                    backgroundMusic.start();
                }
            }
        });
    }

}
