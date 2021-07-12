package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.eric.mybill.R;
import com.eric.mybill.model.BillRank;

import java.util.UUID;

public class DeleteBillFragment extends DialogFragment {

    public static final String EXTRA_BILL_RANK =
            "com.eric.ui.fragment.DeleteBillFragment.bill_rank";

    private static final String ARG_Bill_RANK = "bill_rank";

    private UUID mUUID;

    public static DeleteBillFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_Bill_RANK, uuid);
        DeleteBillFragment fragment = new DeleteBillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        mUUID = (UUID) getArguments().getSerializable(ARG_Bill_RANK);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_delete_bill, null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(" ")
                .setView(v)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .create();
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BILL_RANK, mUUID);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
