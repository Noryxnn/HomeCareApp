package com.example.homecare.ui.bills;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.homecare.data.entity.Bill;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Date;

public class BillFragment extends Fragment {
    private static final String PREFS_NAME = "HomeCarePrefs";
    private static final String KEY_BILLS_INITIALIZED = "bills_initialized";
    
    private BillViewModel viewModel;
    private BillAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BillAdapter();
        recyclerView.setAdapter(adapter);

        // Set up ViewModel
        viewModel = new ViewModelProvider(this).get(BillViewModel.class);
        viewModel.getAllBills().observe(getViewLifecycleOwner(), bills -> adapter.submitList(bills));

        // Initialize default bills only once
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean billsInitialized = prefs.getBoolean(KEY_BILLS_INITIALIZED, false);
        
        if (!billsInitialized) {
            initializeDefaultBills();
            prefs.edit().putBoolean(KEY_BILLS_INITIALIZED, true).apply();
        }

        // Set up FAB
        FloatingActionButton fab = view.findViewById(R.id.fab_add_bill);
        fab.setOnClickListener(v -> showAddBillDialog());

        return view;
    }

    private void initializeDefaultBills() {
        Date today = new Date();
        viewModel.insert(new Bill("Water Bill", 50.00, today));
        viewModel.insert(new Bill("Electricity Bill", 120.00, today));
        viewModel.insert(new Bill("Internet Bill", 80.00, today));
    }

    private void showAddBillDialog() {
        AddBillDialog dialog = new AddBillDialog();
        dialog.show(getChildFragmentManager(), "AddBillDialog");
    }
} 