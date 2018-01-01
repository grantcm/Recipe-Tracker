package com.todo.recipeTracker;

import android.support.annotation.NonNull;

/**
 * Created by Grant on 12/29/17.
 */

public class RecipeItem {
    private String step;
    private boolean checked, editClicked = false;
    private long time = 0;

    public RecipeItem(String step) {
        this(step, false);
    }

    public RecipeItem(String step, boolean checked) {
        this.step=step;
        this.checked=checked;
    }

    public RecipeItem(String step, boolean checked, long time) {
        this(step, checked);
        this.time = time;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(@NonNull boolean checked) {this.checked = checked; }

    public String getStep() {
        return step;
    }

    public void setStep(@NonNull String step) { this.step = step; }

    public boolean getRequiresClock() {
        return time != 0;
    }

    public void setTime(@NonNull long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setEditClicked (@NonNull boolean clicked) { this.editClicked = clicked; }

    public boolean getEditClicked () { return editClicked; }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(step);
        output.append(" ");
        output.append(checked);
        output.append(" ");
        output.append(time);
        return output.toString();
    }
}
