package com.westframework.waterbillingapp.data.bill;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;

import java.util.List;

public class WaterBillRepository {

    private WaterBillDao waterBillDao;
    private LiveData<List<WaterBill>> waterBills;

    public WaterBillRepository(Application application){
        WaterBilliingAppDatabase database = WaterBilliingAppDatabase.getInstance(application);
        waterBillDao = database.waterBillDao();
        waterBills = waterBillDao.getAllWaterBills();
    }

    public void insert(WaterBill waterBill){
        new InsertBillAsyncTask(waterBillDao).execute(waterBill);
    }

    public void update(WaterBill waterBill){
        new UpdateBillAsyncTask(waterBillDao).execute(waterBill);
    }

    public void delete(WaterBill waterBill){
        new DeleteBillAsyncTask(waterBillDao).execute(waterBill);
    }

    public LiveData<List<WaterBill>> getWaterBills(){ return waterBills;}

    private static class InsertBillAsyncTask extends AsyncTask<WaterBill, Void, Void> {
        private WaterBillDao waterBillDao;
        private InsertBillAsyncTask(WaterBillDao waterBillDao){
            this.waterBillDao = waterBillDao;
        }
        @Override
        protected Void doInBackground(WaterBill... waterBills){
            waterBillDao.Insert(waterBills[0]);
            return null;
        }
    }

    private static class UpdateBillAsyncTask extends AsyncTask<WaterBill, Void, Void> {
        private WaterBillDao waterBillDao;
        private UpdateBillAsyncTask(WaterBillDao waterBillDao){
            this.waterBillDao = waterBillDao;
        }
        @Override
        protected Void doInBackground(WaterBill... waterBills){
            waterBillDao.Update(waterBills[0]);
            return null;
        }
    }

    private static class DeleteBillAsyncTask extends AsyncTask<WaterBill, Void, Void> {
        private WaterBillDao waterBillDao;
        private DeleteBillAsyncTask(WaterBillDao waterBillDao){
            this.waterBillDao = waterBillDao;
        }
        @Override
        protected Void doInBackground(WaterBill... waterBills){
            waterBillDao.Delete(waterBills[0]);
            return null;
        }
    }
}
