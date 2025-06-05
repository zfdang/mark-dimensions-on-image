package com.zfdang.mdoi.ui.edit;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.chrisbanes.photoview.PhotoView;
import com.zfdang.mdoi.R;
import com.zfdang.mdoi.databinding.FragmentEditBinding;
import com.zfdang.mdoi.model.Measure;

public class EditFragment extends Fragment implements DrawingOverlayView.OnRulerInteractionListener {

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

        // Load lines from SharedPreferences when the fragment is created/view is created
        viewModel.loadLines(requireContext());

        Bundle arguments = getArguments();
        if (arguments != null) {
            String imageUriString = arguments.getString("imageUri");
            if (imageUriString != null) {
                try {
                    Uri imageUri = Uri.parse(imageUriString);
                    photoView.setImageURI(imageUri);
                } catch (Exception e) {
                    Log.e("EditFragment", "Error loading image: " + imageUriString, e);
                    // Optionally, show a placeholder or error image
                    photoView.setImageResource(R.drawable.sample); // Fallback to sample
                }
            } else {
                Log.e("EditFragment", "Image URI string is null");
                photoView.setImageResource(R.drawable.sample); // Fallback to sample
            }
        } else {
            Log.e("EditFragment", "Arguments bundle is null");
            // Load sample image if no URI is passed
            photoView.setImageResource(R.drawable.sample);
        }
        
        // Observe line data changes
        viewModel.getLines().observe(getViewLifecycleOwner(), lines -> {
            drawingOverlay.setLines(lines);
        });
        
        // Update ViewModel when new lines are drawn
        drawingOverlay.setOnDrawListener(() -> {
            // Pass context for SharedPreferences access in ViewModel
            if (getContext() != null) {
                viewModel.setLines(drawingOverlay.getLines(), getContext());
            }
        });

        drawingOverlay.setOnRulerInteractionListener(this);
    }

    @Override
    public void onRulerClicked(Measure ruler) {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Calibrate Ruler");

        // Set up the input fields
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        final EditText actualLengthInput = new EditText(getContext());
        actualLengthInput.setHint("Actual Length (e.g., 10.5)");
        actualLengthInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (ruler.actualLength > 0) {
            actualLengthInput.setText(String.valueOf(ruler.actualLength));
        }
        layout.addView(actualLengthInput);

        final EditText unitInput = new EditText(getContext());
        unitInput.setHint("Unit (e.g., cm, m, in, ft)");
        if (ruler.unit != null && !ruler.unit.isEmpty()) {
            unitInput.setText(ruler.unit);
        }
        layout.addView(unitInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String actualLengthStr = actualLengthInput.getText().toString();
            String unitStr = unitInput.getText().toString();

            if (actualLengthStr.isEmpty() || unitStr.isEmpty()) {
                Toast.makeText(getContext(), "Both length and unit are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float actualLength = Float.parseFloat(actualLengthStr);
                if (actualLength <= 0) {
                    Toast.makeText(getContext(), "Actual length must be positive.", Toast.LENGTH_SHORT).show();
                    return;
                }
                ruler.setCalibration(actualLength, unitStr);
                // Update ViewModel which will also save to SharedPreferences and update LiveData
                if (getContext() != null) {
                    viewModel.setLines(drawingOverlay.getLines(), getContext());
                } else { // Fallback if context is somehow null, though unlikely here
                    drawingOverlay.invalidate();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid number format for length.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onRulerLongPressed(Measure ruler) {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set Ruler Label");

        // Set up the input field
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        final EditText labelInput = new EditText(getContext());
        labelInput.setHint("Enter label (e.g., Wall A)");
        if (ruler.label != null && !ruler.label.isEmpty()) {
            labelInput.setText(ruler.label);
        }
        layout.addView(labelInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String labelStr = labelInput.getText().toString();
            // Allow empty string to clear the label
            ruler.setLabel(labelStr);
            // Update ViewModel which will also save to SharedPreferences and update LiveData
            if (getContext() != null) {
                viewModel.setLines(drawingOverlay.getLines(), getContext());
            } else { // Fallback if context is somehow null
                drawingOverlay.invalidate();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}