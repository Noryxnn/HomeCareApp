package com.example.homecare.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.homecare.data.entity.GroceryItem;
import java.util.List;

@Dao
public interface GroceryItemDao {
    @Insert
    void insert(GroceryItem item);

    @Update
    void update(GroceryItem item);

    @Delete
    void delete(GroceryItem item);

    @Query("SELECT * FROM grocery_items ORDER BY category ASC, name ASC")
    LiveData<List<GroceryItem>> getAllItems();

    @Query("SELECT * FROM grocery_items WHERE isBought = 0 ORDER BY category ASC, name ASC")
    LiveData<List<GroceryItem>> getPendingItems();

    @Query("SELECT * FROM grocery_items WHERE id = :itemId")
    LiveData<GroceryItem> getItemById(long itemId);

    @Query("SELECT DISTINCT category FROM grocery_items ORDER BY category ASC")
    LiveData<List<String>> getAllCategories();
} 