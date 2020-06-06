package com.ndduy.gamecollection2020.Sudoku.game;

import java.util.ArrayList;

public class SudokuSolver {
    private int NUM_OF_ROWS = 9;
    private int NUM_OF_COLS = 9;
    private int NUM_OF_VALS = 9;
    private int NUM_OF_CONSTRAINTS = 4;
    private int NUM_OF_CELLS = NUM_OF_ROWS * NUM_OF_COLS;

    private int[][] mConstraintMatrix;
    private Node[][] mList;
    private Node mHead;
    private ArrayList<Node> mSolution;

    public SudokuSolver() {
        initConstraintMatrix();
        initList();
    }

    public void setPuzzle(CellCollection cells) {
        Cell[][] board = cells.getCells();

        for (int row = 0; row < NUM_OF_ROWS; row++) {
            for (int col = 0; col < NUM_OF_COLS; col++) {
                Cell cell = board[row][col];
                int val = cell.getValue();
                if (!cell.isEditable()) {
                    int matrixRow = convertCellToRow(row, col, val-1, true);
                    int matrixCol = NUM_OF_ROWS*row + col;

                    Node rowNode = mList[matrixRow][matrixCol];
                    Node rightNode = rowNode;
                    do {
                        cover(rightNode);
                        rightNode = rightNode.right;
                    } while (rightNode != rowNode);
                }
            }
        }
    }

    public ArrayList<int[]> solve() {
        mSolution = new ArrayList<>();
        mSolution = getSolution();

        ArrayList<int[]> finalValues = new ArrayList<>();
        for (Node node : mSolution) {
            int matrixRow = node.row;
            int[] rowColVal = convertRowToCell(matrixRow, true);
            finalValues.add(rowColVal);
        }

        return finalValues;
    }

    // 1 dong Sudoku chiem 81 dong trong matrix, 1 cot Sudoku chiem 9 dong trong matrix, 1 value chiem 1 dong trong matrix
    private void initConstraintMatrix() {
        mConstraintMatrix = new int[NUM_OF_CELLS * NUM_OF_VALS + 1][NUM_OF_CELLS * NUM_OF_CONSTRAINTS];
        for (int j = 0; j < mConstraintMatrix[0].length; j++)
            mConstraintMatrix[0][j] = 1;

        int rowShift = NUM_OF_CELLS;
        int colShift = NUM_OF_CELLS * 2;
        int boxShift = NUM_OF_CELLS * 3;
        int cellCol = 0;
        int rowCol = 0;
        int colCol = 0;
        int boxCol = 0;

        for (int row = 0; row < NUM_OF_ROWS; row++, rowCol += 9, colCol -= 81) {
            for (int col = 0; col < NUM_OF_COLS; col++, cellCol++, rowCol -= 9) {
                for (int val = 0; val < NUM_OF_VALS; val++, rowCol++, colCol++, boxCol++) {
                    int matrixRow = convertCellToRow(row, col, val, true);

                    mConstraintMatrix[matrixRow][cellCol] = 1; // cell constraint

                    mConstraintMatrix[matrixRow][rowCol + rowShift] = 1; // row constraint

                    mConstraintMatrix[matrixRow][colCol + colShift] = 1; // col constraint

                    mConstraintMatrix[matrixRow][boxCol + boxShift] = 1; // box constraint
                }

                if (col % 3 != 2) {
                    boxCol -= 9;
                }
            }

            if (row % 3 != 2) {
                boxCol -= 27;
            }
        }
    }

