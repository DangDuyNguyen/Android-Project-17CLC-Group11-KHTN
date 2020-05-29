package com.example.project.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.project.R;
import com.example.project.game.Cell;
import com.example.project.game.SudokuGame;
import com.example.project.UI.SudokuBoard;
import com.example.project.UI.SudokuBoard.OnCellSelectedListener;
import com.example.project.UI.SudokuBoard.OnCellTappedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputControl extends LinearLayout {
    public static final int INPUT_METHOD_POPUP = 0;
    public static final int INPUT_METHOD_NUMPAD = 1;

    private Context mContext;
    private SudokuBoard mBoard;
    private SudokuGame mGame;
    private int mNumOfHints;

    private List<InputMethod> mInputMethods = new ArrayList<InputMethod>();
    private int mActiveMethodIndex = -1;

    public InputControl(Context context) {
        super(context);
        mContext = context;
    }

    public InputControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void init(SudokuBoard board, SudokuGame game, int numOfHints) {
        mBoard = board;
        mBoard.setOnCellTappedListener(mOnCellTapListener);
        mBoard.setOnCellSelectedListener(mOnCellSelected);

        mGame = game;
        mNumOfHints = numOfHints;

        createInputMethods();
    }

    public void activate1stInputMethod() {
        ensureInputMethods();
        if (mActiveMethodIndex == -1 || !mInputMethods.get(mActiveMethodIndex).isEnabled()) {
            activateInputMethod(0);
        }

    }

    public void activateInputMethod(int methodID) {
        if (methodID < -1 || methodID >= mInputMethods.size()) {
            throw new IllegalArgumentException(String.format("Invalid method id: %s.", methodID));
        }

        ensureInputMethods();

        if (mActiveMethodIndex != -1) {
            mInputMethods.get(mActiveMethodIndex).deactivate();
        }

        boolean idFound = false;
        int id = methodID;
        int numOfCycles = 0;

        if (id != -1) {
            while (numOfCycles <= mInputMethods.size()) {
                if (mInputMethods.get(id).isEnabled()) {
                    ensureControlPanel(id);
                    idFound = true;
                    break;
                }

                id++;
                if (id == mInputMethods.size()) {
                    id = 0;
                }
                numOfCycles++;
            }
        }

        if (!idFound) {
            id = -1;
        }

        for (int i = 0; i < mInputMethods.size(); i++) {
            InputMethod im = mInputMethods.get(i);
            if (im.haveInputMethodView()) {
                im.getInputMethodView().setVisibility(i == id ? View.VISIBLE : View.GONE);
            }
        }

        mActiveMethodIndex = id;
        if (mActiveMethodIndex != -1) {
            InputMethod activeMethod = mInputMethods.get(mActiveMethodIndex);
            activeMethod.activate();
        }
    }

    public void activateNextInputMethod() {
        ensureInputMethods();

        int id = mActiveMethodIndex + 1;
        if (id >= mInputMethods.size()) {
            id = 0;
        }
        activateInputMethod(id);
    }

    public <T extends InputMethod> T getInputMethod(int methodId) {
        ensureInputMethods();

        return (T) mInputMethods.get(methodId);
    }

    public List<InputMethod> getInputMethods() {
        return Collections.unmodifiableList(mInputMethods);
    }

    public int getActiveMethodIndex() {
        return mActiveMethodIndex;
    }

    public void pause() {
        for (InputMethod im : mInputMethods) {
            im.pause();
        }
    }

    private void ensureInputMethods() {
        if (mInputMethods.size() == 0) {
            throw new IllegalStateException("Input methods are not created!");
        }

    }

    private void createInputMethods() {
        if (mInputMethods.size() == 0) {
            addInputMethod(INPUT_METHOD_POPUP, new PopUpNumpadMethod());
            addInputMethod(INPUT_METHOD_NUMPAD, new NumpadMethod());
        }
    }

    private void addInputMethod(int methodIndex, InputMethod method) {
        method.init(mContext, this, mGame, mBoard);
        mInputMethods.add(methodIndex, method);
    }

    private void ensureControlPanel(int methodID) {
        InputMethod method = mInputMethods.get(methodID);
        if (!method.haveInputMethodView()) {
            View controlPanel = method.getInputMethodView();

            Button switchModeButton = controlPanel.findViewById(R.id.switchInputBtn);
            switchModeButton.setOnClickListener(mSwitchModeListener);

            Button undoButton = controlPanel.findViewById(R.id.undoBtn);
            undoButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGame.undoAction();
                    selectLastChangedCell();
                }
            });

            Button resetButton = controlPanel.findViewById(R.id.resetBtn);
            resetButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGame.reset();
                }
            });

            AlertDialog puzzleCannotSolveDialog = new AlertDialog.Builder(mContext)
                    .setTitle(R.string.app_name)
                    .setMessage("This sudoku cannot be solved")
                    .setPositiveButton(android.R.string.ok, null)
                    .create();

            AlertDialog cannotGiveHintDialog = new AlertDialog.Builder(mContext)
                    .setTitle(R.string.app_name)
                    .setMessage("Please select a cell to get hint!")
                    .setPositiveButton(android.R.string.ok, null)
                    .create();

            AlertDialog noHintLeftDialog = new AlertDialog.Builder(mContext)
                    .setTitle(R.string.app_name)
                    .setMessage("You have no hint left!")
                    .setPositiveButton(android.R.string.ok, null)
                    .create();

            Button hintButton = controlPanel.findViewById(R.id.hintBtn);
            hintButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mNumOfHints > 0)
                    {
                        Cell cell = mBoard.getSelectedCell();
                        if (cell != null && cell.isEditable()) {
                            if (mGame.isSolvable()) {
                                mGame.solveCell(cell);
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
            this.addView(controlPanel, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
    }

    private void selectLastChangedCell() {
        Cell cell = mGame.getLastChangedCell();
        if (cell != null)
            mBoard.moveCellSelectionTo(cell.getRow(), cell.getColumn());
    }

    private OnCellTappedListener mOnCellTapListener = cell -> {
        if (mActiveMethodIndex != -1 && mInputMethods != null) {
            mInputMethods.get(mActiveMethodIndex).onCellTapped(cell);
        }
    };

    private OnCellSelectedListener mOnCellSelected = cell -> {
        if (mActiveMethodIndex != -1 && mInputMethods != null) {
            mInputMethods.get(mActiveMethodIndex).onCellSelected(cell);
        }
    };

    private OnClickListener mSwitchModeListener = v -> activateNextInputMethod();
}
