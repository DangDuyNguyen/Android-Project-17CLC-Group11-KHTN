package com.example.project.game;

import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuGame {
    public static final int GAME_STATE_PLAYING = 0;
    public static final int GAME_STATE_NOT_STARTED = 1;
    public static final int GAME_STATE_COMPLETED = 2;

    private long mId;
    private long mLevelId;
    private int mState;
    private long mTime;
    private long mLastPlayTime;
    private CellCollection mCells;
    private SudokuSolver mSolver;
    private boolean mSolved = false;

    private OnPuzzleSolvedListener mOnPuzzleSolvedListener;
    private ActionStack mStack;

    private long mStartTime = -1;

    public static SudokuGame createEmptyGame() {
        SudokuGame game = new SudokuGame();
        game.setCells(CellCollection.initBoardEmpty());
        return game;
    }

    public SudokuGame() {
        mTime = 0;
        mLastPlayTime = 0;
        mLevelId = 0;
        mState = GAME_STATE_NOT_STARTED;
    }

    public void setOnPuzzleSolvedListener(OnPuzzleSolvedListener l) {
        mOnPuzzleSolvedListener = l;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public void setLevelId(long lvId) {
        mLevelId = lvId;
    }

    public long getLevelId() {
        return mLevelId;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public long getTime() {
        if (mStartTime != -1) {
            return mTime + SystemClock.uptimeMillis() - mStartTime;
        } else {
            return mTime;
        }
    }

    public void setLastPlayTime(long lastPlayTime) {
        mLastPlayTime = lastPlayTime;
    }

    public long getLastPlayTime() {
        return mLastPlayTime;
    }

    public void setCells(CellCollection cells) {
        mCells = cells;
        validate();
        mStack = new ActionStack(mCells);
    }

    public CellCollection getCells() {
        return mCells;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setActionStack(ActionStack actionStack) {
        mStack = actionStack;
    }

    public ActionStack getActionStack() {
        return mStack;
    }

    public void setCellValue(Cell cell, int value) {
        if (cell == null)
            throw new IllegalArgumentException("Cell cannot be null.");
        if (value < 0 || value > 9)
            throw new IllegalArgumentException("Value must be between 0 - 9");

        if (cell.isEditable()) {
            executeCommand(new SetCellValueAction(cell, value));
            validate();
            if (isCompleted()) {
                finish();
                if (mOnPuzzleSolvedListener != null) {
                    mOnPuzzleSolvedListener.onPuzzleSolved();
                }
            }
        }
    }

    private void executeCommand(Action action) {
        mStack.execute(action);
    }

    public void undoAction() {
        mStack.undo();
    }

    public void undoPreviousState() {
        mStack.undoSolvableState();
    }

    @Nullable
    public Cell getLastChangedCell() {
        return mStack.getLastChangedCell();
    }

    public void start() {
        mState = GAME_STATE_PLAYING;
        resume();
    }

    public void resume() {
        mStartTime = SystemClock.uptimeMillis();
    }

    public void pause() {
        mTime += SystemClock.uptimeMillis() - mStartTime;
        mStartTime = -1;

        setLastPlayTime(System.currentTimeMillis());
    }

    public boolean isSolvable () {
        mSolver = new SudokuSolver();
        mSolver.setPuzzle(mCells);
        ArrayList<int[]> finalValues = mSolver.solve();
        return !finalValues.isEmpty();
    }

    public void solve() {
        mSolved = true;
        mSolver = new SudokuSolver();
        mSolver.setPuzzle(mCells);
        ArrayList<int[]> finalValues = mSolver.solve();
        for (int[] rowColVal : finalValues) {
            int row = rowColVal[0];
            int col = rowColVal[1];
            int val = rowColVal[2];
            Cell cell = mCells.getCell(row, col);
            this.setCellValue(cell, val);
        }
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void solveCell(Cell cell) {
        mSolver = new SudokuSolver();
        mSolver.setPuzzle(mCells);
        ArrayList<int[]> finalValues = mSolver.solve();

        int row = cell.getRow();
        int col = cell.getColumn();
        for (int[] rowColVal : finalValues) {
            if (rowColVal[0] == row && rowColVal[1] == col) {
                int val = rowColVal[2];
                this.setCellValue(cell, val);
            }
        }
    }

    private void finish() {
        pause();
        mState = GAME_STATE_COMPLETED;
    }

    public void reset() {
        for (int r = 0; r < CellCollection.SIZE; r++) {
            for (int c = 0; c < CellCollection.SIZE; c++) {
                Cell cell = mCells.getCell(r, c);
                if (cell.isEditable()) {
                    cell.setValue(0);
                }
            }
        }

        mStack = new ActionStack(mCells);
        validate();
        setTime(0);
        setLastPlayTime(0);
        mState = GAME_STATE_NOT_STARTED;
        mSolved = false;
    }

    public boolean isCompleted() {
        return mCells.isCompleted();
    }

    public void validate() {
        mCells.validate();
    }

    public interface OnPuzzleSolvedListener {
        void onPuzzleSolved();
    }
}
