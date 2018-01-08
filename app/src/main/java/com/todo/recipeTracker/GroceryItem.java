package com.todo.recipeTracker;

/**
 * Created by Grant on 1/8/18.
 */

public class GroceryItem {
    private String name;
    private int count;

    public GroceryItem(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount(int increment) {
        count+=increment;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return Integer.toString(count).concat(" ").concat(name);
    }

    @Override
    public boolean equals(Object other) {
        return name.equals(((GroceryItem) other).getName());
    }
}
