package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.eric.mybill.R;
import com.eric.mybill.base.AbstractMaintenanceFragment;
import com.eric.mybill.model.Maintenance;
import com.eric.mybill.util.DateTransform;

import java.io.Serializable;
import java.util.Date;

public class WriteMaintenanceFragment extends AbstractMaintenanceFragment {

    public static WriteMaintenanceFragment newInstance(){

        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_NEW, true);
        args.putSerializable(ARG_MAINTENANCE, new Maintenance());
        WriteMaintenanceFragment  fragment = new WriteMaintenanceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
