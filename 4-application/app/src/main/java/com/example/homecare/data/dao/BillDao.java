package com.example.homecare.data.dao;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.homecare.data.entity.Bill;
import java.util.List;

@Dao
public interface BillDao {
    public static final String TAG = "BillDao";

    @Insert
    default void insert(Bill bill) {
        Log.d(TAG, "Inserting bill: " + bill.getName());
        insertBill(bill);
    }

    @Insert
    void insertBill(Bill bill);

    @Update
    default void update(Bill bill) {
        Log.d(TAG, "Updating bill: " + bill.getName());
        updateBill(bill);
    }

    @Update
    void updateBill(Bill bill);

    @Delete
    default void delete(Bill bill) {
        Log.d(TAG, "Deleting bill: " + bill.getName());
        deleteBill(bill);
    }

    @Delete
    void deleteBill(Bill bill);

    @Query("SELECT * FROM bills ORDER BY dueDate ASC")
    LiveData<List<Bill>> getAllBills();

    @Query("SELECT * FROM bills ORDER BY dueDate ASC")
    default List<Bill> getAllBillsSync() {
        Log.d(TAG, "Getting all bills synchronously");
        List<Bill> bills = getAllBillsSyncImpl();
        Log.d(TAG, "Found " + (bills != null ? bills.size() : 0) + " bills");
        return bills;
    }

    @Query("SELECT * FROM bills ORDER BY dueDate ASC")
    List<Bill> getAllBillsSyncImpl();

    @Query("SELECT * FROM bills WHERE isPaid = 0 ORDER BY dueDate ASC")
    LiveData<List<Bill>> getPendingBills();

    @Query("SELECT * FROM bills WHERE id = :billId")
    LiveData<Bill> getBillById(long billId);
} 