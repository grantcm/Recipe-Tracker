package com.todo.recipeTracker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static com.todo.recipeTracker.R.id.text;
import static com.todo.recipeTracker.R.id.textView;


/**
 * Created by Grant on 12/28/17.
 */

public class InspectArrayAdapter extends ArrayAdapter<RecipeItem> {

    private LayoutInflater mInflater;
    private int mResource;
    private InspectRecipeFragment parentClass;
    final private static String SEPARATOR = ". ";

    public InspectArrayAdapter(Context context, int resource, List<RecipeItem> objects,
                               InspectRecipeFragment parentClass) {
        super(context, R.layout.recipe_row, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
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
        RecipeItem item = getItem(position);

        if (item.getEditClicked()) {
            convertView = inflater.inflate(R.layout.edit_recipe_row, parent, false);

            EditText editText = (EditText) convertView.findViewById(R.id.edit_recipe_text);
            Button addEditButton = (Button) convertView.findViewById(R.id.add_step);
            TextView timerPrompt = (TextView) convertView.findViewById(R.id.timer_prompt);
            RadioButton timerButton = (RadioButton) convertView.findViewById(R.id.timer_button);
            EditText timerText = (EditText) convertView.findViewById(R.id.time_value);
            editText.setText(item.getStep());

            setupAddButton(addEditButton, position, editText, timerText);
            setupTimerRadioButton(timerButton, timerText, timerPrompt);

            if (item.getRequiresClock()) {
                timerButton.setChecked(true);
                timerText.setVisibility(View.VISIBLE);
                timerText.setText(Long.toString(item.getTime()));
                timerPrompt.setVisibility(View.GONE);
            }
        } else {
            convertView = inflater.inflate(R.layout.recipe_row, parent, false);
            TextView text = (TextView) convertView.findViewById(textView);
            CheckBox check = (CheckBox) convertView.findViewById(R.id.checkBox);
            Button button = (Button) convertView.findViewById(R.id.edit_button);

            setupCheckBox(check, item);
            setupTextView(text, item, position);
            setupEditButton(button, position);

            if (parentClass.getInEditView()) {
                check.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                text.setText(item.getStep());
            } else {
                check.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                String message = Integer.toString(++position).concat(SEPARATOR.concat(item.getStep()));
                text.setText(message);
            }
        }

        return convertView;
    }

    private void setupTimerRadioButton(final RadioButton radioButton,
                                       final EditText editTimerText, final TextView timerPrompt) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerPrompt.getVisibility() == View.VISIBLE) {
                    editTimerText.setVisibility(View.VISIBLE);
                    timerPrompt.setVisibility(View.GONE);
                } else {
                    radioButton.setChecked(false);
                    editTimerText.setVisibility(View.GONE);
                    timerPrompt.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    /**
     * Initializes the checkbox with an onclick listener to connect to the progress bar
     *
     * @param checkBox checkbox to be set up
     * @param item     Recipe Item containing instruction and timer information
     * @return Initialized checkbox
     */
    private CheckBox setupCheckBox(final CheckBox checkBox, final RecipeItem item) {
        checkBox.setChecked(item.getChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                parentClass.setDataChanged(true);
                if (checkBox.isChecked()) {
                    parentClass.updateProgress(1);
                    item.setChecked(true);
                } else {
                    parentClass.updateProgress(-1);
                    item.setChecked(false);
                }
            }
        });
        return checkBox;
    }

    private Button setupEditButton(final Button button, final int position) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(position).setEditClicked(true);
                notifyDataSetChanged();
            }
        });
        return button;
    }

    private void setupAddButton(final Button button, final int position, final EditText editText,
                                final EditText timerText) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeItem item = getItem(position);
                item.setEditClicked(false);
                item.setStep(editText.getText().toString());
                if (timerText.getVisibility() == View.VISIBLE) {
                    item.setTime(Long.parseLong(timerText.getText().toString()));
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * Initializes the textview with the position and step of the item
     * Creates on an click listener if the item requires a clock
     */
    private TextView setupTextView(TextView textView, final RecipeItem item, int position) {
        String message = ++position + SEPARATOR.concat(item.getStep());
        textView.setText(message);
        if (item.getRequiresClock()) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.getChecked()) {
                        //Launch clock activity
                        parentClass.launchClock(item.getTime(), item.getStep());
                    }
                }
            });
        }

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (parentClass.getInEditView()) {
                    remove(item);
                    notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        return textView;
    }
}
