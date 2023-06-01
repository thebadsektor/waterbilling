package com.westframework.waterbillingapp.data.amnesty;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AmnestyDao {

    @Insert
    void Insert(Amnesty amnesty);

    @Update
    void Update(Amnesty amnesty);

    @Delete
    void Delete(Amnesty amnesty);

    @Query("SELECT * FROM ws_amnesty ORDER BY id ASC")
    LiveData<List<Amnesty>> getAmnesties();

    @Query("SELECT COUNT(*) FROM ws_amnesty")
    int getCount();
}
