package com.todo.recipeTracker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private int mResource;

    public RecipeArrayAdapter(Context context, int textViewResourceId, List<Recipe> objects) {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
        mResource = textViewResourceId;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return this.createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private View createViewFromResource(LayoutInflater inflater, int position,
                                        View convertView, ViewGroup parent, int resource) {
        final View view;
        final TextView text;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            text = (TextView) view;
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        final Recipe item = getItem(position);
        text.setText(item.getTitle());

        return view;
    }
}
