package com.westframework.waterbillingapp.data.discount;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ws_discount_tbl")
public class Discount {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String category;
    public String type;
    public String description;
    public double amount;

    public Discount(String category, String type, String description, double amount){
        this.category = category;
        this.type = type;
        this.description = description;
        this.amount = amount;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getCategory(){return category;}
    public String getType(){return type;}
    public String getDescription(){return description;}
    public double getAmount(){return amount;}

}
