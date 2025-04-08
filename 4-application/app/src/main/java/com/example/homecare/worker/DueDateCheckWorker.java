package com.example.homecare.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.homecare.data.AppDatabase;
import com.example.homecare.data.dao.BillDao;
import com.example.homecare.data.dao.MaintenanceTaskDao;
import com.example.homecare.data.entity.Bill;
import com.example.homecare.data.entity.MaintenanceTask;
import com.example.homecare.util.NotificationHelper;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DueDateCheckWorker extends Worker {
    private static final String TAG = "DueDateCheckWorker";
    private final MaintenanceTaskDao taskDao;
    private final BillDao billDao;
    private final ExecutorService executorService;

    public DueDateCheckWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        AppDatabase db = AppDatabase.getDatabase(context);
        taskDao = db.maintenanceTaskDao();
        billDao = db.billDao();
        executorService = Executors.newSingleThreadExecutor();
        Log.d(TAG, "DueDateCheckWorker initialized");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Starting due date check at " + new Date());
        CountDownLatch latch = new CountDownLatch(2); // One for tasks, one for bills
        boolean[] success = {true};

        try {
            Log.d(TAG, "Checking maintenance tasks...");
            executorService.execute(() -> {
                try {
                    checkMaintenanceTasks();
                } catch (Exception e) {
                    Log.e(TAG, "Error checking maintenance tasks", e);
                    success[0] = false;
                } finally {
                    latch.countDown();
                }
            });

            Log.d(TAG, "Checking bills...");
            executorService.execute(() -> {
                try {
                    checkBills();
                } catch (Exception e) {
                    Log.e(TAG, "Error checking bills", e);
                    success[0] = false;
                } finally {
                    latch.countDown();
                }
            });

            // Wait for both checks to complete with a timeout
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                Log.e(TAG, "Due date check timed out after 30 seconds");
                return Result.failure();
            }

            Log.d(TAG, "Due date check completed at " + new Date());
            return success[0] ? Result.success() : Result.failure();
        } catch (Exception e) {
            Log.e(TAG, "Error during due date check", e);
            return Result.failure();
        } finally {
            executorService.shutdown();
        }
    }

    private void checkMaintenanceTasks() {
        Log.d(TAG, "Starting maintenance tasks check");
        List<MaintenanceTask> taskList = taskDao.getAllTasksSync();
        if (taskList == null) {
            Log.e(TAG, "Maintenance tasks list is null");
            return;
        }

        Log.d(TAG, "Found " + taskList.size() + " maintenance tasks to check");
        Date now = new Date();
        int dueTasks = 0;
        for (MaintenanceTask task : taskList) {
            Log.d(TAG, "Checking task: " + task.getTitle() + ", Due date: " + task.getDueDate() + ", Completed: " + task.isCompleted());
            if (!task.isCompleted() && isDueDate(task.getDueDate(), now)) {
                dueTasks++;
                Log.d(TAG, "Sending notification for maintenance task: " + task.getTitle());
                NotificationHelper.showTaskNotification(
                    getApplicationContext(),
                    "Maintenance Task Due",
                    task.getTitle() + " is due today!"
                );
            }
        }
        Log.d(TAG, "Found " + dueTasks + " due maintenance tasks");
    }

    private void checkBills() {
        Log.d(TAG, "Starting bills check");
        List<Bill> billList = billDao.getAllBillsSync();
        if (billList == null) {
            Log.e(TAG, "Bills list is null");
            return;
        }

        Log.d(TAG, "Found " + billList.size() + " bills to check");
        Date now = new Date();
        int dueBills = 0;
        for (Bill bill : billList) {
            Log.d(TAG, "Checking bill: " + bill.getName() + ", Due date: " + bill.getDueDate() + ", Paid: " + bill.isPaid());
            if (!bill.isPaid() && isDueDate(bill.getDueDate(), now)) {
                dueBills++;
                Log.d(TAG, "Sending notification for bill: " + bill.getName());
                NotificationHelper.showTaskNotification(
                    getApplicationContext(),
                    "Bill Due",
                    bill.getName() + " - $" + bill.getAmount() + " is due today!"
                );
            }
        }
        Log.d(TAG, "Found " + dueBills + " due bills");
    }

    private boolean isDueDate(Date dueDate, Date now) {
        if (dueDate == null) {
            Log.e(TAG, "Due date is null");
            return false;
        }

        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTime(dueDate);
        
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(now);
        
        // Check if the due date is today or overdue
        boolean isDue = dueCalendar.get(Calendar.YEAR) <= nowCalendar.get(Calendar.YEAR) &&
                       (dueCalendar.get(Calendar.YEAR) < nowCalendar.get(Calendar.YEAR) ||
                        dueCalendar.get(Calendar.MONTH) < nowCalendar.get(Calendar.MONTH) ||
                        (dueCalendar.get(Calendar.MONTH) == nowCalendar.get(Calendar.MONTH) &&
                         dueCalendar.get(Calendar.DAY_OF_MONTH) <= nowCalendar.get(Calendar.DAY_OF_MONTH)));
        
        Log.d(TAG, "Checking due date: " + dueDate + " against now: " + now + ", isDue: " + isDue);
        return isDue;
    }
} 