package com.project.arcadedestroyer.Sudoku.UI;

import java.util.Formatter;

public class TimeFormat {
    private static final int LARGE_TIME = 99 * 60000 +  59 * 1000;

    private StringBuilder mTimeText;
    private Formatter mTimeFormatter;

    public String formatTime(long time) {
        mTimeText = new StringBuilder();
        mTimeFormatter = new Formatter(mTimeText);
        if (time > LARGE_TIME) {
            mTimeFormatter.format("%d:%02d", time / 60000, time / 1000 % 60);
        } else {
            mTimeFormatter.format("%02d:%02d", time / 60000, time / 1000 % 60);
        }
        return mTimeText.toString();
    }
}