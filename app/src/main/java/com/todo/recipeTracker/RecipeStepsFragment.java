package com.todo.recipeTracker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

/**
 * Created by Grant on 12/31/17.
 */

public class RecipeStepsFragment extends Fragment {

    private static final String TITLE_KEYWORD = "TITLE";
    private String titleMessage;
    private ViewSwitcher titleViewSwitcher;
    private TextView title;
    private EditText editTitle;
    private ListView tasks;
    private ArrayList<RecipeItem> taskList;
    private InspectArrayAdapter inspectArrayAdapter;
    private ProgressBar progress;
    private Data data;
    private Button addRowButton;
    private RecipeItem lastEdited = null;
    private boolean dataChanged = false;
    private boolean inEditView = false;

    public String FILENAME;
    public static final String FILE_END = ".txt";
    public static final String COUNTDOWN = "COUNTDOWN";
    public static final String INSTRUCTION = "INSTRUCTIONS";
    public static final int STEP_POS = 0;
    public static final int CHECKED_POS = 1;
    public static final int TIME_POS = 2;


    public RecipeStepsFragment() {
        taskList = new ArrayList<>();
    }

    public static RecipeStepsFragment newInstance(String title) {
        RecipeStepsFragment newFragment = new RecipeStepsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEYWORD, title);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data(getActivity());
        Bundle arguments = getArguments();
        titleMessage = arguments.getString(TITLE_KEYWORD);
        FILENAME = titleMessage.concat(FILE_END);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.steps_fragment, container, false);
        tasks = view.findViewById(R.id.steps);
        progress = view.findViewById(R.id.progress);
        addRowButton = view.findViewById(R.id.add_new_row_button);
        titleViewSwitcher = view.findViewById(R.id.title_view_switcher);
        title = titleViewSwitcher.findViewById(R.id.title);
        editTitle = titleViewSwitcher.findViewById(R.id.edit_recipe_title_box);
        title.setText(titleMessage);
        setupTitleOnLongClick();
        inspectArrayAdapter = new InspectArrayAdapter(this.getContext(),
                android.R.layout.simple_list_item_checked, taskList, this);
        tasks.setAdapter(inspectArrayAdapter);
        return view;
    }

    public InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public boolean handleBackPressed(){
        if (inEditView && lastEdited.getEditClicked()) {
            lastEdited.setEditClicked(false);
            inspectArrayAdapter.notifyDataSetChanged();
            return true;
        } else if (inEditView) {
            updateMainViewToEdit();
            return true;
        }
        return false;
    }

    @Override
    public void onResume(){
        readData();
        super.onResume();
    }

    @Override
    public void onPause(){
        writeData();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateProgress(int change) {
        progress.setProgress(progress.getProgress()+change, true);
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }

    private void setupTitleOnLongClick() {
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                updateMainViewToEdit();
                return true;
            }
        });

        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeItem item = taskList.get(position);
                if(inEditView) {
                    if(lastEdited != null) {
                        lastEdited.setEditClicked(false);
                    }
                    lastEdited = item;
                    item.setEditClicked(true);
                    inspectArrayAdapter.notifyDataSetChanged();
                }
            }
        });

        tasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(inEditView) {
                    taskList.remove(position);
                    inspectArrayAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    /**
     * Writes out recipe list data
     */
    private void writeData() {
        if (dataChanged) {
            ArrayList<String> dataItems = new ArrayList<>();
            dataItems.add(Integer.toString(progress.getProgress()));
            for (RecipeItem recipeItem : taskList) {
                dataItems.add(recipeItem.toString());
            }
            data.writeFile(FILENAME, dataItems);
            dataChanged=false;
        }
    }

    /**
     * Reads in recipe list data
     */
    private void readData() {
        ArrayList<String> newData = data.readFile(FILENAME);
        if (newData.size() != taskList.size()) {
            taskList.clear();
            int completed = Integer.parseInt(newData.get(0));
            newData.remove(0);
            for(String s: newData) {
                String[] entry = s.split("%");
                taskList.add(new RecipeItem(entry[STEP_POS], Boolean.parseBoolean(entry[CHECKED_POS]),
                        Integer.parseInt(entry[TIME_POS])));
            }
            progress.setMax(taskList.size());
            progress.setProgress(completed);
        }
    }

    /**
     * Launches new clock activity with parameters time and step
     * @param time to countdown
     * @param step instruction to display
     */
    public void launchClock(long time, String step){
        Intent intent = new Intent(this.getActivity(), ClockActivity.class);
        intent.putExtra(COUNTDOWN, time);
        intent.putExtra(INSTRUCTION, step);
        startActivity(intent);
    }

    public boolean getInEditView() {
        return inEditView;
    }

    public void updateMainViewToEdit() {
        dataChanged = true;

        if (inEditView) {
            addRowButton.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            inEditView = false;
            title.setText(editTitle.getText().toString());
            titleViewSwitcher.setDisplayedChild(0);
            for (RecipeItem r : taskList) {
                r.setEditClicked(false);
            }
            inspectArrayAdapter.notifyDataSetChanged();
        } else {
            editTitle.setText(title.getText().toString());
            progress.setVisibility(View.GONE);
            titleViewSwitcher.setDisplayedChild(1);
            addRowButton.setVisibility(View.VISIBLE);
            inEditView = true;
            inspectArrayAdapter.notifyDataSetChanged();
        }
    }

    public void addNewRow() {
        if (inEditView) {
            dataChanged = true;
            RecipeItem newItem = new RecipeItem("New", false, true);
            if (lastEdited != null) {
                lastEdited.setEditClicked(false);
            }
            lastEdited = newItem;
            inspectArrayAdapter.add(newItem);

        }
    }
}
