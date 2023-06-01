package com.westframework.waterbillingapp.data.discount;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DiscountViewModel extends AndroidViewModel {

    private DiscountRepository repository;
    private final LiveData<List<Discount>> discounts;

    public DiscountViewModel(@NonNull android.app.Application application){
        super  (application);
        repository = new DiscountRepository(application);
        discounts = repository.getAllDiscounts();
    }

    public void insert(Discount discount)
    {
        repository.insert(discount);
    }
    public void update(Discount discount)
    {
        repository.update(discount);
    }

    public void delete(Discount discount){repository.delete(discount);}

    public LiveData<List<Discount>> getDiscounts(){
        return discounts;
    }
}
