package com.project.arcadedestroyer.Sudoku.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.project.arcadedestroyer.R;
import com.project.arcadedestroyer.Sudoku.game.Cell;
import com.project.arcadedestroyer.Sudoku.game.CellCollection;
import com.project.arcadedestroyer.Sudoku.game.SudokuGame;

public class SudokuBoard extends View {
    public static final int DEFAULT_SIZE = 100;

    private static final int NONE_COLOR = 0;

    private float mCellWidth;
    private float mCellHeight;

    private Cell mTouchedCell;
    private Cell mSelectedCell;
    private int mHighlightedValue = 0;
    private boolean mReadonly = false;
    private boolean mHighlightWrongVals = true;
    private boolean mHighlightTouchedCell = true;
    private boolean mAutoHideTouchedCellHint = true;
    public enum HighlightMode {
        NONE,
        NUMBERS
    };
    private HighlightMode mHighlightSimilarCells = HighlightMode.NONE;

    private SudokuGame mGame;
    private CellCollection mCells;

    private OnCellTappedListener mOnCellTappedListener;
    private OnCellSelectedListener mOnCellSelectedListener;

    private Paint mLinePaint;
    private Paint mSectorLinePaint;
    private Paint mCellValuePaint;
    private Paint mCellValueReadonlyPaint;
    private int mNumberLeft;
    private int mNumberTop;
    private int mSectorLineWidth;
    private Paint mColorSecondary;
    private Paint mColorReadOnly;
    private Paint mColorTouched;
    private Paint mColorSelected;
    private Paint mColorHighlighted;

    private Paint mCellValueInvalidPaint;

    public SudokuBoard(Context context) {
        this(context, null);
    }

    public SudokuBoard(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);

        mLinePaint = new Paint();
        mSectorLinePaint = new Paint();
        mCellValuePaint = new Paint();
        mCellValueReadonlyPaint = new Paint();
        mCellValueInvalidPaint = new Paint();
        mColorSecondary = new Paint();
        mColorReadOnly = new Paint();
        mColorTouched = new Paint();
        mColorSelected = new Paint();
        mColorHighlighted = new Paint();

