package com.project.arcadedestroyer.Sudoku.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.project.arcadedestroyer.R;
import com.project.arcadedestroyer.Sudoku.game.Cell;
import com.project.arcadedestroyer.Sudoku.game.CellCollection.OnChangeListener;
import com.project.arcadedestroyer.Sudoku.game.SudokuGame;
import com.project.arcadedestroyer.Sudoku.UI.InputControlState.StateBundle;

import java.util.HashMap;
import java.util.Map;

public class NumpadMethod extends InputMethod {

    private boolean mHighlightCompletedValues = true;

    private Cell mSelectedCell;

    private Map<Integer, Button> mNumberButtons;


    public boolean getHighlightCompletedValues() {
        return mHighlightCompletedValues;
    }

    public void setHighlightCompletedValues(boolean highlightCompletedValues) {
        mHighlightCompletedValues = highlightCompletedValues;
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
                }
            }
        }
    };

    private OnChangeListener mOnCellsChangeListener = () -> {
        if (mActive) {

        }
    };

    @Override
    protected void onSaveState(StateBundle outState) { }

    @Override
    protected void onRestoreState(StateBundle savedState) {
        if (haveInputMethodView()) {

        }
    }
}
