package com.zfdang.mdoi.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.chrisbanes.photoview.PhotoView;
import com.zfdang.mdoi.R;
import com.zfdang.mdoi.databinding.FragmentEditBinding;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;

    private PhotoView photoView;
    private DrawingOverlayView drawingOverlay;
    private EditViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EditViewModel dashboardViewModel =
                new ViewModelProvider(this).get(EditViewModel.class);

        binding = FragmentEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        photoView = binding.photoView;
        drawingOverlay = binding.drawingOverlay;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EditViewModel.class);
        
        // Load sample image
        photoView.setImageResource(R.drawable.sample);
        
        // Observe line data changes
        viewModel.getLines().observe(getViewLifecycleOwner(), lines -> {
            drawingOverlay.setLines(lines);
        });
        
        // Update ViewModel when new lines are drawn
        drawingOverlay.setOnDrawListener(() -> {
            viewModel.setLines(drawingOverlay.getLines());
        });
    }
}