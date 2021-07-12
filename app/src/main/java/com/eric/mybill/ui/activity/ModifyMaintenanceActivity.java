package com.eric.mybill.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.eric.mybill.base.SingleFragmentActivity;
import com.eric.mybill.model.Maintenance;
import com.eric.mybill.ui.fragment.ModifyMaintenanceFragment;

public class ModifyMaintenanceActivity extends SingleFragmentActivity {

    public static final String ARG_MAINTENANCE = "maintenance";

    private Maintenance mMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        mMaintenance = (Maintenance) getIntent().getSerializableExtra(ARG_MAINTENANCE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return ModifyMaintenanceFragment.newInstance(mMaintenance);
    }
}
