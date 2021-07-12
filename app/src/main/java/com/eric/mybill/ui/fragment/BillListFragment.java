package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eric.mybill.R;
import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.model.BillRankLab;
import com.eric.mybill.model.Maintenance;
import com.eric.mybill.model.MaintenanceLab;
import com.eric.mybill.ui.activity.ModifyBillActivity;
import com.eric.mybill.ui.activity.ModifyMaintenanceActivity;
import com.eric.mybill.util.DateTransform;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BillListFragment extends Fragment {

    private static final int REQUEST_DELETE = 1;

    private static final String DIALOG_DELETE = "DialogDelete";

    private Spinner mMonthSpinner;
    private BottomNavigationView mTypeBottomNavigationView;
    private TextView mSelectYearMonthTextView;
    private ImageButton mProImageButton;
    private ImageButton mNextImageButton;
    private RecyclerView mRecyclerView;

    private int mYear;
    private int mMonth;

    private boolean mIsBill;

    public static Fragment newInstance() {
        return new BillListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mYear = DateTransform.getThisYear();
        mIsBill = true;
        mMonth = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);

        mTypeBottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottom_type);
        mSelectYearMonthTextView = (TextView)view.findViewById(R.id.text_view_select_month);
        mMonthSpinner = (Spinner)view.findViewById(R.id.spinner_month);
        mProImageButton = (ImageButton)view.findViewById(R.id.image_button_pro);
        mNextImageButton = (ImageButton)view.findViewById(R.id.image_button_next);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_bill_rank_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mSelectYearMonthTextView.setText(mYear + "");

        mTypeBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_bill_list:
                        mIsBill = true;
                        setUp();
                        break;
                    case R.id.item_maintenance_list:
                        mIsBill = false;
                        setUp();
                        break;
                }

                return true;
            }
        });

        mProImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear--;
                mSelectYearMonthTextView.setText(mYear + "");
                setUp();
            }
        });

        mNextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear++;
                mSelectYearMonthTextView.setText(mYear + "");
                setUp();
            }
        });

        mMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMonth = position;
                setUp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DELETE){
            UUID uuid = (UUID) data.getSerializableExtra(DeleteBillFragment.EXTRA_BILL_RANK);
            BillRankLab.get(getActivity()).deleteBill(uuid);
            MaintenanceLab.get(getActivity()).deleteMaintenance(uuid);
            setUp();
        }
    }

    private void setUp(){
        if(isAdded()){
            if(mIsBill){
                List<BillRank> billRanks = BillRankLab.get(getActivity()).getBillRanks(mYear, mMonth);
                BillRankAdapter billRankAdapter = new BillRankAdapter(billRanks);
                mRecyclerView.setAdapter(billRankAdapter);
            }else {
                List<Maintenance> maintenances = MaintenanceLab.get(getActivity()).getMaintenance(mYear, mMonth);
                MaintenanceAdapter maintenanceAdapter = new MaintenanceAdapter(maintenances);
                mRecyclerView.setAdapter(maintenanceAdapter);
            }
        }
    }

    private class BillRankHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton mOkImageButton;
        private ImageButton mEditBillRankImageButton;
        private ImageButton mClearBillRankImageButton;
        private RecyclerView mBillRankRecyclerView;

        private BillAdapter mBillAdapter;

        private BillRank mBillRank;
        private List<Bill> mBillList;

        public BillRankHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_bill_rank, parent, false));

            mOkImageButton = (ImageButton)itemView.findViewById(R.id.image_button_ok);
            mEditBillRankImageButton = (ImageButton)itemView.findViewById(R.id.image_button_edit_bill);
            mClearBillRankImageButton = (ImageButton)itemView.findViewById(R.id.image_button_clear_bill_rank);
            mBillRankRecyclerView = (RecyclerView)itemView.findViewById(R.id.recycler_view_bill_rank);
            mBillRankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mOkImageButton.setOnClickListener(this);
            mClearBillRankImageButton.setOnClickListener(this);
            mEditBillRankImageButton.setOnClickListener(this);
        }

        public void bind(BillRank billRank){
            mBillRank = billRank;
            mBillList = BillLab.get(getActivity()).getBills(billRank.getUUID());
            mBillAdapter = new BillAdapter(mBillList);
            mBillRankRecyclerView.setAdapter(mBillAdapter);
        }

        @Override
        public void onClick(View v) {
            if(v == mClearBillRankImageButton){
                DeleteBillFragment dialog = DeleteBillFragment.newInstance(mBillRank.getUUID());
                dialog.setTargetFragment(BillListFragment.this, REQUEST_DELETE);
                dialog.show(getFragmentManager(), DIALOG_DELETE);
            }else if(v == mEditBillRankImageButton){
                Intent intent = new Intent(getActivity(), ModifyBillActivity.class);
                intent.putExtra(ModifyBillActivity.ARG_BILL_RANK, mBillRank);
                startActivity(intent);
            }else if(v == mOkImageButton){
                mBillRank.setForm(true);
                Iterator<Bill> iterator = mBillList.iterator();
                while (iterator.hasNext()){
                    iterator.next().setTotalPriceSolve(true);
                }
                BillRankLab.get(getActivity()).updateBillRank(mBillRank);
                BillLab.get(getActivity()).handleBills(mBillList);
                setUp();
            }
        }

        private class BillHolder extends RecyclerView.ViewHolder{

            private LinearLayout mBillLinearLayout;

            private TextView mBillDateTextView;
            private TextView mBillGoodTypeTextView;
            private TextView mBillDepartureTextView;
            private TextView mBillDestinationTextView;
            private TextView mBillTotalPriceTextView;

            private Bill mBill;

            public BillHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_bill, parent, false));

                mBillLinearLayout = (LinearLayout)itemView.findViewById(R.id.linear_layout_bill);
                mBillDateTextView = (TextView)itemView.findViewById(R.id.text_view_bill_date);
                mBillGoodTypeTextView = (TextView)itemView.findViewById(R.id.text_view_bill_good_type);
                mBillDepartureTextView = (TextView)itemView.findViewById(R.id.text_view_bill_departure);
                mBillDestinationTextView = (TextView)itemView.findViewById(R.id.text_view_bill_destination);
                mBillTotalPriceTextView = (TextView)itemView.findViewById(R.id.text_view_bill_total_price);
            }

            public void bind(Bill bill){
                mBill = bill;
                mBillDateTextView.setText(DateTransform.stringFromDate2(mBill.getCreateDate()));
                mBillGoodTypeTextView.setText(mBill.getGoodType());
                mBillDepartureTextView.setText(mBill.getDeparture());
                mBillDestinationTextView.setText(mBill.getDestination());
                mBillTotalPriceTextView.setText(mBill.getTotalPrice() + "");
                if(mBill.isTotalPriceSolve()){
                    mBillLinearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.border2));
                }else {
                    mBillLinearLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.border3));
                }
            }
        }

        private class BillAdapter extends RecyclerView.Adapter<BillHolder>{

            private List<Bill> mBillList;

            public BillAdapter(List<Bill> bills){
                mBillList = bills;
            }

            @NonNull
            @Override
            public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                return new BillHolder(inflater, parent);
            }

            @Override
            public void onBindViewHolder(@NonNull BillHolder holder, int position) {
                Bill bill = mBillList.get(position);
                holder.bind(bill);
            }

            @Override
            public int getItemCount() {
                return mBillList.size();
            }
        }
    }

    private class BillRankAdapter extends RecyclerView.Adapter<BillRankHolder>{

        private List<BillRank> mBillRanks;

        public BillRankAdapter(List<BillRank> billRanks){
            mBillRanks = billRanks;
        }

        @NonNull
        @Override
        public BillRankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BillRankHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BillRankHolder holder, int position) {
            BillRank billRank = mBillRanks.get(position);
            holder.bind(billRank);
        }

        @Override
        public int getItemCount() {
            return mBillRanks.size();
        }
    }

    private class MaintenanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mMaintenanceDateTextView;
        private TextView mMaintenanceNameTextView;
        private TextView mMaintenanceTotalPriceTextView;
        private ImageButton mMaintenanceClearImageButton;

        private Maintenance mMaintenance;

        public MaintenanceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_maintenance, parent, false));

            mMaintenanceDateTextView = (TextView)itemView.findViewById(R.id.text_view_maintenance_date);
            mMaintenanceNameTextView = (TextView)itemView.findViewById(R.id.text_view_maintenance_name);
            mMaintenanceTotalPriceTextView = (TextView)itemView.findViewById(R.id.text_view_maintenance_total_price);
            mMaintenanceClearImageButton = (ImageButton)itemView.findViewById(R.id.image_button_clear_maintenance);

            mMaintenanceClearImageButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Maintenance maintenance){
            mMaintenance = maintenance;
            mMaintenanceDateTextView.setText(DateTransform.stringFromDate2(maintenance.getCreateDate()));
            mMaintenanceNameTextView.setText(maintenance.getName());
            mMaintenanceTotalPriceTextView.setText(maintenance.getTotalPrice() + "");
        }

        @Override
        public void onClick(View v) {
            if(v == mMaintenanceClearImageButton){
                DeleteBillFragment dialog = DeleteBillFragment.newInstance(mMaintenance.getUUID());
                dialog.setTargetFragment(BillListFragment.this, REQUEST_DELETE);
                dialog.show(getFragmentManager(), DIALOG_DELETE);
            }else {
                Intent intent = new Intent(getActivity(), ModifyMaintenanceActivity.class);
                intent.putExtra(ModifyMaintenanceActivity.ARG_MAINTENANCE, mMaintenance);
                startActivity(intent);
            }
        }
    }

    private class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceHolder>{

        private List<Maintenance> mMaintenances;

        public MaintenanceAdapter(List<Maintenance> maintenances){
            mMaintenances = maintenances;
        }

        @NonNull
        @Override
        public MaintenanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MaintenanceHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MaintenanceHolder holder, int position) {
            Maintenance maintenance = mMaintenances.get(position);
            holder.bind(maintenance);
        }

        @Override
        public int getItemCount() {
            return mMaintenances.size();
        }
    }
}
