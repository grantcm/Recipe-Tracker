package com.todo.recipeTracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Grant on 12/31/17.
 */

public class InspectRecipeFragment extends Fragment {

    private static final String TITLE_KEYWORD = "TITLE";
    private String titleMessage;
    private TextView title;
    private ListView tasks;
    private ArrayList<RecipeItem> taskList;
    private InspectArrayAdapter inspectArrayAdapter;
    private ProgressBar progress;
    private Data data;
    private Button editButton;
    private boolean dataChanged = false;
    private boolean inEditView = false;

    public String FILENAME;
    public static final String FILE_END = ".txt";
    public static final String COUNTDOWN = "COUNTDOWN";
    public static final String INSTRUCTION = "INSTRUCTIONS";
    public static final int STEP_POS = 0;
    public static final int CHECKED_POS = 1;
    public static final int TIME_POS = 2;


    public InspectRecipeFragment() {
        data = new Data();
        taskList = new ArrayList<>();
    }

    public static InspectRecipeFragment newInstance(String title) {
        InspectRecipeFragment newFragment = new InspectRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEYWORD, title);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        titleMessage = arguments.getString(TITLE_KEYWORD);
        FILENAME = titleMessage.concat(FILE_END);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.inspect_fragment, container, false);
        title = (TextView) view.findViewById(R.id.title);
        tasks = (ListView) view.findViewById(R.id.steps);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        editButton = (Button) view.findViewById(R.id.edit_recipe);
        title.setText(titleMessage);
        inspectArrayAdapter = new InspectArrayAdapter(this.getContext(),
                android.R.layout.simple_list_item_checked, taskList, progress, this);
        tasks.setAdapter(inspectArrayAdapter);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        readData();

    }

    @Override
    public void onPause(){
        super.onPause();
        writeData();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateProgress(int change) {
        progress.setProgress(progress.getProgress()+change, true);
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
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
                String[] entry = s.split(" ");
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
        if (editButton.getText().toString().equals("Done")) {
            editButton.setText(R.string.edit_string);
            inEditView = false;
            for (RecipeItem r : taskList) {
                r.setEditClicked(false);
            }
            inspectArrayAdapter.notifyDataSetChanged();
        } else {
            editButton.setText(R.string.done);
            inEditView = true;
            inspectArrayAdapter.notifyDataSetChanged();
        }

    }

}
