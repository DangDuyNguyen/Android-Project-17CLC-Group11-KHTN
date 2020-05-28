package com.example.project.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;

import com.example.project.game.SudokuGame;
import com.example.project.game.Cell;
import com.example.project.game.CellCollection;
import com.example.project.R;

public class SudokuBoard extends View {
    public static final int DEFAULT_BOARD_SIZE = 100;
    private static final int NONE_COLOR = 0;

    private float mCellWidth;
    private float mCellHeight;

    private Cell mTouchedCell;
    private Cell mSelectedCell;
    private int mHighlightedValue = 0;
    private boolean mReadOnly = false;
    private boolean mHighlightWrongVals = true;
    private boolean mHighlightTouchedCell = true;

    private SudokuGame mSudokuGame;
    private String mLevel;
    private long mScore;

    public static final int MENU_ITEM_RESTART = Menu.FIRST;
    public static final int MENU_ITEM_TIMER = Menu.FIRST + 1;

    public SudokuBoard(Context context) {
        super(context);
    }

    public enum HighlightMode {
        NONE,
        NUMBERS,
    };

    private HighlightMode mHighlightSimilarCells = HighlightMode.NONE;

    private CellCollection mCells;

    private OnCellTappedListener mOnCellTappedListener;
    private OnCellSelectedListener mOnCellSelectedListener;

    private Paint mLinePaint;
    private Paint mBoxLinePaint;
    private Paint mValuePaint;
    private Paint mGeneratedValuePaint;
    private int mNumberLeft;
    private int mNumberTop;
    private int mBoxLineWidth;
    private Paint mColorSecondary;
    private Paint mColorReadOnly;
    private Paint mColorTouched;
    private Paint mColorSelected;
    private Paint mColorHighlighted;

    private Paint mInvalidValuePaint;

    public SudokuBoard(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);

        mLinePaint = new Paint();
        mBoxLinePaint = new Paint();
        mValuePaint = new Paint();
        mGeneratedValuePaint = new Paint();
        mInvalidValuePaint = new Paint();
        mColorSecondary = new Paint();
        mColorReadOnly = new Paint();
        mColorTouched = new Paint();
        mColorSelected = new Paint();
        mColorHighlighted = new Paint();

