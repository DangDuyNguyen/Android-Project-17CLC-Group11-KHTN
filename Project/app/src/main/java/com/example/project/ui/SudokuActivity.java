package com.example.project.UI;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.DB.SudokuDB;
import com.example.project.game.Cell;
import com.example.project.game.SudokuGame;
import com.example.project.game.SudokuGame.OnPuzzleSolvedListener;

import java.util.ArrayList;

public class SudokuActivity extends Activity {
    private ArrayList<Long> mSudokuList;
    private int mCurrentSudokuIndex = -1;
    private SudokuGame mCurrentSudokuGame;

    private SudokuDB mDB;

    private Handler mHandler;

    private ViewGroup mLayout;
    private SudokuBoard mSudokuBoard;
    private TextView mLevelInfo;
    private TextView mScoreInfo;
    private TextView mTimeInfo;
    private Spinner mMenuSetting;

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
        mTimeInfo = findViewById(R.id.timeInfo);
        mMenuSetting = findViewById(R.id.ingameSetting);

        mDB = new SudokuDB(getApplicationContext());

        mHandler = new Handler();

        mGameTimer = new GameTimer();

        mControl = new InputControl(this);
        mControl = findViewById(R.id.inputMethods);

        mNumOfHints = 3;

        mControlState = new InputControlState(this);

        if (savedInstanceState == null) {
            mSudokuList = mDB.getSudokuId(mLevel);
            if(!mSudokuList.isEmpty())
            {
                mCurrentSudokuIndex = 0;
                loadGame(mCurrentSudokuIndex);
            }
        } else {
            mCurrentSudokuGame = new SudokuGame();
            mCurrentSudokuGame.restoreState(savedInstanceState);
            mGameTimer.restoreState(savedInstanceState);
        }

        restartDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_restart_icon)
                .setTitle(R.string.game_name)
                .setMessage("Are you sure you want to restart this game?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    mSudokuList = mDB.getSudokuId(mLevel);
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
                .setTitle(R.string.game_name)
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

        String[] menuItems = new String[]{"Settings", "Restart", "Change Difficulty", "On/Off Time Counting"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,menuItems) {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMenuSetting.setAdapter(adapter);
        mMenuSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        restartDialog.show();
                        break;
                    case 2:
                        CharSequence[] arr = new CharSequence[] {"Endless Mode","Easy Mode", "Medium Mode", "Hard Mode"};
                        AlertDialog choice = new AlertDialog.Builder(SudokuActivity.this)
                                .setTitle("Select difficulty:")
                                .setSingleChoiceItems(arr, -1, null)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichBtn) {
                                        dialog.dismiss();
                                        int selection = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                        if(selection >= 0 && selection < 4) {
                                            mLevel = selection;
                                            restartDialog.show();
                                        }
                                    }
                                })
                                .create();
                        choice.show();
                        break;
                    case 3:
                        restartTimeModeDialog.show();
                        break;
                }
                mMenuSetting.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        mScoreInfo.setText("" + mScore);

        if(!mShowTime)
            mTimeInfo.setText("");

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

        mCurrentSudokuGame.resume();

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

        mCurrentSudokuGame.pause();
        mCurrentSudokuGame.saveState(outState);
        mGameTimer.saveState(outState);
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
            mTimeInfo.setText(mFormat.formatTime(mCurrentSudokuGame.getTime()));
        } else {
            mTimeInfo.setText("");
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
