package com.todo.simpletodo;

/**
 * Created by Grant on 12/26/17.
 */

enum Type {
    Low,
    Medium,
    High
};

public class Todo {
    private Type priority;
    private String title;
    private String[] subTitle;

    public Todo(Type priority, String title, String[] subTitle) {
        this.priority=priority;
        this.title = title;
        this.subTitle = subTitle;
    }

    public Todo(String title, String[] subTitle) {
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
        return getTitle().equals(((Todo) other).getTitle())
                && getSubTitle().equals(((Todo) other).getSubTitle())
                && getPriority().equals(((Todo) other).getPriority());
    }
}
