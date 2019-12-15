package com.example.parkwayz.ui.charging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChargingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChargingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is charging session fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}