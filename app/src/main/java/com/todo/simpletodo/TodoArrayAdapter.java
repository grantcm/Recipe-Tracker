package com.todo.simpletodo;

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

import static com.todo.simpletodo.R.color.High;
import static com.todo.simpletodo.R.color.Low;
import static com.todo.simpletodo.R.color.Medium;


/**
 * Created by Grant on 12/26/17.
 */

public class TodoArrayAdapter extends ArrayAdapter<Todo> {

    private LayoutInflater mInflater;
    private int mResource;

    public TodoArrayAdapter(Context context, int textViewResourceId, List<Todo> objects) {
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

        final Todo item = getItem(position);
        text.setText(item.getTitle());
        Type priority = item.getPriority();

        if (priority == null) {
          //Add color later
        } else if(priority == Type.Low) {
            text.setBackgroundColor(view.getResources().getColor(Low, null));
        } else if (priority == Type.Medium) {
            text.setBackgroundColor(view.getResources().getColor(Medium, null));
        } else if (priority == Type.High){
            text.setBackgroundColor(view.getResources().getColor(High, null));
        }

        return view;
    }
}
