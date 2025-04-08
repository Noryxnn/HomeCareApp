package com.example.homecare.data.dao;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.homecare.data.entity.MaintenanceTask;
import java.util.List;

@Dao
public interface MaintenanceTaskDao {
    public static final String TAG = "MaintenanceTaskDao";

    @Insert
    default void insert(MaintenanceTask task) {
        Log.d(TAG, "Inserting task: " + task.getTitle());
        insertTask(task);
    }

    @Insert
    void insertTask(MaintenanceTask task);

    @Update
    default void update(MaintenanceTask task) {
        Log.d(TAG, "Updating task: " + task.getTitle());
        updateTask(task);
    }

    @Update
    void updateTask(MaintenanceTask task);

    @Delete
    default void delete(MaintenanceTask task) {
        Log.d(TAG, "Deleting task: " + task.getTitle());
        deleteTask(task);
    }

    @Delete
    void deleteTask(MaintenanceTask task);

    @Query("SELECT * FROM maintenance_tasks ORDER BY dueDate ASC")
    LiveData<List<MaintenanceTask>> getAllTasks();

    @Query("SELECT * FROM maintenance_tasks ORDER BY dueDate ASC")
    default List<MaintenanceTask> getAllTasksSync() {
        Log.d(TAG, "Getting all tasks synchronously");
        List<MaintenanceTask> tasks = getAllTasksSyncImpl();
        Log.d(TAG, "Found " + (tasks != null ? tasks.size() : 0) + " tasks");
        return tasks;
    }

    @Query("SELECT * FROM maintenance_tasks ORDER BY dueDate ASC")
    List<MaintenanceTask> getAllTasksSyncImpl();

    @Query("SELECT * FROM maintenance_tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    LiveData<List<MaintenanceTask>> getPendingTasks();

    @Query("SELECT * FROM maintenance_tasks WHERE id = :taskId")
    LiveData<MaintenanceTask> getTaskById(long taskId);
} 