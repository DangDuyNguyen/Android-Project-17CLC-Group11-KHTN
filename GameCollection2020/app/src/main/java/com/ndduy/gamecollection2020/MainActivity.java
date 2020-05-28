package com.ndduy.gamecollection2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.google.android.material.tabs.TabLayout;
//import com.google.android.gms.games.snapshot;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    //in-code attributes
    TabLayout tabs;
    MyViewPager viewPager;

    //Main Game Fragment
    GameFragment gameFrag = new GameFragment();
    LobbyFragment lobbyFrag = new LobbyFragment();
    StoreFragment storeFrag = new StoreFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        EditText coin = (EditText) findViewById(R.id.coin);

        final Button setting_btn = (Button) findViewById(R.id.setting_btn);
        final EditText nameChange = (EditText) findViewById(R.id.name);

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
                    setting_btn.setClickable(true);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getText().toString().equals("Character"))
                    setting_btn.setClickable(false);
                    nameChange.setFocusableInTouchMode(false);
                    nameChange.setEnabled(false);
                    //nameChange.setFocusable(false);
                    //nameChange.setEnabled(false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabs.getTabAt(0).setIcon(R.drawable.game_icon);
        tabs.getTabAt(1).setIcon(R.drawable.character_icon);
        tabs.getTabAt(2).setIcon(R.drawable.cart_icon);

        Intent intent = getIntent();
        String message = intent.getStringExtra("editname");

        Bundle bundle = new Bundle();
        bundle.putString("editname", message);
        lobbyFrag.setArguments(bundle);
    }
}
