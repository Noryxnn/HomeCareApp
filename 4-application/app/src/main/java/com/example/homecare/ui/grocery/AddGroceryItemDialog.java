package com.example.homecare.ui.grocery;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.homecare.R;
import com.example.homecare.data.entity.GroceryItem;
import java.util.ArrayList;
import java.util.List;

public class AddGroceryItemDialog extends DialogFragment {
    private EditText nameEditText;
    private Spinner categorySpinner;
    private List<String> categories;
    private ArrayAdapter<String> categoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = new ArrayList<>();
        categories.add("Fruits & Vegetables");
        categories.add("Dairy");
        categories.add("Meat");
        categories.add("Pantry");
        categories.add("Other");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_grocery_item, null);

        nameEditText = view.findViewById(R.id.edit_text_name);
        categorySpinner = view.findViewById(R.id.spinner_category);

        categoryAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        builder.setView(view)
                .setTitle(R.string.add_item)
                .setPositiveButton(R.string.save, (dialog, id) -> {
                    String name = nameEditText.getText().toString();
                    String category = categorySpinner.getSelectedItem().toString();
                    if (!name.isEmpty()) {
                        GroceryItem item = new GroceryItem(name, category);
                        GroceryViewModel viewModel = new ViewModelProvider(requireActivity())
                                .get(GroceryViewModel.class);
                        viewModel.insert(item);
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dismiss());

        return builder.create();
    }
} 