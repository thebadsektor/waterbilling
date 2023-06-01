package com.westframework.waterbillingapp.data.amnesty;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;

import java.util.List;

public class AmnestyRepository {

    private AmnestyDao amnestyDao;
    private LiveData<List<Amnesty>> amnesties;

    public AmnestyRepository(Application application){
        WaterBilliingAppDatabase database = WaterBilliingAppDatabase.getInstance(application);
        amnestyDao = database.amnestyDao();
        amnesties = amnestyDao.getAmnesties();
    }

    public void insert(Amnesty amnesty){
        new InsertAmnestyAsyncTask(amnestyDao).execute(amnesty);
    }

    public void update(Amnesty amnesty){
        new UpdateAmnestyAsyncTask(amnestyDao).execute(amnesty);
    }

    public void delete(Amnesty amnesty){
        new DeleteAmnestyAsyncTask(amnestyDao).execute(amnesty);
    }

    public LiveData<List<Amnesty>> getAmnesties(){return amnesties;}

    private static class InsertAmnestyAsyncTask extends AsyncTask<Amnesty, Void, Void>{
        private AmnestyDao amnestyDao;
        private InsertAmnestyAsyncTask(AmnestyDao amnestyDao){this.amnestyDao = amnestyDao;}
        @Override
        protected Void doInBackground(Amnesty... amnesties){
            amnestyDao.Insert(amnesties[0]);
            return null;
        }
    }

    private static class UpdateAmnestyAsyncTask extends AsyncTask<Amnesty, Void, Void>{
        private AmnestyDao amnestyDao;
        private UpdateAmnestyAsyncTask(AmnestyDao amnestyDao){this.amnestyDao = amnestyDao;}
        @Override
        protected Void doInBackground(Amnesty... amnesties){
            amnestyDao.Update(amnesties[0]);
            return null;
        }
    }

    public static class DeleteAmnestyAsyncTask extends AsyncTask<Amnesty, Void, Void>{
        private AmnestyDao amnestyDao;
        private DeleteAmnestyAsyncTask(AmnestyDao amnestyDao){this.amnestyDao = amnestyDao;}
        @Override
        protected Void doInBackground(Amnesty... amnesties){
            amnestyDao.Delete(amnesties[0]);
            return null;
        }
    }
}
