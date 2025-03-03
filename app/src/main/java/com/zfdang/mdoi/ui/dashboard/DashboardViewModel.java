package com.zfdang.mdoi.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<DrawingOverlayView.Line>> mLines = new MutableLiveData<>();

    public DashboardViewModel() {
        mLines.setValue(new ArrayList<>());
    }

    public LiveData<List<DrawingOverlayView.Line>> getLines() {
        return mLines;
    }

    public void setLines(List<DrawingOverlayView.Line> lines) {
        mLines.setValue(new ArrayList<>(lines));
    }
}