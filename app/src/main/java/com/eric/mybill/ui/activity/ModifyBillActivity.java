package com.eric.mybill.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.eric.mybill.base.SingleFragmentActivity;
import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.ui.fragment.ModifyBillFragment;

import java.util.List;

public class ModifyBillActivity extends SingleFragmentActivity {

    public static final String ARG_BILL_RANK = "bill_rank";

    private BillRank mBillRank;
    private List<Bill> mBillList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Intent intent = getIntent();
        mBillRank = (BillRank) intent.getSerializableExtra(ARG_BILL_RANK);
        mBillList = BillLab.get(this).getBills(mBillRank.getUUID());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {

        return ModifyBillFragment.newInstance(mBillRank, mBillList);
    }

}
