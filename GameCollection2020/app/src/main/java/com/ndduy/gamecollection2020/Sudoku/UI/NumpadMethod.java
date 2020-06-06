package com.ndduy.gamecollection2020.Sudoku.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ndduy.gamecollection2020.R;
import com.ndduy.gamecollection2020.Sudoku.game.Cell;
import com.ndduy.gamecollection2020.Sudoku.game.CellCollection.OnChangeListener;
import com.ndduy.gamecollection2020.Sudoku.game.SudokuGame;
import com.ndduy.gamecollection2020.Sudoku.UI.InputControlState.StateBundle;

import java.util.HashMap;
import java.util.Map;

public class NumpadMethod extends InputMethod {

    private boolean moveCellSelectionOnPress = true;
    private boolean mHighlightCompletedValues = true;
    private boolean mShowNumberTotals = false;

    private Cell mSelectedCell;

    private Map<Integer, Button> mNumberButtons;

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
        View controlPanel = inflater.inflate(R.layout.numpad_layout, null);

        mNumberButtons = new HashMap<>();
        mNumberButtons.put(1, (Button) controlPanel.findViewById(R.id.btn1));
        mNumberButtons.put(2, (Button) controlPanel.findViewById(R.id.btn2));
        mNumberButtons.put(3, (Button) controlPanel.findViewById(R.id.btn3));
        mNumberButtons.put(4, (Button) controlPanel.findViewById(R.id.btn4));
        mNumberButtons.put(5, (Button) controlPanel.findViewById(R.id.btn5));
        mNumberButtons.put(6, (Button) controlPanel.findViewById(R.id.btn6));
        mNumberButtons.put(7, (Button) controlPanel.findViewById(R.id.btn7));
        mNumberButtons.put(8, (Button) controlPanel.findViewById(R.id.btn8));
        mNumberButtons.put(9, (Button) controlPanel.findViewById(R.id.btn9));

        for (Integer num : mNumberButtons.keySet()) {
            Button b = mNumberButtons.get(num);
            b.setTag(num);
            b.setOnClickListener(mNumberButtonClick);
        }

        return controlPanel;

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
        public void onClick(View v) {
            int selNumber = (Integer) v.getTag();
            Cell selCell = mSelectedCell;

            if (selCell != null) {
                if (selNumber >= 0 && selNumber <= 9) {
                    mGame.setCellValue(selCell, selNumber);
                    mBoard.setHighlightedValue(selNumber);
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
        Map<Integer, Integer> valuesUseCount = null;
        if (mHighlightCompletedValues || mShowNumberTotals)
            valuesUseCount = mGame.getCells().countValuesUsed();

        if (mShowNumberTotals) {
            for (Map.Entry<Integer, Integer> entry : valuesUseCount.entrySet()) {
                Button b = mNumberButtons.get(entry.getKey());
                b.setText(entry.getKey() + " (" + entry.getValue() + ")");
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
