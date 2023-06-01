package com.westframework.waterbillingapp.data.amnesty;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ws_amnesty")
public class Amnesty {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @Nullable
    @ColumnInfo(name = "ordinance_name")
    public String ordinance_name;

    @Nullable
    @ColumnInfo(name = "bill_month")
    public String bill_month;

    @Nullable
    @ColumnInfo(name = "amnesty_desc")
    public String amnesty_desc;

    @Nullable
    @ColumnInfo(name = "bill_year")
    public String bill_year;

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    public String updated_at;

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    public String created_at;

    public Amnesty(
            String ordinance_name,
            String bill_month,
            String amnesty_desc,
            String bill_year,
            String updated_at,
            String created_at
    )
    {
        this.ordinance_name = ordinance_name;
        this.bill_month = bill_month;
        this.amnesty_desc = amnesty_desc;
        this.bill_year = bill_year;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getOrdinanceName(){return ordinance_name;}
    public String getBillMonth(){return bill_month;}
    public String getDesc(){return amnesty_desc;}
    public String getBillYear(){return bill_year;}
    public String getCreatedAt(){ return created_at; }
    public String getUpdatedAt(){ return updated_at; }
}
