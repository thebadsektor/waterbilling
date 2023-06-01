package com.westframework.waterbillingapp.data.classification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ClassificationViewModel extends AndroidViewModel {

    private ClassificationRepository repository;
    private final LiveData<List<Classification>> classifications;

    public ClassificationViewModel(@NonNull Application application){
        super  (application);
        repository = new ClassificationRepository(application);
        classifications = repository.getClassifications();
    }

    public void insert(Classification classification)
    {
        repository.insert(classification);
    }
    public void update(Classification classification)
    {
        repository.update(classification);
    }

    public void delete(Classification classification){repository.delete(classification);}

    public LiveData<List<Classification>> getClassifications(){
        return classifications;
    }
}
