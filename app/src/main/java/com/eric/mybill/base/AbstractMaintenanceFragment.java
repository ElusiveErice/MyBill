package com.eric.mybill.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.eric.mybill.model.Maintenance;
import com.eric.mybill.model.MaintenanceLab;
import com.eric.mybill.ui.fragment.DatePickerFragment;
import com.eric.mybill.util.DateTransform;

import java.util.Date;

public class AbstractMaintenanceFragment extends Fragment {

    public static final String ARG_IS_NEW = "is_new";
    public static final String ARG_MAINTENANCE = "Maintenance";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private EditText mMaintenanceNameEditText;
    private Button mCreateDateButton;
    private EditText mMaintenanceTotalPriceEditText;
    private EditText mMaintenanceDetailInfoEditText;

    private Maintenance mMaintenance;
    private boolean mIsNew;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mIsNew = getArguments().getBoolean(ARG_IS_NEW);
        mMaintenance = (Maintenance) getArguments().getSerializable(ARG_MAINTENANCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_maintenance, container, false);

        mMaintenanceNameEditText = (EditText)view.findViewById(R.id.edit_text_maintenance_name);
        mCreateDateButton = (Button)view.findViewById(R.id.button_create_date);
        mMaintenanceTotalPriceEditText = (EditText)view.findViewById(R.id.edit_text_maintenance_total_price);
        mMaintenanceDetailInfoEditText = (EditText)view.findViewById(R.id.edit_text_maintenance_detail_info);

        mMaintenanceNameEditText.setText(mMaintenance.getName());
        mMaintenanceTotalPriceEditText.setText(mMaintenance.getTotalPrice() + "");
        mMaintenanceDetailInfoEditText.setText(mMaintenance.getNotes());
        mCreateDateButton.setText(DateTransform.stringFromDate(mMaintenance.getCreateDate()));

        mCreateDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(mMaintenance.getCreateDate());
                dialog.setTargetFragment(AbstractMaintenanceFragment.this, REQUEST_DATE);
                dialog.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.submit_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_submit:
                mMaintenance.setName(mMaintenanceNameEditText.getText().toString());
                mMaintenance.setTotalPrice(Integer.parseInt(mMaintenanceTotalPriceEditText.getText().toString()));
                mMaintenance.setNotes(mMaintenanceDetailInfoEditText.getText().toString());
                if(mIsNew){
                    MaintenanceLab.get(getActivity()).addMaintenance(mMaintenance);
                }else {
                    MaintenanceLab.get(getActivity()).updateMaintenance(mMaintenance);
                }
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCreateDateButton.setText(DateTransform.stringFromDate(date));
            mMaintenance.setCreateDate(date);
        }
    }
}
