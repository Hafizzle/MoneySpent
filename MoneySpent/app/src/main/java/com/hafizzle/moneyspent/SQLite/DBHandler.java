package com.hafizzle.moneyspent.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hafizzle.moneyspent.Objects.UserEntry;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "moneyDB.db";//was productsDB
    public static final String TABLE_MONEY = "money";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATESTAMP = "datestamp";
    public static final String COLUMN_MONEY = "money";
    public static final String ITEM_INDEX = "_index";

    ArrayList<UserEntry> mUserEntries;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryMoney = "CREATE TABLE " + TABLE_MONEY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATESTAMP + " TEXT," + COLUMN_MONEY + " REAL," + ITEM_INDEX + " INTEGER" +")";
        db.execSQL(queryMoney);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONEY);
        onCreate(db);
    }


    public void addData(UserEntry userEntry, ArrayList<UserEntry> userEntries) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_DATESTAMP, userEntry.getDateStamp());
        values.put(COLUMN_MONEY, userEntry.getMoneyAmount());
        values.put(ITEM_INDEX, userEntries.size());

        Log.d(TAG, "addData: adding data");

        db.insert(TABLE_MONEY, null, values);
    }

    public void deleteRow(int position){

        Log.d(TAG, "deleteRow: called");
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MONEY + " WHERE " +
                ITEM_INDEX + " = " + position + ";");
        db.execSQL("UPDATE " + TABLE_MONEY + " SET " + ITEM_INDEX + " = " +
                ITEM_INDEX + " -1 " + " WHERE " + ITEM_INDEX + " > " + position + ";");
        db.close();
    }


    public double getTotal() {
        double total = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_MONEY + ") as Total FROM " + TABLE_MONEY, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndex("Total"));
        }
        double roundOff = (double) Math.round(total * 100) / 100;
        return roundOff;
    }

    public ArrayList<UserEntry> getData(){
        mUserEntries = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONEY, null);

        while (cursor.moveToNext()){
                String dateStamp = cursor.getString(cursor.getColumnIndex(COLUMN_DATESTAMP));
                double moneyAmount = cursor.getDouble(cursor.getColumnIndex(COLUMN_MONEY));
                mUserEntries.add(new UserEntry(dateStamp, moneyAmount));
        }
        return mUserEntries;
    }
}

