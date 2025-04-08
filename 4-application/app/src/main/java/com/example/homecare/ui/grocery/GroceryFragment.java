package com.example.homecare.ui.grocery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homecare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroceryFragment extends Fragment {
    private GroceryViewModel viewModel;
    private GroceryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroceryAdapter();
        recyclerView.setAdapter(adapter);

        // Set up ViewModel
        viewModel = new ViewModelProvider(this).get(GroceryViewModel.class);
        viewModel.getAllItems().observe(getViewLifecycleOwner(), items -> adapter.submitList(items));

        // Set up FAB
        FloatingActionButton fab = view.findViewById(R.id.fab_add_item);
        fab.setOnClickListener(v -> showAddItemDialog());

        return view;
    }

    private void showAddItemDialog() {
        AddGroceryItemDialog dialog = new AddGroceryItemDialog();
        dialog.show(getChildFragmentManager(), "AddGroceryItemDialog");
    }
} 