package com.eric.mybill.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.eric.mybill.model.Bill;
import com.eric.mybill.database.DbSchema.BillTable;
import com.eric.mybill.database.DbSchema.MaintenanceTable;
import com.eric.mybill.database.DbSchema.BillRankTable;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.model.Maintenance;
import com.eric.mybill.util.DateTransform;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public class BillCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */

    public BillCursorWrapper(Cursor cursor){
        super(cursor);

    }

    public int getYear(){
        return getInt(getColumnIndex(BillTable.Cols.YEAR));
    }

    public Maintenance getMaintenance(){
        String uuidString = getString(getColumnIndex(MaintenanceTable.Cols.UUID));
        int year = getInt(getColumnIndex(MaintenanceTable.Cols.YEAR));
        int month = getInt(getColumnIndex(MaintenanceTable.Cols.MONTH));
        int day = getInt(getColumnIndex(MaintenanceTable.Cols.DAY));
        int totalPrice = getInt(getColumnIndex(MaintenanceTable.Cols.TOTAL_PRICE));
        String name = getString(getColumnIndex(MaintenanceTable.Cols.NAME));
        String notes = getString(getColumnIndex(MaintenanceTable.Cols.NOTES));

        Maintenance maintenance = new Maintenance();
        maintenance.setUUID(UUID.fromString(uuidString));
        try{
            Date date = DateTransform.toDate(year, month, day);
            maintenance.setCreateDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        maintenance.setTotalPrice(totalPrice);
        maintenance.setName(name);
        maintenance.setNotes(notes);

        return maintenance;
    }
    public Bill getBill(){
        String time = getString(getColumnIndex(BillTable.Cols.TIMES));
        String uuidString = getString(getColumnIndex(BillTable.Cols.UUID));
        int year = getInt(getColumnIndex(BillTable.Cols.YEAR));
        int month = getInt(getColumnIndex(BillTable.Cols.MONTH));
        int day = getInt(getColumnIndex(BillTable.Cols.DAY));
        int oilCard = getInt(getColumnIndex(BillTable.Cols.OIL_CARD));
        String departure = getString(getColumnIndex(BillTable.Cols.DEPARTURE));
        String destination = getString(getColumnIndex(BillTable.Cols.DESTINATION));
        String goodType = getString(getColumnIndex(BillTable.Cols.GOOD_TYPE));
        int price = getInt(getColumnIndex(BillTable.Cols.PRICE));
        int weight = getInt(getColumnIndex(BillTable.Cols.WEIGHT));
        int totalPrice = getInt(getColumnIndex(BillTable.Cols.TOTAL_PRICE));
        boolean totalPriceSolve = getInt(getColumnIndex(BillTable.Cols.TOTAL_PRICE_SOLVE)) > 0;
        boolean oilCardSolve = getInt(getColumnIndex(BillTable.Cols.OIL_CARD_SOLVE)) > 0;

        String notes = getString(getColumnIndex(BillTable.Cols.NOTES));

        Bill bill = new Bill();
        bill.setTimes(UUID.fromString(time));
        bill.setUUID(UUID.fromString(uuidString));
        try {
            Date date = DateTransform.toDate(year, month, day);
            bill.setCreateDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        bill.setDeparture(departure);
        bill.setDestination(destination);
        bill.setGoodType(goodType);
        bill.setOilCard(oilCard);
        bill.setPrice(price);
        bill.setWeight(weight);
        bill.setTotalPrice(totalPrice);
        bill.setTotalPriceSolve(totalPriceSolve);
        bill.setOilCardSolve(oilCardSolve);
        bill.setNotes(notes);

        return bill;
    }

    public BillRank getBillRank(){
        String uuidString = getString(getColumnIndex(BillRankTable.Cols.UUID));
        int year = getInt(getColumnIndex(BillRankTable.Cols.YEAR));
        int month = getInt(getColumnIndex(BillRankTable.Cols.MONTH));
        int day = getInt(getColumnIndex(BillRankTable.Cols.DAY));
        boolean form = getInt(getColumnIndex(BillRankTable.Cols.FORM)) > 0;

        BillRank billRank = new BillRank();
        billRank.setUUID(UUID.fromString(uuidString));
        try {
            Date date = DateTransform.toDate(year, month, day);
            billRank.setCreateDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        billRank.setForm(form);

        return billRank;
    }
}
