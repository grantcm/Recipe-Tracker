package com.todo.recipeTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.todo.recipeTracker.MainActivity.LIST_ITEMS;


/**
 * Created by Grant on 12/26/17.
 */

public class InspectRecipeActivity extends AppCompatActivity {

    private TextView title;
    private ListView tasks;
    private ArrayList<RecipeItem> taskList;
    private InspectArrayAdapter taskAdapter;
    private ProgressBar progress;
    private Data data;
    private boolean dataChanged = false;

    public String FILENAME;
    public static final String COUNTDOWN = "COUNTDOWN";
    public static final String INSTRUCTION = "INSTRUCTIONS";
    public static final int STEP_POS = 1;
    public static final int CHECKED_POS = 2;
    public static final int TIME_POS = 3;


    public InspectRecipeActivity() {
        data = new Data();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_activity);
        title = (TextView) findViewById(R.id.title);
        tasks = (ListView) findViewById(R.id.steps);
        progress = (ProgressBar) findViewById(R.id.progress);

        Intent intent = getIntent();
        parseIntent(intent);
        progress.setMax(taskList.size());
        taskAdapter = new InspectArrayAdapter(this, android.R.layout.simple_list_item_checked,
                taskList, progress, this);
        tasks.setAdapter(taskAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        readData();

    }

    @Override
    protected void onPause(){
        super.onPause();
        writeData();
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
            for (RecipeItem recipeItem : taskList) {
                dataItems.add(recipeItem.toString());
            }
            data.writeFile(FILENAME, dataItems, this);
            dataChanged=false;
        }
    }

    /**
     * Reads in recipe list data
     */
    private void readData() {
        ArrayList<String> newData = data.readFile(FILENAME, this);
        if (newData.size() != taskList.size()) {
            taskList.clear();
            for(String s: newData) {
                String[] entry = s.split(" ");
                taskList.add(new RecipeItem(entry[STEP_POS], Boolean.parseBoolean(entry[CHECKED_POS]),
                        Integer.parseInt(entry[TIME_POS])));
            }
        }
    }

    /**
     * Parses the passed intent for title and list of items
     * @param intent
     */
    private void parseIntent(Intent intent) {
        String pageTitle = intent.getStringExtra(MainActivity.TITLE);
        String[] tasks = intent.getStringArrayExtra(LIST_ITEMS);
        //TODO: Parse enum extra
        //Type priority = intent.get

        title.setText(pageTitle);
        FILENAME = pageTitle;
        taskList = new ArrayList<>();
        for(String s: tasks){
            taskList.add(new RecipeItem(s, false, 10));
        }
        dataChanged=true;
        writeData();
    }


    public void launchClock(int time, String step){
        Intent intent = new Intent(this, ClockActivity.class);
        intent.putExtra(COUNTDOWN, time);
        intent.putExtra(INSTRUCTION, step);
        startActivity(intent);
    }
}
