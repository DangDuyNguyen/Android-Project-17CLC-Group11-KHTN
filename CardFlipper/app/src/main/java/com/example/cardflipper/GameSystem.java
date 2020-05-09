package com.example.cardflipper;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class GameSystem {
    private Context context;
    public Dialog diff;
    private Audio audio;

    public GameSystem(Context context,Audio audio) {
        this.context = context;
        this.diff = new Dialog(context);
        this.audio = audio;
    }

    public void showDifficultyDialog()
    {
        this.diff.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.diff.setContentView(R.layout.custom_dialog);
        this.diff.show();
    }
}
