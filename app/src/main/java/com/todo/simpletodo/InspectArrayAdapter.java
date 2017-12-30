package com.todo.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import static com.todo.simpletodo.InspectTodoActivity.COUNTDOWN;


/**
 * Created by Grant on 12/28/17.
 */

public class InspectArrayAdapter extends ArrayAdapter<RecipeItem> {

    private LayoutInflater mInflater;
    private int mResource;
    private ProgressBar progressBar;
    private InspectTodoActivity parentClass;
    final private static String SEPARATOR = ". ";

    public InspectArrayAdapter(Context context, int resource, List<RecipeItem> objects,
                               ProgressBar progressBar, InspectTodoActivity parentClass) {
        super(context, R.layout.recipe_row, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.progressBar=progressBar;
        this.parentClass = parentClass;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    /**
     * @return View with textview and checkbox for each recipe item
     */
    private View createViewFromResource(LayoutInflater inflater, int position,
                                        View convertView, final ViewGroup parent, int resource) {
        final View view;
        final TextView text;
        final CheckBox check;

        if (convertView == null) {
            view = inflater.inflate(R.layout.recipe_row, parent, false);
        } else {
            view = convertView;
        }

        try {
            text = (TextView) view.findViewById(R.id.textView);
            check = (CheckBox) view.findViewById(R.id.checkBox);

        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a CheckTextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a CheckedTextView", e);
        }

        final RecipeItem item = getItem(position);
        String message = ++position + SEPARATOR.concat(item.getStep());
        text.setText(message);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getRequiresClock() && !item.getChecked()) {
                    //Launch clock activity
                    parentClass.launchClock(item.getTime(), item.getStep());
                }
            }
        });

        check.setChecked(item.getChecked());
        check.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (check.isChecked()){
                    progressBar.setProgress(progressBar.getProgress()+1, true);
                    item.setChecked(true);
                } else {
                    progressBar.setProgress(progressBar.getProgress()-1, true);
                    item.setChecked(false);
                }
            }
        });
        return view;
    }
}
