package com.example.homecare.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.homecare.data.dao.GroceryItemDao;
import com.example.homecare.data.dao.MaintenanceTaskDao;
import com.example.homecare.data.dao.BillDao;
import com.example.homecare.data.entity.GroceryItem;
import com.example.homecare.data.entity.MaintenanceTask;
import com.example.homecare.data.entity.Bill;
import com.example.homecare.util.DateConverter;

@Database(entities = {MaintenanceTask.class, GroceryItem.class, Bill.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract MaintenanceTaskDao maintenanceTaskDao();
    public abstract GroceryItemDao groceryItemDao();
    public abstract BillDao billDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "homecare_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 