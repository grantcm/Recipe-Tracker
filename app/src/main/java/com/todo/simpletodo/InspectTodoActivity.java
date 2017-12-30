package com.todo.simpletodo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.logging.Logger;

import static com.todo.simpletodo.MainActivity.LIST_ITEMS;


/**
 * Created by Grant on 12/26/17.
 */

public class InspectTodoActivity extends AppCompatActivity {

    private TextView title;
    private ListView tasks;
    private ArrayList<RecipeItem> taskList;
    private InspectArrayAdapter taskAdapter;
    private ProgressBar progress;

    public static final String COUNTDOWN = "COUNTDOWN";
    public static final String INSTRUCTION = "INSTRUCTIONS";

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
        taskList = new ArrayList<>();
        for(String s: tasks){
            taskList.add(new RecipeItem(s, false, 10));
        }
    }


    public void launchClock(int time, String step){
        Intent intent = new Intent(this, ClockActivity.class);
        intent.putExtra(COUNTDOWN, time);
        intent.putExtra(INSTRUCTION, step);
        startActivity(intent);
    }
}
