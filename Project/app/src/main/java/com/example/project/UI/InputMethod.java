package com.example.project.UI;

import android.content.Context;
import android.view.View;

import com.example.project.game.Cell;
import com.example.project.game.SudokuGame;
import com.example.project.UI.InputControlState.StateBundle;

public abstract class InputMethod {
    protected Context mContext;
    protected SudokuGame mGame;
    protected SudokuBoard mBoard;
    protected InputControl mControl;

    private String mInputMethodName;
    protected View mInputMethodView;

    protected boolean mActive = false;
    private boolean mEnabled = true;

    public InputMethod() { }

    protected void init(Context context, InputControl control, SudokuGame game, SudokuBoard board) {
        mContext = context;
        mControl = control;
        mGame = game;
        mBoard = board;
        mInputMethodName = this.getClass().getSimpleName();
    }

    public boolean haveInputMethodView() {
        return mInputMethodView != null;
    }

    public View getInputMethodView() {
        if (mInputMethodView == null) {
            mInputMethodView = createView();

            onViewCreated(mInputMethodView);
        }

        return mInputMethodView;
    }

    public void pause() {
        onPause();
    }

    protected void onPause() { }

    protected String getInputMethodName() {
        return mInputMethodName;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;

        if (!enabled) {
            mControl.activateNextInputMethod();
        }
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void activate() {
        mActive = true;
        onActivated();
    }

    public void deactivate() {
        mActive = false;
        onDeactivated();
    }

    protected abstract View createView();

    protected void onViewCreated(View view) { }

    protected void onActivated() { }

    protected void onDeactivated() { }

    protected void onCellSelected(Cell cell) { }

    protected void onCellTapped(Cell cell) { }

    protected void onSaveState(StateBundle outState) { }

    protected void onRestoreState(StateBundle savedState) { }
}
