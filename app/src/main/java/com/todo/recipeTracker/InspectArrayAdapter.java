package com.todo.recipeTracker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Grant on 12/28/17.
 */

public class InspectArrayAdapter extends ArrayAdapter<RecipeItem> {

    private LayoutInflater mInflater;
    private int mResource;
    private RecipeStepsFragment parentClass;
    private InputMethodManager inputMethodManager;
    final private static String SEPARATOR = ". ";

    public InspectArrayAdapter(Context context, int resource, List<RecipeItem> objects,
                               RecipeStepsFragment parentClass) {
        super(context, R.layout.recipe_row, objects);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        this.parentClass = parentClass;
        this.inputMethodManager = parentClass.getInputMethodManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    /**
     * @return View with textview and checkbox for each recipe item
     */
    private View createViewFromResource(final LayoutInflater inflater, int position,
                                        View convertView, final ViewGroup parent, int resource) {
        final RecipeItem item = getItem(position);

        if (item.getEditClicked()) {
            //Item Edit Mode
            convertView = inflater.inflate(R.layout.edit_recipe_row, parent, false);

            final EditText editText = (EditText) convertView.findViewById(R.id.edit_recipe_text);
            TextView timerPrompt = (TextView) convertView.findViewById(R.id.timer_prompt);
            final RadioButton timerButton = (RadioButton) convertView.findViewById(R.id.timer_button);
            EditText timerText = (EditText) convertView.findViewById(R.id.time_value);

            editText.setText(item.getStep());
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        item.setStep(editText.getText().toString());
                    }
                }
            });

            setupTimerRadioButton(timerButton, item, timerText, timerPrompt);

            if (item.getRequiresClock()) {
                timerButton.setChecked(true);
                timerText.setVisibility(View.VISIBLE);
                timerText.setText(Double.toString(item.getTime() / 60.0));
                timerPrompt.setVisibility(View.GONE);
            }
        } else {
            //Normal Display
            convertView = inflater.inflate(R.layout.recipe_row, parent, false);
            TextView text = (TextView) convertView.findViewById(R.id.textView);
            CheckBox check = (CheckBox) convertView.findViewById(R.id.check_Box);
            setupCheckBox(check, item);
            setupTextView(text, item, position);

            if (parentClass.getInEditView()) {
                //Hide the check if top view is in edit mode
                check.setVisibility(View.GONE);
                text.setText(item.getStep());
            } else {
                check.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private void setupTimerRadioButton(final RadioButton radioButton, final RecipeItem item,
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

        editTimerText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && radioButton.isChecked() && !editTimerText.getText().toString().isEmpty()) {
                    //Convert from decimal to long
                    item.setTime((long) (Double.parseDouble(editTimerText.getText().toString()) * 60.0));
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
    private void setupCheckBox(final CheckBox checkBox, final RecipeItem item) {
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
    }

    /**
     * Initializes the textview with the position and step of the item
     * Creates on an click listener if the item requires a clock
     */
    private void setupTextView(TextView textView, final RecipeItem item, int position) {
        String message = item.getStep();
        textView.setText(message);
        if (!parentClass.getInEditView()) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getRequiresClock() && !item.getChecked() && !parentClass.getInEditView()) {
                        //Launch clock activity
                        parentClass.launchClock(item.getTime(), item.getStep());
                    }
                }
            });
        }
    }
}
