package com.example.project.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.project.R;
import com.example.project.game.Cell;
import com.example.project.game.CellCollection.OnChangeListener;
import com.example.project.game.SudokuGame;
import com.example.project.UI.InputControlState.StateBundle;

import java.util.HashMap;
import java.util.Map;

public class NumpadMethod extends InputMethod {
    private boolean moveCellSelectionOnPress = true;
    private boolean mHighlightCompletedValues = true;
    private boolean mShowNumberTotals = false;
    private Cell mSelectedCell;
    private Map<Integer, Button> mNumberBtns;

    public boolean isMoveCellSelectionOnPress() {
        return moveCellSelectionOnPress;
    }

    public void setMoveCellSelectionOnPress(boolean moveCellSelectionOnPress) {
        this.moveCellSelectionOnPress = moveCellSelectionOnPress;
    }

    public boolean getHighlightCompletedValues() {
        return mHighlightCompletedValues;
    }

    public void setHighlightCompletedValues(boolean highlightCompletedValues) {
        mHighlightCompletedValues = highlightCompletedValues;
    }

    public boolean getShowNumberTotals() {
        return mShowNumberTotals;
    }

    public void setShowNumberTotals(boolean showNumberTotals) {
        mShowNumberTotals = showNumberTotals;
    }

    @Override
    protected void init(Context context, InputControl control, SudokuGame game, SudokuBoard board) {
        super.init(context, control, game, board);

        game.getCells().addOnChangeListener(mOnCellsChangeListener);
    }

    @Override
    protected View createView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null);
        View numpad = view.findViewById(R.id.numpadLayout);

        mNumberBtns = new HashMap<>();
        mNumberBtns.put(1, numpad.findViewById(R.id.num1Btn));
        mNumberBtns.put(2, numpad.findViewById(R.id.num2Btn));
        mNumberBtns.put(3, numpad.findViewById(R.id.num3Btn));
        mNumberBtns.put(4, numpad.findViewById(R.id.num4Btn));
        mNumberBtns.put(5, numpad.findViewById(R.id.num5Btn));
        mNumberBtns.put(6, numpad.findViewById(R.id.num6Btn));
        mNumberBtns.put(7, numpad.findViewById(R.id.num7Btn));
        mNumberBtns.put(8, numpad.findViewById(R.id.num8Btn));
        mNumberBtns.put(9, numpad.findViewById(R.id.num9Btn));

        for (Integer num : mNumberBtns.keySet()) {
            Button btn = mNumberBtns.get(num);
            btn.setTag(num);
            btn.setOnClickListener(mNumberButtonClick);
        }

        return numpad;
    }

    @Override
    protected void onActivated() {
        onCellSelected(mBoard.isReadOnly() ? null : mBoard.getSelectedCell());
    }

    @Override
    protected void onCellSelected(Cell cell) {
        if (cell != null) {
            mBoard.setHighlightedValue(cell.getValue());
        } else {
            mBoard.setHighlightedValue(0);
        }

        mSelectedCell = cell;
        update();
    }

    private OnClickListener mNumberButtonClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int selectNum = (Integer) view.getTag();
            Cell selectCell = mSelectedCell;

            if (selectCell != null) {
                if (selectNum >= 1 && selectNum <= 9) {
                    mGame.setCellValue(selectCell, selectNum);
                    mBoard.setHighlightedValue(selectNum);
                    if (isMoveCellSelectionOnPress()) {
                        mBoard.moveCellSelectionRight();
                    }
                }
            }
        }

    };

    private OnChangeListener mOnCellsChangeListener = () -> {
        if (mActive) {
            update();
        }
    };

    private void update() {
        Map<Integer, Integer> countValuesUsed = null;
        if (mHighlightCompletedValues || mShowNumberTotals)
            countValuesUsed = mGame.getCells().countValuesUsed();

        if (mShowNumberTotals) {
            for (Map.Entry<Integer, Integer> entry : countValuesUsed.entrySet()) {
                Button btn = mNumberBtns.get(entry.getKey());
                btn.setText(entry.getKey() + " (" + entry.getValue() + ")");
            }
        }
    }

    @Override
    protected void onSaveState(StateBundle outState) { }

    @Override
    protected void onRestoreState(StateBundle savedState) {
        if (haveInputMethodView()) {
            update();
        }
    }
}
