package com.eric.mybill.ui.activity;

import androidx.fragment.app.Fragment;

import com.eric.mybill.base.SingleFragmentActivity;
import com.eric.mybill.ui.fragment.WriteBillFragment;

public class WriteBillActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return WriteBillFragment.newInstance();
    }
}
