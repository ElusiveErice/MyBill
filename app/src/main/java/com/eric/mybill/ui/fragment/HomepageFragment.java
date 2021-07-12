package com.eric.mybill.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.eric.mybill.ui.activity.ModifyBillActivity;
import com.eric.mybill.util.DateTransform;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class HomepageFragment extends Fragment {

    private static final int REQUEST_TYPE = 0;
    private static final int REQUEST_DELETE = 1;

    private static final String DIALOG_DELETE = "DialogDelete";
    private static final String DIALOG_TYPE_SELECT = "DialogTypeSelect";

    private RecyclerView mBillRankListRecyclerView;
    private BillRankAdapter mBillRankAdapter;

    private List<BillRank> mBillRanks;

    public static Fragment newInstance() {
        return new HomepageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_homepage, container, false);


        mBillRankListRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_bill_rank_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        mBillRankListRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setUp();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.write_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_edit:
                TypeSelectFragment dialog = TypeSelectFragment.newInstance();
                dialog.setTargetFragment(this, REQUEST_TYPE);
                dialog.show(this.getFragmentManager(), DIALOG_TYPE_SELECT);
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

        if(requestCode == REQUEST_TYPE){
            startActivity(data);
        }else if(requestCode == REQUEST_DELETE){
            BillRankLab.get(getActivity()).deleteBill((UUID) data.getSerializableExtra(DeleteBillFragment.EXTRA_BILL_RANK));
            setUp();
        }
    }


    private void setUp(){
        if(isAdded()){
            mBillRanks = BillRankLab.get(getActivity()).getNotPayOffBills();
            mBillRankAdapter = new BillRankAdapter(mBillRanks);
            mBillRankListRecyclerView.setAdapter(mBillRankAdapter);
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
                dialog.setTargetFragment(HomepageFragment.this, REQUEST_DELETE);
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
}
