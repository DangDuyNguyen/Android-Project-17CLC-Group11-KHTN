package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.chip.Chip;

public class SettingClass extends PopupWindow{
    PopupWindow popupWindow;
    boolean sound = true;
    public void showPopupWindow(View view, final LifecycleOwner activity, final FragmentManager fragmentManager) {
        //Create a View object yourself through inflater
        final LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.custom_setting, null);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final CheckBox vol_checkbox = (CheckBox) popupView.findViewById(R.id.sound_checkbox);
        final Button close_btn = (Button) popupView.findViewById(R.id.setting_close_btn);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        if (sound)
            vol_checkbox.setChecked(true);
        else
            vol_checkbox.setChecked(false);

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);


        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

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

}
