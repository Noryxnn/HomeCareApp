package com.example.homecare.ui.maintenance;

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

public class MaintenanceFragment extends Fragment {
    private MaintenanceViewModel viewModel;
    private MaintenanceAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MaintenanceAdapter();
        recyclerView.setAdapter(adapter);

        // Set up ViewModel
        viewModel = new ViewModelProvider(this).get(MaintenanceViewModel.class);
        viewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> adapter.submitList(tasks));

        // Set up FAB
        FloatingActionButton fab = view.findViewById(R.id.fab_add_task);
        fab.setOnClickListener(v -> showAddTaskDialog());

        return view;
    }

    private void showAddTaskDialog() {
        AddTaskDialog dialog = new AddTaskDialog();
        dialog.show(getChildFragmentManager(), "AddTaskDialog");
    }
} 