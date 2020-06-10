package com.project.arcadedestroyer.Sudoku.game;

import java.util.ArrayList;
import java.util.Stack;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class ActionStack {
    private Stack<Action> mStack = new Stack<>();

    private CellCollection mCells;

    public ActionStack(CellCollection cells) {
        mCells = cells;
    }

    public String stackToString() {
        StringBuilder sb = new StringBuilder();
        stackToString(sb);
        return sb.toString();
    }

    public void stackToString(StringBuilder data) {
        data.append(mStack.size()).append("|");
        for (int i = 0; i < mStack.size(); i++) {
            Action action = mStack.get(i);
            action.actionToString(data);
        }
    }

    public static ActionStack stringToStack(String data, CellCollection cells) {
        StringTokenizer st = new StringTokenizer(data, "|");
        return stringToStack(st, cells);
    }

    public static ActionStack stringToStack(StringTokenizer data, CellCollection cells) {
        ActionStack stack = new ActionStack(cells);
        int stackSize = Integer.parseInt(data.nextToken());
        for (int i = 0; i < stackSize; i++) {
            Action action = Action.stringToAction(data);
            stack.push(action);
        }

        return stack;
    }

    public boolean isEmpty() {
        return mStack.empty();
    }

    public void execute(Action action) {
        push(action);
        action.execute();
    }

    public void undo() {
        if (!mStack.empty()) {
            Action action = pop();
            action.undo();
            validateCells();
        }
    }

    private boolean hasMistake(ArrayList<int[]> finalValues) {
        for (int[] rowColVal : finalValues) {
            int row = rowColVal[0];
            int col = rowColVal[1];
            int val = rowColVal[2];
            Cell cell = mCells.getCell(row, col);

            if (cell.getValue() != val && cell.getValue() != 0)
                return true;
        }

        return false;
    }

    public void undoSolvableState() {
        SudokuSolver solver = new SudokuSolver();
        solver.setPuzzle(mCells);
        ArrayList<int[]> finalValues = solver.solve();

        while (!mStack.empty() && hasMistake(finalValues)) {
            mStack.pop().undo();
        }

        validateCells();
    }

    public Cell getLastChangedCell() {
        ListIterator<Action> iterator = mStack.listIterator(mStack.size());
        while (iterator.hasPrevious()) {
            Action action = iterator.previous();
            if (action instanceof EditACellAction)
                return ((EditACellAction)action).getCell();
        }

        return null;
    }

    private void push(Action action) {
        if (action instanceof EditCellAction) {
            ((EditCellAction) action).setCells(mCells);
        }
        mStack.push(action);
    }

    private Action pop() {
        return mStack.pop();
    }

    private void validateCells() {
        mCells.validate();
    }


}
