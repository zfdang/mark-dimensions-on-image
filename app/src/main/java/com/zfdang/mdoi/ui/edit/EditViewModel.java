package com.zfdang.mdoi.ui.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class EditViewModel extends ViewModel {

    private final MutableLiveData<List<DrawingOverlayView.Line>> mLines = new MutableLiveData<>();

    public EditViewModel() {
        mLines.setValue(new ArrayList<>());
    }

    public LiveData<List<DrawingOverlayView.Line>> getLines() {
        return mLines;
    }

    public void setLines(List<DrawingOverlayView.Line> lines) {
        mLines.setValue(new ArrayList<>(lines));
    }
}