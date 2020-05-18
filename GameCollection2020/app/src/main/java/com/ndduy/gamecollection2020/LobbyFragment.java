package com.ndduy.gamecollection2020;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

public class LobbyFragment extends Fragment {
    public LobbyFragment() {
        // Required empty public constructor
    }
    Button setting_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setting_btn = (Button) getView().findViewById(R.id.setting_btn);

        setting_btn.setOnClickListener();

        return inflater.inflate(R.layout.fragment_lobby, container, false);

    }


}


