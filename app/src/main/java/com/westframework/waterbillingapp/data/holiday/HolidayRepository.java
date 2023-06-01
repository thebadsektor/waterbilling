package com.westframework.waterbillingapp.data.holiday;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;

import java.util.List;

public class HolidayRepository {

    private HolidayDao holidayDao;
    private LiveData<List<Holiday>> holidays;

    public HolidayRepository(Application application){
        WaterBilliingAppDatabase database = WaterBilliingAppDatabase.getInstance(application);
        holidayDao = database.holidayDao();
        holidays = holidayDao.getAllHolidays();
    }

    public void insert(Holiday holiday){
        new InsertHolidayAsyncTask(holidayDao).execute(holiday);
    }

    public void update(Holiday holiday){
        new UpdateHolidayAsyncTask(holidayDao).execute(holiday);
    }

    public void delete(Holiday holiday){
        new DeleteHolidayAsyncTask(holidayDao).execute(holiday);
    }

    public LiveData<List<Holiday>> getHolidays(){return holidays;}

    private static class InsertHolidayAsyncTask extends AsyncTask<Holiday, Void, Void>{
        private HolidayDao holidayDao;
        private InsertHolidayAsyncTask(HolidayDao holidayDao){this.holidayDao = holidayDao;}
        @Override
        protected Void doInBackground(Holiday... holidays){
            holidayDao.Insert(holidays[0]);
            return null;
        }
    }

    private static class UpdateHolidayAsyncTask extends AsyncTask<Holiday, Void, Void>{
        private HolidayDao holidayDao;
        private UpdateHolidayAsyncTask(HolidayDao holidayDao){this.holidayDao = holidayDao;}
        @Override
        protected Void doInBackground(Holiday... holidays){
            holidayDao.Update(holidays[0]);
            return null;
        }
    }

    public static class DeleteHolidayAsyncTask extends AsyncTask<Holiday, Void, Void>{
        private HolidayDao holidayDao;
        private DeleteHolidayAsyncTask(HolidayDao holidayDao){this.holidayDao = holidayDao;}
        @Override
        protected Void doInBackground(Holiday... holidays){
            holidayDao.Delete(holidays[0]);
            return null;
        }
    }


}
