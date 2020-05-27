package com.example.project.game;

import com.example.project.game.Cell;

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
    public void setAction(StringBuilder data) {
        super.setAction(data);

        data.append(mValue).append("|");
        data.append(mOldValue).append("|");
    }

    @Override
    protected void getAct(StringTokenizer data) {
        super.getAct(data);

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
