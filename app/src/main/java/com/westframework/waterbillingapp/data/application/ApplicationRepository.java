package com.westframework.waterbillingapp.data.application;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;

import java.util.List;

public class ApplicationRepository {

    private ApplicationDao applicationDao;
    private LiveData<List<Application>> applications;

    public ApplicationRepository(android.app.Application application){
        WaterBilliingAppDatabase database = WaterBilliingAppDatabase.getInstance(application);
        applicationDao = database.applicationDao();
        applications = applicationDao.getAllApplications();
    }

    public void insert(Application application){
        new InsertBillAsyncTask(applicationDao).execute(application);
    }

    public void update(Application application){
        new UpdateBillAsyncTask(applicationDao).execute(application);
    }

    public void delete(Application application){
        new DeleteBillAsyncTask(applicationDao).execute(application);
    }

    public LiveData<List<Application>> getAllApplications(){ return applications;}

    private static class InsertBillAsyncTask extends AsyncTask<Application, Void, Void> {
        private ApplicationDao applicationDao;
        private InsertBillAsyncTask(ApplicationDao applicationDao){
            this.applicationDao = applicationDao;
        }
        @Override
        protected Void doInBackground(Application... applications){
            applicationDao.Insert(applications[0]);
            return null;
        }
    }

    private static class UpdateBillAsyncTask extends AsyncTask<Application, Void, Void> {
        private ApplicationDao applicationDao;
        private UpdateBillAsyncTask(ApplicationDao applicationDao){
            this.applicationDao = applicationDao;
        }
        @Override
        protected Void doInBackground(Application... applications){
            applicationDao.Update(applications[0]);
            return null;
        }
    }

    private static class DeleteBillAsyncTask extends AsyncTask<Application, Void, Void> {
        private ApplicationDao applicationDao;
        private DeleteBillAsyncTask(ApplicationDao applicationDao){
            this.applicationDao = applicationDao;
        }
        @Override
        protected Void doInBackground(Application... applications){
            applicationDao.Delete(applications[0]);
            return null;
        }
    }
}
