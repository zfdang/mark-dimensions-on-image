package com.zfdang.mdoi.ui.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zfdang.mdoi.model.Measure;

import java.util.ArrayList;
import java.util.List;

public class EditViewModel extends ViewModel {

    private final MutableLiveData<List<Measure>> mLines = new MutableLiveData<>();

    public EditViewModel() {
        mLines.setValue(new ArrayList<>());
        List<Measure> initialLines = new ArrayList<>();
        initialLines.add(new Measure(100, 500, 900, 500,0)); // Horizontal line
        initialLines.add(new Measure(500, 100, 500, 900,0)); // Vertical line
        initialLines.add(new Measure(200, 200, 800, 800,0)); // Diagonal line
        mLines.setValue(initialLines);
    }

    public LiveData<List<Measure>> getLines() {
        return mLines;
    }

    public void setLines(List<Measure> lines) {
        mLines.setValue(new ArrayList<>(lines));
    }
}