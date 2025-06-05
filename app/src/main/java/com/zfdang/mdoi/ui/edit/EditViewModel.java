package com.zfdang.mdoi.ui.edit;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfdang.mdoi.model.Measure;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EditViewModel extends ViewModel {

    private final MutableLiveData<List<Measure>> mLines = new MutableLiveData<>();
    private static final String PREFS_NAME = "MdoiPrefs";
    private static final String LINES_KEY = "saved_measures";
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences; // To be initialized

    public EditViewModel() {
        // mLines will be initialized by loadLines or with defaults if no saved data.
    }

    // Call this method from the Fragment/Activity, as ViewModel shouldn't directly access Context for SharedPreferences init.
    // However, for simplicity in this context, we might initialize it if a context is passed.
    // A better approach is using Application context via AndroidViewModel or passing SharedPreferences instance.
    private void initSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }

    public LiveData<List<Measure>> getLines() {
        return mLines;
    }

    public void setLines(List<Measure> lines, Context context) {
        initSharedPreferences(context); // Ensure SharedPreferences is initialized
        mLines.setValue(new ArrayList<>(lines));
        saveLinesToPrefs(new ArrayList<>(lines));
    }

    private void saveLinesToPrefs(List<Measure> lines) {
        if (sharedPreferences == null) return; // Or handle error: SharedPreferences not initialized
        String jsonLines = gson.toJson(lines);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LINES_KEY, jsonLines);
        editor.apply();
    }

    public void loadLines(Context context) {
        initSharedPreferences(context); // Ensure SharedPreferences is initialized
        String jsonLines = sharedPreferences.getString(LINES_KEY, null);
        if (jsonLines != null) {
            Type type = new TypeToken<ArrayList<Measure>>() {}.getType();
            List<Measure> loadedLines = gson.fromJson(jsonLines, type);
            if (loadedLines != null) {
                mLines.setValue(loadedLines);
                return;
            }
        }
        // If no saved data or error in loading, set to default/initial state
        mLines.setValue(createDefaultLines());
    }

    private List<Measure> createDefaultLines() {
        // This is the previous initial setup.
        // Consider if you want to keep these defaults or start fresh.
        List<Measure> initialLines = new ArrayList<>();
//        initialLines.add(new Measure(100, 500, 900, 500,0)); // Horizontal line
//        initialLines.add(new Measure(500, 100, 500, 900,0)); // Vertical line
//        initialLines.add(new Measure(200, 200, 800, 800,0)); // Diagonal line
        return initialLines; // Start with an empty list if no saved data
    }
}