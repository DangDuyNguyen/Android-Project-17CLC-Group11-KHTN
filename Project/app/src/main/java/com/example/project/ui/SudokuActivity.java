package com.example.project.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;

import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.project.game.Cell;
import com.example.project.ui.SudokuBoard;
import com.example.project.R;

public class SudokuActivity extends Activity {
    private SudokuBoard mSudokuBoard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSudokuBoard = findViewById(R.id.sudoku_board);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, SudokuActivity.class), null, intent, 0, null);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void restartActivity() {
        startActivity(getIntent());
        finish();
    }

    public interface OnSelectedNumberChangedListener {
        void onSelectedNumberChanged(int number);
    }

    private OnSelectedNumberChangedListener onSelectedNumberChangedListener = new OnSelectedNumberChangedListener() {
        @Override
        public void onSelectedNumberChanged(int number) {
            if (number != 0) {
                Cell cell = mSudokuBoard.getSelectedCell();
                mSudokuBoard.setHighlightedValue(number);
                if (cell != null) {
                    mSudokuBoard.moveSelectionTo(cell.getRow(), cell.getColumn());
                }
            } else {
                mSudokuBoard.clearCellSelection();
            }
        }
    };
}
