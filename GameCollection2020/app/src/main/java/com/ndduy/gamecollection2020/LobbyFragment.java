package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class LobbyFragment extends Fragment {

    final int CHARACTER_ITEM = 8;
    final int BACKGROUND_ITEM = 9;

    //current user state
    User currentUser = new User();
    ArrayList<String> bg_list;
    ArrayList<String> char_list;
    ArrayList<String> obtained_item;

    //game UI components
    private TextView hungry_text, flatter_text, mood_text, sleepy_text;
    private Button hungriness, flattering, mood, sleepiness;
    private EditText name;
    private EditText coin;
    private Button setting_btn;
    SettingClass settingClass = new SettingClass();

    public LobbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getParentFragmentManager().setFragmentResultListener("coin", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int extraCoin = result.getInt("coin");
                coin.setText(Integer.toString(Integer.parseInt(coin.getText().toString()) + extraCoin));
                currentUser.setCoin(coin.getText().toString());
            }
        });

        getParentFragmentManager().setFragmentResultListener("Snake_coin", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int extraCoin = result.getInt("coin");
                coin.setText(Integer.toString(Integer.parseInt(coin.getText().toString()) + extraCoin));
                currentUser.setCoin(coin.getText().toString());
            }
        });

        getParentFragmentManager().setFragmentResultListener("request_save_data", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("request")){
                    Bundle savedata = new Bundle();
                    savedata.putString("user_name", name.getText().toString());
                    savedata.putString("user_coin", coin.getText().toString());
                    savedata.putInt("user_hungry_status", Integer.parseInt(hungry_text.getText().toString().substring(0, hungry_text.getText().toString().indexOf('%'))));
                    savedata.putInt("user_flatter_status", Integer.parseInt(flatter_text.getText().toString().substring(0, flatter_text.getText().toString().indexOf('%'))));
                    savedata.putInt("user_sleep_status", Integer.parseInt(sleepy_text.getText().toString().substring(0, sleepy_text.getText().toString().indexOf('%'))));
                    savedata.putInt("user_mood_status", Integer.parseInt(mood_text.getText().toString().substring(0, mood_text.getText().toString().indexOf('%'))));

                    getParentFragmentManager().setFragmentResult("savedata", savedata);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_lobby, container, false);

        //get UI components id
        readVarUI(rootView);

        //initialize game value
        initValue();

        //load user data
        loadUser(currentUser);

        //load UI from the user data
        loadUI(currentUser);

        updateCoin();

        receiveDatafromStore();

        //update Status
        updateStatus();

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RenameClass editName = new RenameClass();
                editName.showPopupWindow(v);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingClass.showPopupWindow(v, getActivity(), getParentFragmentManager());
            }
        });

        hungriness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseHungryStatus(20);
            }
        });

        flattering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseFlatterStatus(30);
            }
        });

        return rootView;
    }

    public void readVarUI (View rootView) {
        hungriness = (Button) rootView.findViewById(R.id.hungry_status);
        flattering = (Button) rootView.findViewById(R.id.bathroom_status);
        sleepiness = (Button) rootView.findViewById(R.id.sleepy_status);
        mood = (Button) rootView.findViewById(R.id.mood_status);

        hungry_text = (TextView) rootView.findViewById(R.id.hungry_stat);
        flatter_text = (TextView) rootView.findViewById(R.id.flatter_stat);
        mood_text = (TextView) rootView.findViewById(R.id.mood_stat);
        sleepy_text = (TextView) rootView.findViewById(R.id.sleepy_stat);

        name = (EditText) rootView.findViewById(R.id.name);
        coin = (EditText) rootView.findViewById(R.id.coin);

        setting_btn = (Button) rootView.findViewById(R.id.setting_btn);
        name.setInputType(InputType.TYPE_NULL);
        coin.setInputType(InputType.TYPE_NULL);
    }

    public void initValue() {
        bg_list = new ArrayList<>();
        char_list = new ArrayList<>();
        obtained_item = new ArrayList<>();

        bg_list.add("bg1");
        char_list.add("char_chicken");

        obtained_item.addAll(bg_list);
        obtained_item.addAll(char_list);
    }

    public void loadUser (final User user){
        getParentFragmentManager().setFragmentResultListener("loaduserdata", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                user.setName(result.getString("user_name"));
                user.setCoin(result.getString("user_coin"));
                user.getHungriness().setPercentage(result.getInt("user_hungry_status"));
                user.getFlattering().setPercentage(result.getInt("user_flatter_status"));
                user.getSleepiness().setPercentage(result.getInt("user_sleep_status"));
                user.getMood().setPercentage(result.getInt("user_mood_status"));

                Toast.makeText(getActivity(), user.getName(), Toast.LENGTH_LONG).show();

                if (user.getHungriness().getPercentage() > 70)
                    user.getHungriness().setImage(R.drawable.green_hungry_status_button);
                else if (user.getHungriness().getPercentage() > 40)
                    user.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
                else
                    user.getHungriness().setImage(R.drawable.red_hungry_status_button);

                if (user.getFlattering().getPercentage() > 70)
                    user.getFlattering().setImage(R.drawable.green_bathroom_status_button);
                else if (user.getFlattering().getPercentage() > 40)
                    user.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
                else
                    user.getFlattering().setImage(R.drawable.red_bathroom_status_button);

                if (user.getSleepiness().getPercentage() > 70)
                    user.getSleepiness().setImage(R.drawable.green_sleepy_status_button);
                else if (user.getSleepiness().getPercentage() > 40)
                    user.getSleepiness().setImage(R.drawable.yellow_sleepy_status_button);
                else
                    user.getSleepiness().setImage(R.drawable.red_sleepy_status_button);

                if (user.getMood().getPercentage() > 70)
                    user.getMood().setImage(R.drawable.green_mood_status_button);
                else if (user.getMood().getPercentage() > 40)
                    user.getMood().setImage(R.drawable.yellow_mood_status_button);
                else
                    user.getMood().setImage(R.drawable.red_mood_status_button);

            }
        });
    }

    public void loadUI (User user){
        name.setText(user.getName());
        coin.setText(user.getCoin());

        hungry_text.setText(String.format("%s%%", Integer.toString(user.getHungriness().getPercentage())));
        flatter_text.setText(String.format("%s%%", Integer.toString(user.getFlattering().getPercentage())));
        sleepy_text.setText(String.format("%s%%", Integer.toString(user.getSleepiness().getPercentage())));
        mood_text.setText(String.format("%s%%", Integer.toString(user.getMood().getPercentage())));

        hungriness.setBackgroundResource(user.getHungriness().getImage());
        flattering.setBackgroundResource(user.getFlattering().getImage());
        sleepiness.setBackgroundResource(user.getSleepiness().getImage());
        mood.setBackgroundResource(user.getMood().getImage());
    }

    public void updateStatus() {
        final Handler hungry_update = new Handler();
        hungry_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getHungriness().getPercentage() > 0)
                    currentUser.getHungriness().setPercentage(currentUser.getHungriness().getPercentage() - 1);

                if (currentUser.getHungriness().getPercentage() > 70)
                    currentUser.getHungriness().setImage(R.drawable.green_hungry_status_button);
                else if (currentUser.getHungriness().getPercentage() > 40)
                    currentUser.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
                else
                    currentUser.getHungriness().setImage(R.drawable.red_hungry_status_button);

                hungry_text.setText(String.format("%s%%", Integer.toString(currentUser.getHungriness().getPercentage())));
                hungriness.setBackgroundResource(currentUser.getHungriness().getImage());

                hungry_update.postDelayed(this, 10000);
            }
        }, 10000); //after 10s

        final Handler flatter_update = new Handler();
        flatter_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getFlattering().getPercentage() > 0)
                    currentUser.getFlattering().setPercentage(currentUser.getFlattering().getPercentage() - 1);

                if (currentUser.getFlattering().getPercentage() > 70)
                    currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
                else if (currentUser.getFlattering().getPercentage() > 40)
                    currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
                else
                    currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

                flatter_text.setText(String.format("%s%%", Integer.toString(currentUser.getFlattering().getPercentage())));
                flattering.setBackgroundResource(currentUser.getFlattering().getImage());

                flatter_update.postDelayed(this, 12000);
            }
        }, 12000); //after 12s

        final Handler sleepy_update = new Handler();
        sleepy_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getSleepiness().getPercentage() > 0)
                    currentUser.getSleepiness().setPercentage(currentUser.getSleepiness().getPercentage() - 1);

                if (currentUser.getSleepiness().getPercentage() > 70)
                    currentUser.getSleepiness().setImage(R.drawable.green_sleepy_status_button);
                else if (currentUser.getSleepiness().getPercentage() > 40)
                    currentUser.getSleepiness().setImage(R.drawable.yellow_sleepy_status_button);
                else
                    currentUser.getSleepiness().setImage(R.drawable.red_sleepy_status_button);

                sleepy_text.setText(String.format("%s%%", Integer.toString(currentUser.getSleepiness().getPercentage())));
                sleepiness.setBackgroundResource(currentUser.getSleepiness().getImage());

                sleepy_update.postDelayed(this, 20000);
            }
        }, 20000); //after 20s

        final Handler mood_update = new Handler();
        mood_update.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser.getMood().getPercentage() > 0)
                    currentUser.getMood().setPercentage(currentUser.getMood().getPercentage() - 1);

                if (currentUser.getMood().getPercentage() > 70)
                    currentUser.getMood().setImage(R.drawable.green_mood_status_button);
                else if (currentUser.getMood().getPercentage() > 40)
                    currentUser.getMood().setImage(R.drawable.yellow_mood_status_button);
                else
                    currentUser.getMood().setImage(R.drawable.red_mood_status_button);

                mood_text.setText(String.format("%s%%", Integer.toString(currentUser.getMood().getPercentage())));
                mood.setBackgroundResource(currentUser.getMood().getImage());

                mood_update.postDelayed(this, 15000);
            }
        }, 15000); //after 15s

    }

    public void sendDatatoStore(){
        Bundle bundle = new Bundle();
        bundle.putInt("coin", Integer.parseInt(coin.getText().toString()));
        bundle.putStringArrayList("item_list", obtained_item);
        getParentFragmentManager().setFragmentResult("lobby_to_store", bundle);
    }

    public void receiveDatafromStore(){
        getParentFragmentManager().setFragmentResultListener("purchase_success", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String item = result.getString("purchased_item", null);
                obtained_item.add(item);
                if (result.getInt("item_type", 0) == CHARACTER_ITEM) {
                    char_list.add(item);
                    Toast.makeText(getActivity(), "Add to character", Toast.LENGTH_LONG).show();
                }
                else if (result.getInt("item_type", 0) == BACKGROUND_ITEM) {
                    bg_list.add(item);
                    Toast.makeText(getActivity(), "Add to background", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getActivity(), "Item: " + item, Toast.LENGTH_LONG).show();

                int cost = result.getInt("cost_coin", 0);
                coin.setText(Integer.toString(Integer.parseInt(coin.getText().toString()) - cost));
                sendDatatoStore();
            }
        });
    }

    public void updateCoin(){
        final Handler update_coin_handler = new Handler();
        update_coin_handler.post(new Runnable() {
            @Override
            public void run() {
                sendDatatoStore();
                update_coin_handler.post(this);
            }
        });
    }

    public void increaseHungryStatus(int range){

        int new_status = Integer.parseInt(hungry_text.getText().toString().substring(0, hungry_text.getText().toString().indexOf('%'))) + range;
        if (new_status < 100) {
            hungry_text.setText(Integer.toString(new_status) + "%");
            currentUser.getHungriness().setPercentage(new_status);
        }

        else {
            hungry_text.setText("100%");
            currentUser.getHungriness().setPercentage(100);
        }
        if (new_status < 100 + range) {
            int new_coin = Integer.parseInt(currentUser.getCoin().toString()) - 5;
            currentUser.setCoin(Integer.toString(new_coin));
            coin.setText(Integer.toString(new_coin));
        }

        int new_flatter = Integer.parseInt(flatter_text.getText().toString().substring(0, flatter_text.getText().toString().indexOf('%')));
        if (new_flatter - 10 <= 0){
            currentUser.getFlattering().setPercentage(0);
            flatter_text.setText("0%");
        }
        else{
            currentUser.getFlattering().setPercentage(new_flatter - 10);
            flatter_text.setText(Integer.toString(new_flatter - 10) + "%");
        }

        if (currentUser.getHungriness().getPercentage() > 70)
            currentUser.getHungriness().setImage(R.drawable.green_hungry_status_button);
        else if (currentUser.getHungriness().getPercentage() > 40)
            currentUser.getHungriness().setImage(R.drawable.yellow_hungry_status_button);
        else
            currentUser.getHungriness().setImage(R.drawable.red_hungry_status_button);

        hungriness.setBackgroundResource(currentUser.getHungriness().getImage());

        if (currentUser.getFlattering().getPercentage() > 70)
            currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
        else if (currentUser.getFlattering().getPercentage() > 40)
            currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
        else
            currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

        flattering.setBackgroundResource(currentUser.getFlattering().getImage());

    }

    public void increaseFlatterStatus(int range){

        int new_status = Integer.parseInt(flatter_text.getText().toString().substring(0, flatter_text.getText().toString().indexOf('%'))) + range;
        if (new_status < 100) {
            flatter_text.setText(Integer.toString(new_status) + "%");
            currentUser.getFlattering().setPercentage(new_status);
        }

        else {
            flatter_text.setText("100%");
            currentUser.getFlattering().setPercentage(100);
        }
        if (new_status < 100 + range) {
            int new_coin = Integer.parseInt(currentUser.getCoin().toString()) - 5;
            currentUser.setCoin(Integer.toString(new_coin));
            coin.setText(Integer.toString(new_coin));
        }

        if (currentUser.getFlattering().getPercentage() > 70)
            currentUser.getFlattering().setImage(R.drawable.green_bathroom_status_button);
        else if (currentUser.getFlattering().getPercentage() > 40)
            currentUser.getFlattering().setImage(R.drawable.yellow_bathroom_status_button);
        else
            currentUser.getFlattering().setImage(R.drawable.red_bathroom_status_button);

        flattering.setBackgroundResource(currentUser.getFlattering().getImage());
    }
}


