package com.eric.mybill.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class BillRank implements Serializable {

    private UUID mUUID;
    private Date mCreateDate;

    private boolean mForm;

    public BillRank(){
        mUUID = UUID.randomUUID();
        mCreateDate = new Date(System.currentTimeMillis());
        mForm = false;
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

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public boolean isForm() {
        return mForm;
    }

    public void setForm(boolean form) {
        mForm = form;
    }
}
