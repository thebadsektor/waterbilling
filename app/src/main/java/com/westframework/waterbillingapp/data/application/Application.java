package com.westframework.waterbillingapp.data.application;


import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ws_application_tbl")
public class Application {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @Nullable
    public String application_no;

    @Nullable
    public String application_date;

    @Nullable
    public String lname;

    @Nullable
    public String mname;

    @Nullable
    public String fname;

    @Nullable
    public String consumer_type;

    @Nullable
    public String house_no;

    @Nullable
    public String building_no;

    @Nullable
    public String street;

    @Nullable
    public String barangay;

    @Nullable
    public String water_meter_num;

    @Nullable
    public String or_status;

    @Nullable
    @ColumnInfo(name = "status", defaultValue = "ACTIVE")
    public String status;

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    public String updated_at;

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    public String created_at;

    public Application(
            String application_no,
            String application_date,
            String lname,
            String mname,
            String fname,
            String consumer_type,
            String house_no,
            String building_no,
            String street,
            String barangay,
            String water_meter_num,
            String or_status,
            String status,
            String updated_at,
            String created_at
            ){
        this.application_no = application_no;
        this.application_date = application_date;
        this.lname = lname;
        this.mname = mname;
        this.fname = fname;
        this.consumer_type = consumer_type;
        this.house_no = house_no;
        this.building_no = building_no;
        this.street = street;
        this.barangay = barangay;
        this.water_meter_num = water_meter_num;
        this.or_status = or_status;
        this.status = status;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getApplicationNo(){ return application_no; }
    public String getApplicationDate(){ return application_date; }
    public String getLastName(){ return lname; }
    public String getMiddleInitial(){ return mname; }
    public String getFirstName(){ return fname; }
    public String getApplicationType(){ return consumer_type; }
    public String getHouseNo(){ return house_no; }
    public String getBuildingNo(){ return building_no; }
    public String getStreet(){ return street; }
    public String getBrgy(){ return barangay; }
    public String getMeterNo(){ return water_meter_num; }
    public String getOrStatus(){ return or_status; }
    public String getStatus(){ return status; }
    public String getUpdatedAt(){ return updated_at; }
    public String getCreatedAt(){ return created_at; }

}
