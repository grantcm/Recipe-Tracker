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

import java.util.List;

import static com.todo.recipeTracker.R.id.textView;


/**
 * Created by Grant on 12/28/17.
 */

public class InspectArrayAdapter extends ArrayAdapter<RecipeItem> {

    private LayoutInflater mInflater;
    private int mResource;
    private ProgressBar progressBar;
    private InspectRecipeFragment parentClass;
    final private static String SEPARATOR = ". ";

    public InspectArrayAdapter(Context context, int resource, List<RecipeItem> objects,
                               ProgressBar progressBar, InspectRecipeFragment parentClass) {
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
        RecipeItem item = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder == null){
            viewHolder = new ViewHolder();

            viewHolder.inspectView = inflater.inflate(R.layout.recipe_row, parent, false);
            viewHolder.editView = inflater.inflate(R.layout.edit_recipe_row, parent, false);
            viewHolder.text = (TextView) viewHolder.inspectView.findViewById(textView);
            viewHolder.check = (CheckBox) viewHolder.inspectView.findViewById(R.id.checkBox);
            viewHolder.button = (Button) viewHolder.inspectView.findViewById(R.id.edit_button);
            viewHolder.editText = (EditText) viewHolder.editView.findViewById(R.id.edit_recipe_text);
            viewHolder.addEditButton = (Button) viewHolder.editView.findViewById(R.id.add_step);
            viewHolder.timerPrompt = (TextView) viewHolder.editView.findViewById(R.id.timer_prompt);
            viewHolder.timerButton = (RadioButton) viewHolder.editView.findViewById(R.id.timer_button);
            viewHolder.timerText = (EditText) viewHolder.editView.findViewById(R.id.time_value);

            setupAddButton(viewHolder.addEditButton, position, viewHolder.editText);
            setupCheckBox(viewHolder.check,item);
            setupTextView(viewHolder.text, item, position);
            setupEditButton(viewHolder.button, position);
            setupTimerRadioButton(viewHolder.timerButton, viewHolder.timerText, viewHolder.timerPrompt);

            convertView = viewHolder.inspectView;
            convertView.setTag(viewHolder);
        }

        if(item.getEditClicked()) {
            convertView = viewHolder.editView;
            viewHolder.editText.setText(item.getStep());
        } else {
            if(parentClass.getInEditView()) {
                viewHolder.check.setVisibility(View.GONE);
                viewHolder.button.setVisibility(View.VISIBLE);
                viewHolder.text.setText(item.getStep());
            } else {
                viewHolder.check.setVisibility(View.VISIBLE);
                viewHolder.button.setVisibility(View.GONE);
                viewHolder.text.setText(++position + SEPARATOR.concat(item.getStep()));
            }
            convertView = viewHolder.inspectView;
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
     * @param checkBox checkbox to be set up
     * @param item Recipe Item containing instruction and timer information
     * @return Initialized checkbox
     */
    private CheckBox setupCheckBox(final CheckBox checkBox, final RecipeItem item) {
        checkBox.setChecked(item.getChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                parentClass.setDataChanged(true);
                if (checkBox.isChecked()){
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

    private void setupAddButton(final Button button, final int position, final EditText editText) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeItem item = getItem(position);
                item.setEditClicked(false);
                item.setStep(editText.getText().toString());
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
                    if(!item.getChecked()) {
                        //Launch clock activity
                        parentClass.launchClock(item.getTime(), item.getStep());
                    }
                }
            });
        }
        return textView;
    }

    public static class ViewHolder {
        public View inspectView;
        public View editView;
        public EditText editText;
        public TextView text;
        public CheckBox check;
        public Button button;
        public Button addEditButton;
        public TextView timerPrompt;
        public RadioButton timerButton;
        public EditText timerText;
    }
}
