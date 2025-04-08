package com.example.homecare.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grocery_items")
public class GroceryItem {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String category;
    private boolean isBought;

    public GroceryItem(String name, String category) {
        this.name = name;
        this.category = category;
        this.isBought = false;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isBought() { return isBought; }
    public void setBought(boolean bought) { isBought = bought; }
} 