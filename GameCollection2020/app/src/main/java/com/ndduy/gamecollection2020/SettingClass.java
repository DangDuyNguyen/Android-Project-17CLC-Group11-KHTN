package com.ndduy.gamecollection2020;

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

import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;

public class SettingClass {

    public void showPopupWindow(final View view, final FragmentManager fragmentManager) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.custom_setting, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        final CheckBox vol_checkbox = (CheckBox) popupView.findViewById(R.id.sound_checkbox);
        final Button close_btn = (Button) popupView.findViewById(R.id.setting_close_btn);

        vol_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Bundle bundle = new Bundle();
                if(isChecked){
                    //check box checked
                    bundle.putInt("volume", 1);
                }
                else {
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
