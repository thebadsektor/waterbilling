package com.westframework.waterbillingapp.data.classification;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClassificationDao {

    @Insert
    void Insert(Classification classification);

    @Update
    void Update(Classification classification);

    @Delete
    void Delete(Classification classification);

    @Query("SELECT * FROM ws_classification_tbl ORDER BY id ASC")
    LiveData<List<Classification>> getAllClassifications();

    @Query("SELECT COUNT(*) FROM ws_classification_tbl")
    int getCount();
}
