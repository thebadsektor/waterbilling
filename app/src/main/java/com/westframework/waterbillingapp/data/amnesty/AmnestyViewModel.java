package com.westframework.waterbillingapp.data.amnesty;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AmnestyViewModel extends AndroidViewModel {

    private AmnestyRepository repository;
    private final LiveData<List<Amnesty>> amnesties;

    public AmnestyViewModel(@NonNull Application application){
        super  (application);
        repository = new AmnestyRepository(application);
        amnesties = repository.getAmnesties();
    }

    public void insert(Amnesty amnesty)
    {
        repository.insert(amnesty);
    }
    public void update(Amnesty amnesty)
    {
        repository.update(amnesty);
    }

    public void delete(Amnesty amnesty){repository.delete(amnesty);}

    public LiveData<List<Amnesty>> getAmnesties(){
        return amnesties;
    }
}
