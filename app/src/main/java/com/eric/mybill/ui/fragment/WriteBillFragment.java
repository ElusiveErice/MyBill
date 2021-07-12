package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eric.mybill.R;
import com.eric.mybill.base.AbstractBillFragment;
import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.model.BillRankLab;
import com.eric.mybill.util.DateTransform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class WriteBillFragment extends AbstractBillFragment {

    public static WriteBillFragment newInstance(){
        Bundle args = new Bundle();

        BillRank billRank = new BillRank();
        List<Bill> bills = new ArrayList<Bill>();
        Bill bill = new Bill();
        bill.setTimes(billRank.getUUID());
        bill.setNew(true);
        bills.add(bill);
        args.putSerializable(ARG_BILLS, (Serializable) bills);
        args.putSerializable(ARG_IS_NES, true);
        args.putSerializable(ARG_BILL_RANK, billRank);

        WriteBillFragment fragment = new WriteBillFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
