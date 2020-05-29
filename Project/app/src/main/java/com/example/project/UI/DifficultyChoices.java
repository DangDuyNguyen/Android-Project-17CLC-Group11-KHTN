package com.example.project.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.project.R;

import java.util.HashMap;
import java.util.Map;

public class DifficultyChoices extends Dialog {
    private Context mContext;
    private LayoutInflater mInflater;
    private RadioGroup groupBtn;
    private int mSelectedChoice;

    public DifficultyChoices(Context context) {
        super(context);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.difficulty_choices);
    }

    public void updateChoice(Integer choice) {
        mSelectedChoice = choice;
    }

    public int getChoice() { return mSelectedChoice; }

    private View createEditChoiceView() {
        View view = mInflater.inflate(R.layout.difficulty_choices, null);
        groupBtn = view.findViewById(R.id.difficultyChoices);
        groupBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateChoice(checkedId);

                view.setVisibility(View.GONE);
            }
        });

        return view;
    }
}