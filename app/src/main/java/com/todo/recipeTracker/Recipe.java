package com.todo.recipeTracker;

/**
 * Created by Grant on 12/26/17.
 */

enum Type {
    Low,
    Medium,
    High
};

public class Recipe {
    private Type priority;
    private String title;
    private String[] subTitle;

    public Recipe(String title) {
        this.title = title;
    }

    public Recipe(Type priority, String title, String[] subTitle) {
        this.priority=priority;
        this.title = title;
        this.subTitle = subTitle;
    }

    public Recipe(String title, String[] subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public Type getPriority() {
        return priority;
    }

    public String[] getSubTitle() {
        return subTitle;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        return getTitle().equals(((Recipe) other).getTitle());
//                && getSubTitle().equals(((Recipe) other).getSubTitle())
//                && getPriority().equals(((Recipe) other).getPriority());
    }
}
