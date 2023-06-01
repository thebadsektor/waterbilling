package com.westframework.waterbillingapp.data.discount;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DiscountDao {

    @Insert
    void Insert(Discount discount);

    @Update
    void Update(Discount discount);

    @Delete
    void Delete(Discount discount);

    @Query("SELECT * FROM ws_discount_tbl ORDER BY id ASC")
    LiveData<List<Discount>> getAllDisounts();

    @Query("SELECT COUNT(*) FROM ws_discount_tbl")
    int getCount();
}
