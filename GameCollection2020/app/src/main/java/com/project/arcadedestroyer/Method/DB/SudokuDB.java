package com.project.arcadedestroyer.Method.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.project.arcadedestroyer.Sudoku.game.CellCollection;
import com.project.arcadedestroyer.Sudoku.game.SudokuGame;

import java.util.ArrayList;

public class SudokuDB {
    public static final String DB_NAME = "Game";
    public static final String TABLE_NAME = "Sudoku";

    private DBHelper mHelper;

    public SudokuDB(Context context) {
        mHelper = new DBHelper(context);
    }

    public ArrayList<Long> getSudokuId(int type) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(TABLE_NAME);

        if(type > 0 && type < 4)
            qb.appendWhere("levelId = " + type);

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = qb.query(db, null, "id", null,
                null, null, null);

        ArrayList<Long> res = new ArrayList<>();

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                res.add(cursor.getLong(0));
            }
        }

        return res;
    }

    public SudokuGame getSudoku(long boardId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(TABLE_NAME);
        qb.appendWhere("id = " + boardId);

        SQLiteDatabase db;
        SudokuGame game = null;
        db = mHelper.getReadableDatabase();
        try (Cursor cursor = qb.query(db, null, null,
                null, null, null, null)) {
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                long levelId = cursor.getLong(cursor.getColumnIndex("levelId"));
                String data = cursor.getString(cursor.getColumnIndex("data"));

                game = new SudokuGame();
                game.setId(id);
                game.setLevelId(levelId);
                game.setCells(CellCollection.setCells(data));
            }
        }

        return game;
    }

    public void close() {
        mHelper.close();
    }

    public void beginTransaction() {
        mHelper.getWritableDatabase().beginTransaction();
    }

    public void setTransactionSuccessful() {
        mHelper.getWritableDatabase().setTransactionSuccessful();
    }

    public void endTransaction() {
        mHelper.getWritableDatabase().endTransaction();
    }
}
