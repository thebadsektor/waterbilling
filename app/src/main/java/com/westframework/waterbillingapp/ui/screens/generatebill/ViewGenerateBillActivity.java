package com.westframework.waterbillingapp.ui.screens.generatebill;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.westframework.waterbillingapp.BuildConfig;
import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.amnesty.AmnestyViewModel;
import com.westframework.waterbillingapp.data.bill.WaterBill;
import com.westframework.waterbillingapp.data.bill.WaterBillAdapterSmall;
import com.westframework.waterbillingapp.data.bill.WaterBillViewModel;
import com.westframework.waterbillingapp.data.bill.helpers.DBHelperBill;
import com.westframework.waterbillingapp.data.helpers.DateHelper;
import com.westframework.waterbillingapp.data.helpers.Generator;
import com.westframework.waterbillingapp.data.holiday.HolidayViewModel;
import com.westframework.waterbillingapp.ui.screens.MainActivity;
import com.westframework.waterbillingapp.ui.screens.bill.UpdateBillActivity;
import com.westframework.waterbillingapp.ui.screens.bill.ViewBillActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ViewGenerateBillActivity extends AppCompatActivity {
    int appId;
    TextView tvTitle, tvAppNum, tvFullName, tvAddress, tvClassification, tvMeterNo,
            tvPeriodCovered, tvBillingMonth, etActual, etUsed, tvOrAmount, tvOthers, tvSeniorId,
            tvDiscount, tvBillAmount, tvSurchargeAmount, tvPaymentAfterDue,
            tvDueDate, tvArrears;
    TextView tvReadDate;
    EditText etPreRead;
    AppCompatImageView mButtonReadDate;
    AppCompatImageView btnBack;
    ListView lvArrears;

    private WaterBillViewModel waterBillViewModel;
    private HolidayViewModel holidayViewModel;
    private AmnestyViewModel amnestyViewModel;
    private DBHelperBill dbHelperBill;

    String appApplicationNo;
    String readingMonth;
    String appAppType;
    String mainDueDate;
    String billCat;
    String period;
    String orNum;
    double orAmount;
    double grandTotal;
    double arrears;
    double surcharge;
    int pre;
    int actual;
    int used;

    private AppCompatImageView cancel;
    private AppCompatImageView save;

    DateFormat inputFormat = new SimpleDateFormat("mm-dd-yyyy", Locale.US);
    DateFormat inputFormat2 = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
    DateFormat outputFormat = new SimpleDateFormat("mm-dd-yyyy", Locale.US);
    DateFormat outputFormat2 = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    DateFormat outputYear = new SimpleDateFormat("yyyy", Locale.US);
    DateFormat outputMonth = new SimpleDateFormat("MM", Locale.US);
    DateFormat outputMonth2 = new SimpleDateFormat("mm", Locale.US);
    DateFormat outputDay = new SimpleDateFormat("dd", Locale.US);
    DateFormat outputWeek = new SimpleDateFormat("w", Locale.US);
    Calendar calendar = Calendar.getInstance(Locale.US);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DecimalFormat df = new DecimalFormat("0.00");

    //PDF Export START
    private View mLayout;
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    //PDF Export END

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().setTitle("Generate Bill");
        setContentView(R.layout.activity_view_generatebill);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tvAppNum = findViewById(R.id.tvAppNum);
        tvFullName = findViewById(R.id.tvFullName);
        tvAddress = findViewById(R.id.tvAddress);
        tvClassification = findViewById(R.id.tvClassification);
        tvMeterNo = findViewById(R.id.tvMeterNo);
        tvReadDate = findViewById(R.id.tvReadDate);
        mButtonReadDate = findViewById(R.id.mButtonReadDate);
        tvDueDate = findViewById(R.id.tvDueDate);
        tvPeriodCovered = findViewById(R.id.tvMonthCover);
        tvBillingMonth = findViewById(R.id.tvBillingMonth);
        etPreRead = findViewById(R.id.etPreRead);
        etActual = findViewById(R.id.etActual);
        etUsed = findViewById(R.id.etUsed);
        tvOrAmount = findViewById(R.id.tvOrAmount);
        tvOthers = findViewById(R.id.tvOthers);
        tvSeniorId = findViewById(R.id.tvSeniorId);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvBillAmount = findViewById(R.id.tvBillAmount);
        tvSurchargeAmount = findViewById(R.id.tvSurchargeAmount);
        tvPaymentAfterDue = findViewById(R.id.tvPaymentAfterDue);
        //tvOrStatus = findViewById(R.id.tvOrStatus);
        tvArrears = findViewById(R.id.tvArrears);

        mLayout = findViewById(R.id.mLayout);
        cancel = findViewById(R.id.btnBillCancel);
        save = findViewById(R.id.btnBillSave);

        dbHelperBill = new DBHelperBill(this);

        final WaterBillAdapterSmall waterBillAdapter = new WaterBillAdapterSmall();

        waterBillViewModel = ViewModelProviders.of(this).get(WaterBillViewModel.class);
        waterBillViewModel.getWaterBills().observe(this, new Observer<List<WaterBill>>() {
            @Override
            public void onChanged(List<WaterBill> waterBills) {
                waterBillAdapter.setWaterBills(waterBills);
            }
        });

        grandTotal = 0;
        orAmount = 0;
        pre = 0;
        actual = 0;
        used = 0;
        arrears = 0;
        surcharge = 0;

        getData();

        tvAppNum.setText(appApplicationNo);
        etPreRead.setText(String.valueOf(dbHelperBill.getPreviousReading(appApplicationNo)));
        dbHelperBill.close();

        //Material Date Picker START
        MaterialDatePicker.Builder mReadDateBuilder = MaterialDatePicker.Builder.datePicker();
        //Calendar cal = Calendar.getInstance();
        //String initReadDate = outputFormat2.format(cal.getTime());

        // Get the current date
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentDate = calendar.getTime();

        // Format the date to MM-dd-yyyy format
        Calendar calendarDue = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendarDue.setTimeInMillis(calendar.getTimeInMillis());
        calendarDue.add(Calendar.DATE, 15);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        String formattedDue = dateFormat.format(calendarDue.getTime());
        String rawDueDate = dateFormat.format(calendarDue.getTime());
        int monthNum = Integer.parseInt(outputMonth.format(currentDate.getTime()));
        // Set the formatted date to tvReadDate
        tvReadDate.setText(formattedDate);
        String year = outputYear.format(currentDate.getTime());
        tvBillingMonth.setText(DateHelper.getMonth(monthNum) + " " + year);
        tvPeriodCovered.setText(formattedDate + " to " + rawDueDate);
        try {
            mainDueDate = DateHelper.getGrandDate(getHolidayArray(), formattedDue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvDueDate.setText(mainDueDate);

        final MaterialDatePicker mReadDatePicker = mReadDateBuilder.build();

        mButtonReadDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReadDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
        mReadDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Long selection) {

                        Calendar calendarSelect = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        Calendar calendarDue = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendarSelect.setTimeInMillis(selection);
                        calendarDue.setTimeInMillis(selection);
                        calendarDue.add(Calendar.DATE, 15);
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                        String formattedDate = format.format(calendarSelect.getTime());
                        String formattedDue = format.format(calendarDue.getTime());
                        String rawDueDate = format.format(calendarDue.getTime());

                        int monthNum = Integer.parseInt(outputMonth.format(calendarSelect.getTime()));
                        String year = outputYear.format(calendarSelect.getTime());

                        period = DateHelper.getMonth(monthNum) + " " + year;

                        try {
                            mainDueDate = DateHelper.getGrandDate(getHolidayArray(), formattedDue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        tvPeriodCovered.setText(formattedDate + " to " + rawDueDate);
                        tvDueDate.setText(mainDueDate);

                        int exists = dbHelperBill.billMonthExists(appApplicationNo, period);

                        if(exists > 0){
                            billMonthExistsDialog();
                            tvReadDate.setText("NOT SET");
                            tvBillingMonth.setText("NOT SET");
                            tvPeriodCovered.setText("NOT SET");
                            tvDueDate.setText("NOT SET");
                        }else  {
                            tvReadDate.setError(null);
                            tvReadDate.setText(formattedDate);
                            tvBillingMonth.setText(DateHelper.getMonth(monthNum) + " " + year);
                            tvPeriodCovered.setText(formattedDate + " to " + rawDueDate);
                            tvDueDate.setText(mainDueDate);
                        }
                        dbHelperBill.close();
                    }
                });
        //Material Date Picker END

        etUsed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().trim().equals("")){

                    if (Integer.parseInt(etUsed.getText().toString()) > 9999) {
                        etUsed.setText("9999");
                    }
                }

                if("".equals(etUsed.getText().toString())){ used = 0;
                }else{  used = Integer.parseInt(etUsed.getText().toString()); }

                orAmount = calculateOrAmount(appAppType, used);
                BigDecimal deciOrAmount  = new BigDecimal(orAmount).setScale(2, RoundingMode.HALF_UP);
                tvOrAmount.setText(String.valueOf(deciOrAmount));
                double arrears = Double.parseDouble(tvArrears.getText().toString());
                grandTotal = orAmount + arrears;
                BigDecimal deciGrandTotal  = new BigDecimal(grandTotal).setScale(2, RoundingMode.HALF_UP);
                tvBillAmount.setText(String.valueOf(deciGrandTotal));
                surcharge = (surcharge + (grandTotal * 0.6));
                BigDecimal deciSurcharge = new BigDecimal(surcharge ).setScale(2, RoundingMode.HALF_UP);
                tvSurchargeAmount.setText(String.valueOf(deciSurcharge));
                BigDecimal deciPaymentAfterDue = new BigDecimal(grandTotal + surcharge).setScale(2, RoundingMode.HALF_UP);
                tvPaymentAfterDue.setText(String.valueOf(deciPaymentAfterDue));
            }
        });

        etActual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!editable.toString().trim().equals("")){
                    pre = Integer.parseInt(etPreRead.getText().toString());
                    actual = Integer.parseInt(etActual.getText().toString());

                    if (Integer.parseInt(etActual.getText().toString()) > 9999) {
                        etActual.setText("9999");
                    }


                    if((actual - pre) < 0){
                        etUsed.setText(String.valueOf("0"));
                    }else{
                        etUsed.setText( String.valueOf(actual - pre));
                    }

                }

                if("".equals(etUsed.getText().toString())){
                    used = 0;
                }else{
                    used = Integer.parseInt(etUsed.getText().toString());
                }

                used = Integer.parseInt(etUsed.getText().toString());

                orAmount = calculateOrAmount(appAppType, used);
                BigDecimal deciOrAmount  = new BigDecimal(orAmount).setScale(2, RoundingMode.HALF_UP);
                tvOrAmount.setText(String.valueOf(deciOrAmount));
                double arrears = Double.parseDouble(tvArrears.getText().toString());
                grandTotal = orAmount + arrears;
                BigDecimal deciGrandTotal  = new BigDecimal(grandTotal).setScale(2, RoundingMode.HALF_UP);
                tvBillAmount.setText(String.valueOf(deciGrandTotal));
                surcharge = (surcharge + (grandTotal * 0.6));
                BigDecimal deciSurcharge = new BigDecimal(surcharge ).setScale(2, RoundingMode.HALF_UP);
                tvSurchargeAmount.setText(String.valueOf(deciSurcharge));
                BigDecimal deciPaymentAfterDue = new BigDecimal(grandTotal + surcharge).setScale(2, RoundingMode.HALF_UP);
                tvPaymentAfterDue.setText(String.valueOf(deciPaymentAfterDue));
            }
        });

        ArrayList<HashMap<String, String>> unpaidByAppNum = dbHelperBill.getUnpaidByAppNum(appApplicationNo);
        for (HashMap<String, String> map : unpaidByAppNum){
            String grandDate = "";
            String dueDate =  "";
            String message = "";
            boolean grandDateIsLater = false;
            int amnestyCount = 0;
            double grandAmount = 0;
            double billAmount = 0;
            double discountAmount = 0;
            long pastDue = 0;
            String classification = "";
            billCat = "";

            for (Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey() == "due_date"){
                    dueDate = entry.getValue();
                    //Holiday START
                    try {
                        grandDate = DateHelper.getGrandDate(getHolidayArray(), dueDate);
                        grandDateIsLater = checkIfLater(grandDate);
                        pastDue = countDaysLate(grandDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Holiday END
                }

                if(entry.getKey() == "reading_month"){
                    readingMonth = entry.getValue();
                }

                if(entry.getKey() == "classification"){
                    classification = entry.getValue();
                }

                if(entry.getKey() == "bill_cat"){
                    billCat = entry.getValue();
                }

                if(entry.getKey() == "bill_amount"){
                    billAmount = Double.valueOf(entry.getValue());
                }

                if(entry.getKey() == "discount_amount"){
                    discountAmount = Double.valueOf(entry.getValue());
                }

                if(entry.getKey() == "due_date"){
                    dueDate = entry.getValue();
                }

                ArrayList<HashMap<String, String>> amnesties = dbHelperBill.getAmnestiesByReadingMonth(readingMonth);
                for (HashMap<String, String> amnesty : amnesties){
                    for (Map.Entry<String, String> amnesty_entry : amnesty.entrySet()){
                        //Log.d("Amnesty", amnesty_entry.getKey() + " => " + amnesty_entry.getValue());
                    }
                }

                amnestyCount = amnesties.size();

                //message += entry.getKey() + ": " + entry.getValue() + "\n";
                //Amnesty START
                if(amnestyCount > 0){
                    grandAmount = billAmount - discountAmount;
                }else{
                    if( pastDue > 0){
                        if("government".equals(classification.toLowerCase())){
                            grandAmount = billAmount - discountAmount;
                        }else{
                            if("ws bill".equals(billCat.toLowerCase())){
                                double penalty = billAmount * (pastDue * 0.02);
                                grandAmount = billAmount + penalty;
                                surcharge = grandAmount * 0.6;
                            }else{
                                grandAmount = billAmount - discountAmount;
                            }
                        }
                    }else{
                        grandAmount = billAmount - discountAmount;
                    }
                }
                //Amnesty END
            }
            arrears += grandAmount;
            BigDecimal deciBillAmount = new BigDecimal(billAmount).setScale(2, RoundingMode.HALF_UP);
            BigDecimal deciGrandAmount = new BigDecimal(grandAmount).setScale(2, RoundingMode.HALF_UP);
            String title = readingMonth + " / " + String.valueOf(deciBillAmount) + " / Days Past Due: " + String.valueOf(pastDue) + " / " + String.valueOf(deciGrandAmount);
            map.put("title", title);
            map.put("grand_amount", String.valueOf(deciGrandAmount));
            map.put("surcharge", String.valueOf(surcharge));
            map.put("grand_date", grandDate);
            Log.d("Message", message + "Due Date: "
                    + dueDate + " | Adjusted Due Date: "
                    + grandDate + "\n"
                    + "Bill Total: " + String.valueOf(grandAmount));
            Log.d("Amnesty", String.valueOf(amnestyCount));
            Log.d("grandDateIsLater", "Days Past Due: " + pastDue);
        }
        Log.d("Total Arrears", "Total Arrears: " + String.valueOf(arrears));

        BigDecimal deciArrears = new BigDecimal(arrears).setScale(2, RoundingMode.HALF_UP);
        tvArrears.setText(String.valueOf(deciArrears));
        grandTotal = orAmount + arrears;
        BigDecimal deciBillAmount = new BigDecimal(grandTotal).setScale(2, RoundingMode.HALF_UP);
        tvBillAmount.setText(String.valueOf(deciBillAmount));
        surcharge = (surcharge + (grandTotal * 0.6));
        BigDecimal deciSurcharge = new BigDecimal(surcharge ).setScale(2, RoundingMode.HALF_UP);
        tvSurchargeAmount.setText(String.valueOf(deciSurcharge));
        BigDecimal deciPaymentAfterDue = new BigDecimal(grandTotal + surcharge).setScale(2, RoundingMode.HALF_UP);
        tvPaymentAfterDue.setText(String.valueOf(deciPaymentAfterDue));

        cancel.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Nothing Updated", Toast.LENGTH_LONG).show();
            finish();
        });

        tvArrears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewGenerateBillActivity.this);
                builder.setTitle("List of Arrears");
                builder.setMessage("Total Arrears Amount: " + String.valueOf(grandTotal));
                builder.setCancelable(true);

                // Set the layout using the setView() method
                view = getLayoutInflater().inflate(R.layout.layout_list_arrears, null);
                builder.setView(view);

                lvArrears = view.findViewById(R.id.listView);
                String[] from={"title"};//string array
                int[] to={R.id.textViewTitle};
                SimpleAdapter simpleAdapter=new SimpleAdapter(ViewGenerateBillActivity.this, unpaidByAppNum,R.layout.arrears_item,from,to);//Create object and set the parameters for simpleAdapter
                lvArrears.setAdapter(simpleAdapter);//sets the adapter for listView

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when the "Option 2" button is clicked
                    }
                });

                // Create and show the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                isValidated = CheckAllFields();
