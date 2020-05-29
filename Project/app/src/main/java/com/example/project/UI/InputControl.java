package com.example.project.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.project.R;
import com.example.project.game.SudokuGame;
import com.example.project.UI.SudokuBoard.OnCellSelectedListener;
import com.example.project.UI.SudokuBoard.OnCellTappedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputControl extends RelativeLayout {
    public static final int INPUT_METHOD_POPUP = 0;
    public static final int INPUT_METHOD_NUMPAD = 1;

    private Context mContext;
    private SudokuBoard mBoard;
    private SudokuGame mGame;

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

    public void init(SudokuBoard board, SudokuGame game) {
        mBoard = board;
        mBoard.setOnCellTappedListener(mOnCellTapListener);
        mBoard.setOnCellSelectedListener(mOnCellSelected);

        mGame = game;

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

        boolean idIsFound = false;
        int id = methodID;
        int numOfCycles = 0;

        if (id != -1) {
            while (numOfCycles <= mInputMethods.size()) {
                if (mInputMethods.get(id).isEnabled()) {
                    idIsFound = true;
                    break;
                }

                id++;
                if (id == mInputMethods.size()) {
                    id = 0;
                }
                numOfCycles++;
            }
        }

        if (!idIsFound) {
            id = -1;
        }

        for (int i = 0; i < mInputMethods.size(); i++) {
            InputMethod method = mInputMethods.get(i);
            if (method.haveInputMethodView()) {
                method.getInputMethodView().setVisibility(i == id ? View.VISIBLE : View.GONE);
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
        for (InputMethod method : mInputMethods) {
            method.pause();
        }
    }

    private void ensureInputMethods() {
        if (mInputMethods.size() == 0) {
            throw new IllegalStateException("Input methods are not created enough.");
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
}
