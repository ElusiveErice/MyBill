package com.eric.mybill.ui.activity;

import androidx.fragment.app.Fragment;

import com.eric.mybill.base.SingleFragmentActivity;
import com.eric.mybill.ui.fragment.WriteMaintenanceFragment;

public class WriteMaintenanceActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return WriteMaintenanceFragment.newInstance();
    }
}