        mValuePaint.setAntiAlias(true);
        mGeneratedValuePaint.setAntiAlias(true);
        mInvalidValuePaint.setAntiAlias(true);
        mInvalidValuePaint.setColor(Color.RED);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SudokuBoard);

        setLineColor(a.getColor(R.styleable.SudokuBoard_lineColor, Color.BLACK));
        setBoxLineColor(a.getColor(R.styleable.SudokuBoard_boxLineColor, Color.BLACK));
        setTextColor(a.getColor(R.styleable.SudokuBoard_textColor, Color.BLACK));
        setGeneratedTextColor(a.getColor(R.styleable.SudokuBoard_textColorReadOnly, Color.BLACK));
        setBackgroundColor(a.getColor(R.styleable.SudokuBoard_color, Color.WHITE));
        setColorSecondary(a.getColor(R.styleable.SudokuBoard_colorSecondary, NONE_COLOR));
        setColorReadOnly(a.getColor(R.styleable.SudokuBoard_colorReadOnly, NONE_COLOR));
        setColorTouched(a.getColor(R.styleable.SudokuBoard_colorTouched, Color.rgb(50, 50, 255)));
        setColorSelected(a.getColor(R.styleable.SudokuBoard_colorSelected, Color.YELLOW));
        setColorHighlighted(a.getColor(R.styleable.SudokuBoard_colorHighlighted, Color.GREEN));

        a.recycle();
    }

    public int getLineColor() {
        return mLinePaint.getColor();
    }

    public void setLineColor(int color) {
        mLinePaint.setColor(color);
    }

    public int getBoxLineColor() {
        return mBoxLinePaint.getColor();
    }

    public void setBoxLineColor(int color) {
        mBoxLinePaint.setColor(color);
    }

    public int getTextColor() {
        return mValuePaint.getColor();
    }

    public void setTextColor(int color) {
        mValuePaint.setColor(color);
    }

    public int getGeneratedTextColor() {
        return mGeneratedValuePaint.getColor();
    }

    public void setGeneratedTextColor(int color) {
        mGeneratedValuePaint.setColor(color);
    }

    public int getColorSecondary() {
        return mColorSecondary.getColor();
    }

    public void setColorSecondary(int color) {
        mColorSecondary.setColor(color);
    }

    public int getColorReadOnly() {
        return mColorReadOnly.getColor();
    }

    public void setColorReadOnly(int color) {
        mColorReadOnly.setColor(color);
    }

    public int getColorTouched() {
        return mColorTouched.getColor();
    }

    public void setColorTouched(int color) {
        mColorTouched.setColor(color);
    }

    public int getColorSelected() {
        return mColorSelected.getColor();
    }

    public void setColorSelected(int color) {
        mColorSelected.setColor(color);
    }

    public int getColorHighlighted() {
        return mColorHighlighted.getColor();
    }

    public void setColorHighlighted(int color) {
        mColorHighlighted.setColor(color);
    }

    public CellCollection getCells() {
        return mCells;
    }

    public Cell getSelectedCell() {
        return mSelectedCell;
    }

    public void setReadOnly(boolean readOnly) {
        mReadOnly = readOnly;
        postInvalidate();
    }

    public boolean isReadOnly() {
        return mReadOnly;
    }

    public void setHighlightWrongVals(boolean highlight) {
        mHighlightWrongVals = highlight;
        postInvalidate();
    }

    public boolean getHighlightWrongVals() {
        return mHighlightWrongVals;
    }

    public void setHighlightTouchedCell(boolean highlight) {
        mHighlightTouchedCell = highlight;
    }

    public boolean getHighlightTouchedCell() {
        return mHighlightTouchedCell;
    }

    public void setHighlightSimilarCell(HighlightMode highlight) {
        mHighlightSimilarCells = highlight;
    }

    public void setHighlightedValue(int val) {
        mHighlightedValue = val;
    }

    public int getHighlightedValue() {
        return mHighlightedValue;
    }

    public void setOnCellTappedListener(OnCellTappedListener listener) {
        mOnCellTappedListener = listener;
    }

    protected void onTappedCell(Cell cell) {
        if (mOnCellTappedListener != null) {
            mOnCellTappedListener.onCellTapped(cell);
        }
    }

    public void setOnCellSelectedListener(OnCellSelectedListener listener) {
        mOnCellSelectedListener = listener;
    }

    protected void onSelectedCell(Cell cell) {
        if (mOnCellSelectedListener != null) {
            mOnCellSelectedListener.onCellSelected(cell);
        }
    }

    public void invokeOnSelectedCell() {
        onSelectedCell(mSelectedCell);
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
        }
        else {
            width = DEFAULT_BOARD_SIZE;
            if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
                width = widthSize;
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = DEFAULT_BOARD_SIZE;
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

        float textSize = mCellHeight * 0.6f;
        mValuePaint.setTextSize(textSize);
        mGeneratedValuePaint.setTextSize(textSize);
        mInvalidValuePaint.setTextSize(textSize);

        mNumberLeft = (int) ((mCellWidth - mValuePaint.measureText("0")) / 2);
        mNumberTop = (int) ((mCellHeight - mValuePaint.getTextSize()) / 2);

        computeBoxLineWidth(width, height);
    }

    private void computeBoxLineWidth(int widthPixel, int heightPixel) {
        int sizeInPixel = widthPixel < heightPixel ? widthPixel : heightPixel;
        float dipScale = getContext().getResources().getDisplayMetrics().density;
        float sizeInDip = sizeInPixel / dipScale;

        float boxLineWidthDip = 2.0f;

        if (sizeInDip > 150) {
            boxLineWidthDip = 3.0f;
        }

        mBoxLineWidth = (int) (boxLineWidthDip * dipScale);
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
            boolean hasColorReadOnly = mColorReadOnly.getColor() != NONE_COLOR;

            float numberAscent = mValuePaint.ascent();

            for (int row = 0; row < CellCollection.SIZE; row++) {
                for (int col = 0; col < CellCollection.SIZE; col++) {
                    Cell cell = mCells.getCell(row, col);

                    cellLeft = Math.round((col * mCellWidth) + paddingLeft);
                    cellTop = Math.round((row * mCellHeight) + paddingTop);

                    if (!cell.isEditable() && hasColorReadOnly && (mSelectedCell == null || mSelectedCell != cell)) {
                        if (mColorReadOnly.getColor() != NONE_COLOR) {
                            canvas.drawRect(cellLeft, cellTop, cellLeft + mCellWidth, cellTop + mCellHeight, mColorReadOnly);
                        }
                    }

                    boolean notSelectedCell = (mSelectedCell == null || mSelectedCell != cell);
                    boolean validHighlightedValue = (mHighlightedValue != 0);
                    boolean highlightCell = false;

                    switch (mHighlightSimilarCells) {
                        default:
                        case NONE: {
                            highlightCell = false;
                            break;
                        }

                        case NUMBERS: {
                            highlightCell = notSelectedCell && validHighlightedValue && mHighlightedValue == cell.getValue();
                            break;
                        }
                    }
                }
            }

            if (!mReadOnly && mSelectedCell != null) {
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
                        Paint valuePaint = cell.isEditable() ? mValuePaint : mGeneratedValuePaint;

                        if (mHighlightWrongVals && !cell.isValid()) {
                            valuePaint = mInvalidValuePaint;
                        }

                        canvas.drawText(Integer.toString(value), cellLeft + mNumberLeft, cellTop + mNumberTop - numberAscent, valuePaint);
                    }
                }
            }
        }

        for (int i = 0; i <= CellCollection.SIZE; i++) {
            float x = (i * mCellWidth) + paddingLeft;
            canvas.drawLine(x, paddingTop, x, height, mLinePaint);
        }

        for (int i = 0; i <= CellCollection.SIZE; i++) {
            float y = i * mCellHeight + paddingTop;
            canvas.drawLine(paddingLeft, y, width, y, mLinePaint);
        }

        int boxLineWidth1 = mBoxLineWidth / 2;
        int boxLineWidth2 = boxLineWidth1 + (mBoxLineWidth % 2);

        for (int i = 0; i <= CellCollection.SIZE; i = i + 3) {
            float x = (i * mCellWidth) + paddingLeft;
            canvas.drawRect(x - boxLineWidth1, paddingTop, x + boxLineWidth2, height, mBoxLinePaint);
        }

        for (int i = 0; i <= CellCollection.SIZE; i = i + 3) {
            float y = (i * mCellHeight) + paddingTop;
            canvas.drawRect(paddingLeft, y - boxLineWidth1, width, y + boxLineWidth2, mBoxLinePaint);
        }
    }

    private void setCellValue(Cell cell, int value) {
        if (cell.isEditable()) {
            cell.setValue(value);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mReadOnly) {
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
                        onTappedCell(mSelectedCell);
                        onSelectedCell(mSelectedCell);
                    }

                    break;
                case MotionEvent.ACTION_CANCEL:
                    mTouchedCell = null;
                    break;
            }
            postInvalidate();
        }

        return !mReadOnly;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mReadOnly) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    return moveCellSelection(0, -1);
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    return moveCellSelection(1, 0);
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    return moveCellSelection(0, 1);
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    return moveCellSelection(-1, 0);
                case KeyEvent.KEYCODE_0:
                case KeyEvent.KEYCODE_SPACE:
                case KeyEvent.KEYCODE_DEL:
                    if (mSelectedCell != null) {
                        setCellValue(mSelectedCell, 0);
                        moveCellSelectionRight();
                    }
                    return true;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    if (mSelectedCell != null) {
                        onTappedCell(mSelectedCell);
                    }
                    return true;
            }

            if (keyCode >= KeyEvent.KEYCODE_1 && keyCode <= KeyEvent.KEYCODE_9 && mSelectedCell != null) {
                int selNumber = keyCode - KeyEvent.KEYCODE_0;
                Cell cell = mSelectedCell;

                setCellValue(cell, selNumber);
                moveCellSelectionRight();

                return true;
            }
        }


        return false;
    }

    public void moveCellSelectionRight() {
        if (!moveCellSelection(1, 0)) {
            int selectedRow = mSelectedCell.getRow();
            selectedRow++;
            if (!moveSelectionTo(selectedRow, 0)) {
                moveSelectionTo(0, 0);
            }
        }
        postInvalidate();
    }

    private boolean moveCellSelection(int x, int y) {
        int newRow = 0;
        int newCol = 0;

        if (mSelectedCell != null) {
            newRow = mSelectedCell.getRow() + y;
            newCol = mSelectedCell.getColumn() + x;
        }

        return moveSelectionTo(newRow, newCol);
    }

    public boolean moveSelectionTo(int row, int col) {
        if (col >= 0 && col < CellCollection.SIZE
                && row >= 0 && row < CellCollection.SIZE) {
            mSelectedCell = mCells.getCell(row, col);
            onSelectedCell(mSelectedCell);

            postInvalidate();
            return true;
        }

        return false;
    }

    public void clearCellSelection() {
        mSelectedCell = null;
        onSelectedCell(mSelectedCell);
        postInvalidate();
    }

    private Cell getCellAtPoint(int x, int y) {
        // take into account padding
        int posX = x - getPaddingLeft();
        int posY = y - getPaddingTop();

        int row = (int) (posX / mCellHeight);
        int col = (int) (posY / mCellWidth);

        if (col >= 0 && col < CellCollection.SIZE && row >= 0 && row < CellCollection.SIZE)
            return mCells.getCell(row, col);
        else
            return null;
    }

    public interface OnCellTappedListener {
        void onCellTapped(Cell cell);
    }

    public interface OnCellSelectedListener {
        void onCellSelected(Cell cell);
    }
}
