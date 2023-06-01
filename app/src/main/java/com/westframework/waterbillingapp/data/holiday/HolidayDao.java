package com.westframework.waterbillingapp.data.holiday;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HolidayDao {

    @Insert
    void Insert(Holiday holiday);

    @Update
    void Update(Holiday holiday);

    @Delete
    void Delete(Holiday holiday);

    @Query("SELECT * FROM ws_holidays ORDER BY id ASC")
    LiveData<List<Holiday>> getAllHolidays();

    @Query("SELECT COUNT(*) FROM ws_holidays")
    int getCount();
}
