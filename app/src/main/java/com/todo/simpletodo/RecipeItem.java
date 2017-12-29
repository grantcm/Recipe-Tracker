package com.todo.simpletodo;

/**
 * Created by Grant on 12/29/17.
 */

public class RecipeItem {
    private String step;
    private int checked = 0;
    public RecipeItem(String step, int checked) {
        this.step=step;
        this.checked=checked;
    }

    public int getChecked() {
        return checked;
    }

    public String getStep() {
        return step;
    }
}
