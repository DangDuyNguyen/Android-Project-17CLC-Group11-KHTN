package com.project.arcadedestroyer.Sudoku.game;

public abstract class EditCellAction extends Action {

    private CellCollection mCells;

    protected CellCollection getCells() {
        return mCells;
    }

    protected void setCells(CellCollection mCells) {
        this.mCells = mCells;
    }
}
