package com.eric.mybill.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eric.mybill.database.BillBaseHelper;
import com.eric.mybill.database.BillCursorWrapper;
import com.eric.mybill.database.DbSchema.BillTable;
import com.eric.mybill.util.DateTransform;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class BillLab {

    private static BillLab sBillLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static BillLab get(Context context){
        if(sBillLab == null){
            sBillLab = new BillLab(context);
        }
        return sBillLab;
    }

    public File getPhotoFile(Bill bill){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, bill.getPhotoFilename());
    }

    private BillLab(Context context){
        mContext = context;
        mDatabase = new BillBaseHelper(context)
                .getWritableDatabase();

    }

    public List<Bill> getBills(){
        List<Bill>bills = new ArrayList<>();

        BillCursorWrapper cursor = queryBills(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                bills.add(cursor.getBill());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }


        return bills;
    }

    public List<Bill> getBills(UUID uuid){
        List<Bill>bills = new ArrayList<>();

        BillCursorWrapper cursor = queryBills(
                BillTable.Cols.TIMES + " = ?",
                new String[]{uuid.toString()}
        );

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                bills.add(cursor.getBill());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }


        return bills;
    }

    public Bill getBill(UUID uuid){
        BillCursorWrapper cursor = queryBills(
                BillTable.Cols.UUID +" = ?",
                new String[]{uuid.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getBill();
        }finally {
            cursor.close();
        }
    }

    public void handleBills(List<Bill> bills){
        Iterator<Bill> iterator = bills.iterator();
        while (iterator.hasNext()){
            Bill bill = iterator.next();
            if(bill.isNew()){
                addBill(bill);
            }else {
                updateBill(bill);
            }
        }
    }

    public void addBill(Bill bill){
        ContentValues values = getContentValues(bill);

        mDatabase.insert(BillTable.NAME, null,values);
    }

    public void updateBill(Bill bill){
        String uuidString = bill.getUUID().toString();
        ContentValues values = getContentValues(bill);
        mDatabase.update(BillTable.NAME, values,
                BillTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void deleteBill(UUID uuid){
        mDatabase.delete(BillTable.NAME,
                BillTable.Cols.TIMES + "= ?",
                new String[] {uuid.toString()});
    }

    private BillCursorWrapper queryBills(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                BillTable.NAME,
                null,//Colums - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new BillCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Bill bill){
        ContentValues values = new ContentValues();
        values.put(BillTable.Cols.TIMES, bill.getTimes().toString());
        values.put(BillTable.Cols.UUID, bill.getUUID().toString());
        int dateInfo[] = DateTransform.fromDate(bill.getCreateDate());
        values.put(BillTable.Cols.YEAR, dateInfo[0]);
        values.put(BillTable.Cols.MONTH, dateInfo[1]);
        values.put(BillTable.Cols.DAY, dateInfo[2]);
        values.put(BillTable.Cols.OIL_CARD, bill.getOilCard());
        values.put(BillTable.Cols.DEPARTURE, bill.getDeparture());
        values.put(BillTable.Cols.DESTINATION, bill.getDestination());
        values.put(BillTable.Cols.GOOD_TYPE, bill.getGoodType());
        values.put(BillTable.Cols.PRICE, bill.getPrice());
        values.put(BillTable.Cols.WEIGHT, bill.getWeight());
        values.put(BillTable.Cols.TOTAL_PRICE, bill.getTotalPrice());
        values.put(BillTable.Cols.TOTAL_PRICE_SOLVE, bill.isTotalPriceSolve());
        values.put(BillTable.Cols.OIL_CARD_SOLVE, bill.isOilCardSolve());
        values.put(BillTable.Cols.NOTES, bill.getNotes());

        return values;
    }
}
