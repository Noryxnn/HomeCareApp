package com.example.homecare.ui.maintenance;

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
import com.example.homecare.data.entity.MaintenanceTask;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MaintenanceAdapter extends ListAdapter<MaintenanceTask, MaintenanceAdapter.TaskViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    protected MaintenanceAdapter() {
        super(new DiffUtil.ItemCallback<MaintenanceTask>() {
            @Override
            public boolean areItemsTheSame(@NonNull MaintenanceTask oldItem, @NonNull MaintenanceTask newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull MaintenanceTask oldItem, @NonNull MaintenanceTask newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                       oldItem.getDescription().equals(newItem.getDescription()) &&
                       oldItem.getDueDate().equals(newItem.getDueDate()) &&
                       oldItem.isCompleted() == newItem.isCompleted();
            }
        });
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_maintenance_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        MaintenanceTask task = getItem(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.dueDateTextView.setText(dateFormat.format(task.getDueDate()));
        holder.completedCheckBox.setChecked(task.isCompleted());
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView descriptionTextView;
        final TextView dueDateTextView;
        final CheckBox completedCheckBox;

        TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            dueDateTextView = itemView.findViewById(R.id.text_due_date);
            completedCheckBox = itemView.findViewById(R.id.checkbox_completed);
        }
    }
} 