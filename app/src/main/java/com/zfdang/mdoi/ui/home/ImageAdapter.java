package com.zfdang.mdoi.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zfdang.mdoi.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private final int[] imageResourceIds;

    public ImageAdapter(int[] imageResourceIds) {
        this.imageResourceIds = imageResourceIds;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imageResourceIds.length;
    }

}