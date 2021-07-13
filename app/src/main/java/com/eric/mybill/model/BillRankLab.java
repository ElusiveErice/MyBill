package com.eric.mybill.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eric.mybill.database.BillBaseHelper;
import com.eric.mybill.database.BillCursorWrapper;
import com.eric.mybill.database.DbSchema.BillRankTable;
import com.eric.mybill.util.DateTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class BillRankLab {

    private static BillRankLab sBillRankLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static BillRankLab get(Context context){
        if(sBillRankLab == null){
            sBillRankLab = new BillRankLab(context);
        }

        return sBillRankLab;
    }

    private BillRankLab(Context context){
        mContext = context;
        mDatabase = new BillBaseHelper(context)
                .getWritableDatabase();
    }

    public List<BillRank> getBillRanks(){
        List<BillRank> billRanks = new ArrayList<>();

        BillCursorWrapper cursor = queryBills(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                billRanks.add(cursor.getBillRank());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }


        return billRanks;
    }

    public List<BillRank> getBillRanks(int year, int month){
        List<BillRank> billRanks = new ArrayList<>();

        BillCursorWrapper cursor;


        if(month == 0){
            cursor = queryBills(
                    BillRankTable.Cols.YEAR + " = " + year,
                    null
            );
        }else {
            cursor = queryBills(
                    BillRankTable.Cols.YEAR + " = " + year + " and " +
                    BillRankTable.Cols.MONTH + " = " + month,
                    null
            );
        }

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                billRanks.add(cursor.getBillRank());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }


        return billRanks;
    }

    public List<BillRank> getNotPayOffBills(){
        List<BillRank> billRanks = new ArrayList<>();

        BillCursorWrapper cursor = queryBills(
                BillRankTable.Cols.FORM + " = 0",
                null
        );

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                billRanks.add(cursor.getBillRank());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }


        return billRanks;
    }

    public void addBillRank(BillRank billRank){
        ContentValues values = getContentValues(billRank);

        mDatabase.insert(BillRankTable.NAME, null,values);
    }

    public void updateBillRank(BillRank billRank){
        String uuidString = billRank.getUUID().toString();
        ContentValues values = getContentValues(billRank);
        mDatabase.update(BillRankTable.NAME, values,
                BillRankTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void deleteBill(UUID uuid){
        mDatabase.delete(BillRankTable.NAME,
                BillRankTable.Cols.UUID + "= ?",
                new String[] {uuid.toString()});
        BillLab.get(mContext).deleteBill(uuid);
    }

    public Set<Integer> getYears(){
        Set<Integer> years = new TreeSet<Integer>();

        BillCursorWrapper cursor = queryBills(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                years.add(cursor.getYear());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }


        return years;
    }

    private BillCursorWrapper queryBills(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                BillRankTable.NAME,
                null,//Colums - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new BillCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(BillRank billRank){
        ContentValues values = new ContentValues();
        values.put(BillRankTable.Cols.UUID, billRank.getUUID().toString());
        int dateInfo[] = DateTransform.fromDate(billRank.getCreateDate());
        values.put(BillRankTable.Cols.YEAR, dateInfo[0]);
        values.put(BillRankTable.Cols.MONTH, dateInfo[1]);
        values.put(BillRankTable.Cols.DAY, dateInfo[2]);
        values.put(BillRankTable.Cols.FORM, billRank.isForm());

        return values;
    }
}
