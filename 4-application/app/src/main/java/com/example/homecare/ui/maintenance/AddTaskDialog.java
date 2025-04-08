package com.example.homecare.ui.maintenance;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.homecare.R;
import com.example.homecare.data.entity.MaintenanceTask;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.Date;

public class AddTaskDialog extends DialogFragment {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private View dueDateButton;
    private Date selectedDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_task, null);

        titleEditText = view.findViewById(R.id.edit_text_title);
        descriptionEditText = view.findViewById(R.id.edit_text_description);
        dueDateButton = view.findViewById(R.id.button_due_date);

        dueDateButton.setOnClickListener(v -> showDatePicker());

        builder.setView(view)
                .setTitle(R.string.add_task)
                .setPositiveButton(R.string.save, (dialog, id) -> {
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    if (selectedDate != null && !title.isEmpty()) {
                        MaintenanceTask task = new MaintenanceTask(title, description, selectedDate, "General");
                        MaintenanceViewModel viewModel = new ViewModelProvider(requireActivity())
                                .get(MaintenanceViewModel.class);
                        viewModel.insert(task);
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dismiss());

        return builder.create();
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.due_date)
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = new Date(selection);
            dueDateButton.setTag(selection);
        });

        datePicker.show(getChildFragmentManager(), "DATE_PICKER");
    }
} 