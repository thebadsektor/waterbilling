package com.westframework.waterbillingapp.data.classification;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.westframework.waterbillingapp.data.WaterBilliingAppDatabase;

import java.util.List;

public class ClassificationRepository {

    private ClassificationDao classificationDao;
    private LiveData<List<Classification>> classifications;

    public ClassificationRepository(Application application){
        WaterBilliingAppDatabase database = WaterBilliingAppDatabase.getInstance(application);
        classificationDao = database.classificationDao();
        classifications = classificationDao.getAllClassifications();
    }

    public void insert(Classification classification){
        new InsertClassificationAsyncTask(classificationDao).execute(classification);
    }

    public void update(Classification classification){
        new UpdateClassificationAsyncTask(classificationDao).execute(classification);
    }

    public void delete(Classification classification){
        new DeleteClassificationAsyncTask(classificationDao).execute(classification);
    }

    public LiveData<List<Classification>> getClassifications(){return classifications;}

    private static class InsertClassificationAsyncTask extends AsyncTask<Classification, Void, Void>{
        private ClassificationDao classificationDao;
        private InsertClassificationAsyncTask(ClassificationDao classificationDao){this.classificationDao = classificationDao;}
        @Override
        protected Void doInBackground(Classification... classifications){
            classificationDao.Insert(classifications[0]);
            return null;
        }
    }

    private static class UpdateClassificationAsyncTask extends AsyncTask<Classification, Void, Void>{
        private ClassificationDao classificationDao;
        private UpdateClassificationAsyncTask(ClassificationDao classificationDao){this.classificationDao = classificationDao;}
        @Override
        protected Void doInBackground(Classification... classifications){
            classificationDao.Update(classifications[0]);
            return null;
        }
    }

    public static class DeleteClassificationAsyncTask extends AsyncTask<Classification, Void, Void>{
        private ClassificationDao classificationDao;
        private DeleteClassificationAsyncTask(ClassificationDao classificationDao){this.classificationDao = classificationDao;}
        @Override
        protected Void doInBackground(Classification... classifications){
            classificationDao.Delete(classifications[0]);
            return null;
        }
    }


}
