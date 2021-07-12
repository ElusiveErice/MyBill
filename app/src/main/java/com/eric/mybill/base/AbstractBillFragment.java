package com.eric.mybill.base;

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
import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.model.BillRankLab;
import com.eric.mybill.ui.fragment.DatePickerFragment;
import com.eric.mybill.util.DateTransform;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class AbstractBillFragment extends Fragment {

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    public static final String ARG_BILL_RANK = "bill_rank";
    public static final String ARG_IS_NES = "is_new";
    public static final String ARG_BILLS = "bills";

    private Button mNewBillButton;
    private RecyclerView mWriteBillRecyclerView;

    private WriteBillAdapter mWriteBillAdapter;

    protected boolean isNewBill;
    protected BillRank mBillRank;
    protected List<Bill> mBills;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mBillRank = (BillRank) getArguments().getSerializable(ARG_BILL_RANK);
        isNewBill = getArguments().getBoolean(ARG_IS_NES);
        mBills = (List<Bill>) getArguments().getSerializable(ARG_BILLS);
        mWriteBillAdapter = new WriteBillAdapter(mBills);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_edit_bill, container, false);

        mWriteBillRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_write_bill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mWriteBillRecyclerView.setLayoutManager(linearLayoutManager);
        mWriteBillRecyclerView.setAdapter(mWriteBillAdapter);

        mNewBillButton = (Button)view.findViewById(R.id.button_new_bill);
        mNewBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bill bill = new Bill();
                bill.setNew(true);
                bill.setTimes(mBillRank.getUUID());
                mBills.add(bill);
                mWriteBillAdapter.notifyDataSetChanged();
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
                Iterator<Bill> iterator = mBills.iterator();
                mBillRank.setForm(true);
                while (iterator.hasNext()){
                    if(iterator.next().isTotalPriceSolve() == false){
                        mBillRank.setForm(false);
                    }
                }
                if(isNewBill){
                    BillRankLab.get(getActivity()).addBillRank(mBillRank);
                }else {
                    BillRankLab.get(getActivity()).updateBillRank(mBillRank);
                }
                BillLab.get(getActivity()).handleBills(mBills);
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
            UUID uuid = (UUID) data.getSerializableExtra(DatePickerFragment.EXTRA_UUID);
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            Iterator<Bill> iterator = mBills.iterator();
            while (iterator.hasNext()){
                Bill bill = iterator.next();
                if(bill.getUUID().equals( uuid)){
                    bill.setCreateDate(date);
                    break;
                }
            }
            mWriteBillAdapter.notifyDataSetChanged();
        }
    }

    private class WriteBillHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Bill mBill;

        private Button mCreateDateButton;
        private EditText mGoodTypeEditText;
        private EditText mDepartureEditText;
        private EditText mDestinationEditText;
        private EditText mPriceEditText;
        private EditText mWeightEditText;
        private EditText mTotalPriceEditText;
        private EditText mOilCardEditText;
        private EditText mNotesEditText;
        private ImageButton mNotesImageButton;
        private CheckBox mTotalPriceCheckBox;
        private CheckBox mOilCardCheckBox;

        private ImageButton mClearButton;

        public WriteBillHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_edit_bill, parent, false));

            mCreateDateButton = (Button)itemView.findViewById(R.id.button_create_date);
            mGoodTypeEditText = (EditText)itemView.findViewById(R.id.edit_text_good_type);
            mDepartureEditText = (EditText)itemView.findViewById(R.id.edit_text_departure);
            mDestinationEditText = (EditText)itemView.findViewById(R.id.edit_text_destination);
            mPriceEditText = (EditText)itemView.findViewById(R.id.edit_text_price);
            mWeightEditText = (EditText)itemView.findViewById(R.id.edit_text_weight);
            mTotalPriceEditText = (EditText)itemView.findViewById(R.id.edit_text_total_price);
            mOilCardEditText = (EditText)itemView.findViewById(R.id.edit_text_oil_card);
            mNotesEditText = (EditText)itemView.findViewById(R.id.edit_text_notes);
            mNotesImageButton = (ImageButton)itemView.findViewById(R.id.image_button_notes);
            mTotalPriceCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_total_price);
            mOilCardCheckBox = (CheckBox)itemView.findViewById(R.id.checkbox_oil_card);
            mClearButton = (ImageButton)itemView.findViewById(R.id.image_button_clear);

            mGoodTypeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setGoodType(mGoodTypeEditText.getText().toString());
                }
            });
            mDepartureEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setDeparture(mDepartureEditText.getText().toString());
                }
            });
            mDestinationEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setDestination(mDestinationEditText.getText().toString());
                }
            });
            mPriceEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setPrice(Integer.parseInt(mPriceEditText.getText().toString()));
                    mTotalPriceEditText.setText(mBill.getPrice()*mBill.getWeight() + "");
                }
            });
            mWeightEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setWeight(Integer.parseInt(mWeightEditText.getText().toString()));
                    mTotalPriceEditText.setText(mBill.getPrice()*mBill.getWeight() + "");
                }
            });
            mTotalPriceEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setTotalPrice(Integer.parseInt(mTotalPriceEditText.getText().toString()));
                }
            });
            mOilCardEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setOilCard(Integer.parseInt(mOilCardEditText.getText().toString()));
                }
            });
            mNotesEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mBill.setNotes(mNotesEditText.getText().toString());
                }
            });

            mTotalPriceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mBill.setTotalPriceSolve(isChecked);
                }
            });

            mOilCardCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mBill.setOilCardSolve(isChecked);
                }
            });

            mClearButton.setOnClickListener(this);
            mCreateDateButton.setOnClickListener(this);
            mNotesImageButton.setOnClickListener(this);

            mTotalPriceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mBill.setTotalPriceSolve(isChecked);
                    if(isChecked){
                        mOilCardCheckBox.setSelected(true);
                    }
                }
            });
            mOilCardCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mBill.setOilCardSolve(isChecked);
                }
            });
        }

        public void bind(Bill bill){
            mBill = bill;
            mGoodTypeEditText.setText(bill.getGoodType());
            mDepartureEditText.setText(bill.getDeparture());
            mDestinationEditText.setText(bill.getDestination());
            mPriceEditText.setText("" + bill.getPrice());
            mWeightEditText.setText("" + bill.getWeight());
            mTotalPriceEditText.setText("" + bill.getTotalPrice());
            mOilCardEditText.setText("" + bill.getOilCard());
            mNotesEditText.setText(bill.getNotes());
            mTotalPriceCheckBox.setChecked(bill.isTotalPriceSolve());
            mOilCardCheckBox.setChecked(bill.isOilCardSolve());
            mCreateDateButton.setText(DateTransform.stringFromDate(bill.getCreateDate()));

        }

        @Override
        public void onClick(View v) {
            if(v == mCreateDateButton){
                DatePickerFragment dialog = DatePickerFragment.newInstance(mBill.getCreateDate(), mBill.getUUID());
                dialog.setTargetFragment(AbstractBillFragment.this, REQUEST_DATE);
                dialog.show(getFragmentManager(), DIALOG_DATE);
            }else if(v == mNotesImageButton){
                if(mNotesEditText.getVisibility() == View.GONE){
                    mNotesEditText.setVisibility(View.VISIBLE);
                }else {
                    mNotesEditText.setVisibility(View.GONE);
                }
            }else if(v == mClearButton){
                mBills.remove(mBill);
                mWriteBillAdapter.notifyDataSetChanged();
            }
        }
    }

    private class WriteBillAdapter extends RecyclerView.Adapter<WriteBillHolder>{

        private List<Bill> mBillList;

        public WriteBillAdapter(List<Bill> bills){
            mBillList = bills;
        }

        @NonNull
        @Override
        public WriteBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WriteBillHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WriteBillHolder holder, int position) {
            Bill bill = mBillList.get(position);
            holder.bind(bill);
        }

        @Override
        public int getItemCount() {
            return mBillList.size();
        }
    }
}
