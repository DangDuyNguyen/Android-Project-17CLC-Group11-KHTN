package com.example.project.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
    private Button mUndoBtn;
    private Button mResetBtn;
    private Button mHintBtn;
    private Button mInputMethodBtn;

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
    private int numOfHints;

    public static final int MENU_ITEM_RESTART = Menu.FIRST;
    public static final int MENU_ITEM_DIFFICULTY = Menu.FIRST + 1;
    public static final int MENU_ITEM_SHOWTIME = Menu.FIRST + 2;

    private static AlertDialog restartDialog;
    private static AlertDialog puzzleCannotSolveDialog;
    private static AlertDialog cannotGiveHintDialog;
    private static AlertDialog noHintLeftDialog;

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

        mUndoBtn = findViewById(R.id.undoBtn);
        mResetBtn = findViewById(R.id.resetBtn);
        mHintBtn = findViewById(R.id.hintBtn);
        mInputMethodBtn = findViewById(R.id.switchInputTypeBtn);

        View numpad = findViewById(R.id.numpadLayout);
        numpad.setVisibility(View.GONE);

        numOfHints = 3;

        puzzleCannotSolveDialog = new AlertDialog.Builder(SudokuActivity.this)
                .setTitle(R.string.app_name)
                .setMessage("This sudoku cannot be solved")
                .setPositiveButton(android.R.string.ok, null)
                .create();

        cannotGiveHintDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Please select a cell to get hint!")
                .setPositiveButton(android.R.string.ok, null)
                .create();

        noHintLeftDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("You have no hint left!")
                .setPositiveButton(android.R.string.ok, null)
                .create();

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

        mUndoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSudokuGame.isUndoable()) {
                    mCurrentSudokuGame.undoAction();
                    selectLastChangedCell();
                }
            }
        });

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGame(mCurrentSudokuIndex);
            }
        });

        mHintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numOfHints > 0)
                {
                    Cell cell = mSudokuBoard.getSelectedCell();
                    if (cell != null && cell.isEditable()) {
                        if (mCurrentSudokuGame.isSolvable()) {
                            mCurrentSudokuGame.solveCell(cell);
                        }
                        else {
                            puzzleCannotSolveDialog.show();
                        }
                    }
                    else {
                        cannotGiveHintDialog.show();
                    }
                }
                else noHintLeftDialog.show();
            }
        });

        mInputMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mControl.activateNextInputMethod();
                View numpad = findViewById(R.id.numpadLayout);
                if(numpad.getVisibility() == View.VISIBLE)
                    numpad.setVisibility(View.GONE);
                else numpad.setVisibility(View.VISIBLE);
            }
        });

        mDB = new SudokuDB(getApplicationContext());

        mHandler = new Handler();

        mGameTimer = new GameTimer();

        mSudokuList = mDB.getSudokuId(mLevel);
        if(!mSudokuList.isEmpty())
        {
            mCurrentSudokuIndex = 0;
            loadGame(mCurrentSudokuIndex);
        }

        mControl = findViewById(R.id.numpadLayout);
        mControl.init(mSudokuBoard, mCurrentSudokuGame);

        mControlState = new InputControlState(this);

        mPopupNumpad = mControl.getInputMethod(InputControl.INPUT_METHOD_POPUP);
        mNumpad = mControl.getInputMethod(InputControl.INPUT_METHOD_NUMPAD);
    }

    public void loadGame(int index)
    {
        index = (index < mSudokuList.size()) ? index : 0;
        mCurrentSudokuGame = mDB.getSudoku(mSudokuList.get(index).longValue());
        mCurrentSudokuGame.start();

        mSudokuBoard.setGame(mCurrentSudokuGame);
        mCurrentSudokuGame.setOnPuzzleSolvedListener(onSolvedListener);

        long levelId = mCurrentSudokuGame.getLevelId();
        if(levelId == 1)
            mLevelInfo.setText("Easy");
        else if (levelId == 2)
            mLevelInfo.setText("Medium");
        else mLevelInfo.setText("Hard");

        mScoreInfo.setText("Score: " + mScore);

        Cell cell = mCurrentSudokuGame.getLastChangedCell();
        if (cell != null && !mSudokuBoard.isReadOnly())
            mSudokuBoard.moveSelectionTo(cell.getRow(), cell.getColumn());
        else
            mSudokuBoard.moveSelectionTo(0, 0);
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

        mTimeInfo.setVisibility(mShowTime ? View.VISIBLE : View.GONE);

        mPopupNumpad.setEnabled(true);
        mNumpad.setEnabled(false);
        mNumpad.setMoveCellSelectionOnPress(false);
        mPopupNumpad.setHighlightCompletedValues(true);
        mPopupNumpad.setShowNumberTotals(false);
        mNumpad.setHighlightCompletedValues(true);
        mNumpad.setShowNumberTotals(false);

        mControl.activate1stInputMethod();
        mControlState.restoreState(mControl);

        if (!mSudokuBoard.isReadOnly()) {
            mSudokuBoard.invokeOnSelectedCell();
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

        if (mCurrentSudokuGame.getState() == SudokuGame.GAME_STATE_PLAYING) {
            mCurrentSudokuGame.pause();
        }

        mGameTimer.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

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
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(MENU_ITEM_RESTART).setEnabled(true);
        menu.findItem(MENU_ITEM_DIFFICULTY).setEnabled(true);
        menu.findItem(MENU_ITEM_SHOWTIME).setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_RESTART:
                restartDialog.show();
                return true;
            case MENU_ITEM_DIFFICULTY:
                DifficultyChoices choice = new DifficultyChoices(this);
                mLevel = choice.getChoice();
                mSudokuList = mDB.getSudokuId(mLevel);
                restartDialog.show();
                return true;
            case MENU_ITEM_SHOWTIME:
                if(mShowTime) mShowTime = false;
                else mShowTime = true;
                restartDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private void selectLastChangedCell() {
        Cell cell = mCurrentSudokuGame.getLastChangedCell();
        if (cell != null)
            mSudokuBoard.moveSelectionTo(cell.getRow(), cell.getColumn());
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
                    mSudokuBoard.moveSelectionTo(cell.getRow(), cell.getColumn());
                }
            } else {
                mSudokuBoard.clearCellSelection();
            }
        }
    };

    void updateTime() {
        if (mShowTime) {
            setTitle(mFormat.formatTime(mCurrentSudokuGame.getTime()));
            mTimeInfo.setText(mFormat.formatTime(mCurrentSudokuGame.getTime()));
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
