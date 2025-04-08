package com.example.homecare.ui.bills;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.homecare.data.AppDatabase;
import com.example.homecare.data.dao.BillDao;
import com.example.homecare.data.entity.Bill;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BillViewModel extends AndroidViewModel {
    private final BillDao billDao;
    private final ExecutorService executorService;

    public BillViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        billDao = db.billDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Bill>> getAllBills() {
        return billDao.getAllBills();
    }

    public LiveData<List<Bill>> getPendingBills() {
        return billDao.getPendingBills();
    }

    public void insert(Bill bill) {
        executorService.execute(() -> billDao.insert(bill));
    }

    public void update(Bill bill) {
        executorService.execute(() -> billDao.update(bill));
    }

    public void delete(Bill bill) {
        executorService.execute(() -> billDao.delete(bill));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 