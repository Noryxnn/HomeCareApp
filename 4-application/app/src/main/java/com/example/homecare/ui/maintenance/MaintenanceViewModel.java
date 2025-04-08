package com.example.homecare.ui.maintenance;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.homecare.data.AppDatabase;
import com.example.homecare.data.dao.MaintenanceTaskDao;
import com.example.homecare.data.entity.MaintenanceTask;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MaintenanceViewModel extends AndroidViewModel {
    private final MaintenanceTaskDao taskDao;
    private final ExecutorService executorService;

    public MaintenanceViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.maintenanceTaskDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<MaintenanceTask>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public LiveData<List<MaintenanceTask>> getPendingTasks() {
        return taskDao.getPendingTasks();
    }

    public void insert(MaintenanceTask task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(MaintenanceTask task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(MaintenanceTask task) {
        executorService.execute(() -> taskDao.delete(task));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 