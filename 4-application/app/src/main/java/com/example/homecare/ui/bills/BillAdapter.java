package com.example.homecare.ui.bills;

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
import com.example.homecare.data.entity.Bill;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BillAdapter extends ListAdapter<Bill, BillAdapter.BillViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    protected BillAdapter() {
        super(new DiffUtil.ItemCallback<Bill>() {
            @Override
            public boolean areItemsTheSame(@NonNull Bill oldItem, @NonNull Bill newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Bill oldItem, @NonNull Bill newItem) {
                return oldItem.getName().equals(newItem.getName()) &&
                       oldItem.getAmount() == newItem.getAmount() &&
                       oldItem.getDueDate().equals(newItem.getDueDate()) &&
                       oldItem.isPaid() == newItem.isPaid();
            }
        });
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = getItem(position);
        holder.nameTextView.setText(bill.getName());
        holder.amountTextView.setText(String.format("$%.2f", bill.getAmount()));
        holder.dueDateTextView.setText(dateFormat.format(bill.getDueDate()));
        holder.paidCheckBox.setChecked(bill.isPaid());
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTextView;
        final TextView amountTextView;
        final TextView dueDateTextView;
        final CheckBox paidCheckBox;

        BillViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            amountTextView = itemView.findViewById(R.id.text_amount);
            dueDateTextView = itemView.findViewById(R.id.text_due_date);
            paidCheckBox = itemView.findViewById(R.id.checkbox_paid);
        }
    }
} 