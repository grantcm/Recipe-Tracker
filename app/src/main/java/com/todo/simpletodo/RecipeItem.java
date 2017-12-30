package com.todo.simpletodo;

/**
 * Created by Grant on 12/29/17.
 */

public class RecipeItem {
    private String step;
    private boolean checked;
    private int time = 0;

    public RecipeItem(String step) {
        this(step, false);
    }

    public RecipeItem(String step, boolean checked) {
        this.step=step;
        this.checked=checked;
    }

    public RecipeItem(String step, boolean checked, int time) {
        this(step, checked);
        this.time = time;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {this.checked = checked; }

    public String getStep() {
        return step;
    }

    public boolean getRequiresClock() {
        return time != 0;
    }

    public int getTime() {
        return time;
    }
}
