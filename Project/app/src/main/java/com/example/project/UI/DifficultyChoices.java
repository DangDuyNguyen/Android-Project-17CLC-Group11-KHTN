package com.example.project.UI;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.project.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DifficultyChoices extends Dialog {
    private Context mContext;
    private LayoutInflater mInflater;

    private RadioGroup groupBtns;

    private int mSelectedChoice;

    public DifficultyChoices(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedChoice = -1;
        View v = createView();
        setContentView(v);
    }

    private View createView() {
        View v = mInflater.inflate(R.layout.difficulty_choices, null);
        groupBtns = v.findViewById(R.id.choices);

        groupBtns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.endlessMode:
                        mSelectedChoice = 0;
                        break;
                    case R.id.easyMode:
                        mSelectedChoice = 1;
                        break;
                    case R.id.mediumMode:
                        mSelectedChoice = 2;
                        break;
                    case R.id.hardMode:
                        mSelectedChoice = 3;
                        break;
                }

                dismiss();
            }
        });

        return v;
    }

    public int getChoice() { return mSelectedChoice; }
}
