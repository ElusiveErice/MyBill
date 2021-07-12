package com.eric.mybill.ui.fragment;

import android.os.Bundle;

import com.eric.mybill.base.AbstractMaintenanceFragment;
import com.eric.mybill.model.Maintenance;

import java.io.Serializable;

public class ModifyMaintenanceFragment extends AbstractMaintenanceFragment {

    public static ModifyMaintenanceFragment newInstance(Maintenance maintenance){

        Bundle args = new Bundle();

        args.putSerializable(ARG_MAINTENANCE, maintenance);
        args.putBoolean(ARG_IS_NEW, false);
        ModifyMaintenanceFragment fragment = new ModifyMaintenanceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
