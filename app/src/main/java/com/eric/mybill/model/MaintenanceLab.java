package com.eric.mybill.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eric.mybill.database.BillBaseHelper;
import com.eric.mybill.database.BillCursorWrapper;
import com.eric.mybill.database.DbSchema.MaintenanceTable;
import com.eric.mybill.util.DateTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class MaintenanceLab {

    private static MaintenanceLab sMaintenanceRecordLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MaintenanceLab get(Context context){
        if(sMaintenanceRecordLab == null){
            sMaintenanceRecordLab = new MaintenanceLab(context);
        }
        return sMaintenanceRecordLab;
    }

    private MaintenanceLab(Context context){
        mContext = context;
        mDatabase = new BillBaseHelper(context)
                .getWritableDatabase();
    }

    public List<Maintenance> getMaintenance(){
        List<Maintenance> maintenances = new ArrayList<>();

        BillCursorWrapper cursor = queryBills(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                maintenances.add(cursor.getMaintenance());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return maintenances;
    }

    public List<Maintenance> getMaintenance(int year, int month){
        List<Maintenance> maintenances = new ArrayList<>();
        BillCursorWrapper cursor;
        if(month == 0){
            cursor = queryBills(
                    MaintenanceTable.Cols.YEAR + " = " + year,
                    null
            );
        }else {
            cursor = queryBills(
                    MaintenanceTable.Cols.YEAR + " = " + year + " and "
                    + MaintenanceTable.Cols.MONTH + " = " + month,
                    null
            );
        }


        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                maintenances.add(cursor.getMaintenance());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return maintenances;
    }

    public Maintenance getMaintenance(UUID uuid){
        BillCursorWrapper cursor = queryBills(
                MaintenanceTable.Cols.UUID +" = ?",
                new String[]{uuid.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMaintenance();
        }finally {
            cursor.close();
        }
    }

    public void addMaintenance(Maintenance maintenance){
        ContentValues values = getContentValues(maintenance);
        mDatabase.insert(MaintenanceTable.NAME, null,values);
    }

    public void updateMaintenance(Maintenance maintenance){
        String uuidString = maintenance.getUUID().toString();
        ContentValues values = getContentValues(maintenance);
        mDatabase.update(MaintenanceTable.NAME, values,
                MaintenanceTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    public void deleteMaintenance(UUID uuid){
        mDatabase.delete(MaintenanceTable.NAME,
                MaintenanceTable.Cols.UUID + "= ?",
                new String[] {uuid.toString()});
    }

    private BillCursorWrapper queryBills(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                MaintenanceTable.NAME,
                null,//Colums - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new BillCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Maintenance maintenance){
        ContentValues values = new ContentValues();
        values.put(MaintenanceTable.Cols.UUID, maintenance.getUUID().toString());
        int dateInfo[] = DateTransform.fromDate(maintenance.getCreateDate());
        values.put(MaintenanceTable.Cols.YEAR, dateInfo[0]);
        values.put(MaintenanceTable.Cols.MONTH, dateInfo[1]);
        values.put(MaintenanceTable.Cols.DAY, dateInfo[2]);
        values.put(MaintenanceTable.Cols.NAME, maintenance.getName());
        values.put(MaintenanceTable.Cols.TOTAL_PRICE, maintenance.getTotalPrice());
        values.put(MaintenanceTable.Cols.NOTES, maintenance.getNotes());

        return values;
    }
}
