package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.chip.Chip;

public class SettingClass extends PopupWindow{
    View popupView;
    PopupWindow popupWindow;
    boolean sound = true;
    CheckBox vol_checkbox;
    Button close_btn;
    Button firebase_signin;
    TextView login_status_msg;
    String username = "Login now to sync your data!";
    String login_state = "LOGIN";

    public void showPopupWindow(View view, final Context context, final FragmentManager fragmentManager) {
        //Create a View object yourself through inflater
        final LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

        popupView = inflater.inflate(R.layout.custom_setting, null);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        vol_checkbox = (CheckBox) popupView.findViewById(R.id.sound_checkbox);
        close_btn = (Button) popupView.findViewById(R.id.setting_close_btn);
        firebase_signin = (Button) popupView.findViewById(R.id.firebaseSigninBtn);
        login_status_msg = (TextView) popupView.findViewById(R.id.AuthenticationStatus);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        if (sound)
            vol_checkbox.setChecked(true);
        else
            vol_checkbox.setChecked(false);

        firebase_signin.setText(login_state);
        login_status_msg.setText(username);

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        firebase_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (firebase_signin.getText().equals("LOGIN"))
                    bundle.putBoolean("islogin", true);
                else if (firebase_signin.getText().equals("LOGOUT"))
                    bundle.putBoolean("islogin", false);

                fragmentManager.setFragmentResult("login_request", bundle);
            }
        });

        vol_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Bundle bundle = new Bundle();
                if(isChecked){
                    //check box checked
                    sound = true;
                    bundle.putInt("volume", 1);
                }
                else {
                    sound = false;
                    //check box unchecked
                    bundle.putInt("volume", 0);
                }
                fragmentManager.setFragmentResult("volume", bundle);
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void setOnLogin(View view, String username, String state){
        this.username = "Login as " + username;
        this.login_state = state;

        login_status_msg.setText("Login as " + username);
        firebase_signin.setText("LOGOUT");

        popupWindow.dismiss();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void setOnLogout(View view){
        this.username = "Login now to sync your data!";
        this.login_state = "LOGIN";

        login_status_msg.setText("Login now to sync your data!");
        firebase_signin.setText("LOGIN");

        popupWindow.dismiss();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public boolean isSound()
    {
        return sound;
    }

}
