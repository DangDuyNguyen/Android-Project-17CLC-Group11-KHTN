package com.ndduy.gamecollection2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    TabLayout tabs;
    ViewPager viewPager;
    ArrayList<Status> statuses = new ArrayList<>();
    Button hungriness, flattering, mood, sleepiness;
    Button setting_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        EditText coin = (EditText) findViewById(R.id.coin);
        hungriness = (Button) findViewById(R.id.hungry_status);
        flattering = (Button) findViewById(R.id.bathroom_status);
        sleepiness = (Button) findViewById(R.id.sleepy_status);
        mood = (Button) findViewById(R.id.mood_status);

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), 1);
        adapter.addFragment(new GameFragment(), "Game");
        adapter.addFragment(new LobbyFragment(), "Character");
        adapter.addFragment(new StoreFragment(), "Store");

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            FragmentTransaction fragmentTransaction;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentStore, new StoreFragment());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentLobby, new LobbyFragment());
                        fragmentTransaction.commit();
                        break;
                    case 3:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentGame, new GameFragment());
                        fragmentTransaction.commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabs.getTabAt(0).setIcon(R.drawable.game_icon);
        tabs.getTabAt(1).setIcon(R.drawable.character_icon);
        tabs.getTabAt(2).setIcon(R.drawable.cart_icon);

        statuses.add(new Status("Hungriness", 100, 0.5, ContextCompat.getDrawable(this, R.drawable.green_hungry_status_button)));
        statuses.add(new Status("Flattering", 100, 0.7, ContextCompat.getDrawable(this, R.drawable.green_hungry_status_button)));
        statuses.add(new Status("Sleepiness", 100, 1, ContextCompat.getDrawable(this, R.drawable.green_hungry_status_button)));
        statuses.add(new Status("Mood", 100, 0.8, ContextCompat.getDrawable(this, R.drawable.green_hungry_status_button)));

    }

    /*public void changeStatus(Button btn, Status status, Drawable image) {
        if (status.updateImage() == 0)
            btn.setBackground(image);
        else if (status.updateImage() == 1)
            btn.setBackground(getDrawable(R.drawable.yellow_hungry_status_button));
        else
            btn.setBackground(getDrawable(R.drawable.green_hungry_status_button));

    }*/
}
