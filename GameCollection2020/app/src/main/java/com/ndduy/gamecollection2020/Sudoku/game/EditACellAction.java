package com.ndduy.gamecollection2020.Sudoku.game;

import java.util.StringTokenizer;

public abstract class EditACellAction extends EditCellAction {
    private int mCellRow;
    private int mCellColumn;

    public EditACellAction(Cell cell) {
        mCellRow = cell.getRow();
        mCellColumn = cell.getColumn();
    }

    EditACellAction() { }

    @Override
    public void actionToString(StringBuilder data) {
        super.actionToString(data);

        data.append(mCellRow).append("|");
        data.append(mCellColumn).append("|");
    }

    @Override
    protected void _stringToAction(StringTokenizer data) {
        super._stringToAction(data);
        mCellRow = Integer.parseInt(data.nextToken());
        mCellColumn = Integer.parseInt(data.nextToken());
    }

    public Cell getCell() {
        return getCells().getCell(mCellRow, mCellColumn);
    }
}
