package com.eric.mybill.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Maintenance implements Serializable {

    private UUID mUUID;
    private String mName;
    private Date mCreateDate;
    private int mTotalPrice;
    private String mNotes;

    public Maintenance(){

        mUUID = UUID.randomUUID();
        mName = "";
        mCreateDate = new Date(System.currentTimeMillis());
        mTotalPrice = 0;
        mNotes = "";
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public int getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        mTotalPrice = totalPrice;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }
}
