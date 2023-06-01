package com.westframework.waterbillingapp.data.bill;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ws_water_bill")
public class WaterBill {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @Nullable
    public String application_no;

    @Nullable
    public String read_date;

    @Nullable
    public String due_date;

    @Nullable
    public String full_name;

    @Nullable
    public String bill_address;

    @Nullable
    public String classification;
    
    @Nullable
    @ColumnInfo(name = "meter_read", defaultValue = "0")
    public int meter_read;

    @Nullable
    public String senior_ids;

    @Nullable
    public String reading_month;

    @Nullable
    @ColumnInfo(name = "pre_reading", defaultValue = "0")
    public int pre_reading;

    @Nullable
    @ColumnInfo(name = "actual_reading", defaultValue = "0")
    public int actual_reading;

    @Nullable
    @ColumnInfo(name = "cu_m_used", defaultValue = "0")
    public int cu_m_used;

    @Nullable
    @ColumnInfo(name = "discount", defaultValue = "0")
    public double discount;

    @Nullable
    @ColumnInfo(name = "penalty", defaultValue = "0")
    public double penalty;

    @Nullable
    @ColumnInfo(name = "discount_amount", defaultValue = "0")
    public double discount_amount;

    @Nullable
    @ColumnInfo(name = "bill_amount", defaultValue = "0")
    public double bill_amount;

    @Nullable
    public String status;

    @Nullable
    public String or_num;

    @Nullable
    @ColumnInfo(name = "or_amount", defaultValue = "0")
    public double or_amount;

    @Nullable
    @ColumnInfo(name = "arrears", defaultValue = "0")
    public double arrears;

    @Nullable
    @ColumnInfo(name = "others", defaultValue = "0")
    public double others;

    @Nullable
    public String or_date;

    @Nullable
    public String bill_cat;

    @Nullable
    public String bill_status;

    @Nullable
    @ColumnInfo(name = "surcharge", defaultValue = "0")
    public double surcharge;

    @Nullable
    @ColumnInfo(name = "payment_after_due", defaultValue = "0")
    public double payment_after_due;

    @Nullable
    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    public String updated_at;

    @Nullable
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    public String created_at;

    public WaterBill(
                     String application_no,
                     String read_date,
                     String due_date,
                     String full_name,
                     String bill_address,
                     String classification,
                     int meter_read,
                     String senior_ids,
                     String reading_month,
                     int pre_reading,
                     int actual_reading,
                     int cu_m_used,
                     double discount,
                     double penalty,
                     double discount_amount,
                     double bill_amount,
                     String status,
                     String or_num,
                     double or_amount,
                     double arrears,
                     double others,
                     String or_date,
                     String bill_cat,
                     String bill_status,
                     double surcharge,
                     double payment_after_due,
                     String updated_at,
                     String created_at
    ){
        this.application_no = application_no;
        this.read_date = read_date;
        this.due_date = due_date;
        this.full_name = full_name;
        this.bill_address = bill_address;
        this.classification = classification;
        this.meter_read = meter_read;
        this.senior_ids = senior_ids;
        this.reading_month = reading_month;
        this.pre_reading = pre_reading;
        this.actual_reading = actual_reading;
        this.cu_m_used = cu_m_used;
        this.discount = discount;
        this.penalty = penalty;
        this.discount_amount = discount_amount;
        this.bill_amount = bill_amount;
        this.status = status;
        this.or_num = or_num;
        this.or_amount = or_amount;
        this.arrears = arrears;
        this.others = others;
        this.or_date = or_date;
        this.bill_cat = bill_cat;
        this.bill_status = bill_status;
        this.surcharge = surcharge;
        this.payment_after_due = payment_after_due;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getApplicationNo(){return application_no;}
    public String getReadDate(){return read_date;}
    public String getDueDate(){return due_date;}
    public String getFullName(){return full_name;}
    public String getBillAddress(){return bill_address;}
    public String getClassification(){return classification;}
    public int getMeterRead(){return meter_read;}
    public String getSeniorIds(){return senior_ids;}
    public String getReadingMonth(){return reading_month;}
    public int getPreReading(){return pre_reading;}
    public int getActualReading(){return actual_reading;}
    public int getUsed(){return cu_m_used;}
    public double getDiscount(){return discount;}
    public double getPenalty(){return penalty;}
    public double getDiscountAmount(){return discount_amount;}
    public double getBillAmount(){return bill_amount;}
    public String getStatus(){return status;}
    public String getOrNum(){return or_num;}
    public double getOrAmount(){return or_amount;}
    public double getArrears(){return arrears;}
    public double getOthers(){return others;}
    public String getOrDate(){return or_date;}
    public String getBillCat(){return bill_cat;}
    public String getBillStatus(){return bill_status;}
    public double getSurcharge(){return surcharge;}
    public double getPaymentAfterDue(){return payment_after_due;}
    public String getUpdatedAt(){return updated_at;}
    public String getCreatedAt(){return created_at;}

}
