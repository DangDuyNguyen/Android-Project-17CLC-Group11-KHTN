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

public class LobbyFragment extends Fragment {

    Context context;


    //current user
    User currentUser = new User();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        final View rootView = inflater.inflate(R.layout.fragment_lobby, container, false);

        //get UI components id
        readVarUI(rootView);

        name.setInputType(InputType.TYPE_NULL);
        coin.setInputType(InputType.TYPE_NULL);

        //temporarily set user data
        loadUser(currentUser);

        //load UI from the user data
        loadUI(currentUser);

        //update Status
        updateStatus();

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldName = name.getText().toString();
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

            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        getParentFragmentManager().setFragmentResultListener("request_save_data", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("request")){
                    Bundle savedata = new Bundle();
                    savedata.putString("user_name", name.getText().toString());
                    savedata.putString("user_coin", currentUser.getCoin());
                    savedata.putInt("user_hungry_status", Integer.parseInt(hungry_text.getText().toString()));
                    Toast.makeText(getActivity(), hungry_text.getText(), Toast.LENGTH_LONG);
                    savedata.putInt("user_flatter_status", Integer.parseInt(flatter_text.getText().toString()));
                    savedata.putInt("user_sleep_status", Integer.parseInt(sleepy_text.getText().toString()));
                    savedata.putInt("user_mood_status", Integer.parseInt(mood_text.getText().toString()));

                    getParentFragmentManager().setFragmentResult("savedata", savedata);

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //send save data to main activity
    }

    public void readVarUI (View rootView)
    {
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

    public void updateStatus()
    {
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

                hungry_update.postDelayed(this, 5000);
            }
        }, 5000); //after 5s

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

                sleepy_update.postDelayed(this, 30000);
            }
        }, 30000); //after 30s

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


}


