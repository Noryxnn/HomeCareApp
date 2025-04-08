package com.example.homecare.ui.bills;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.homecare.R;
import com.example.homecare.data.entity.Bill;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBillDialog extends DialogFragment {
    private BillViewModel viewModel;
    private EditText dueDateEditText;
    private Calendar selectedDate;
    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment()).get(BillViewModel.class);
        selectedDate = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_bill, null);

        EditText nameEditText = view.findViewById(R.id.edit_text_name);
        EditText amountEditText = view.findViewById(R.id.edit_text_amount);
        dueDateEditText = view.findViewById(R.id.edit_text_due_date);
        Button saveButton = view.findViewById(R.id.button_save);
        Button cancelButton = view.findViewById(R.id.button_cancel);

        // Set initial date
        updateDueDateDisplay();

        // Set up date picker
        dueDateEditText.setOnClickListener(v -> showDatePicker());

        builder.setView(view);

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String amountStr = amountEditText.getText().toString();

            if (name.isEmpty() || amountStr.isEmpty()) {
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Bill bill = new Bill(name, amount, selectedDate.getTime());
                viewModel.insert(bill);
                dismiss();
            } catch (NumberFormatException e) {
                amountEditText.setError("Please enter a valid amount");
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDueDateDisplay();
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDueDateDisplay() {
        dueDateEditText.setText(dateFormat.format(selectedDate.getTime()));
    }
} 