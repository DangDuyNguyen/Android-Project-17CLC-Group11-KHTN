package com.example.storegacha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class change_background extends AppCompatActivity {

    int chosen_BG;
    Button change;
    ArrayList<Background> list_bg;
    LinearLayout bg;
    BGAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);
        change = findViewById(R.id.change_button);
        getBG();
        bg = findViewById(R.id.mainBG);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }


    private void getBG()
    {
       list_bg = new ArrayList<>();
       list_bg.add(new Background("Heart",R.drawable.background));
       list_bg.add(new Background("Royal",R.drawable.background2));
    }

    private void setUpComponent(final Dialog dial)
    {
        GridView list = dial.findViewById(R.id.gridTable);
        BGAdapter adapter = new BGAdapter(this,R.layout.background_item,list_bg);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosen_BG = list_bg.get(position).getImg();
                bg.setBackgroundResource(chosen_BG);
            }
        });

    }

    private void showDialog()
    {
        Dialog dial = new Dialog(this);
        dial.setCanceledOnTouchOutside(true);
        dial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dial.setContentView(R.layout.custom_background_dialog);
        GridView list = dial.findViewById(R.id.gridTable);
        BGAdapter adapter = new BGAdapter(this,R.layout.background_item,list_bg);
        //adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosen_BG = list_bg.get(position).getImg();
                bg.setBackgroundResource(chosen_BG);
            }
        });
        dial.show();
    }
}