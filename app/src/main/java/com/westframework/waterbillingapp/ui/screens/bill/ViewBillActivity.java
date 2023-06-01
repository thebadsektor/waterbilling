package com.westframework.waterbillingapp.ui.screens.bill;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.westframework.waterbillingapp.BuildConfig;
import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.bill.WaterBill;
import com.westframework.waterbillingapp.data.bill.WaterBillAdapter;
import com.westframework.waterbillingapp.data.bill.WaterBillViewModel;
import com.westframework.waterbillingapp.data.helpers.DateHelper;
import com.westframework.waterbillingapp.ui.screens.generatebill.ViewGenerateBillActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewBillActivity extends AppCompatActivity {

    private WaterBillViewModel waterBillViewModel;

    int billId;
    private String bApplicationNo;
    private String bFullName;
    private String bOrNum;
    TextView tvTitle, tvAppNum, tvFullName, tvAddress, tvClassification, tvMeterNo,
            tvMonthCover, tvBillingMonth, tvReadDate, tvPreRead, tvActual, tvUsed, tvDueDate,
            tvOrAmount, tvOthers, tvSeniorId, tvDiscount, tvBillAmount,
            tvSurchargeAmount, tvPaymentAfterDue, tvArrears;
    AppCompatImageView btnBack;
    AppCompatImageView btnEditBill;
    AppCompatImageView btnPrintBill;

    //PDF Export START
    private View mLayout;
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    //PDF Export END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        mLayout = findViewById(R.id.mLayout);
        tvTitle = findViewById(R.id.tvTitle);
        tvAppNum = findViewById(R.id.tvAppNum);
        tvFullName = findViewById(R.id.tvFullName);
        tvAddress = findViewById(R.id.tvAddress);
        tvClassification = findViewById(R.id.tvClassification);
        tvMeterNo = findViewById(R.id.tvMeterNo);
        tvMonthCover = findViewById(R.id.tvMonthCover);
        tvBillingMonth = findViewById(R.id.tvBillingMonth);
        tvReadDate = findViewById(R.id.tvReadDate);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvPreRead = findViewById(R.id.tvPreRead);
        tvActual = findViewById(R.id.tvActual);
        tvUsed = findViewById(R.id.tvUsed);
        tvOrAmount = findViewById(R.id.tvOrAmount);
        tvArrears = findViewById(R.id.tvArrears);
        tvOthers = findViewById(R.id.tvOthers);
        tvSeniorId = findViewById(R.id.tvSeniorId);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvBillAmount = findViewById(R.id.tvBillAmount);
        tvSurchargeAmount = findViewById(R.id.tvSurchargeAmount);
        tvPaymentAfterDue = findViewById(R.id.tvPaymentAfterDue);

        final WaterBillAdapter waterBillAdapter = new WaterBillAdapter();
        waterBillViewModel = ViewModelProviders.of(this).get(WaterBillViewModel.class);
        waterBillViewModel.getWaterBills().observe(this, new Observer<List<WaterBill>>() {
            @Override
            public void onChanged(List<WaterBill> waterBills) {
                waterBillAdapter.setWaterBills(waterBills);
            }
        });

        getData();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnEditBill = findViewById(R.id.btnBillEdit);
        btnEditBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBillActivity.this, UpdateBillActivity.class);
                intent.putExtra("id", billId);
                intent.putExtra("application_no", bApplicationNo);
                intent.putExtra("full_name", bFullName);
                intent.putExtra("bill_address", tvAddress.getText().toString());
                intent.putExtra("classification", tvClassification.getText().toString());
                intent.putExtra("meter_read", Integer.parseInt(tvMeterNo.getText().toString()));
                intent.putExtra("period_covered", tvMonthCover.getText().toString());
                intent.putExtra("reading_month", tvBillingMonth.getText().toString());
                intent.putExtra("read_date", tvReadDate.getText().toString());
                intent.putExtra("due_date", tvDueDate.getText().toString());
                intent.putExtra("pre_reading", Integer.parseInt(tvPreRead.getText().toString()));
                intent.putExtra("actual_reading", Integer.parseInt(tvActual.getText().toString()));
                intent.putExtra("cu_m_used", Integer.parseInt(tvUsed.getText().toString()));
                intent.putExtra("or_num", tvTitle.getText().toString());
                intent.putExtra("or_amount", Double.parseDouble(tvOrAmount.getText().toString()));
                intent.putExtra("arrears", Double.parseDouble(tvArrears.getText().toString()));
                intent.putExtra("others", Double.parseDouble(tvOthers.getText().toString()));
                intent.putExtra("senior_ids", tvSeniorId.getText().toString());
                intent.putExtra("discount_amount", Double.parseDouble(tvDiscount.getText().toString()));
                intent.putExtra("bill_amount", Double.parseDouble(tvBillAmount.getText().toString()));
                intent.putExtra("surcharge", Double.parseDouble(tvSurchargeAmount.getText().toString()));
                intent.putExtra("payment_after_due", Double.parseDouble(tvPaymentAfterDue.getText().toString()));
                startActivityForResult(intent, 1);
            }
        });

        btnPrintBill = findViewById(R.id.btnPrintBill);
        btnPrintBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                print();
            }
        });
    }

    private void getData() {
        Intent i = getIntent();
        billId = i.getIntExtra("id", -1);

        bApplicationNo = i.getStringExtra("application_no");
        bFullName = i.getStringExtra("full_name");
        String bAddress = i.getStringExtra("bill_address");
        String bClassification = i.getStringExtra("classification");
        int bMeterNo = i.getIntExtra("meter_read", 0);
        String bBillingMonth = i.getStringExtra("reading_month");
        String bReadDate = i.getStringExtra("read_date");
        String bMonthCover = DateHelper.getPeriodCovered(bReadDate);
        String bDueDate = i.getStringExtra("due_date");
        int bPreRead = i.getIntExtra("pre_reading", 0);
        int bActual = i.getIntExtra("actual_reading", 0);
        int bUsed = i.getIntExtra("cu_m_used", 0);
        bOrNum = i.getStringExtra("or_num");
        double bOrAmount = i.getDoubleExtra("or_amount", 0);
        double bArrears = i.getDoubleExtra("arrears", 0);
        double bOthers = i.getDoubleExtra("others", 0);
        String bSeniorId = i.getStringExtra("senior_ids");
        double bDiscount = i.getDoubleExtra("discount_amount", 0);
        double bBillAmount = i.getDoubleExtra("bill_amount", 0);
        double bSurchargeAmount = i.getDoubleExtra("surcharge", 0);
        double bPaymentAfterDue = i.getDoubleExtra("payment_after_due", 0);
        String bCreatedAt = i.getStringExtra("created_at");
        String bUpdatedAt = i.getStringExtra("updated_at");

        tvTitle.setText(bOrNum);
        tvAppNum.setText(bApplicationNo);
        tvFullName.setText(bFullName);
        tvAddress.setText(bAddress);
        tvClassification.setText(bClassification);
        tvMeterNo.setText(String.valueOf(bMeterNo));
        tvMonthCover.setText(bMonthCover);
        tvBillingMonth.setText(String.valueOf(bBillingMonth));
        tvReadDate.setText(bReadDate);
        tvDueDate.setText(bDueDate);
        tvPreRead.setText(String.valueOf(bPreRead));
        tvActual.setText(String.valueOf(bActual));
        tvUsed.setText(String.valueOf(bUsed));
        tvOrAmount.setText(String.valueOf(bOrAmount));
        tvArrears.setText(String.valueOf(bArrears));
        tvOthers.setText(String.valueOf(bOthers));
        tvSeniorId.setText(String.valueOf(bSeniorId));
        tvDiscount.setText(String.valueOf(bDiscount));
        tvBillAmount.setText(String.valueOf(bBillAmount));
        tvSurchargeAmount.setText(String.valueOf(bSurchargeAmount));
        tvPaymentAfterDue.setText(String.valueOf(bPaymentAfterDue));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.edit_menu:
                Intent intent = new Intent(ViewBillActivity.this, UpdateBillActivity.class);
                intent.putExtra("application_no", bApplicationNo);
                intent.putExtra("id", billId);
                intent.putExtra("full_name", bFullName);
                startActivityForResult(intent, 2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = s.format(new Date());

        if(requestCode == 1 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("id", -1);
            bApplicationNo = data.getStringExtra("application_no");
            bFullName = data.getStringExtra("full_name");
            String bAddress = data.getStringExtra("bill_address");
            String bClassification = data.getStringExtra("classification");
            int bMeterNo = Integer.parseInt(data.getStringExtra("meter_read"));
            String bMonthCover = data.getStringExtra("period_covered");
            String bBillingMonth = data.getStringExtra("reading_month");
            String bReadDate = data.getStringExtra("read_date");
            String bDueDate = data.getStringExtra("due_date");
            int bPreRead = Integer.parseInt(data.getStringExtra("pre_reading"));
            int bActual = Integer.parseInt(data.getStringExtra("actual_reading"));
            int bUsed = Integer.parseInt(data.getStringExtra("cu_m_used"));
            String bOrNum = data.getStringExtra("or_num");
            double bOrAmount = Double.parseDouble(data.getStringExtra("or_amount"));
            String bStatus = "";
            double bArrears = Double.parseDouble(data.getStringExtra("arrears"));
            double bOthers = Double.parseDouble(data.getStringExtra("others"));
            String bSeniorId = data.getStringExtra("senior_ids");
            double bDiscount = Double.parseDouble(data.getStringExtra("discount_amount"));
            double bBillAmount = Double.parseDouble(data.getStringExtra("bill_amount"));
            double bSurchargeAmount = Double.parseDouble(data.getStringExtra("surcharge"));
            double bPaymentAfterDue = Double.parseDouble(data.getStringExtra("payment_after_due"));

            WaterBill bill = new WaterBill(bApplicationNo, bReadDate, bDueDate, bFullName,
                    bAddress, bClassification, bMeterNo, bSeniorId, bBillingMonth,
                    bPreRead, bActual, bUsed, 0, 0, bDiscount,
                    bBillAmount, bStatus, bOrNum, bOrAmount, bArrears, bOthers, timeStamp, "",
                    "", bSurchargeAmount, bPaymentAfterDue, timeStamp, timeStamp);

            bill.setId(id);
            waterBillViewModel.update(bill);
            Toast.makeText(getApplicationContext(), "Updated (" + bOrNum + ")", Toast.LENGTH_LONG).show();

            tvTitle.setText(bOrNum);
            tvAppNum.setText(bApplicationNo);
            tvFullName.setText(bFullName);
            tvAddress.setText(bAddress);
            tvClassification.setText(bClassification);
            tvMeterNo.setText(String.valueOf(bMeterNo));
            tvMonthCover.setText(bMonthCover);
            tvBillingMonth.setText(String.valueOf(bBillingMonth));
            tvReadDate.setText(bReadDate);
            tvDueDate.setText(bDueDate);
            tvPreRead.setText(String.valueOf(bPreRead));
            tvActual.setText(String.valueOf(bActual));
            tvUsed.setText(String.valueOf(bUsed));
            tvOrAmount.setText(String.valueOf(bOrAmount));
            tvArrears.setText(String.valueOf(bArrears));
            tvOthers.setText(String.valueOf(bOthers));
            tvSeniorId.setText(String.valueOf(bSeniorId));
            tvDiscount.setText(String.valueOf(bDiscount));
            tvBillAmount.setText(String.valueOf(bBillAmount));
            tvSurchargeAmount.setText(String.valueOf(bSurchargeAmount));
            tvPaymentAfterDue.setText(String.valueOf(bPaymentAfterDue));


        }
    }

    //Export PDF START
    private void print(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewBillActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to print?");
        builder.setIcon(R.drawable.ic_add_60px);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                String appNum = tvAppNum.getText().toString();
                String fullName = tvFullName.getText().toString();
                String address = tvAddress.getText().toString();
                String classification = tvClassification.getText().toString();
                String meterNo = tvMeterNo.getText().toString();
                String periodCovered = tvMonthCover.getText().toString();
                String billingMonth = tvBillingMonth.getText().toString();
                String readDate = tvReadDate.getText().toString();
                String dueDate = tvDueDate.getText().toString();
                String preRead = tvPreRead.getText().toString();
                String actual = tvActual.getText().toString();
                String used = tvUsed.getText().toString();
                String orNum = bOrNum;
                String orAmount = tvOrAmount.getText().toString();
                String arrears = tvArrears.getText().toString();
                String others = tvOthers.getText().toString();
                String seniorId = tvSeniorId.getText().toString();
                String discount = tvDiscount.getText().toString();
                String billAmount = tvBillAmount.getText().toString();
                String surchargeAmount = tvSurchargeAmount.getText().toString();
                String paymentAfterDue = tvPaymentAfterDue.getText().toString();

                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timeStamp = s.format(new Date());

                if("-".equals(preRead.trim())){preRead = "0";}
                if("-".equals(actual.trim())){actual = "0";}
                if("-".equals(used.trim())){used = "0";}
                if("-".equals(orAmount.trim())){orAmount = "0";}
                if("-".equals(arrears.trim())){arrears = "0";}
                if("-".equals(others.trim())){others = "0";}
                if("-".equals(discount.trim())){discount = "0";}
                if("-".equals(billAmount.trim())){billAmount = "0";}
                if("-".equals(surchargeAmount.trim())){surchargeAmount = "0";}
                if("-".equals(paymentAfterDue.trim())){paymentAfterDue = "0";}
                try {
                    if(CheckPermission()){
                        createPdf(orNum, appNum, fullName, address, classification,
                                meterNo, periodCovered, billingMonth, readDate,
                                dueDate, preRead, actual, used,
                                orAmount, arrears, others, seniorId,
                                discount, billAmount, surchargeAmount, paymentAfterDue);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //Export PDF END

    //PDF Export START
    private void createPdf(
            String orNum, String appNum, String fullName, String address, String classification,
            String meterNo, String periodCovered, String billingMonth, String readDate,
            String dueDate, String preRead, String actual, String used,
            String orAmount, String arrears, String others, String seniorId,
            String discount, String billAmount, String surchargeAmount, String paymentAfterDue)
            throws FileNotFoundException {

        ActivityCompat.requestPermissions( this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );

        // If you have access to the external storage, do whatever you need
        if (CheckPermission()){

            String pdfPath = Environment.getExternalStorageDirectory().toString() + "/exported_pdf/";
            try {
                Files.createDirectories(Paths.get(pdfPath));
            } catch (IOException e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }

            String filename = appNum + " - " + billingMonth + ".pdf";
            File file = new File(pdfPath, filename);
            OutputStream outputStream = new FileOutputStream(file);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            Rectangle rectangle3x5 = new Rectangle(295.2f, 726.4f);
            PageSize pagesize3x5 = new PageSize(rectangle3x5);
            pdfDocument.setDefaultPageSize(pagesize3x5);
            document.setMargins(0,0,0,0);

            float[] width = {150f, 150f};
            Table table = new Table(width);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph mainheader = new Paragraph("WS Water Bill").setMarginTop(15)
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
            Paragraph subheader = new Paragraph("WS Water Billing App").setTextAlignment(TextAlignment.CENTER);
            Paragraph footer = new Paragraph("\u00A9 WS Water Billing App").setTextAlignment(TextAlignment.CENTER).setMarginTop(10);

            Paragraph cx = new Paragraph("CUSTOMER INFORMATION").setTextAlignment(TextAlignment.CENTER);
            Cell cxHeader = new Cell(1,3);
            cxHeader.setBackgroundColor(ColorConstants.BLACK);
            cxHeader.setFontColor(ColorConstants.WHITE);
            cxHeader.add(cx);
            table.addCell(cxHeader);

            table.addCell(new Cell().add(new Paragraph("OR Number:")));
            table.addCell(new Cell().add(new Paragraph(orNum)));

            table.addCell(new Cell().add(new Paragraph("Account Number:")));
            table.addCell(new Cell().add(new Paragraph(appNum)));

            table.addCell(new Cell().add(new Paragraph("Concessioner's Name")));
            table.addCell(new Cell().add(new Paragraph(fullName)));

            table.addCell(new Cell().add(new Paragraph("Address:")));
            table.addCell(new Cell().add(new Paragraph(address)));

            table.addCell(new Cell().add(new Paragraph("Classification")));
            table.addCell(new Cell().add(new Paragraph(classification)));

            table.addCell(new Cell().add(new Paragraph("Meter Number:")));
            table.addCell(new Cell().add(new Paragraph(meterNo)));

            table.addCell(new Cell().add(new Paragraph("Period Covered:")));
            table.addCell(new Cell().add(new Paragraph(periodCovered)));

            table.addCell(new Cell().add(new Paragraph("Billing Month:")));
            table.addCell(new Cell().add(new Paragraph(billingMonth)));

            table.addCell(new Cell().add(new Paragraph("Read DAte:")));
            table.addCell(new Cell().add(new Paragraph(readDate)));

            table.addCell(new Cell().add(new Paragraph("Due Date:")));
            table.addCell(new Cell().add(new Paragraph(dueDate)));

            float[] width2 = {100f, 100f, 100f};
            Table table2 = new Table(width2);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph header = new Paragraph("BILLING INFORMATION").setTextAlignment(TextAlignment.CENTER);
            Cell cellHeader = new Cell(1,3);
            cellHeader.setBackgroundColor(ColorConstants.BLACK);
            cellHeader.setFontColor(ColorConstants.WHITE);
            cellHeader.add(header);
            table2.addCell(cellHeader);

            Paragraph previous = new Paragraph("PREVIOUS").setTextAlignment(TextAlignment.CENTER);
            Paragraph present = new Paragraph("PRESENT").setTextAlignment(TextAlignment.CENTER);
            Paragraph cumused = new Paragraph("CM USED").setTextAlignment(TextAlignment.CENTER);
            table2.addCell(new Cell().add(previous));
            table2.addCell(new Cell().add(present));
            table2.addCell(new Cell().add(cumused));

            Paragraph prevAmount = new Paragraph(preRead).setTextAlignment(TextAlignment.CENTER);
            Paragraph presAmount = new Paragraph(actual).setTextAlignment(TextAlignment.CENTER);
            Paragraph cumAmount = new Paragraph(used).setTextAlignment(TextAlignment.CENTER);
            table2.addCell(new Cell().add(prevAmount));
            table2.addCell(new Cell().add(presAmount));
            table2.addCell(new Cell().add(cumAmount));

            table2.addCell(new Cell(1,2).add(new Paragraph("Amount Due:")));
            table2.addCell(new Cell().add(new Paragraph(orAmount)));

            table2.addCell(new Cell(1,2).add(new Paragraph("Arrears:")));
            table2.addCell(new Cell().add(new Paragraph(arrears)));

            table2.addCell(new Cell(1,2).add(new Paragraph("Other Service/ Installment:")));
            table2.addCell(new Cell().add(new Paragraph(others)));

            table2.addCell(new Cell().add(new Paragraph("Senior Citizen ID:")));
            table2.addCell(new Cell(1,2).add(new Paragraph(seniorId)));
            table2.addCell(new Cell(1,2).add(new Paragraph("Discount:")));
            table2.addCell(new Cell().add(new Paragraph(discount)));

            Paragraph amount = new Paragraph("BILLING INFORMATION").setTextAlignment(TextAlignment.CENTER);
            Cell cellAmount = new Cell(1,3);
            cellAmount.setBackgroundColor(ColorConstants.BLUE);
            cellAmount.setFontColor(ColorConstants.WHITE);
            cellAmount.add(amount);
            table2.addCell(cellAmount);

            table2.addCell(new Cell(1,2).add(new Paragraph("TOTAL AMOUNT:")));
            table2.addCell(new Cell().add(new Paragraph(billAmount)));
            table2.addCell(new Cell(1,2).add(new Paragraph("Surcharge Amount:")));
            table2.addCell(new Cell().add(new Paragraph(surchargeAmount)));
            table2.addCell(new Cell(1,2).add(new Paragraph("Payment After Due Date:")));
            table2.addCell(new Cell().add(new Paragraph(paymentAfterDue)));

            document.add(mainheader);
            document.add(subheader);
            document.add(table);
            document.add(table2);
            document.add(footer);
            document.close();
            Toast.makeText(this, "PDF Created", Toast.LENGTH_LONG).show();

            displayPdf(file, filename);

        }else{
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    //Open PDF START
    private void displayPdf(File file, String filename) {
        if(file.exists()){
            File newPdfPath = new File(Environment.getExternalStorageDirectory(),"exported_pdf");
            File newPdfFile = new File(newPdfPath, filename);
            Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", newPdfFile);

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent intent = Intent.createChooser(pdfIntent, "Open File");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(ViewBillActivity.this, "No such file exists", Toast.LENGTH_SHORT).show();
    }

    public File getFile(Context context, String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        File storageDir = context.getExternalFilesDir(null);
        return new File(storageDir, fileName);
    }

    public Uri getFileUri(Context context, String fileName) {
        File file = getFile(context, fileName);
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }
    //Open PDF End

    //PERMISSIONS START
    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Snackbar.make(mLayout, R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStoragePermission();
                }
            }).show();

            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_MEMORY_ACCESS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Snackbar.make(mLayout, R.string.storage_access_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ViewBillActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_MEMORY_ACCESS);

        } else {
            Snackbar.make(mLayout, R.string.storage_unavailable, Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }
    //PERMISSIONS END
}