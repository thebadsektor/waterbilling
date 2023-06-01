package com.westframework.waterbillingapp.data.bill;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WaterBillViewModel extends AndroidViewModel {

    private WaterBillRepository repository;
    private final LiveData<List<WaterBill>> waterBills;

    public WaterBillViewModel(@NonNull Application application){
        super  (application);
        repository = new WaterBillRepository(application);
        waterBills = repository.getWaterBills();
        }

    public void insert(WaterBill waterBill)
    {
        repository.insert(waterBill);
    }
    public void update(WaterBill waterBill)
    {
        repository.update(waterBill);
    }

    public void delete(WaterBill waterBill){repository.delete(waterBill);}

    public LiveData<List<WaterBill>> getWaterBills(){
        return waterBills;
    }
}
