package com.example.homecare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.example.homecare.util.NotificationHelper;
import com.example.homecare.worker.DueDateCheckWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }

        // Initialize notification channel
        NotificationHelper.createNotificationChannel(this);

        // Schedule periodic work for checking due dates
        scheduleDueDateCheck();

        // Set up Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Set up Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_maintenance) {
                navController.navigate(R.id.maintenanceFragment);
                return true;
            } else if (itemId == R.id.navigation_grocery) {
                navController.navigate(R.id.groceryFragment);
                return true;
            } else if (itemId == R.id.navigation_bills) {
                navController.navigate(R.id.billsFragment);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted");
            } else {
                Log.e(TAG, "Notification permission denied");
            }
        }
    }

    private void scheduleDueDateCheck() {
        Log.d(TAG, "Scheduling due date check worker");
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        // Create a repeating work request with 1-minute interval
        OneTimeWorkRequest dueDateCheckRequest =
                new OneTimeWorkRequest.Builder(DueDateCheckWorker.class)
                        .setConstraints(constraints)
                        .build();

        // Schedule the work to repeat every minute
        WorkManager.getInstance(this)
                .beginUniqueWork(
                        "DueDateCheck",
                        ExistingWorkPolicy.REPLACE,
                        dueDateCheckRequest
                )
                .enqueue();

        // Schedule the next check
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            scheduleDueDateCheck();
        }, 60000); // 1 minute

        Log.d(TAG, "Due date check worker scheduled with 1-minute interval");
    }
} 