package com.westframework.waterbillingapp.data.classification;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ws_classification_tbl")
public class Classification {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "class_cat", defaultValue = "Residential")
    public String class_cat;

    @Nullable
    @ColumnInfo(name = "cu_m_min", defaultValue = "0")
    public double cu_m_min;

    @Nullable
    @ColumnInfo(name = "cu_m_max", defaultValue = "0")
    public double cu_m_max;

    @Nullable
    @ColumnInfo(name = "amount", defaultValue = "0")
    public double amount;

    @Nullable
    public int increment_by;

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    public String updated_at;

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    public String created_at;

    public Classification(
            String class_cat,
            double cu_m_min,
            double cu_m_max,
            double amount,
            int increment_by,
            String updated_at,
            String created_at
    )
    {
        this.class_cat = class_cat;
        this.cu_m_min = cu_m_min;
        this.cu_m_max = cu_m_max;
        this.amount = amount;
        this.increment_by = increment_by;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public String getClassCat(){return class_cat;}
    public double cuMin(){return cu_m_min;}
    public double cuMax(){return cu_m_max;}
    public double amount(){return amount;}
    public int getIncrementBy(){return increment_by;}
    public String getCreatedAt(){ return created_at; }
    public String getUpdatedAt(){ return updated_at; }
}
