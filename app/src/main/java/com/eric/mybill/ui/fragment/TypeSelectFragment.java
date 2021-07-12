package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.eric.mybill.R;
import com.eric.mybill.ui.activity.WriteBillActivity;
import com.eric.mybill.ui.activity.WriteMaintenanceActivity;

public class TypeSelectFragment extends DialogFragment {

    private RadioButton mBillRecordRadioButton;

    public static TypeSelectFragment newInstance(){
        return new TypeSelectFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_type_select, null);

        mBillRecordRadioButton = (RadioButton)v.findViewById(R.id.radio_button_bill_record);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.type_select)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent;
        if(mBillRecordRadioButton.isChecked()){
            intent = new Intent(getActivity(), WriteBillActivity.class);
        }else {
            intent = new Intent(getActivity(), WriteMaintenanceActivity.class);
        }

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
