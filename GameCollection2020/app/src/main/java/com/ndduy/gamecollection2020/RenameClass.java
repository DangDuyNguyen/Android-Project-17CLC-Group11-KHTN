package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Binder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

class RenameClass {
    String changedName = "";
   //PopupWindow display method

    public void showPopupWindow(final View view, final User user) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_changename, null);

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

        final EditText editName = (EditText) popupView.findViewById(R.id.editName);
        Button savebtn = (Button) popupView.findViewById(R.id.save_button);
        Button cancelbtn = (Button) popupView.findViewById(R.id.cancel_button);
        final EditText name = (EditText) view.findViewById(R.id.name);


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editName.getText().toString().equals("")){
                    user.setName(editName.getText().toString());
                    name.setText(editName.getText());
                }
                else
                    name.setText(name.getText());
                popupWindow.dismiss();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                editName.clearFocus();
                return true;
            }
        });

    }

    public String getChangedName(){
        return changedName;
    }
}
