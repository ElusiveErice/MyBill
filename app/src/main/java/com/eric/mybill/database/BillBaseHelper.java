package com.eric.mybill.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eric.mybill.database.DbSchema.BillTable;
import com.eric.mybill.database.DbSchema.MaintenanceTable;
import com.eric.mybill.database.DbSchema.BillRankTable;

public class BillBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "myBillBase.db";

    public BillBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ BillTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                BillTable.Cols.TIMES + "," +
                BillTable.Cols.UUID + "," +
                BillTable.Cols.YEAR + "," +
                BillTable.Cols.MONTH + "," +
                BillTable.Cols.DAY + "," +
                BillTable.Cols.OIL_CARD + "," +
                BillTable.Cols.DEPARTURE + "," +
                BillTable.Cols.DESTINATION  + "," +
                BillTable.Cols.GOOD_TYPE + "," +
                BillTable.Cols.PRICE + "," +
                BillTable.Cols.WEIGHT + "," +
                BillTable.Cols.TOTAL_PRICE + "," +
                BillTable.Cols.TOTAL_PRICE_SOLVE + "," +
                BillTable.Cols.OIL_CARD_SOLVE + "," +
                BillTable.Cols.NOTES +
                ")"
        );

        db.execSQL("create table "+ BillRankTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                BillRankTable.Cols.UUID + "," +
                BillRankTable.Cols.YEAR + "," +
                BillRankTable.Cols.MONTH + "," +
                BillRankTable.Cols.DAY + "," +
                BillRankTable.Cols.FORM +
                ")"
        );

        db.execSQL("create table "+ MaintenanceTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                MaintenanceTable.Cols.UUID + "," +
                MaintenanceTable.Cols.YEAR + "," +
                MaintenanceTable.Cols.MONTH + "," +
                MaintenanceTable.Cols.DAY + "," +
                MaintenanceTable.Cols.NAME + "," +
                MaintenanceTable.Cols.TOTAL_PRICE + "," +
                MaintenanceTable.Cols.NOTES +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