    private void initList() {
        mList = new Node[NUM_OF_ROWS * NUM_OF_COLS * NUM_OF_VALS + 1][NUM_OF_CELLS * NUM_OF_CONSTRAINTS];
        mHead = new Node();

        for (int i = 0; i < mList.length; i++) {
            for (int j = 0; j < mList[0].length; j++) {
                if (mConstraintMatrix[i][j] == 1) {
                    mList[i][j] = new Node();
                }
            }
        }

        for (int i = 0; i < mList.length; i++) {
            for (int j = 0; j < mList[0].length; j++) {
                if (mConstraintMatrix[i][j] == 1) {
                    int a = i, b = j;

                    do {
                        b = moveLeft(b, mList[0].length);
                    } while (mConstraintMatrix[a][b] != 1);
                    mList[i][j].left = mList[a][b];

                    a = i;
                    b = j;
                    do {
                        b = moveRight(b, mList[0].length);
                    } while (mConstraintMatrix[a][b] != 1);
                    mList[i][j].right = mList[a][b];

                    a = i;
                    b = j;
                    do {
                        a = moveUp(a, mList.length);
                    } while (mConstraintMatrix[a][b] != 1);
                    mList[i][j].up = mList[a][b];

                    a = i;
                    b = j;
                    do {
                        a = moveDown(a, mList.length);
                    } while (mConstraintMatrix[a][b] != 1);
                    mList[i][j].down = mList[a][b];

                    // initialize remaining node info
                    mList[i][j].columnHead = mList[0][j];
                    mList[i][j].row = i;
                    mList[i][j].col = j;
                }
            }
        }

        mHead.right = mList[0][0];
        mHead.left = mList[0][mList[0].length - 1];
        mList[0][0].left = mHead;
        mList[0][mList[0].length - 1].right = mHead;
    }

    // return array of solution nodes or empty array
    public ArrayList<Node> getSolution() {
        if (mHead.right == mHead)
            return mSolution;

        Node colNode = chooseCol();
        cover(colNode);

        Node rowNode;
        for (rowNode = colNode.down; rowNode != colNode; rowNode = rowNode.down) {
            mSolution.add(rowNode);

            Node rightNode;
            for (rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right)
                cover(rightNode);

            ArrayList<Node> tempSolution = getSolution();
            if (!tempSolution.isEmpty())
                return tempSolution;

            // undo and try next row
            mSolution.remove(mSolution.size() - 1);
            colNode = rowNode.columnHead;
            Node leftNode;
            for (leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left)
                uncover(leftNode);
        }
        uncover(colNode);

        return new ArrayList<>();
    }

    private int convertCellToRow(int row, int col, int val, boolean headerInMatrix) {
        int matrixRow = NUM_OF_CELLS*row + NUM_OF_COLS*col + val;
        if (headerInMatrix)
            matrixRow++;

        return matrixRow;
    }

    private int[] convertRowToCell(int matrixRow, boolean headerInMatrix) {
        int[] rowColVal = new int[3];
        if (headerInMatrix)
            matrixRow--;

        rowColVal[0] = matrixRow / NUM_OF_CELLS;
        rowColVal[1] = matrixRow % NUM_OF_CELLS / NUM_OF_COLS;
        rowColVal[2] = matrixRow % NUM_OF_COLS + 1;
        return rowColVal;
    }

    private int moveLeft (int j, int numCols) {return j - 1 < 0 ? numCols - 1 : j - 1;}
    private int moveRight (int j, int numCols) {return (j + 1) % numCols;}
    private int moveUp (int i, int numRows) {return i - 1 < 0 ? numRows - 1 : i -1;}
    private int moveDown (int i, int numRows) {return (i + 1) % numRows;}

    private void cover(Node node) {
        Node colNode = node.columnHead;
        colNode.left.right = colNode.right;
        colNode.right.left = colNode.left;

        Node rowNode;
        for (rowNode = colNode.down; rowNode != colNode; rowNode = rowNode.down) {
            Node rightNode;
            for (rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right) {
                rightNode.up.down = rightNode.down;
                rightNode.down.up = rightNode.up;
                rightNode.columnHead.count--;
            }
        }
    }

    private void uncover(Node node) {
        Node colNode = node.columnHead;
        Node upNode;
        for (upNode = colNode.up; upNode != colNode; upNode = upNode.up) {
            Node leftNode;
            for (leftNode = upNode.left; leftNode != upNode; leftNode = leftNode.left) {
                leftNode.up.down = leftNode;
                leftNode.down.up = leftNode;

                leftNode.columnHead.count++;
            }
        }
        colNode.left.right = colNode;
        colNode.right.left = colNode;
    }

    private Node chooseCol() {
        Node bestNode = null;
        int lowestNum = 10000;

        Node currentNode = mHead.right;
        while (currentNode != mHead) {
            if (currentNode.count < lowestNum) {
                bestNode = currentNode;
                lowestNum = currentNode.count;
            }
            currentNode = currentNode.right;
        }
        return bestNode;
    }
}