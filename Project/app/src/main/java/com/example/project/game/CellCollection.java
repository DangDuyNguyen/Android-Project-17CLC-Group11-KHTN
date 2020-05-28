package com.example.project.game;

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

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                arrCells[i][j] = new Cell();
            }
        }

        return new CellCollection(arrCells);
    }

    public boolean isEmpty() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = mCells[i][j];
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
        for (int i = 0; i < CellCollection.SIZE; i++) {
            for (int j = 0; j < CellCollection.SIZE; j++) {
                int value = 0;
                while (pos < data.length()) {
                    if (data.charAt(pos) >= '0'
                            && data.charAt(pos) <= '9') {
                        value = data.charAt(pos) - '0';
                        break;
                    }
                    pos++;
                }
                Cell cell = new Cell();
                cell.setValue(value);
                cell.setEditable(value == 0);
                cells[i][j] = cell;
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

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = mCells[i][j];
                cell.initCollection(this, i, j, mBoxes[((j / 3) * 3) + (i / 3)], mRows[j], mCols[i]);
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
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = mCells[i][j];
                if (cell.getValue() == value)
                    return cell;
            }
        }

        return null;
    }


    public void markAllCellsValid() {
        changeEnabled = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                mCells[i][j].setValid(true);
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
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = mCells[i][j];
                if (cell.getValue() == 0 || !cell.isValid()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void markAllCellsEditable() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = mCells[i][j];
                cell.setEditable(true);
            }
        }
    }

    public void markGeneratedCellsNotEditable() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Cell cell = mCells[i][j];
                if(cell.getValue() != 0)
                    cell.setEditable(false);
            }
        }
    }

    public Map<Integer, Integer> countValuesUsed() {
        Map<Integer, Integer> valsUsed = new HashMap<>();
        for (int val = 1; val <= CellCollection.SIZE; val++) {
            valsUsed.put(val, 0);
        }

        for (int i = 0; i < CellCollection.SIZE; i++) {
            for (int j = 0; j < CellCollection.SIZE; j++) {
                int val = this.getCell(i, j).getValue();
                if (val != 0) {
                    valsUsed.put(val, valsUsed.get(val) + 1);
                }
            }
        }

        return valsUsed;
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
