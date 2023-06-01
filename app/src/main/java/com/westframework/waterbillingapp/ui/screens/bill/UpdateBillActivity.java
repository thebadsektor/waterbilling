package com.westframework.waterbillingapp.ui.screens.bill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.westframework.waterbillingapp.R;
import com.westframework.waterbillingapp.data.bill.helpers.DBHelperBill;
import com.westframework.waterbillingapp.data.helpers.DateHelper;
import com.westframework.waterbillingapp.ui.screens.generatebill.ViewGenerateBillActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class UpdateBillActivity extends AppCompatActivity {

    TextView tvTitle, tvAppNum, tvFullName, tvAddress, tvClassification, tvMeterNo,
            tvPeriodCovered, tvBillingMonth, tvReadDate, etPrePread, etActual, etUsed, tvDueDate,
            tvOrAmount, tvOthers, tvSeniorId, tvDiscount, tvBillAmount,
            tvSurchargeAmount, tvPaymentAfterDue, tvArrears;
    AppCompatImageView btnBack;
    Button btnEditBill;
    private DBHelperBill dbHelperBill;

    private AppCompatImageView cancel;
    private AppCompatImageView save;
    //--date picker
    private AppCompatImageView mButtonReadDate;

    private Button mPickOrDate;
    private TextView mTextViewOrDate;

    private Button mPickDueDate;
    //---------------

    boolean isValidated = false;

    int billId;
    private String bApplicationNo;
    private String bFullName;
    private String bOrNum;
    private String readingMonth;
    private String bClassification;
    private String mainDueDate;
    private String bAppType;
    private String period;
    double bOrAmount;
    double grandTotal;
    double surcharge;
    double arrears;
    int pre;
    int actual;
    int used;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bill);

        tvTitle = findViewById(R.id.tvTitleUpdate);
        tvTitle = findViewById(R.id.tvTitleUpdate);
        tvAppNum = findViewById(R.id.tvAppNumUpdate);
        tvFullName = findViewById(R.id.tvFullNameUpdate);
        tvAddress = findViewById(R.id.tvAddressUpdate);
        tvClassification = findViewById(R.id.tvClassificationUpdate);
        tvMeterNo = findViewById(R.id.tvMeterNoUpdate);
        tvPeriodCovered = findViewById(R.id.tvMonthCoverUpdate);
        tvBillingMonth = findViewById(R.id.tvBillingMonthUpdate);
        tvReadDate = findViewById(R.id.tvReadDateUpdate);
        tvDueDate = findViewById(R.id.tvDueDateUpdate);
        etPrePread = findViewById(R.id.tiPreReadUpdate);
        etActual = findViewById(R.id.tiActualUpdate);
        etUsed = findViewById(R.id.tiUsedUpdate);
        tvOrAmount = findViewById(R.id.tvOrAmountUpdate);
        tvArrears = findViewById(R.id.tvArrearsUpdate);
        tvOthers = findViewById(R.id.tvOthersUpdate);
        tvSeniorId = findViewById(R.id.tvSeniorIdUpdate);
        tvDiscount = findViewById(R.id.tvDiscountUpdate);
        tvBillAmount = findViewById(R.id.tvBillAmountUpdate);
        tvSurchargeAmount = findViewById(R.id.tvSurchargeAmountUpdate);
        tvPaymentAfterDue = findViewById(R.id.tvPaymentAfterDueUpdate);

        dbHelperBill = new DBHelperBill(this);

        MaterialDatePicker.Builder mReadDateBuilder = MaterialDatePicker.Builder.datePicker();
        mReadDateBuilder.setTitleText("Set Read Date");

        final MaterialDatePicker mReadDatePicker = mReadDateBuilder.build();


        mButtonReadDate = findViewById(R.id.mButtonReadDateUpdate);
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

                        int exists = dbHelperBill.billMonthExists(bApplicationNo, period);

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
        //---------------

        cancel = findViewById(R.id.btnBillCancel);
        save = findViewById(R.id.btnBillSave);

        getData();

        cancel.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Nothing Updated", Toast.LENGTH_LONG).show();
            finish();
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                isValidated = CheckAllFields();
//                if(isValidated){
//                    updateBill();
//                }

                updateBill();
            }
        });

        etPrePread.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().trim().equals("")){

                    if (Integer.parseInt(etPrePread.getText().toString()) > 9999) {
                        etPrePread.setText("9999");
                    }

                }

                if((actual - pre) < 0){ etUsed.setText(String.valueOf("0"));
                }else{ etUsed.setText( String.valueOf(actual - pre));}

                actual = Integer.parseInt(etActual.getText().toString());

                if("".equals(etPrePread.getText().toString())){ pre = 0;
                }else{ pre = Integer.parseInt(etPrePread.getText().toString());}

                used = Integer.parseInt(etUsed.getText().toString());

                bOrAmount = calculateOrAmount(bApplicationNo, used);
                BigDecimal deciOrAmount  = new BigDecimal(bOrAmount).setScale(2, RoundingMode.HALF_UP);
                tvOrAmount.setText(String.valueOf(deciOrAmount));
                arrears = Double.parseDouble(tvArrears.getText().toString());
                grandTotal = bOrAmount + arrears;
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

                    if (Integer.parseInt(etActual.getText().toString()) > 9999) {
                        etActual.setText("9999");
                    }
                }

                pre = Integer.parseInt(etPrePread.getText().toString());

                if((actual - pre) < 0){ etUsed.setText(String.valueOf("0"));
                }else{ etUsed.setText( String.valueOf(actual - pre));}

                if("".equals(etActual.getText().toString())){ actual = 0;
                }else{ actual = Integer.parseInt(etActual.getText().toString()); }

                used = Integer.parseInt(etUsed.getText().toString());

                bOrAmount = calculateOrAmount(bApplicationNo, used);

                BigDecimal deciOrAmount  = new BigDecimal(bOrAmount).setScale(2, RoundingMode.HALF_UP);
                tvOrAmount.setText(String.valueOf(deciOrAmount));
                arrears = Double.parseDouble(tvArrears.getText().toString());
                grandTotal = bOrAmount + arrears;
                BigDecimal deciGrandTotal  = new BigDecimal(grandTotal).setScale(2, RoundingMode.HALF_UP);
                tvBillAmount.setText(String.valueOf(deciGrandTotal));
                surcharge = (surcharge + (grandTotal * 0.6));
                BigDecimal deciSurcharge = new BigDecimal(surcharge ).setScale(2, RoundingMode.HALF_UP);
                tvSurchargeAmount.setText(String.valueOf(deciSurcharge));
                BigDecimal deciPaymentAfterDue = new BigDecimal(grandTotal + surcharge).setScale(2, RoundingMode.HALF_UP);
                tvPaymentAfterDue.setText(String.valueOf(deciPaymentAfterDue));
            }
        });

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
                }else{used = Integer.parseInt(etUsed.getText().toString()); }

                bOrAmount = calculateOrAmount(bApplicationNo, used);
                Log.d("orAmount Afters", String.valueOf(bOrAmount));
                BigDecimal deciOrAmount  = new BigDecimal(bOrAmount).setScale(2, RoundingMode.HALF_UP);
                tvOrAmount.setText(String.valueOf(deciOrAmount));
                arrears = Double.parseDouble(tvArrears.getText().toString());
                grandTotal = bOrAmount + arrears;
                BigDecimal deciGrandTotal  = new BigDecimal(grandTotal).setScale(2, RoundingMode.HALF_UP);
                tvBillAmount.setText(String.valueOf(deciGrandTotal));
                surcharge = (surcharge + (grandTotal * 0.6));
                BigDecimal deciSurcharge = new BigDecimal(surcharge ).setScale(2, RoundingMode.HALF_UP);
                tvSurchargeAmount.setText(String.valueOf(deciSurcharge));
                BigDecimal deciPaymentAfterDue = new BigDecimal(grandTotal + surcharge).setScale(2, RoundingMode.HALF_UP);
                tvPaymentAfterDue.setText(String.valueOf(deciPaymentAfterDue));
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

    public void updateBill(){

        String lastTitle = tvTitle.getText().toString();
        //bApplicationNo;
        //bFullName;
        String lastAddress = tvAddress.getText().toString();
        String lastClassification = tvClassification.getText().toString();
        String lastMeterNo = tvMeterNo.getText().toString();
        String lastPeriodCovered = tvPeriodCovered.getText().toString();
        String lastBillingMonth = tvBillingMonth.getText().toString();
        String lastReadDate = tvReadDate.getText().toString();
        String lastDueDate = tvDueDate.getText().toString();
        String lastPrePread = etPrePread.getText().toString();
        String lastActual = etActual.getText().toString();
        String lastUsed = etUsed.getText().toString();
        //bOrNum
        String lastOrAmount = tvOrAmount.getText().toString();
        String lastArrears = tvArrears.getText().toString();
        String lastOthers = tvOthers.getText().toString();
        String lastSeniorId = tvSeniorId.getText().toString();
        String lastDiscount = tvDiscount.getText().toString();
        String lastBillAmount = tvBillAmount.getText().toString();
        String lastSurchargeAmount = tvSurchargeAmount.getText().toString();
        String lastPaymentAfterDue = tvPaymentAfterDue.getText().toString();

        if("".equals(lastPrePread.trim())){lastPrePread="0";}
        if("".equals(lastActual.trim())){lastActual="0";}
        if("".equals(lastUsed.trim())){lastUsed="0";}

        Intent intent = new Intent();
        intent.putExtra("id", billId);
        intent.putExtra("application_no", bApplicationNo);
        intent.putExtra("full_name", bFullName);
        intent.putExtra("bill_address", lastAddress);
        intent.putExtra("classification", lastClassification);
        intent.putExtra("meter_read",lastMeterNo );
        intent.putExtra("period_covered", lastPeriodCovered);
        intent.putExtra("reading_month", lastBillingMonth);
        intent.putExtra("read_date", lastReadDate);
        intent.putExtra("due_date", lastDueDate);
        intent.putExtra("pre_reading", lastPrePread);
        intent.putExtra("actual_reading", lastActual);
        intent.putExtra("cu_m_used",lastUsed);
        intent.putExtra("or_num", bOrNum);
        intent.putExtra("or_amount", lastOrAmount);
        intent.putExtra("arrears", lastArrears);
        intent.putExtra("others", lastOthers);
        intent.putExtra("senior_ids", lastSeniorId);
        intent.putExtra("discount_amount", lastDiscount);
        intent.putExtra("bill_amount", lastBillAmount);
        intent.putExtra("surcharge", lastSurchargeAmount);
        intent.putExtra("payment_after_due", lastPaymentAfterDue);

        if(billId != -1)
        {
            intent.putExtra("billId", billId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void getData()
    {
        Intent i = getIntent();
        billId = i.getIntExtra("id", -1);

        bApplicationNo = i.getStringExtra("application_no");
        bFullName = i.getStringExtra("full_name");
        String bAddress = i.getStringExtra("bill_address");
        bClassification = i.getStringExtra("classification");
        int bMeterNo = i.getIntExtra("meter_read", 0);
        String bMonthCover = i.getStringExtra("period_covered");
        String bBillingMonth = i.getStringExtra("reading_month");
        String bReadDate = i.getStringExtra("read_date");
        String bDueDate = i.getStringExtra("due_date");
        int bPreRead = i.getIntExtra("pre_reading", 0);
        int bActual = i.getIntExtra("actual_reading", 0);
        int bUsed = i.getIntExtra("cu_m_used", 0);
        bOrNum = i.getStringExtra("or_num");
        bOrAmount = i.getDoubleExtra("or_amount", 0);
        BigDecimal deciOrAmount  = new BigDecimal(bOrAmount).setScale(2, RoundingMode.HALF_UP);
        double bArrears = i.getDoubleExtra("arrears", 0);
        BigDecimal deciArrears  = new BigDecimal(bArrears).setScale(2, RoundingMode.HALF_UP);
        double bOthers = i.getDoubleExtra("others", 0);
        String bSeniorId = i.getStringExtra("senior_ids");
        double bDiscount = i.getDoubleExtra("discount_amount", 0);
        double bBillAmount = i.getDoubleExtra("bill_amount", 0);
        BigDecimal deciBillAmount  = new BigDecimal(bBillAmount).setScale(2, RoundingMode.HALF_UP);
        double bSurchargeAmount = i.getDoubleExtra("surcharge", 0);
        BigDecimal deciSurcharge  = new BigDecimal(bSurchargeAmount).setScale(2, RoundingMode.HALF_UP);
        double bPaymentAfterDue = i.getDoubleExtra("payment_after_due", 0);
        BigDecimal deciPaymentAfterDue  = new BigDecimal(bPaymentAfterDue).setScale(2, RoundingMode.HALF_UP);

        if(bReadDate == null || bReadDate == ""){
            bReadDate = "Read Date Not Set";
        }

        tvTitle.setText(bOrNum);
        tvAppNum.setText(bApplicationNo);
        tvFullName.setText(bFullName);
        tvAddress.setText(bAddress);
        tvClassification.setText(bClassification);
        tvMeterNo.setText(String.valueOf(bMeterNo));
        tvPeriodCovered.setText(bMonthCover);
        tvBillingMonth.setText(String.valueOf(bBillingMonth));
        tvReadDate.setText(bReadDate);
        tvDueDate.setText(bDueDate);
        etPrePread.setText(String.valueOf(bPreRead));
        etActual.setText(String.valueOf(bActual));
        etUsed.setText(String.valueOf(bUsed));
        tvOrAmount.setText(String.valueOf(deciOrAmount));
        tvArrears.setText(String.valueOf(deciArrears));
        tvOthers.setText(String.valueOf(bOthers));
        tvSeniorId.setText(String.valueOf(bSeniorId));
        tvDiscount.setText(String.valueOf(bDiscount));
        tvBillAmount.setText(String.valueOf(deciBillAmount));
        tvSurchargeAmount.setText(String.valueOf(deciSurcharge));
        tvPaymentAfterDue.setText(String.valueOf(deciPaymentAfterDue));
    }

    private double calculateOrAmount( String appAppType, int used) {
        double amount = 0;
        int min = 0;
        int max = 0;
        int classAmount = 0;
        int increment = 0;

        ArrayList<HashMap<String, String>> classifications = dbHelperBill.getClass(bClassification, used);
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

        return amount;
    }

    //Bill Month Exists START
    public void billMonthExistsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBillActivity.this);
        builder.setTitle("ALERT!");
        builder.setMessage("This billing period already exists!");
        builder.setIcon(R.drawable.ic_add_60px);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Log.d("Bill Month Exists", "Bill Month Exists");
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
                    year = entry.getValue();
                }
                if(entry.getKey() == "holiday_month"){
                    switch (entry.getValue().toLowerCase()){
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
                    day = entry.getValue();
                }
            }
            rawDate = month + "-" + day + "-" + year;
            Date date = inputFormat.parse(rawDate);
            String outputText = outputFormat.format(date);

            calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
            int week = calendar.get(Calendar.WEEK_OF_MONTH);
            holidays.add(outputText);
        }
        dbHelperBill.close();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBillActivity.this);
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

    //Trim Leadin Zeroes
    private String trimLeadingZeros(String string)
    {
        boolean startsWithZero = true;
        while (startsWithZero)
        {
            if(string.startsWith("0") && string.length()>=2 && !string.substring(1,2).equalsIgnoreCase("."))
            {string = string.substring(1);}
            else
            {startsWithZero = false;}
        }
        return  string;
    }
}