package com.westframework.waterbillingapp.data.application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ApplicationDao {

    @Insert
    void Insert(Application application);

    @Update
    void Update(Application application);

    @Delete
    void Delete(Application application);

    @Query("SELECT * FROM ws_application_tbl ORDER BY id ASC")
    LiveData<List<Application>> getAllApplications();

    @Query("SELECT COUNT(*) FROM ws_application_tbl")
    int getCount();
}
