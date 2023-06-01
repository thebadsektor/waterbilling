package com.westframework.waterbillingapp.data.discount;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;
import android.app.Application;

import java.util.List;

public class DiscountRepository {

    private DiscountDao discountDao;
    private LiveData<List<Discount>> discounts;

    public DiscountRepository(Application application){
        WaterBilliingAppDatabase database = WaterBilliingAppDatabase.getInstance(application);
        discountDao = database.discountDao();
        discounts = discountDao.getAllDisounts();
    }

    public void insert(Discount discount){
        new DiscountRepository.InsertDiscountAsyncTask(discountDao).execute(discount);
    }

    public void update(Discount discount){
        new DiscountRepository.UpdateDiscountAsyncTask(discountDao).execute(discount);
    }

    public void delete(Discount discount){
        new DiscountRepository.DeleteDiscountAsyncTask(discountDao).execute(discount);
    }

    public LiveData<List<Discount>> getAllDiscounts(){ return discounts;}

    private static class InsertDiscountAsyncTask extends AsyncTask<Discount, Void, Void> {
        private DiscountDao discountDao;
        private InsertDiscountAsyncTask(DiscountDao applicationDao){
            this.discountDao = discountDao;
        }
        @Override
        protected Void doInBackground(Discount... discounts){
            discountDao.Insert(discounts[0]);
            return null;
        }
    }

    private static class UpdateDiscountAsyncTask extends AsyncTask<Discount, Void, Void> {
        private DiscountDao discountDao;
        private UpdateDiscountAsyncTask(DiscountDao applicationDao){
            this.discountDao = discountDao;
        }
        @Override
        protected Void doInBackground(Discount... applications){
            discountDao.Update(applications[0]);
            return null;
        }
    }

    private static class DeleteDiscountAsyncTask extends AsyncTask<Discount, Void, Void> {
        private DiscountDao discountDao;
        private DeleteDiscountAsyncTask(DiscountDao applicationDao){
            this.discountDao = discountDao;
        }
        @Override
        protected Void doInBackground(Discount... discounts){
            discountDao.Delete(discounts[0]);
            return null;
        }
    }
}
