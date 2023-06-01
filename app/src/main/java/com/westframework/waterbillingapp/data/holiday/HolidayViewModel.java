package com.westframework.waterbillingapp.data.holiday;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HolidayViewModel extends AndroidViewModel {

    private HolidayRepository repository;
    private final LiveData<List<Holiday>> holidays;

    public HolidayViewModel(@NonNull Application application){
        super  (application);
        repository = new HolidayRepository(application);
        holidays = repository.getHolidays();
    }

    public void insert(Holiday holiday)
    {
        repository.insert(holiday);
    }
    public void update(Holiday holiday)
    {
        repository.update(holiday);
    }

    public void delete(Holiday holiday){repository.delete(holiday);}

    public LiveData<List<Holiday>> getHolidays(){
        return holidays;
    }
}
