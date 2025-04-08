package com.example.homecare.ui.grocery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homecare.R;
import com.example.homecare.data.entity.GroceryItem;

public class GroceryAdapter extends ListAdapter<GroceryItem, GroceryAdapter.ItemViewHolder> {

    protected GroceryAdapter() {
        super(new DiffUtil.ItemCallback<GroceryItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull GroceryItem oldItem, @NonNull GroceryItem newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull GroceryItem oldItem, @NonNull GroceryItem newItem) {
                return oldItem.getName().equals(newItem.getName()) &&
                       oldItem.getCategory().equals(newItem.getCategory()) &&
                       oldItem.isBought() == newItem.isBought();
            }
        });
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        GroceryItem item = getItem(position);
        holder.nameTextView.setText(item.getName());
        holder.categoryTextView.setText(item.getCategory());
        holder.boughtCheckBox.setChecked(item.isBought());
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTextView;
        final TextView categoryTextView;
        final CheckBox boughtCheckBox;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            categoryTextView = itemView.findViewById(R.id.text_category);
            boughtCheckBox = itemView.findViewById(R.id.checkbox_bought);
        }
    }
} 