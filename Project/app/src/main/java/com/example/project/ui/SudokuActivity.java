package com.example.project.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.DB.SudokuDB;
import com.example.project.game.Cell;
import com.example.project.game.SudokuGame;
import com.example.project.game.SudokuGame.OnPuzzleSolvedListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SudokuActivity extends AppCompatActivity {
    private ArrayList<Long> mSudokuList;
    private int mCurrentSudokuIndex = -1;
    private SudokuGame mCurrentSudokuGame;

    private SudokuDB mDB;

    private Handler mHandler;

    private ViewGroup mLayout;
    private SudokuBoard mSudokuBoard;
    private TextView mLevelInfo;
    private TextView mScoreInfo;

    private Menu mMenu;

    private InputControl mControl;
    private InputControlState mControlState;
    private NumpadMethod mNumpad;
    private PopUpNumpadMethod mPopupNumpad;

    private boolean mShowTime = false;
    private GameTimer mGameTimer;
    private TimeFormat mFormat = new TimeFormat();

    private int mScore = 0;
    private int mLevel = 0;
    private int mNumOfHints;

    public static final int MENU_ITEM_RESTART = Menu.FIRST;
    public static final int MENU_ITEM_DIFFICULTY = Menu.FIRST + 1;
    public static final int MENU_ITEM_SHOWTIME = Menu.FIRST + 2;

    private static AlertDialog restartDialog;
    private static AlertDialog restartTimeModeDialog;

    private static final int REQUEST_SETTINGS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.main_layout);
        mSudokuBoard = findViewById(R.id.sudoku_board);
        mLevelInfo = findViewById(R.id.levelInfo);
        mScoreInfo = findViewById(R.id.scoreInfo);

        mDB = new SudokuDB(getApplicationContext());

        mHandler = new Handler();

        mGameTimer = new GameTimer();

        mControl = new InputControl(this);
        mControl = findViewById(R.id.inputMethods);

        mNumOfHints = 3;

        mControlState = new InputControlState(this);

        mSudokuList = mDB.getSudokuId(mLevel);
        if(!mSudokuList.isEmpty())
        {
            mCurrentSudokuIndex = 0;
            loadGame(mCurrentSudokuIndex);
        }

        restartDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_restart_icon)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure you want to restart this game?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    mGameTimer.reset();
                    mCurrentSudokuIndex = 0;
                    loadGame(mCurrentSudokuIndex);
                    if(mShowTime)
                        mGameTimer.start();
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        restartTimeModeDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_restart_icon)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure you want to restart this game?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    mGameTimer.reset();
                    mCurrentSudokuIndex = 0;
                    if(mShowTime) mShowTime = false;
                    else mShowTime = true;
                    loadGame(mCurrentSudokuIndex);
                    if(mShowTime)
                        mGameTimer.start();
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }

    public void loadGame(int index)
    {
        index = (index < mSudokuList.size()) ? index : 0;
        mCurrentSudokuGame = mDB.getSudoku(mSudokuList.get(index).longValue());
        mCurrentSudokuGame.setOnPuzzleSolvedListener(onSolvedListener);
        mCurrentSudokuGame.start();

        mSudokuBoard.setGame(mCurrentSudokuGame);

        long levelId = mCurrentSudokuGame.getLevelId();
        if(levelId == 1)
            mLevelInfo.setText("Easy");
        else if (levelId == 2)
            mLevelInfo.setText("Medium");
        else mLevelInfo.setText("Hard");

        mScoreInfo.setText("Score: " + mScore);

        if(!mShowTime)
            setTitle(R.string.app_name);

        mControl.init(mSudokuBoard, mCurrentSudokuGame, mNumOfHints);
        mPopupNumpad = mControl.getInputMethod(InputControl.INPUT_METHOD_POPUP);
        mNumpad = mControl.getInputMethod(InputControl.INPUT_METHOD_NUMPAD);
        mControl.activate1stInputMethod();
        mControlState.restoreState(mControl);

        Cell cell = mCurrentSudokuGame.getLastChangedCell();
        if (cell != null && !mSudokuBoard.isReadOnly())
            mSudokuBoard.moveCellSelectionTo(cell.getRow(), cell.getColumn());
        else
            mSudokuBoard.moveCellSelectionTo(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSudokuBoard.setHighlightWrongVals(true);
        mSudokuBoard.setHighlightTouchedCell(true);

        boolean highlightSimilarCells = true;
        if (highlightSimilarCells) {
            mSudokuBoard.setHighlightSimilarCell(SudokuBoard.HighlightMode.NUMBERS);
        } else {
            mSudokuBoard.setHighlightSimilarCell(SudokuBoard.HighlightMode.NONE);
        }

        mPopupNumpad.setEnabled(true);
        mNumpad.setEnabled(true);
        mNumpad.setMoveCellSelectionOnPress(false);
        mPopupNumpad.setHighlightCompletedValues(true);
        mPopupNumpad.setShowNumberTotals(false);
        mNumpad.setHighlightCompletedValues(true);
        mNumpad.setShowNumberTotals(false);

        if (!mSudokuBoard.isReadOnly()) {
            mSudokuBoard.invokeOnCellSelected();
        }

        if(mShowTime)
            mGameTimer.start();

        updateTime();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mGameTimer.stop();
        mControl.pause();
        mControlState.saveState(mControl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDB.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mGameTimer.stop();

        mGameTimer.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_RESTART, 0, "Restart")
                .setIcon(R.drawable.ic_restart_icon);

        menu.add(0, MENU_ITEM_DIFFICULTY, 1, "Change Difficulty")
                .setIcon(R.drawable.ic_difficulty_icon);

        menu.add(0, MENU_ITEM_SHOWTIME, 2, "On/Off Time Counting")
                .setIcon(R.drawable.ic_time_icon);

        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, SudokuActivity.class), null, intent, 0, null);

        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(MENU_ITEM_RESTART).setEnabled(true);
        menu.findItem(MENU_ITEM_DIFFICULTY).setEnabled(true);
        menu.findItem(MENU_ITEM_SHOWTIME).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_RESTART:
                restartDialog.show();
                return true;
            case MENU_ITEM_DIFFICULTY:
                DifficultyChoices choice = new DifficultyChoices(this);
                choice.show();
                choice.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        int selection = choice.getChoice();
                        if(selection >= 0 && selection < 4) {
                            mLevel = selection;
                            mSudokuList = mDB.getSudokuId(mLevel);
                            restartDialog.show();
                        }
                    }
                });
                return true;
            case MENU_ITEM_SHOWTIME:
                restartTimeModeDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SETTINGS:
                restartActivity();
                break;
        }
    }

    private void restartActivity() {
        startActivity(getIntent());
        finish();
    }

    private OnPuzzleSolvedListener onSolvedListener = new OnPuzzleSolvedListener() {
        @Override
        public void onPuzzleSolved() {
            if (mShowTime) {
                mGameTimer.stop();
                if(mCurrentSudokuGame.getLevelId() == 1 && mGameTimer.getTotalTime() < 2 * 60 * 1000)
                    mScore += 15;
                else if(mCurrentSudokuGame.getLevelId() == 1 && mGameTimer.getTotalTime() >= 2 * 60 * 1000 &&
                        mGameTimer.getTotalTime() < 8 * 60 * 1000)
                    mScore += 12;
                else if(mCurrentSudokuGame.getLevelId() == 2 && mGameTimer.getTotalTime() < 5 * 60 * 1000)
                    mScore += 25;
                else if(mCurrentSudokuGame.getLevelId() == 2 && mGameTimer.getTotalTime() >= 5 * 60 * 1000 &&
                        mGameTimer.getTotalTime() < 12 * 60 * 1000)
                    mScore += 22;
                else if(mCurrentSudokuGame.getLevelId() == 3 && mGameTimer.getTotalTime() < 8 * 60 * 1000)
                    mScore += 45;
                else if(mCurrentSudokuGame.getLevelId() == 3 && mGameTimer.getTotalTime() >= 8 * 60 * 1000 &&
                        mGameTimer.getTotalTime() < 20 * 60 * 1000)
                    mScore += 42;

                mGameTimer.reset();
            }
            else
            {
                if(mCurrentSudokuGame.getLevelId() == 1)
                    mScore += 10;
                else if(mCurrentSudokuGame.getLevelId() == 2)
                    mScore += 20;
                else mScore += 40;
            }

            mNumOfHints = mControl.getNumOfHints();
            mCurrentSudokuIndex++;
            loadGame(mCurrentSudokuIndex);
        }
    };

    public interface OnSelectedNumberChangedListener {
        void onSelectedNumberChanged(int number);
    }

    private OnSelectedNumberChangedListener onSelectedNumberChangedListener = new OnSelectedNumberChangedListener() {
        @Override
        public void onSelectedNumberChanged(int number) {
            if (number != 0) {
                Cell cell = mCurrentSudokuGame.getCells().findCellByValue(number);
                mSudokuBoard.setHighlightedValue(number);
                if (cell != null) {
                    mSudokuBoard.moveCellSelectionTo(cell.getRow(), cell.getColumn());
                }
            } else {
                mSudokuBoard.clearCellSelection();
            }
        }
    };

    void updateTime() {
        if (mShowTime) {
            setTitle(mFormat.formatTime(mCurrentSudokuGame.getTime()));
        } else {
            setTitle("Sudoku");
        }

    }

    private final class GameTimer extends Timer {

        GameTimer() {
            super(1000);
        }

        @Override
        protected boolean step(int count, long time) {
            updateTime();

            return false;
        }
    }
}
