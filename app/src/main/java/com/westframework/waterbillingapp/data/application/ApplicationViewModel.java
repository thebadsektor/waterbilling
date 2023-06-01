package com.westframework.waterbillingapp.data.application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {

    private ApplicationRepository repository;
    private final LiveData<List<Application>> applications;

    public ApplicationViewModel(@NonNull android.app.Application application){
        super  (application);
        repository = new ApplicationRepository(application);
        applications = repository.getAllApplications();
        }

    public void insert(Application application)
    {
        repository.insert(application);
    }
    public void update(Application application)
    {
        repository.update(application);
    }

    public void delete(Application application){repository.delete(application);}

    public LiveData<List<Application>> getApplications(){
        return applications;
    }
}
