package com.project.arcadedestroyer.Method.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.project.arcadedestroyer.Method.Item;

import java.util.ArrayList;

public class ItemDB {
    public static final String DB_NAME = "Game";

    private DBHelper mHelper;

    public ItemDB(Context context) {
        mHelper = new DBHelper(context);
    }

    public ArrayList<Item> getItemList(int gacha) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables("GachaList");

        if(gacha > 0 && gacha < 3)
            qb.appendWhere("gachaId = " + gacha);

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = qb.query(db, null, "itemId", null,
                null, null, null);

        ArrayList<Integer> itemsId = new ArrayList<>();

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                itemsId.add(cursor.getInt(0));
            }
        }

        ArrayList<Item> res = new ArrayList<>();

        for(int i = 0; i < itemsId.size(); i++)
        {
            Item item = getItem(itemsId.get(i));
            res.add(item);
        }

        return res;
    }

    public Item getItem(int itemId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables("Item");
        qb.appendWhere("itemId = " + itemId);

        SQLiteDatabase db;
        Item item = null;
        db = mHelper.getReadableDatabase();
        try (Cursor cursor = qb.query(db, null, null,
                null, null, null, null)) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("itemId"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                item = new Item(id, name, image, type);
            }
        }

        return item;
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
