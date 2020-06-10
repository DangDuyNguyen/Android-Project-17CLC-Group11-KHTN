package com.project.arcadedestroyer.Sudoku.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CellCollection {
    public static final int SIZE = 9;
    private Cell[][] mCells;

    private CellGroup[] mBoxes;
    private CellGroup[] mRows;
    private CellGroup[] mCols;

    private boolean changeEnabled = true;

    private final List<OnChangeListener> changeListeners = new ArrayList<OnChangeListener>();

    public static CellCollection initBoardEmpty() {
        Cell[][] arrCells = new Cell[SIZE][SIZE];

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                arrCells[r][c] = new Cell();
            }
        }

        return new CellCollection(arrCells);
    }

    public boolean isEmpty() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                if (cell.getValue() != 0)
                    return false;
            }
        }
        return true;
    }

    public Cell[][] getCells() {
        return mCells;
    }

    public static CellCollection setCells(String data) {
        Cell[][] cells = new Cell[SIZE][SIZE];

        int pos = 0;
        for (int r = 0; r < CellCollection.SIZE; r++) {
            for (int c = 0; c < CellCollection.SIZE; c++) {
                int value = 0;
                while (pos < data.length()) {
                    pos++;
                    if (data.charAt(pos - 1) >= '0' && data.charAt(pos - 1) <= '9') {
                        value = data.charAt(pos - 1) - '0';
                        break;
                    }
                }

                Cell cell = new Cell();
                cell.setValue(value);
                cell.setEditable(value == 0);
                cells[r][c] = cell;
            }
        }

        return new CellCollection(cells);
    }

    private void initCollection() {
        mRows = new CellGroup[SIZE];
        mCols = new CellGroup[SIZE];
        mBoxes = new CellGroup[SIZE];

        for (int i = 0; i < SIZE; i++) {
            mRows[i] = new CellGroup();
            mCols[i] = new CellGroup();
            mBoxes[i] = new CellGroup();
        }

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                cell.initCollection(this, r, c, mBoxes[((c / 3) * 3) + (r / 3)], mRows[c], mCols[r]);
            }
        }
    }

    private CellCollection(Cell[][] arr) {
        mCells = arr;
        initCollection();
    }

    public Cell getCell(int row, int col) {
        return mCells[row][col];
    }


    public Cell findCellByValue(int value) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                if (cell.getValue() == value)
                    return cell;
            }
        }

        return null;
    }


    public void markAllCellsValid() {
        changeEnabled = false;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                mCells[r][c].setValid(true);
            }
        }
        changeEnabled = true;
        onChange();
    }

    public boolean validate() {
        boolean valid = true;

        markAllCellsValid();

        changeEnabled = false;

        for (CellGroup row : mRows) {
            if (!row.validate()) {
                valid = false;
            }
        }

        for (CellGroup col : mCols) {
            if (!col.validate()) {
                valid = false;
            }
        }

        for (CellGroup box : mBoxes) {
            if (!box.validate()) {
                valid = false;
            }
        }

        changeEnabled = true;
        onChange();

        return valid;
    }

    public boolean isCompleted() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                if (cell.getValue() == 0 || !cell.isValid()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void markAllCellsEditable() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                cell.setEditable(true);
            }
        }
    }

    public void markGeneratedCellsNotEditable() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                if(cell.getValue() != 0)
                    cell.setEditable(false);
            }
        }
    }

    public void addOnChangeListener(OnChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("This listener is null.");
        }
        synchronized (changeListeners) {
            if (changeListeners.contains(listener)) {
                throw new IllegalStateException("Listener " + listener + "is already existed.");
            }
            changeListeners.add(listener);
        }
    }

    public String cellToString() {
        StringBuilder temp = new StringBuilder();
        cellToString(temp);
        return temp.toString();
    }

    public void cellToString(StringBuilder data) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Cell cell = mCells[r][c];
                cell.cellToString(data);
            }
        }
    }

    public static CellCollection stringToCell(String data) {
        StringTokenizer temp = new StringTokenizer(data, "|");
        Cell[][] cells = new Cell[SIZE][SIZE];

        int r = 0, c = 0;
        while (temp.hasMoreTokens() && r < 9) {
            cells[r][c] = Cell.stringToCell(temp);
            c++;

            if (c == 9) {
                r++;
                c = 0;
            }
        }

        return new CellCollection(cells);
    }

    public void removeOnChangeListener(OnChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("This listener is null.");
        }
        synchronized (changeListeners) {
            if (!changeListeners.contains(listener)) {
                throw new IllegalStateException("Listener " + listener + " is not existed.");
            }
            changeListeners.remove(listener);
        }
    }

    protected void onChange() {
        if (changeEnabled) {
            synchronized (changeListeners) {
                for (OnChangeListener listener : changeListeners) {
                    listener.onChange();
                }
            }
        }
    }

    public interface OnChangeListener {
        void onChange();
    }
}
