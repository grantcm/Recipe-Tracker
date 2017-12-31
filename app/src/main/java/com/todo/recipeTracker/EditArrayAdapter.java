package com.todo.recipeTracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Grant on 12/31/17.
 */

public class EditArrayAdapter extends ArrayAdapter<RecipeItem> {

    private LayoutInflater mInflater;
    private int mResource;

    public EditArrayAdapter(Context context, int resource, List<RecipeItem> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
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
        ViewHolder viewHolder;
        final RecipeItem item = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.edit_recipe_row, parent, false);
            viewHolder.text = (EditText) convertView.findViewById(R.id.edit_recipe_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(item.getStep());

        return convertView;
    }

    public static class ViewHolder {
        EditText text;
    }
}
