package com.westframework.waterbillingapp.data.holiday;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ws_holidays")
public class Holiday {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @Nullable
    @ColumnInfo(name = "holiday_name")
    public String holiday_name;

    @Nullable
    @ColumnInfo(name = "holiday_month")
    public String holiday_month;

    @Nullable
    @ColumnInfo(name = "holiday_date")
    public String holiday_date;

    @Nullable
    @ColumnInfo(name = "holiday_year")
    public String holiday_year;

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    public String updated_at;

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    public String created_at;

    public Holiday(
            String holiday_name,
            String holiday_month,
            String holiday_date,
            String holiday_year,
            String updated_at,
            String created_at
    )
    {
        this.holiday_name = holiday_name;
        this.holiday_month = holiday_month;
        this.holiday_date = holiday_date;
        this.holiday_year = holiday_year;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getHolidayName(){return holiday_name;}
    public String getHolidayMonth(){return holiday_month;}
    public String getHolidayDate(){return holiday_date;}
    public String getHolidayYear(){return holiday_year;}
    public String getCreatedAt(){ return created_at; }
    public String getUpdatedAt(){ return updated_at; }
}