//                if(isValidated){
//                    updateBill();
//                }

                saveBill();
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void saveBill() {

        if(validateFields()){
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStamp = s.format(new Date());

            String newApplicationNo = appApplicationNo;
            String newFullName = tvFullName.getText().toString();
            String newAddress = tvAddress.getText().toString();
            String newClassification = tvClassification.getText().toString();
            String newMeterNo = tvMeterNo.getText().toString();
            String newPeriodCovered = tvPeriodCovered.getText().toString();
            String newBillingMonth = tvBillingMonth.getText().toString();
            String newReadDate = tvReadDate.getText().toString();
            String newDueDate = tvDueDate.getText().toString();
            String newPrePread = etPreRead.getText().toString();
            String newActual = etActual.getText().toString();
            String newUsed = etUsed.getText().toString();

            if("".equals(newPrePread.trim())){newPrePread="0";}
            if("".equals(newActual.trim())){newActual="0";}
            if("".equals(newUsed.trim())){newUsed="0";}

            int newId = dbHelperBill.getLastBillId()+1;
            String newOrNum = Generator.generateOrNumber(String.valueOf(newMeterNo), DateHelper.todayNoDashes(),newId);

            String newOrAmount = tvOrAmount.getText().toString();
            String newStatus = "unpaid";
            String newArrears = tvArrears.getText().toString();
            String newOthers = tvOthers.getText().toString();
            String newSeniorId = tvSeniorId.getText().toString();
            String newDiscount = tvDiscount.getText().toString();
            String newBillAmount = tvBillAmount.getText().toString();
            String newSurchargeAmount = tvSurchargeAmount.getText().toString();
            String newPaymentAfterDue = tvPaymentAfterDue.getText().toString();

            if("-".equals(newPrePread)){newPrePread = "0";}
            if("-".equals(newActual.trim())){newActual = "0";}
            if("-".equals(newUsed.trim())){newUsed = "0";}
            if("-".equals(newOrAmount.trim())){newOrAmount = "0";}
            if("-".equals(newArrears.trim())){newArrears = "0";}
            if("-".equals(newOthers.trim())){newOthers = "0";}
            if("-".equals(newDiscount.trim())){newDiscount = "0";}
            if("-".equals(newBillAmount.trim())){newBillAmount = "0";}
            if("-".equals(newSurchargeAmount.trim())){newSurchargeAmount = "0";}
            if("-".equals(newPaymentAfterDue.trim())){newPaymentAfterDue = "0";}

            WaterBill bill = new WaterBill(newApplicationNo, newReadDate, newDueDate, newFullName,
                    newAddress, newClassification, Integer.parseInt(newMeterNo), newSeniorId, newBillingMonth,
                    Integer.parseInt(newPrePread), Integer.parseInt(newActual), Integer.parseInt(newUsed), 0, 0, Double.parseDouble(newDiscount),
                    Double.parseDouble(newBillAmount), newStatus, newOrNum, Double.parseDouble(newOrAmount), Double.parseDouble(newArrears), Double.parseDouble(newOthers), timeStamp, "",
                    newStatus, Double.parseDouble(newSurchargeAmount), Double.parseDouble(newPaymentAfterDue), timeStamp, timeStamp);
            waterBillViewModel.insert(bill);

            Toast.makeText(getApplicationContext(), "Saved (" + newOrNum + ")", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(ViewGenerateBillActivity.this, ViewBillActivity.class);
            intent.putExtra("id", newId);
            intent.putExtra("application_no", newApplicationNo);
            intent.putExtra("full_name", newFullName);
            intent.putExtra("bill_address", newAddress);
            intent.putExtra("classification", newClassification);
            intent.putExtra("meter_read", Integer.parseInt(newMeterNo));
//        intent.putExtra("period_covered", newPeriodCovered);
            intent.putExtra("reading_month", newBillingMonth);
            intent.putExtra("read_date", newReadDate);
            intent.putExtra("due_date", newDueDate);
            intent.putExtra("pre_reading", Integer.parseInt(newPrePread));
            intent.putExtra("actual_reading", Integer.parseInt(newActual));
            intent.putExtra("cu_m_used",Integer.parseInt(newUsed));
            intent.putExtra("or_num", newOrNum);
            intent.putExtra("or_amount", Double.parseDouble(newOrAmount));
            intent.putExtra("arrears", Double.parseDouble(newArrears));
            intent.putExtra("others", Double.parseDouble(newOthers));
            intent.putExtra("senior_ids", newSeniorId);
            intent.putExtra("discount_amount", Double.parseDouble(newDiscount));
            intent.putExtra("bill_amount", Double.parseDouble(newBillAmount));
            intent.putExtra("surcharge", Double.parseDouble(newSurchargeAmount));
            intent.putExtra("payment_after_due", Double.parseDouble(newPaymentAfterDue));
            startActivity(intent);
            finish();
        }
    }

    private double calculateOrAmount( String appAppType, int used) {
        double amount = 0;
        int min = 0;
        int max = 0;
        int classAmount = 0;
        int increment = 0;

        ArrayList<HashMap<String, String>> classifications = dbHelperBill.getClass(appAppType, used);
        Log.d("Classifications", tvClassification.getText().toString()  + " | Used: " +  used + " | " + String.valueOf(classifications));
        for (HashMap<String, String> map : classifications) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if((entry.getKey() == "cu_m_min")){
                    min = Integer.parseInt(entry.getValue());
                }
                if((entry.getKey() == "cu_m_max")){
                    max = Integer.parseInt(entry.getValue());
                }
                if((entry.getKey() == "amount")){
                    classAmount = Integer.parseInt(entry.getValue());
                }
                if((entry.getKey() == "increment_by")){
                    increment = Integer.parseInt(entry.getValue());
                }
            }
        }
        dbHelperBill.close();

        amount += classAmount;

        for(int i = used; i > min; i--){
            amount += increment;
        }

        Log.d("orAmount", "Increment: " + String.valueOf((used - min) * increment));
        Log.d("orAmount", "OR Amount: " + String.valueOf(orAmount));

        return amount;
    }



    private boolean checkIfLater(String grandDate) throws ParseException {
        Date parsedNewDate = inputFormat.parse(grandDate);

        Calendar cal = Calendar.getInstance();
        String nowYear = outputYear.format(cal.getTime());
        String nowMonth = outputMonth.format(cal.getTime());
        String nowDay = outputDay.format(cal.getTime());
        String now = outputFormat.format(cal.getTime());

        String grandDateYear = outputYear.format(parsedNewDate);
        String grandDateMonth = outputMonth.format(parsedNewDate);
        String grandDateDay = outputDay.format(parsedNewDate);

        //count days from due date to current date
        LocalDate d1 = LocalDate.of(Integer.valueOf(grandDateYear), Integer.valueOf(grandDateMonth), Integer.valueOf(grandDateDay));
        LocalDate d2 = LocalDate.of(Integer.valueOf(nowYear), Integer.valueOf(nowMonth), Integer.valueOf(nowDay));

        long days = ChronoUnit.DAYS.between(d1, d2);

        int checkIfLater = d1.compareTo(d2);

        if(checkIfLater == 0){
            Log.d("checkIfLater", String.valueOf(checkIfLater) + " - " + String.valueOf(d1) + " is same day as " + String.valueOf(d2));
            return false;
        }else if(checkIfLater > 0){
            Log.d("checkIfLater", String.valueOf(checkIfLater) + " - " + String.valueOf(d1) + " is later than " + String.valueOf(d2));
            return true;
        }else{
            Log.d("checkIfLater", String.valueOf(checkIfLater) + " - " + String.valueOf(d1) + " is earlier than " + String.valueOf(d2));
            return false;
        }
    }

    private long countDaysLate(String grandDate) throws ParseException {
        Date parsedNewDate = inputFormat.parse(grandDate);

        Calendar cal = Calendar.getInstance();
        String now = outputFormat2.format(cal.getTime());
        String nowYear = outputYear.format(cal.getTime());
        String nowMonth = outputMonth.format(cal.getTime());
        String nowDay = outputDay.format(cal.getTime());

        Log.d("NOW", "NOW: " + nowMonth + "-" + nowDay + "-" + nowYear);
        Log.d("NOW-formatted", "NOW-formatted: " + now);

        String grandDateYear = outputYear.format(parsedNewDate);
        String grandDateMonth = outputMonth2.format(parsedNewDate);
        String grandDateDay = outputDay.format(parsedNewDate);

        //count days from due date to current date
        LocalDate d1 = LocalDate.of(Integer.valueOf(grandDateYear), Integer.valueOf(grandDateMonth), Integer.valueOf(grandDateDay));
        LocalDate d2 = LocalDate.of(Integer.valueOf(nowYear), Integer.valueOf(nowMonth), Integer.valueOf(nowDay));

        Log.d("DUE (d1)", "DUE (d1): " + String.valueOf(d1));
        Log.d("NOW (d2)", "NOW (d2): " + String.valueOf(d2));

        long days = ChronoUnit.DAYS.between(d1, d2);
        Log.d("days", String.valueOf(days) + " days from " + grandDate + " to " + now);

        int checkIfLater = d1.compareTo(d2);

        if(checkIfLater == 0){
            Log.d("checkIfLater", String.valueOf(checkIfLater) + " - " + String.valueOf(d1) + " is same day as " + String.valueOf(d2));
            days = 0;
        }else if(checkIfLater > 0){
            Log.d("checkIfLater", String.valueOf(checkIfLater) + " - " + String.valueOf(d1) + " is later than " + String.valueOf(d2));
            days = 0;
        }else{
            Log.d("checkIfLater", String.valueOf(checkIfLater) + " - " + String.valueOf(d1) + " is earlier than " + String.valueOf(d2));
        }

        return days;
    }

    private void getData() {
        Intent i = getIntent();
        appId = i.getIntExtra("id", -1);

        appApplicationNo = i.getStringExtra("applicationNo");
        String appAppDate = i.getStringExtra("applicationDate");
        String appLastName = i.getStringExtra("lastName").trim();
        String appMiddleInitial = i.getStringExtra("middleInitial").trim();
        String appFirstName = i.getStringExtra("firstName").trim();
        appAppType = i.getStringExtra("applicationType");
        Log.d("appAppType", appAppType);
        String appHouseNo = i.getStringExtra("houseNo").trim();
        String appBNo = i.getStringExtra("buildingNo").trim();
        String appStreet = i.getStringExtra("street").trim();
        String appBrgy = i.getStringExtra("brgy").trim();
        String appMeterNo = i.getStringExtra("meterNo");
        String appOrStatus = i.getStringExtra("orStatus");
        String appStatus = i.getStringExtra("status");
        String appUpdatedAt = i.getStringExtra("updated_at");
        String appCreatedAt = i.getStringExtra("created_at");

        //Application Model
        tvTitle.setText(appApplicationNo + " | " + appMeterNo);
        tvFullName.setText(appFirstName + " " + appMiddleInitial + " " + appLastName);
        tvAddress.setText(appHouseNo + " " + appBNo + " " + appStreet + " " + appBrgy);
        tvClassification.setText(appAppType);
        tvMeterNo.setText(appMeterNo);
        tvSeniorId.setText("-");

        //Bill Model
        etPreRead.setText("0");
        etActual.setText("0");
        etUsed.setText("0");

        used = Integer.parseInt(etUsed.getText().toString());
        orAmount = calculateOrAmount(appAppType, used);
        BigDecimal deciOrAmount  = new BigDecimal(orAmount).setScale(2, RoundingMode.HALF_UP);
        tvOrAmount.setText(String.valueOf(deciOrAmount));
//        tvOthers.setText("NOT SET");
//        tvDiscount.setText("NOT SET");
//        tvBillAmount.setText("NOT SET");
//        tvSurchargeAmount.setText("NOT SET");
//        tvPaymentAfterDue.setText("NOT SET");
    }



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

            ActivityCompat.requestPermissions(ViewGenerateBillActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_MEMORY_ACCESS);

        } else {
            Snackbar.make(mLayout, R.string.storage_unavailable, Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }

    //Bill Month Exists START
    public void billMonthExistsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewGenerateBillActivity.this);
        builder.setTitle("ALERT!");
        builder.setMessage("A BILL for this MONTH COVERAGE already exists!");
        builder.setIcon(R.drawable.ic_add_60px);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //Bill Month Exists END

    public ArrayList<String> getHolidayArray() throws ParseException {
        ArrayList<HashMap<String, String>> rawHolidays = dbHelperBill.getHolidays();
        ArrayList<String> holidays = new ArrayList<String>();
        for (HashMap<String, String> holiday : rawHolidays){
            String rawDate = "";
            String year = "";
            String month = "";
            String day = "";
            for (Map.Entry<String, String> entry : holiday.entrySet()){
                if(entry.getKey() == "holiday_year"){
                    year = entry.getValue().trim();
                }
                if(entry.getKey() == "holiday_month"){
                    switch (entry.getValue().trim().toLowerCase()){
                        case "january":
                            month = "1";
                            break;
                        case "february":
                            month = "2";
                            break;
                        case "march":
                            month = "3";
                            break;
                        case "april":
                            month = "4";
                            break;
                        case "may":
                            month = "5";
                            break;
                        case "june":
                            month = "6";
                            break;
                        case "july":
                            month = "7";
                            break;
                        case "august":
                            month = "8";
                            break;
                        case "september":
                            month = "9";
                            break;
                        case "october":
                            month = "10";
                            break;
                        case "november":
                            month = "11";
                            break;
                        case "december":
                            month = "12";
                            break;
                    }
                }
                if(entry.getKey() == "holiday_date"){
                    day = entry.getValue().trim();
                }
            }
            rawDate = month + "-" + day + "-" + year;
            Date date = inputFormat.parse(rawDate);
            String outputText = outputFormat.format(date);

            calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));

            int week = calendar.get(Calendar.WEEK_OF_MONTH);
            holidays.add(outputText);
        }
        Log.d("holidays", String.valueOf(holidays));
        return holidays;
    }

    //Validate Fields before PRINT and SAVE - START
    private boolean validateFields() {
        if (tvReadDate.length() == 0
                || "not set".equals(tvReadDate.getText().toString().toLowerCase().trim())
                || "-".equals(tvReadDate.getText().toString().toLowerCase().trim())) {
            Log.d("tvPeriodCovered", tvPeriodCovered.getText().toString().toLowerCase());
            tvReadDate.setError("This field is required");
            tvReadDate.getParent().requestChildFocus(mButtonReadDate, tvReadDate);

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewGenerateBillActivity.this);
            builder.setTitle("ALERT!");
            builder.setMessage("Please set the READ DATE first!");
            builder.setIcon(R.drawable.ic_gear);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
        return true;
    }
    //Validate Fields before PRINT and SAVE - END

}