package com.eric.mybill;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eric.mybill.model.Bill;
import com.eric.mybill.model.BillLab;
import com.eric.mybill.model.BillRank;
import com.eric.mybill.model.BillRankLab;
import com.eric.mybill.model.Maintenance;
import com.eric.mybill.model.MaintenanceLab;
import com.eric.mybill.ui.activity.MainActivity;
import com.eric.mybill.util.DateTransform;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class MyBillActivity extends AppCompatActivity {

    private static final String TAG = "MyBillActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acctivity_my_bill);

        final List<BillRank> billRanks = BillRankLab.get(this).getBillRanks();
        final List<Bill> billList = BillLab.get(this).getBills();
        final List<Maintenance> maintenanceList = MaintenanceLab.get(this).getMaintenance();

        final TextView tvRankSize = findViewById(R.id.tv_bill_rank_size);
        TextView tvBillSize = findViewById(R.id.tv_bill_size);
        TextView tvMaintenanceSize = findViewById(R.id.tv_maintenance_size);

        tvRankSize.setText("rank size=" + billRanks.size());
        tvBillSize.setText("bill size=" + billList.size());
        tvMaintenanceSize.setText("maintenance size=" + maintenanceList.size());

        Button btOutputData = findViewById(R.id.bt_output_data);
        Button btOpen = findViewById(R.id.bt_open);

        btOutputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(MyBillActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            outputData(billRanks, billList, maintenanceList);
                                        } catch (IOException | WriteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                MyBillActivity.this.finish();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyBillActivity.this, MainActivity.class);
                startActivity(intent);
                MyBillActivity.this.finish();
            }
        });

    }

    public void outputData(List<BillRank> billRanks, List<Bill> bills, List<Maintenance> maintenances) throws IOException, WriteException {

        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "bill.xlsx";
        File excelFile = new File(path);
        if (!excelFile.exists()) {
            excelFile.createNewFile();
        }

        Log.i(TAG, "开始写");
        WritableWorkbook book = Workbook.createWorkbook(excelFile);//excel文件
        WritableSheet rankSheet = book.createSheet("rank", 0);
        WritableSheet billSheet = book.createSheet("bill", 1);
        WritableSheet maintenanceSheet = book.createSheet("maintenance", 2);


        rankSheet.addCell(new Label(0, 0, "uuid"));
        rankSheet.addCell(new Label(1, 0, "year"));
        rankSheet.addCell(new Label(2, 0, "month"));
        rankSheet.addCell(new Label(3, 0, "day"));
        rankSheet.addCell(new Label(4, 0, "form"));
        for (int i = 0; i < billRanks.size(); i++) {
            BillRank billRank = billRanks.get(i);
            int[] date = DateTransform.fromDate(billRank.getCreateDate());

            rankSheet.addCell(new Label(0, i + 1, billRank.getUUID().toString()));
            rankSheet.addCell(new jxl.write.Number(1, i + 1, date[0]));
            rankSheet.addCell(new jxl.write.Number(2, i + 1, date[1]));
            rankSheet.addCell(new jxl.write.Number(3, i + 1, date[2]));
            rankSheet.addCell(new Label(4, i + 1, String.valueOf(billRank.isForm())));
        }

        billSheet.addCell(new Label(0, 0, "times"));
        billSheet.addCell(new Label(1, 0, "uuid"));
        billSheet.addCell(new Label(2, 0, "year"));
        billSheet.addCell(new Label(3, 0, "month"));
        billSheet.addCell(new Label(4, 0, "day"));
        billSheet.addCell(new Label(5, 0, "oil_card"));
        billSheet.addCell(new Label(6, 0, "departure"));
        billSheet.addCell(new Label(7, 0, "destination"));
        billSheet.addCell(new Label(8, 0, "good_type"));
        billSheet.addCell(new Label(9, 0, "price"));
        billSheet.addCell(new Label(10, 0, "weight"));
        billSheet.addCell(new Label(11, 0, "total_price"));
        billSheet.addCell(new Label(12, 0, "notes"));
        billSheet.addCell(new Label(13, 0, "total_price_solve"));
        billSheet.addCell(new Label(14, 0, "oil_card_solve"));
        for (int i = 0; i < bills.size(); i++) {
            Bill bill = bills.get(i);
            int[] date = DateTransform.fromDate(bill.getCreateDate());
            billSheet.addCell(new Label(0, i + 1, bill.getTimes().toString()));
            billSheet.addCell(new Label(1, i + 1, bill.getUUID().toString()));
            billSheet.addCell(new jxl.write.Number(2, i + 1, date[0]));
            billSheet.addCell(new jxl.write.Number(3, i + 1, date[1]));
            billSheet.addCell(new jxl.write.Number(4, i + 1, date[2]));
            billSheet.addCell(new jxl.write.Number(5, i + 1, bill.getOilCard()));
            billSheet.addCell(new Label(6, i + 1, bill.getDeparture()));
            billSheet.addCell(new Label(7, i + 1, bill.getDestination()));
            billSheet.addCell(new Label(8, i + 1, bill.getGoodType()));
            billSheet.addCell(new jxl.write.Number(9, i + 1, bill.getPrice()));
            billSheet.addCell(new jxl.write.Number(10, i + 1, bill.getWeight()));
            billSheet.addCell(new jxl.write.Number(11, i + 1, bill.getTotalPrice()));
            billSheet.addCell(new Label(12, i + 1, bill.getNotes()));
            billSheet.addCell(new Label(13, i + 1, String.valueOf(bill.isTotalPriceSolve())));
            billSheet.addCell(new Label(14, i + 1, String.valueOf(bill.isOilCardSolve())));
        }

        maintenanceSheet.addCell(new Label(0, 0, "uuid"));
        maintenanceSheet.addCell(new Label(1, 0, "year"));
        maintenanceSheet.addCell(new Label(2, 0, "month"));
        maintenanceSheet.addCell(new Label(3, 0, "day"));
        maintenanceSheet.addCell(new Label(4, 0, "name"));
        maintenanceSheet.addCell(new Label(5, 0, "total_price"));
        maintenanceSheet.addCell(new Label(6, 0, "notes"));
        for (int i = 0; i < maintenances.size(); i++) {
            Maintenance maintenance = maintenances.get(i);
            int[] date = DateTransform.fromDate(maintenance.getCreateDate());

            maintenanceSheet.addCell(new Label(0, i + 1, maintenance.getUUID().toString()));
            maintenanceSheet.addCell(new jxl.write.Number(1, i + 1, date[0]));
            maintenanceSheet.addCell(new jxl.write.Number(2, i + 1, date[1]));
            maintenanceSheet.addCell(new jxl.write.Number(3, i + 1, date[2]));
            maintenanceSheet.addCell(new Label(4, i + 1, maintenance.getName()));
            maintenanceSheet.addCell(new jxl.write.Number(5, i + 1, maintenance.getTotalPrice()));
            maintenanceSheet.addCell(new Label(6, i + 1, maintenance.getNotes()));
        }

        book.write();
        book.close();
        Log.i(TAG, "写完成");
    }
}
