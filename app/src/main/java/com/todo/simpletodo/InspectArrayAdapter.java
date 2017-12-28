package com.todo.simpletodo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.List;


/**
 * Created by Grant on 12/28/17.
 */

public class InspectArrayAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private int mResource;

    public InspectArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position,
                                        View convertView, ViewGroup parent, int resource) {
        final View view;
        final CheckedTextView text;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            text = (CheckedTextView) view;
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a CheckTextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a CheckedTextView", e);
        }

        final String item = getItem(position);
        text.setText(item);
        text.setChecked(false);
        return view;
    }
}
