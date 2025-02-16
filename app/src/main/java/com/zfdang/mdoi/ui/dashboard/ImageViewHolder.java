package com.zfdang.mdoi.ui.dashboard;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zfdang.mdoi.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}
