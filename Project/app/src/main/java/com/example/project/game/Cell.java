package com.example.project.game;

public class Cell {
    private CellCollection mCollection;
    private final Object mCollectionLock = new Object();
    private int mRowIndex = -1;
    private int mColIndex = -1;
    private CellGroup mBoxGroup;
    private CellGroup mRowGroup;
    private CellGroup mColGroup;
    private int mValue;
    private boolean mEditable;
    private boolean mValid;

    public Cell() {
        this(0, true, true);
    }

    public Cell(int val) {
        this(val, true, true);
    }

    private Cell(int val, boolean editProp, boolean validProp) {
        if (val < 0 || val > 9) {
            throw new IllegalArgumentException("Value must be between 0 - 9.");
        }

        mValue = val;
        mEditable = editProp;
        mValid = validProp;
    }

    public int getRow() {
        return mRowIndex;
    }

    public int getColumn() {
        return mColIndex;
    }

    protected void initCollection(CellCollection cellCollection, int row, int col, CellGroup boxG, CellGroup rowG, CellGroup colG) {
        synchronized (mCollectionLock) {
            mCollection = cellCollection;
        }

        mRowIndex = row;
        mColIndex = col;
        mBoxGroup = boxG;
        mRowGroup = rowG;
        mColGroup = colG;

        mBoxGroup.addCell(this);
        mRowGroup.addCell(this);
        mColGroup.addCell(this);
    }

    /**
     * Returns sector containing this cell. Sector is 3x3 group of cells.
     *
     * @return Sector containing this cell.
     */
    public CellGroup getBoxGroup() {
        return mBoxGroup;
    }

    public CellGroup getRowGroup() {
        return mRowGroup;
    }

    public CellGroup getColumnGroup() {
        return mColGroup;
    }

    public void setValue(int val) {
        if (val < 0 || val > 9) {
            throw new IllegalArgumentException("Value must be between 0-9.");
        }
        mValue = val;
        onChange();
    }

    public int getValue() {
        return mValue;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public boolean isValid() {
        return mValid;
    }

    public void setEditable(Boolean editProp) {
        mEditable = editProp;
        onChange();
    }

    public void setValid(Boolean validProp) {
        mValid = validProp;
        onChange();
    }

    private void onChange() {
        synchronized (mCollectionLock) {
            if (mCollection != null) {
                mCollection.onChange();
            }

        }
    }
}
