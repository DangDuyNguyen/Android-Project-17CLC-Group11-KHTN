package com.project.arcadedestroyer.Sudoku.game;

import java.util.StringTokenizer;

public class SetCellValueAction extends EditACellAction {
    private int mValue;
    private int mOldValue;

    public SetCellValueAction(Cell cell, int value) {
        super(cell);
        mValue = value;
    }

    SetCellValueAction() { }

    @Override
    public void actionToString(StringBuilder data) {
        super.actionToString(data);

        data.append(mValue).append("|");
        data.append(mOldValue).append("|");
    }

    @Override
    protected void _stringToAction(StringTokenizer data) {
        super._stringToAction(data);

        mValue = Integer.parseInt(data.nextToken());
        mOldValue = Integer.parseInt(data.nextToken());
    }

    @Override
    void execute() {
        Cell cell = getCell();
        mOldValue = cell.getValue();
        cell.setValue(mValue);
    }

    @Override
    void undo() {
        Cell cell = getCell();
        cell.setValue(mOldValue);
    }
}
