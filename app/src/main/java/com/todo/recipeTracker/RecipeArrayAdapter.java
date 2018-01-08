package com.todo.recipeTracker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import static com.todo.recipeTracker.R.color.High;
import static com.todo.recipeTracker.R.color.Low;
import static com.todo.recipeTracker.R.color.Medium;


/**
 * Created by Grant on 12/26/17.
 */

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {

    private LayoutInflater mInflater;
    private boolean checkable = false;
    private MainActivity parentClass;

    public RecipeArrayAdapter(Context context, int textViewResourceId, List<Recipe> objects
            , MainActivity parentClass) {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
        this.parentClass = parentClass;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return this.createViewFromResource(mInflater, position, convertView, parent);
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View createViewFromResource(LayoutInflater inflater, final int position,
                                        View convertView, final ViewGroup parent) {
        final View view;
        final TextView text;
        final CheckBox check;

        if (convertView == null) {
            view = inflater.inflate(R.layout.recipe_row, parent, false);
        } else {
            view = convertView;
        }

        text = view.findViewById(R.id.textView);
        check = view.findViewById(R.id.check_Box);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.isChecked()) {
                    parentClass.addListPosition(position);
                } else {
                    parentClass.removeListPosition(position);
                }
            }
        });
        if(checkable) {
            check.setVisibility(View.VISIBLE);
        } else {
            check.setVisibility(View.GONE);
        }

        final Recipe item = getItem(position);
        text.setText(item.getTitle());

        return view;
    }
}
