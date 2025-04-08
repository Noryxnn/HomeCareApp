package com.example.homecare.ui.grocery;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.homecare.data.AppDatabase;
import com.example.homecare.data.dao.GroceryItemDao;
import com.example.homecare.data.entity.GroceryItem;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroceryViewModel extends AndroidViewModel {
    private final GroceryItemDao itemDao;
    private final ExecutorService executorService;

    public GroceryViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        itemDao = db.groceryItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<GroceryItem>> getAllItems() {
        return itemDao.getAllItems();
    }

    public LiveData<List<GroceryItem>> getPendingItems() {
        return itemDao.getPendingItems();
    }

    public LiveData<List<String>> getAllCategories() {
        return itemDao.getAllCategories();
    }

    public void insert(GroceryItem item) {
        executorService.execute(() -> itemDao.insert(item));
    }

    public void update(GroceryItem item) {
        executorService.execute(() -> itemDao.update(item));
    }

    public void delete(GroceryItem item) {
        executorService.execute(() -> itemDao.delete(item));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 