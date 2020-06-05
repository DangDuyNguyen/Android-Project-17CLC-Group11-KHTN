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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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

        Bundle saverequest = new Bundle();
        saverequest.putBoolean("request", true);
        getSupportFragmentManager().setFragmentResult("request_save_data", saverequest);

        //retrieve game data from fragment after request
        getSupportFragmentManager().setFragmentResultListener("savedata", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String user_name = result.getString("user_name");
                String user_coin = result.getString("user_coin");
                int user_hungry_status = result.getInt("user_hungry_status");
                int user_flatter_status = result.getInt("user_flatter_status");
                int user_sleepy_status = result.getInt("user_sleep_status");
                int user_mood_status = result.getInt("user_mood_status");
                ArrayList<String> char_list = result.getStringArrayList("user_char_list");
                ArrayList<String> bg_list = result.getStringArrayList("user_bg_list");

                File delete = new File(getFilesDir(), "gamedata.txt");
                delete.delete();

                File savefile = new File(getFilesDir() + "/gamedata.txt");
                try {
                    FileOutputStream fout = openFileOutput("gamedata.txt",Context.MODE_PRIVATE);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fout);
                    outputWriter.write(user_name);
                    outputWriter.write("\n");
                    outputWriter.write(user_coin);
                    outputWriter.write("\n");
                    outputWriter.write(Integer.toString(user_hungry_status));
                    outputWriter.write("\n");
                    outputWriter.write(Integer.toString(user_flatter_status));
                    outputWriter.write("\n");
                    outputWriter.write(Integer.toString(user_sleepy_status));
                    outputWriter.write("\n");
                    outputWriter.write(Integer.toString(user_mood_status));
                    outputWriter.write("\n");
                    for(int i = 0; i < char_list.size(); i++){
                        outputWriter.write(char_list.get(i));
                        outputWriter.write("\n");
                    }
                    outputWriter.write("\n");
                    for(int i = 0; i < bg_list.size(); i++){
                        if (i == bg_list.size() - 1)
                            outputWriter.write(bg_list.get(i));
                        else{
                            outputWriter.write(bg_list.get(i));
                            outputWriter.write("\n");
                        }
                    }
                    outputWriter.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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

        /*File delete = new File(getFilesDir(), "gamedata.txt");
                delete.delete();*/

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (MyViewPager) findViewById(R.id.viewpager);
        EditText coin = (EditText) findViewById(R.id.coin);

        final Button setting_btn = (Button) findViewById(R.id.setting_btn);
        final EditText nameChange = (EditText) findViewById(R.id.name);

        /*set background music for the game*/
        backgroundMusic = MediaPlayer.create(this, R.raw.maintheme);
        backgroundMusic.setLooping(true); // Set looping
        backgroundMusic.setVolume(100, 100);
        backgroundMusic.start();

        readData();

        /*get adapter for viewpager for the tablayout*/
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), 1);

        adapter.addFragment(gameFrag, "Game");
        adapter.addFragment(lobbyFrag, "Character");
        adapter.addFragment(storeFrag, "Store");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            FragmentTransaction fragmentTransaction;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy: destroyed");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void readData() {
        String name = "";
        String coin = "";
        int hungry_status = 0;
        int flatter_status = 0;
        int sleepy_status = 0;
        int mood_status = 0;
        ArrayList<String> char_list = new ArrayList<>();
        ArrayList<String> bg_list = new ArrayList<>();

        try {
            FileInputStream fin = openFileInput("gamedata.txt");
            DataInputStream in = new DataInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = "";
            if ((line = br.readLine()) != null)
                name = line;
            if ((line = br.readLine()) != null)
                coin = line;
            if ((line = br.readLine()) != null)
                hungry_status = Integer.parseInt(line);
            if ((line = br.readLine()) != null)
                flatter_status = Integer.parseInt(line);
            if ((line = br.readLine()) != null)
                sleepy_status = Integer.parseInt(line);
            if ((line = br.readLine()) != null)
                mood_status = Integer.parseInt(line);
            while(!(line = br.readLine()).equals(""))
                char_list.add(line);
            while ((line = br.readLine()) != null)
                bg_list.add(line);
            in.close();
            fin.close();

            Bundle loaduserbundle = new Bundle();
            loaduserbundle.putString("user_name", name);
            loaduserbundle.putString("user_coin", coin);
            loaduserbundle.putInt("user_hungry_status", hungry_status);
            loaduserbundle.putInt("user_flatter_status", flatter_status);
            loaduserbundle.putInt("user_sleep_status", sleepy_status);
            loaduserbundle.putInt("user_mood_status", mood_status);
            loaduserbundle.putStringArrayList("user_char_list", char_list);
            loaduserbundle.putStringArrayList("user_bg_list", bg_list);
            getSupportFragmentManager().setFragmentResult("loaduserdata", loaduserbundle);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
