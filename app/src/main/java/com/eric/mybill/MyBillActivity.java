package com.eric.mybill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.model.BillRankLab;
import com.eric.mybill.ui.activity.MainActivity;
import com.eric.mybill.util.DateTransform;

import java.util.Iterator;
import java.util.List;


public class MyBillActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acctivity_my_bill);

//        Intent intent = new Intent(MyBillActivity.this, MainActivity.class);
//        startActivity(intent);
//        MyBillActivity.this.finish();

        StringBuffer str = new StringBuffer("开始\n");

        List<BillRank> billRanks = BillRankLab.get(this).getNotPayOffBills();
        List<Bill> billList = BillLab.get(this).getBills();
        Iterator<BillRank> iterator = billRanks.iterator();
        while(iterator.hasNext()){
            str.append(DateTransform.stringFromDate(iterator.next().getCreateDate()) + "\n");
        }
        TextView textView = findViewById(R.id.text_view_info);
        textView.setText(billList.size() + "");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBillActivity.this, MainActivity.class);
                startActivity(intent);
                MyBillActivity.this.finish();
            }
        });

    }
}
