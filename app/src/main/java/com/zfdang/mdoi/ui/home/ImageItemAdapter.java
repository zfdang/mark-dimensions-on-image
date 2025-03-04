package com.zfdang.mdoi.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zfdang.mdoi.model.ImageItem;
import com.zfdang.mdoi.R;

import java.util.ArrayList;
import java.util.List;

public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {
    private List<ImageItem> items;

    public ImageItemAdapter(List<ImageItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageItem item = items.get(position);
        holder.description.setText(item.getDescription());
        holder.date.setText(item.getDate());
        holder.imageView.setImageResource(item.getImageId());
        // TODO: Set image using Glide/Picasso later
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ImageItem> newItems) {
        items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView description;
        public final TextView date;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);
        }
    }
}