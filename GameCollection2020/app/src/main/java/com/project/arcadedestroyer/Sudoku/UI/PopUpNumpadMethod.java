package com.project.arcadedestroyer.Sudoku.UI;

import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;

import com.project.arcadedestroyer.R;
import com.project.arcadedestroyer.Sudoku.game.Cell;
import com.project.arcadedestroyer.Sudoku.UI.PopUpNumpad.OnNumberEditListener;

import java.util.Map;

public class PopUpNumpadMethod extends InputMethod {
    private boolean mHighlightCompletedValues = true;
    private boolean mShowNumberTotals = false;

    private PopUpNumpad mNumpad;
    private Cell mSelectedCell;

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

    private void ensureEditCellDialog() {
        if (mNumpad == null) {
            mNumpad = new PopUpNumpad(mContext);
            mNumpad.setOnNumberEditListener(mOnNumberEditListener);
            mNumpad.setOnDismissListener(mOnPopupDismissedListener);
        }
    }

    @Override
    protected void onActivated() {
        mBoard.setAutoHideTouchedCellHint(false);
    }

    @Override
    protected void onDeactivated() {
        mBoard.setAutoHideTouchedCellHint(true);
    }

    @Override
    protected void onCellTapped(Cell cell) {
        mSelectedCell = cell;
        if (cell.isEditable()) {
            ensureEditCellDialog();

            mNumpad.updateNumber(cell.getValue());

            Map<Integer, Integer> valuesUseCount = null;
            if (mHighlightCompletedValues || mShowNumberTotals)
                valuesUseCount = mGame.getCells().countValuesUsed();

            if (mShowNumberTotals) {
                for (Map.Entry<Integer, Integer> entry : valuesUseCount.entrySet()) {
                    mNumpad.setValueCount(entry.getKey(), entry.getValue());
                }
            }
            mNumpad.show();
        } else {
            mBoard.hideTouchedCellHint();
        }
    }

    @Override
    protected void onCellSelected(Cell cell) {
        super.onCellSelected(cell);

        if (cell != null) {
            mBoard.setHighlightedValue(cell.getValue());
        } else {
            mBoard.setHighlightedValue(0);
        }
    }

    @Override
    protected void onPause() {
        if (mNumpad != null) {
            mNumpad.cancel();
        }
    }

    @Override
    protected View createView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.pop_up_numpad_layout, null);
    }

    private OnNumberEditListener mOnNumberEditListener = new OnNumberEditListener() {
        @Override
        public boolean onNumberEdit(int number) {
            if (number != -1 && mSelectedCell != null) {
                mGame.setCellValue(mSelectedCell, number);
                mBoard.setHighlightedValue(number);
            }
            return true;
        }
    };

    private OnDismissListener mOnPopupDismissedListener = dialog -> mBoard.hideTouchedCellHint();
}
