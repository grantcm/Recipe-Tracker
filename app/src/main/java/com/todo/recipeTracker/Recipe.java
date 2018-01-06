package com.todo.recipeTracker;

import java.util.ArrayList;

/**
 * Created by Grant on 12/26/17.
 */

public class Recipe {
    private String title;
    private ArrayList<String> ingredients;

    public Recipe(String title) {
        this.title = title;
        this.ingredients = new ArrayList<>();
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getIngredientsToString () {
        StringBuilder output = new StringBuilder();
        for(String s: ingredients) {
            output.append(s);
            output.append("%");
        }
        return output.toString();
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        return getTitle().equals(((Recipe) other).getTitle());
    }
}
