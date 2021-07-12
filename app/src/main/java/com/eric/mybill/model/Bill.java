package com.eric.mybill.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Bill implements Serializable {

    private UUID mTimes;

    private UUID mUUID;
    private Date mCreateDate;
    private String mDeparture;
    private String mDestination;
    private String mGoodType;
    private int oilCard;
    private int mPrice;
    private int mWeight;
    private int mTotalPrice;
    private String mNotes;
    private boolean mTotalPriceSolve;
    private boolean mOilCardSolve;

    private boolean isNew;

    public Bill(){
        mTimes = null;

        mUUID = UUID.randomUUID();
        mCreateDate = new Date(System.currentTimeMillis());
        mDeparture = "";
        mDestination = "";
        mGoodType = "";
        mPrice = 0;
        mWeight = 0;
        mTotalPrice = 0;
        mNotes = "";
        oilCard = 0;
        mTotalPriceSolve = false;
        mOilCardSolve = false;

        isNew = false;
    }

    public UUID getTimes() {
        return mTimes;
    }

    public void setTimes(UUID times) {
        mTimes = times;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(Date date) {
        mCreateDate = date;
    }

    public String getDeparture() {
        return mDeparture;
    }

    public void setDeparture(String departure) {
        mDeparture = departure;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getGoodType() {
        return mGoodType;
    }

    public void setGoodType(String goodType) {
        mGoodType = goodType;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
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

    public int getOilCard() {
        return oilCard;
    }

    public void setOilCard(int oilCard) {
        this.oilCard = oilCard;
    }

    public boolean isTotalPriceSolve() {
        return mTotalPriceSolve;
    }

    public void setTotalPriceSolve(boolean totalPriceSolve) {
        mTotalPriceSolve = totalPriceSolve;
    }

    public boolean isOilCardSolve() {
        return mOilCardSolve;
    }

    public void setOilCardSolve(boolean oilCardSolve) {
        mOilCardSolve = oilCardSolve;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getPhotoFilename(){
        return "IMG_" + getUUID().toString() + ".jpg";
    }
}
