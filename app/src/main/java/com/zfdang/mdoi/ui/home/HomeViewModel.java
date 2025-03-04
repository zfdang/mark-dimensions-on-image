package com.zfdang.mdoi.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zfdang.mdoi.R;
import com.zfdang.mdoi.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<ImageItem>> mImages = new MutableLiveData<>();

    public HomeViewModel() {
        mImages.setValue(new ArrayList<>());
        mImages.getValue().add(new ImageItem(R.drawable.ic_home_black_24dp, "Living Room", "2024-03-15"));
        mImages.getValue().add(new ImageItem(R.drawable.ic_dashboard_black_24dp, "Bedroom", "2024-03-16"));
        mImages.getValue().add(new ImageItem(R.drawable.ic_notifications_24dp, "Kitchen", "2024-03-17"));
    }

    public LiveData<List<ImageItem>> getImages() {
        return mImages;
    }
}