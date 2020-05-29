package com.example.project.game;

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
    public void setAction(StringBuilder data) {
        super.setAction(data);

        data.append(mCellRow).append("|");
        data.append(mCellColumn).append("|");
    }

    @Override
    protected void getAct(StringTokenizer data) {
        super.getAct(data);
        mCellRow = Integer.parseInt(data.nextToken());
        mCellColumn = Integer.parseInt(data.nextToken());
    }

    public Cell getCell() {
        return getCells().getCell(mCellRow, mCellColumn);
    }
}