        mCellValuePaint.setAntiAlias(true);
        mCellValueReadonlyPaint.setAntiAlias(true);
        mCellValueInvalidPaint.setAntiAlias(true);
        mCellValueInvalidPaint.setColor(Color.RED);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SudokuBoard);

        setLineColor(a.getColor(R.styleable.SudokuBoard_lineColor, Color.BLACK));
        setSectorLineColor(a.getColor(R.styleable.SudokuBoard_lineColor, Color.BLACK));
        setTextColor(a.getColor(R.styleable.SudokuBoard_textColor, Color.BLACK));
        setTextColorReadOnly(a.getColor(R.styleable.SudokuBoard_textColorReadOnly, Color.BLACK));
        setBackgroundColor(a.getColor(R.styleable.SudokuBoard_color, Color.WHITE));
        setBackgroundColorSecondary(a.getColor(R.styleable.SudokuBoard_colorSecondary, NONE_COLOR));
        setBackgroundColorReadOnly(a.getColor(R.styleable.SudokuBoard_colorReadOnly, NONE_COLOR));
        setBackgroundColorTouched(a.getColor(R.styleable.SudokuBoard_colorTouched, Color.rgb(247, 197, 247)));
        setBackgroundColorSelected(a.getColor(R.styleable.SudokuBoard_colorSelected, Color.rgb(255, 192, 203)));
        setBackgroundColorHighlighted(a.getColor(R.styleable.SudokuBoard_colorHighlighted, Color.LTGRAY));

        a.recycle();
    }

    public int getLineColor() {
        return mLinePaint.getColor();
    }

    public void setLineColor(int color) {
        mLinePaint.setColor(color);
    }

    public int getSectorLineColor() {
        return mSectorLinePaint.getColor();
    }

    public void setSectorLineColor(int color) {
        mSectorLinePaint.setColor(color);
    }

    public int getTextColor() {
        return mCellValuePaint.getColor();
    }

    public void setTextColor(int color) {
        mCellValuePaint.setColor(color);
    }

    public int getTextColorReadOnly() {
        return mCellValueReadonlyPaint.getColor();
    }

    public void setTextColorReadOnly(int color) {
        mCellValueReadonlyPaint.setColor(color);
    }

    public int getBackgroundColorSecondary() {
        return mColorSecondary.getColor();
    }

    public void setBackgroundColorSecondary(int color) {
        mColorSecondary.setColor(color);
    }

    public int getBackgroundColorReadOnly() {
        return mColorReadOnly.getColor();
    }

    public void setBackgroundColorReadOnly(int color) {
        mColorReadOnly.setColor(color);
    }

    public int getBackgroundColorTouched() {
        return mColorTouched.getColor();
    }

    public void setBackgroundColorTouched(int color) {
        mColorTouched.setColor(color);
    }

    public int getBackgroundColorSelected() {
        return mColorSelected.getColor();
    }

    public void setBackgroundColorSelected(int color) {
        mColorSelected.setColor(color);
    }

    public int getBackgroundColorHighlighted() {
        return mColorHighlighted.getColor();
    }

    public void setBackgroundColorHighlighted(int color) {
        mColorHighlighted.setColor(color);
    }

    public void setGame(SudokuGame game) {
        mGame = game;
        setCells(game.getCells());
    }

    public void setCells(CellCollection cells) {
        mCells = cells;

        if (mCells != null) {
            if (!mReadonly) {
                mSelectedCell = mCells.getCell(0, 0);
                onCellSelected(mSelectedCell);
            }

            mCells.addOnChangeListener(this::postInvalidate);
        }

        postInvalidate();
    }

    public CellCollection getCells() {
        return mCells;
    }

    public Cell getSelectedCell() {
        return mSelectedCell;
    }

    public void setReadOnly(boolean readonly) {
        mReadonly = readonly;
        postInvalidate();
    }

    public boolean isReadOnly() {
        return mReadonly;
    }

    public void setHighlightWrongVals(boolean highlightWrongVals) {
        mHighlightWrongVals = highlightWrongVals;
        postInvalidate();
    }

    public boolean getHighlightWrongVals() {
        return mHighlightWrongVals;
    }

    public void setHighlightTouchedCell(boolean highlightTouchedCell) {
        mHighlightTouchedCell = highlightTouchedCell;
    }

    public boolean getHighlightTouchedCell() {
        return mHighlightTouchedCell;
    }

    public void setAutoHideTouchedCellHint(boolean autoHideTouchedCellHint) {
        mAutoHideTouchedCellHint = autoHideTouchedCellHint;
    }

    public boolean getAutoHideTouchedCellHint() {
        return mAutoHideTouchedCellHint;
    }

    public void setHighlightSimilarCell(HighlightMode highlightSimilarCell) {
        mHighlightSimilarCells = highlightSimilarCell;
    }

    public void setHighlightedValue(int value) {
        mHighlightedValue = value;
    }

    public int getHighlightedValue() {
        return mHighlightedValue;
    }

    public void setOnCellTappedListener(OnCellTappedListener listener) {
        mOnCellTappedListener = listener;
    }

    protected void onCellTapped(Cell cell) {
        if (mOnCellTappedListener != null) {
            mOnCellTappedListener.onCellTapped(cell);
        }
    }

    public void setOnCellSelectedListener(OnCellSelectedListener listener) {
        mOnCellSelectedListener = listener;
    }

    public void hideTouchedCellHint() {
        mTouchedCell = null;
        postInvalidate();
    }

    protected void onCellSelected(Cell cell) {
        if (mOnCellSelectedListener != null) {
            mOnCellSelectedListener.onCellSelected(cell);
        }
    }

    public void invokeOnCellSelected() {
        onCellSelected(mSelectedCell);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = DEFAULT_SIZE;
            if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
                width = widthSize;
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = DEFAULT_SIZE;
            if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
                height = heightSize;
            }
        }

        if (widthMode != MeasureSpec.EXACTLY) {
            width = height;
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = width;
        }

        if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
            height = heightSize;
        }

        mCellWidth = (width - getPaddingLeft() - getPaddingRight()) / 9.0f;
        mCellHeight = (height - getPaddingTop() - getPaddingBottom()) / 9.0f;

        setMeasuredDimension(width, height);

        float cellTextSize = mCellHeight * 0.75f;
        mCellValuePaint.setTextSize(cellTextSize);
        mCellValueReadonlyPaint.setTextSize(cellTextSize);
        mCellValueInvalidPaint.setTextSize(cellTextSize);

        mNumberLeft = (int) ((mCellWidth - mCellValuePaint.measureText("9")) / 2);
        mNumberTop = (int) ((mCellHeight - mCellValuePaint.getTextSize()) / 2);

        computeSectorLineWidth(width, height);
    }

    private void computeSectorLineWidth(int widthInPx, int heightInPx) {
        int sizeInPx = widthInPx < heightInPx ? widthInPx : heightInPx;
        float dipScale = getContext().getResources().getDisplayMetrics().density;
        float sizeInDip = sizeInPx / dipScale;

        float sectorLineWidthInDip = 2.0f;

        if (sizeInDip > 150) {
            sectorLineWidthInDip = 3.0f;
        }

        mSectorLineWidth = (int) (sectorLineWidthInDip * dipScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() - getPaddingRight();
        int height = getHeight() - getPaddingBottom();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mColorSecondary.getColor() != NONE_COLOR) {
            canvas.drawRect(3 * mCellWidth, 0, 6 * mCellWidth, 3 * mCellWidth, mColorSecondary);
            canvas.drawRect(0, 3 * mCellWidth, 3 * mCellWidth, 6 * mCellWidth, mColorSecondary);
            canvas.drawRect(6 * mCellWidth, 3 * mCellWidth, 9 * mCellWidth, 6 * mCellWidth, mColorSecondary);
            canvas.drawRect(3 * mCellWidth, 6 * mCellWidth, 6 * mCellWidth, 9 * mCellWidth, mColorSecondary);
        }

        int cellLeft, cellTop;
        if (mCells != null) {

            boolean hasBackgroundColorReadOnly = mColorReadOnly.getColor() != NONE_COLOR;

            float numberAscent = mCellValuePaint.ascent();

            for (int row = 0; row < CellCollection.SIZE; row++) {
                for (int col = 0; col < CellCollection.SIZE; col++) {
                    Cell cell = mCells.getCell(row, col);

                    cellLeft = Math.round((col * mCellWidth) + paddingLeft);
                    cellTop = Math.round((row * mCellHeight) + paddingTop);

                    if (!cell.isEditable() && hasBackgroundColorReadOnly &&
                            (mSelectedCell == null || mSelectedCell != cell)) {
                        if (mColorReadOnly.getColor() != NONE_COLOR) {
                            canvas.drawRect(
                                    cellLeft, cellTop,
                                    cellLeft + mCellWidth, cellTop + mCellHeight,
                                    mColorReadOnly);
                        }
                    }

                    boolean cellIsNotAlreadySelected = (mSelectedCell == null || mSelectedCell != cell);
                    boolean highlightedValueIsValid = mHighlightedValue != 0;
                    boolean shouldHighlightCell = false;

                    switch (mHighlightSimilarCells) {
                        default:
                        case NONE: {
                            shouldHighlightCell = false;
                            break;
                        }

                        case NUMBERS: {
                            shouldHighlightCell =
                                    cellIsNotAlreadySelected &&
                                            highlightedValueIsValid &&
                                            mHighlightedValue == cell.getValue();
                            break;
                        }
                    }

                    if (shouldHighlightCell) {
                        if (mColorHighlighted.getColor() != NONE_COLOR) {
                            canvas.drawRect(cellLeft, cellTop, cellLeft + mCellWidth, cellTop + mCellHeight, mColorHighlighted);
                        }
                    }
                }
            }

            if (!mReadonly && mSelectedCell != null) {
                cellLeft = Math.round(mSelectedCell.getColumn() * mCellWidth) + paddingLeft;
                cellTop = Math.round(mSelectedCell.getRow() * mCellHeight) + paddingTop;
                canvas.drawRect(cellLeft, cellTop, cellLeft + mCellWidth, cellTop + mCellHeight, mColorSelected);
            }

            if (mHighlightTouchedCell && mTouchedCell != null) {
                cellLeft = Math.round(mTouchedCell.getColumn() * mCellWidth) + paddingLeft;
                cellTop = Math.round(mTouchedCell.getRow() * mCellHeight) + paddingTop;
                canvas.drawRect(cellLeft, paddingTop, cellLeft + mCellWidth, height, mColorTouched);
                canvas.drawRect(paddingLeft, cellTop, width, cellTop + mCellHeight, mColorTouched);
            }

            for (int row = 0; row < CellCollection.SIZE; row++) {
                for (int col = 0; col < CellCollection.SIZE; col++) {
                    Cell cell = mCells.getCell(row, col);

                    cellLeft = Math.round((col * mCellWidth) + paddingLeft);
                    cellTop = Math.round((row * mCellHeight) + paddingTop);

                    int value = cell.getValue();
                    if (value != 0) {
                        Paint cellValuePaint = cell.isEditable() ? mCellValuePaint : mCellValueReadonlyPaint;

                        if (mHighlightWrongVals && !cell.isValid()) {
                            cellValuePaint = mCellValueInvalidPaint;
                        }

                        canvas.drawText(Integer.toString(value), cellLeft + mNumberLeft, cellTop + mNumberTop - numberAscent, cellValuePaint);
                    }
                }
            }
        }

        for (int c = 0; c <= CellCollection.SIZE; c++) {
            float x = (c * mCellWidth) + paddingLeft;
            canvas.drawLine(x, paddingTop, x, height, mLinePaint);
        }

        for (int r = 0; r <= CellCollection.SIZE; r++) {
            float y = r * mCellHeight + paddingTop;
            canvas.drawLine(paddingLeft, y, width, y, mLinePaint);
        }

        int sectorLineWidth1 = mSectorLineWidth / 2;
        int sectorLineWidth2 = sectorLineWidth1 + (mSectorLineWidth % 2);

        for (int c = 0; c <= CellCollection.SIZE; c = c + 3) {
            float x = (c * mCellWidth) + paddingLeft;
            canvas.drawRect(x - sectorLineWidth1, paddingTop, x + sectorLineWidth2, height, mSectorLinePaint);
        }

        for (int r = 0; r <= CellCollection.SIZE; r = r + 3) {
            float y = r * mCellHeight + paddingTop;
            canvas.drawRect(paddingLeft, y - sectorLineWidth1, width, y + sectorLineWidth2, mSectorLinePaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mReadonly) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    mTouchedCell = getCellAtPoint(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mSelectedCell = getCellAtPoint(x, y);
                    invalidate();

                    if (mSelectedCell != null) {
                        onCellTapped(mSelectedCell);
                        onCellSelected(mSelectedCell);
                    }

                    if (mAutoHideTouchedCellHint) {
                        mTouchedCell = null;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mTouchedCell = null;
                    break;
            }
            postInvalidate();
        }

        return !mReadonly;
    }

    public void moveCellSelectionRight() {
        if (!moveCellSelection(1, 0)) {
            int selRow = mSelectedCell.getRow();
            selRow++;
            if (!moveCellSelectionTo(selRow, 0)) {
                moveCellSelectionTo(0, 0);
            }
        }
        postInvalidate();
    }

    private void setCellValue(Cell cell, int value) {
        if (cell.isEditable()) {
            if (mGame != null) {
                mGame.setCellValue(cell, value);
            } else {
                cell.setValue(value);
            }
        }
    }

    private boolean moveCellSelection(int vx, int vy) {
        int newRow = 0;
        int newCol = 0;

        if (mSelectedCell != null) {
            newRow = mSelectedCell.getRow() + vy;
            newCol = mSelectedCell.getColumn() + vx;
        }

        return moveCellSelectionTo(newRow, newCol);
    }

    public boolean moveCellSelectionTo(int row, int col) {
        if (col >= 0 && col < CellCollection.SIZE
                && row >= 0 && row < CellCollection.SIZE) {
            mSelectedCell = mCells.getCell(row, col);
            onCellSelected(mSelectedCell);

            postInvalidate();
            return true;
        }

        return false;
    }

    public void clearCellSelection() {
        mSelectedCell = null;
        onCellSelected(mSelectedCell);
        postInvalidate();
    }

    private Cell getCellAtPoint(int x, int y) {
        int lx = x - getPaddingLeft();
        int ly = y - getPaddingTop();

        int row = (int) (ly / mCellHeight);
        int col = (int) (lx / mCellWidth);

        if (col >= 0 && col < CellCollection.SIZE
                && row >= 0 && row < CellCollection.SIZE) {
            return mCells.getCell(row, col);
        } else {
            return null;
        }
    }

    public interface OnCellTappedListener {
        void onCellTapped(Cell cell);
    }

    public interface OnCellSelectedListener {
        void onCellSelected(Cell cell);
    }
}
