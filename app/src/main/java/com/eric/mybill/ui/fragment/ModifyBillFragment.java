package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.eric.mybill.R;
import com.eric.mybill.base.AbstractBillFragment;
import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.util.DateTransform;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ModifyBillFragment extends AbstractBillFragment {

    public static ModifyBillFragment newInstance(BillRank billRank, List<Bill> bills){
        Bundle args = new Bundle();

        args.putSerializable(ARG_BILLS, (Serializable) bills);
        args.putSerializable(ARG_IS_NES, false);
        args.putSerializable(ARG_BILL_RANK, billRank);

        ModifyBillFragment fragment = new ModifyBillFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
