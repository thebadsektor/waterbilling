package com.westframework.waterbillingapp.data.bill;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WaterBillDao {

    @Insert
    void Insert(WaterBill waterBill);

    @Update
    void Update(WaterBill waterBill);

    @Delete
    void Delete(WaterBill waterBill);

    @Query("SELECT * FROM ws_water_bill ORDER BY created_at DESC")
    LiveData<List<WaterBill>> getAllWaterBills();

    @Query("SELECT COUNT(*) FROM ws_water_bill")
    int getCount();

}
